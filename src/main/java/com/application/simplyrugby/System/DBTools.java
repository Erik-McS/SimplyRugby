package com.application.simplyrugby.System;

import com.application.simplyrugby.Model.*;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Class to interact with the Database. This class will follow the Singleton design pattern.<br>
 * This static class has all the functions to interact with the database.<br>
 * During development, the class was optimised to prevent connection leaks that were causing database locks.<br>
 * To that effect, the class calls a HikariCP class for connection pooling and extensive use of try-catch with resources.
 * @author Erik McSeveney
 */
public class DBTools {

    // private constructor to prevent object creation
    private DBTools() {}

    /**
     * This method loads the JDBC drivers and allows access to a database.
     */
    public static void databaseConnect() {
        try {
            Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
                 NoSuchMethodException | InvocationTargetException e) {
            new CustomAlert("Error loading the JDBC drivers", e.getMessage()).showAndWait();
        }
    }

    /**
     * Method to execute an INSERT, CREATE or UPDATE statement using a parameterised query.<br>
     * Parameters are bound via PreparedStatement to prevent SQL injection.<br>
     * Returns true if execution is successful, false otherwise.
     *
     * @param query  The SQL query with '?' placeholders
     * @param params The parameters to bind to the query
     * @return true if successful, false otherwise
     */
    public static boolean executeUpdateQuery(String query, Object... params) {
        try (
                Connection connect = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connect.prepareStatement(query)
        ) {
            bindParameters(statement, params);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            new CustomAlert("Error while trying to execute the query.", e.getMessage()).showAndWait();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Function to execute a generic SELECT query.<br>
     * Parameters are bound via PreparedStatement to prevent SQL injection.<br>
     * The returned QueryResult has a close() method that must be called after use.
     *
     * @param query  The SELECT query with '?' placeholders
     * @param params The parameters to bind to the query
     * @return the QueryResult, or null on error
     */
    public static QueryResult executeSelectQuery(String query, Object... params) {
        try {
            Connection connection = ConnectionPooling.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            bindParameters(statement, params);
            ResultSet rs = statement.executeQuery();
            return new QueryResult(rs, connection, statement);
        } catch (SQLException e) {
            new CustomAlert("Error Executing Query: ", e.getMessage()).showAndWait();
            return null;
        }
    }

    /**
     * This function retrieves a single integer ID from a table using a parameterised query.<br>
     * Used, for example, to get a member ID from a name/surname selected in a combobox.
     *
     * @param query  The SQL query with '?' placeholders
     * @param params The parameters to bind to the query
     * @return The requested ID, or 0 if not found
     */
    public static int getID(String query, Object... params) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(query)
        ) {
            bindParameters(statement, params);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            new CustomAlert("Error while getting the requested ID", e.getMessage()).showAndWait();
            return 0;
        }
    }

    /**
     * Helper method to bind varargs parameters to a PreparedStatement.<br>
     * Supports String, Integer, and int parameter types.
     *
     * @param statement The PreparedStatement to bind parameters to
     * @param params    The parameters to bind
     * @throws SQLException if a database access error occurs
     */
    private static void bindParameters(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            if (params[i] instanceof String s) {
                statement.setString(i + 1, s);
            } else if (params[i] instanceof Integer n) {
                statement.setInt(i + 1, n);
            } else if (params[i] != null) {
                statement.setObject(i + 1, params[i]);
            } else {
                statement.setNull(i + 1, Types.NULL);
            }
        }
    }

    /**
     * Function to insert a Player or NonPlayer in the database.<br>
     * Uses pattern variable matching (Java 16+) to determine member type.
     *
     * @see <a href="https://www.baeldung.com/java-16-new-features">Pattern variables</a>
     * @param member The club member to save in the database
     * @return true if successful, false otherwise
     */
    public static boolean insertMember(Member member) {
        if (member instanceof Player player) {
            if (!memberExists(player)) {
                return executeUpdateQuery(
                        "INSERT INTO players (first_name,surname,address,date_of_birth,gender,telephone,email,scrums_number,is_assigned_to_squad,doctor_id,kin_id) " +
                        "VALUES (?,?,?,?,?,?,?,?,?,?,?)",
                        player.getFirstName(), player.getSurname(), player.getAddress(),
                        player.getDateOfBirth(), player.getGender(), player.getTelephone(),
                        player.getEmail(), player.getScrumsNumber(), player.isAssignedToSquad(),
                        player.getDoctorID(), player.getKinID()
                );
            }
            return false;
        }
        if (member instanceof NonPlayer nonPlayer) {
            if (!memberExists(nonPlayer)) {
                return executeUpdateQuery(
                        "INSERT INTO non_players (first_name,surname,address,telephone,email,role_id) VALUES (?,?,?,?,?,?)",
                        nonPlayer.getFirstName(), nonPlayer.getSurname(), nonPlayer.getAddress(),
                        nonPlayer.getTelephone(), nonPlayer.getEmail(), nonPlayer.getRole_id()
                );
            }
            return false;
        }
        return false;
    }

    /**
     * Loads a Player or NonPlayer from the database by their ID and returns the corresponding object.
     *
     * @param member   A test Player or NonPlayer instance to indicate the return type
     * @param memberID The member ID to look up
     * @return The loaded Member object, or null on error
     */
    public static Member loadMember(Member member, int memberID) {
        if (member instanceof Player) {
            try (
                    Connection connection = ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "SELECT player_id,first_name,surname,address,date_of_birth,gender,telephone," +
                            "email,scrums_number,is_assigned_to_squad,doctor_id,kin_id FROM players WHERE player_id=?")
            ) {
                statement.setInt(1, memberID);
                try (ResultSet rs = statement.executeQuery()) {
                    return new Player.PlayerBuilder()
                            .setPlayerID(rs.getInt(1))
                            .setFirstName(rs.getString(2))
                            .setSurname(rs.getString(3))
                            .setAddress(rs.getString(4))
                            .setDoB(rs.getString(5))
                            .setGender(rs.getString(6))
                            .setTelephone(rs.getString(7))
                            .setEmail(rs.getString(8))
                            .setScrumsNumber(rs.getInt(9))
                            .setIsAssignedToSquad(rs.getString(10))
                            .setDoctorID(rs.getInt(11))
                            .setKinID(rs.getInt(12))
                            .Builder();
                } catch (SQLException e) {
                    new CustomAlert("Error while trying to create a Member Player object.", e.getMessage()).showAndWait();
                    e.printStackTrace();
                    return null;
                }
            } catch (SQLException | ValidationException e) {
                new CustomAlert("Error while trying to create a Member Player object.", e.getMessage()).showAndWait();
                e.printStackTrace();
                return null;
            }
        }
        if (member instanceof NonPlayer) {
            try (
                    Connection connection = ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "SELECT member_id,first_name,surname,address,telephone,email,role_id FROM non_players WHERE member_id=?")
            ) {
                statement.setInt(1, memberID);
                try (ResultSet rs = statement.executeQuery()) {
                    return new NonPlayer(rs.getInt(1), rs.getString(2), rs.getString(3),
                            rs.getString(4), rs.getString(5), rs.getString(6), rs.getInt(7));
                } catch (SQLException e) {
                    new CustomAlert("Error while trying to create a Member object.", e.getMessage()).showAndWait();
                    e.printStackTrace();
                }
            } catch (SQLException | ValidationException e) {
                new CustomAlert("Error while trying to create a Member object.", e.getMessage()).showAndWait();
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Inserts a NextOfKin or Doctor record in the database.
     *
     * @param person The contact to insert
     * @return true if successful, false otherwise
     */
    public static boolean insertContact(ThirdParty person) {
        if (person instanceof NextOfKin nok) {
            if (!contactExists(nok))
                return executeUpdateQuery(
                        "INSERT INTO next_of_kin (name,surname,telephone) VALUES (?,?,?)",
                        nok.getFirstName(), nok.getSurname(), nok.getTelephone()
                );
            return false;
        }
        if (person instanceof Doctor doc) {
            if (!contactExists(doc))
                return executeUpdateQuery(
                        "INSERT INTO player_doctors (name,surname,telephone) VALUES (?,?,?)",
                        doc.getFirstName(), doc.getSurname(), doc.getTelephone()
                );
            return false;
        }
        return false;
    }

    /**
     * Inserts a training profile for a player in the database.
     *
     * @param tp The training profile to insert
     * @return true if successful, false otherwise
     */
    public static boolean insertTrainingProfile(TrainingProfile tp) {
        try {
            return executeUpdateQuery(
                    "INSERT INTO training_profiles (passing_skill,running_skill,support_skill,tackling_skill,decision_skill,player_id) VALUES (?,?,?,?,?,?)",
                    TrainingProfile.getLevelID(tp.getPassingLevel()),
                    TrainingProfile.getLevelID(tp.getRunningLevel()),
                    TrainingProfile.getLevelID(tp.getSupportLevel()),
                    TrainingProfile.getLevelID(tp.getTacklingLevel()),
                    TrainingProfile.getLevelID(tp.getDecisionLevel()),
                    tp.getPlayerID()
            );
        } catch (ValidationException e) {
            new CustomAlert("Insert Training Profile", e.getMessage()).showAndWait();
            return false;
        }
    }

    /**
     * Searches for a NextOfKin or Doctor record using a full SQL query string.
     *
     * @param tp    The object type to look for
     * @param query The SQL query to use
     * @return The found ThirdParty record, or null on error
     */
    public static ThirdParty selectContact(ThirdParty tp, String query) {
        if (tp instanceof NextOfKin nok) {
            try (
                    Connection connection = ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet rs = statement.executeQuery()
            ) {
                nok.setKinID(rs.getInt(1));
                nok.setFirstName(rs.getString(2));
                nok.setSurname(rs.getString(3));
                nok.setTelephone(rs.getString(4));
                return nok;
            } catch (ValidationException | SQLException e) {
                new CustomAlert("Error", e.getMessage()).showAndWait();
                return null;
            }
        }
        if (tp instanceof Doctor doc) {
            try (
                    Connection connection = ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet rs = statement.executeQuery()
            ) {
                doc.setDoctorID(rs.getInt(1));
                doc.setFirstName(rs.getString(2));
                doc.setSurname(rs.getString(3));
                doc.setTelephone(rs.getString(4));
                return doc;
            } catch (ValidationException | SQLException e) {
                new CustomAlert("Error", e.getMessage()).showAndWait();
            }
        }
        return null;
    }

    /**
     * Searches for a NextOfKin or Doctor record by their ID.
     *
     * @param tp    The object type to look for
     * @param index The ID to search for
     * @return The found ThirdParty record, or null on error
     */
    public static ThirdParty selectContact(ThirdParty tp, int index) {
        if (tp instanceof NextOfKin nok) {
            try (
                    Connection connection = ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "SELECT kin_id,name,surname,telephone FROM next_of_kin WHERE kin_id=?")
            ) {
                statement.setInt(1, index);
                try (ResultSet rs = statement.executeQuery()) {
                    nok.setKinID(rs.getInt(1));
                    nok.setFirstName(rs.getString(2));
                    nok.setSurname(rs.getString(3));
                    nok.setTelephone(rs.getString(4));
                } catch (SQLException e) {
                    new CustomAlert("Error", e.getMessage()).showAndWait();
                    return null;
                }
                return nok;
            } catch (ValidationException | SQLException e) {
                new CustomAlert("Error", e.getMessage()).showAndWait();
                return null;
            }
        }
        if (tp instanceof Doctor doc) {
            try (
                    Connection connection = ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(
                            "SELECT doctor_id,name,surname,telephone FROM player_doctors WHERE doctor_id=?")
            ) {
                statement.setInt(1, index);
                try (ResultSet rs = statement.executeQuery()) {
                    doc.setDoctorID(rs.getInt(1));
                    doc.setFirstName(rs.getString(2));
                    doc.setSurname(rs.getString(3));
                    doc.setTelephone(rs.getString(4));
                    return doc;
                } catch (ValidationException | SQLException e) {
                    new CustomAlert("Error", e.getMessage()).showAndWait();
                    return null;
                }
            } catch (SQLException e) {
                new CustomAlert("Error", e.getMessage()).showAndWait();
                return null;
            }
        }
        return null;
    }

    /**
     * Gets the role description of a non-player member from their role ID.
     *
     * @param roleID The role ID to look up
     * @return The role description string, or null on error
     */
    public static String getRole(int roleID) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT role_description FROM non_players_roles WHERE role_id=?")
        ) {
            statement.setInt(1, roleID);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.getString(1);
            } catch (SQLException e) {
                new CustomAlert("Error getting the Role description", e.getMessage()).showAndWait();
                return null;
            }
        } catch (SQLException e) {
            new CustomAlert("Error getting the Role description", e.getMessage()).showAndWait();
            return null;
        }
    }

    /**
     * Saves a Senior or Junior Squad to the database, including all associated sub-teams
     * (replacement team, coach team, admin team) and initial training profiles for each player.
     *
     * @param squad The Squad to save
     */
    public static void saveSquad(Squad squad) {
        if (squad instanceof SeniorSquad) {
            saveSeniorSquad((SeniorSquad) squad);
        } else if (squad instanceof JuniorSquad) {
            saveJuniorSquad((JuniorSquad) squad);
        }
    }

    /**
     * Handles the database insertion of a SeniorSquad and all associated records.
     */
    private static void saveSeniorSquad(SeniorSquad squad) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement stmtRepTeam = connection.prepareStatement(
                        "INSERT INTO replacement_team(PLAYER_1,PLAYER_2,PLAYER_3,PLAYER_4,PLAYER_5) VALUES (?,?,?,?,?)");
                PreparedStatement stmtCoaches = connection.prepareStatement(
                        "INSERT INTO squad_coaches(COACH_1,COACH_2,COACH_3) VALUES (?,?,?)");
                PreparedStatement stmtAdminTeam = connection.prepareStatement(
                        "INSERT INTO squad_admin_team(CHAIRMAN,FIXTURE_SEC) VALUES (?,?)");
                PreparedStatement stmtSquad = connection.prepareStatement(
                        "INSERT INTO senior_squads(squad_name,loose_head_prop,hooker,tight_head_prop,second_row,second_row2," +
                        "blind_side_flanker,open_side_flanker,number_8,scrum_half,fly_half,left_wing,inside_centre," +
                        "outside_center,right_side,full_back,cogroup_id,adteam_id,repteam_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                PreparedStatement stmtProfile = connection.prepareStatement(
                        "INSERT INTO training_profiles (passing_skill,running_skill,support_skill,tackling_skill,decision_skill,player_id) VALUES (1,1,1,1,1,?)")
        ) {
            // --- Replacement team ---
            ArrayList<Integer> repTeam = extractPlayerIDs(squad.getReplacementTeam().getReplacements());
            for (int i = 0; i < repTeam.size(); i++) {
                stmtRepTeam.setInt(i + 1, repTeam.get(i));
                insertProfileIfAbsent(stmtProfile, repTeam.get(i));
            }
            stmtRepTeam.executeUpdate();
            int repteam_id = getID("SELECT repteam_id FROM replacement_team WHERE player_1=? AND player_2=?",
                    repTeam.get(0), repTeam.get(1));

            // --- Coach team ---
            ArrayList<Integer> coTeam = extractMemberIDs(squad.getCoachTeam().getCoaches());
            for (int i = 0; i < coTeam.size(); i++) stmtCoaches.setInt(i + 1, coTeam.get(i));
            stmtCoaches.executeUpdate();
            int cogroup_id = getID("SELECT cogroup_id FROM squad_coaches WHERE coach_1=? AND coach_2=?",
                    coTeam.get(0), coTeam.get(1));

            // --- Admin team ---
            ArrayList<Integer> adTeam = extractMemberIDs(squad.getAdminTeam().getAdmins());
            for (int i = 0; i < adTeam.size(); i++) stmtAdminTeam.setInt(i + 1, adTeam.get(i));
            stmtAdminTeam.executeUpdate();
            int adteam_id = getID("SELECT adteam_id FROM squad_admin_team WHERE CHAIRMAN=?", adTeam.get(0));

            // --- Squad players ---
            ArrayList<Integer> squadPlayers = extractPlayerIDs(squad.getSquadPlayers());
            stmtSquad.setString(1, squad.getSquadName());
            for (int i = 0; i < squadPlayers.size(); i++) {
                stmtSquad.setInt(i + 2, squadPlayers.get(i));
                insertProfileIfAbsent(stmtProfile, squadPlayers.get(i));
            }
            stmtSquad.setInt(17, cogroup_id);
            stmtSquad.setInt(18, adteam_id);
            stmtSquad.setInt(19, repteam_id);
            stmtSquad.executeUpdate();

            // --- Update player assignment status ---
            updatePlayersSquadStatus(squad.getSquadPlayers());

        } catch (SQLException e) {
            new CustomAlert("Create Squad Error", e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Handles the database insertion of a JuniorSquad and all associated records.
     */
    private static void saveJuniorSquad(JuniorSquad squad) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement stmtRepTeam = connection.prepareStatement(
                        "INSERT INTO replacement_team(PLAYER_1,PLAYER_2,PLAYER_3,PLAYER_4,PLAYER_5) VALUES (?,?,?,?,?)");
                PreparedStatement stmtCoaches = connection.prepareStatement(
                        "INSERT INTO squad_coaches(COACH_1,COACH_2,COACH_3) VALUES (?,?,?)");
                PreparedStatement stmtAdminTeam = connection.prepareStatement(
                        "INSERT INTO squad_admin_team(CHAIRMAN,FIXTURE_SEC) VALUES (?,?)");
                PreparedStatement stmtSquad = connection.prepareStatement(
                        "INSERT INTO junior_squads(squad_name,loose_head_prop,hooker,tight_head_prop,scrum_half," +
                        "fly_half,centre,wing,cogroup_id,adteam_id,repteam_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
                PreparedStatement stmtProfile = connection.prepareStatement(
                        "INSERT INTO training_profiles (passing_skill,running_skill,support_skill,tackling_skill,decision_skill,player_id) VALUES (1,1,1,1,1,?)")
        ) {
            // --- Replacement team ---
            ArrayList<Integer> repTeam = extractPlayerIDs(squad.getReplacementTeam().getReplacements());
            for (int i = 0; i < repTeam.size(); i++) {
                stmtRepTeam.setInt(i + 1, repTeam.get(i));
                insertProfileIfAbsent(stmtProfile, repTeam.get(i));
            }
            stmtRepTeam.executeUpdate();
            int repteam_id = getID("SELECT repteam_id FROM replacement_team WHERE player_1=? AND player_2=?",
                    repTeam.get(0), repTeam.get(1));

            // --- Coach team ---
            ArrayList<Integer> coTeam = extractMemberIDs(squad.getCoachTeam().getCoaches());
            for (int i = 0; i < coTeam.size(); i++) stmtCoaches.setInt(i + 1, coTeam.get(i));
            stmtCoaches.executeUpdate();
            int cogroup_id = getID("SELECT cogroup_id FROM squad_coaches WHERE coach_1=? AND coach_2=?",
                    coTeam.get(0), coTeam.get(1));

            // --- Admin team ---
            ArrayList<Integer> adTeam = extractMemberIDs(squad.getAdminTeam().getAdmins());
            for (int i = 0; i < adTeam.size(); i++) stmtAdminTeam.setInt(i + 1, adTeam.get(i));
            stmtAdminTeam.executeUpdate();
            int adteam_id = getID("SELECT adteam_id FROM squad_admin_team WHERE CHAIRMAN=?", adTeam.get(0));

            // --- Squad players ---
            ArrayList<Integer> squadPlayers = extractPlayerIDs(squad.getSquadPlayers());
            stmtSquad.setString(1, squad.getSquadName());
            for (int i = 0; i < squadPlayers.size(); i++) {
                stmtSquad.setInt(i + 2, squadPlayers.get(i));
                insertProfileIfAbsent(stmtProfile, squadPlayers.get(i));
            }
            stmtSquad.setInt(9, cogroup_id);
            stmtSquad.setInt(10, adteam_id);
            stmtSquad.setInt(11, repteam_id);
            stmtSquad.executeUpdate();

            // --- Update player assignment status ---
            updatePlayersSquadStatus(squad.getSquadPlayers());

        } catch (SQLException e) {
            new CustomAlert("Save Squad Error", e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Extracts player IDs from a list of Player objects.
     */
    private static ArrayList<Integer> extractPlayerIDs(ArrayList<Player> players) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Player p : players) ids.add(p.getPlayerID());
        return ids;
    }

    /**
     * Extracts member IDs from a list of NonPlayer objects.
     */
    private static ArrayList<Integer> extractMemberIDs(ArrayList<NonPlayer> members) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (NonPlayer m : members) ids.add(m.getMember_id());
        return ids;
    }

    /**
     * Inserts a default training profile for a player if one does not already exist.
     */
    private static void insertProfileIfAbsent(PreparedStatement stmtProfile, int playerID) throws SQLException {
        stmtProfile.setInt(1, playerID);
        stmtProfile.executeUpdate();
    }

    /**
     * Updates the is_assigned_to_squad flag to 'YES' for each player in the list.
     */
    private static void updatePlayersSquadStatus(ArrayList<Player> players) {
        for (Player player : players) {
            executeUpdateQuery(
                    "UPDATE players SET is_assigned_to_squad='YES' WHERE player_id=?",
                    player.getPlayerID()
            );
        }
    }

    /**
     * Retrieves a Club record from the database by its ID.
     *
     * @param club_id The Club ID to look up
     * @return The Club object, or null on error
     */
    public static Club getClub(int club_id) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT name,address,telephone,email FROM clubs WHERE club_id=?")
        ) {
            statement.setInt(1, club_id);
            try (ResultSet rs = statement.executeQuery()) {
                Club club = new Club(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                club.setClub_id(club_id);
                return club;
            } catch (NullPointerException | SQLException e) {
                new CustomAlert("Get Club error:", e.getMessage()).showAndWait();
                return null;
            }
        } catch (ValidationException | SQLException e) {
            new CustomAlert("Get Club error:", e.getMessage()).showAndWait();
            return null;
        }
    }

    /**
     * Saves a Club to the database, checking for duplicate names first.
     *
     * @param club The Club to save
     */
    public static void saveClub(Club club) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO clubs (name,address,telephone,email) VALUES (?,?,?,?)");
                QueryResult qs = executeSelectQuery("SELECT name FROM Clubs")
        ) {
            if (qs != null) {
                while (qs.getResultSet().next()) {
                    if (club.getName().equals(qs.getResultSet().getString(1)))
                        throw new ValidationException("A Club with this name already exists in the database");
                }
            } else {
                throw new ValidationException("There are no Clubs saved in the database");
            }
            statement.setString(1, club.getName());
            statement.setString(2, club.getAddress());
            statement.setString(3, club.getTelephone());
            statement.setString(4, club.getEmail());
            if (statement.executeUpdate() == 0)
                throw new ValidationException("Save Club: No row was inserted");

        } catch (SQLException | ValidationException e) {
            new CustomAlert("Save Club Error:", e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Inserts a game into the database and links it to the appropriate squad's games table.
     *
     * @param game The Game to save
     */
    public static void saveGame(Game game) {
        int game_id;
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement stmtGame = connection.prepareStatement(
                        "INSERT INTO games (date,club_id,location_id) VALUES (?,?,?)");
                PreparedStatement stmtSenior = connection.prepareStatement(
                        "INSERT INTO senior_games_played VALUES (?,?,?)");
                PreparedStatement stmtJunior = connection.prepareStatement(
                        "INSERT INTO junior_games_played VALUES (?,?,?)")
        ) {
            stmtGame.setString(1, game.getDate());
            stmtGame.setInt(2, game.getPlayingClub().getClub_id());
            stmtGame.setInt(3, game.getLocation());
            stmtGame.executeUpdate();

            try (QueryResult qs = executeSelectQuery("SELECT MAX(game_id) FROM games LIMIT 1")) {
                game_id = qs.getResultSet().getInt(1);
            } catch (SQLException e) {
                new CustomAlert("Save Game Error:", e.getMessage()).showAndWait();
                e.printStackTrace();
                return;
            }

            if (game.getSquad() instanceof SeniorSquad squad) {
                stmtSenior.setInt(1, getID("SELECT squad_id FROM senior_squads WHERE squad_name=?", squad.getSquadName()));
                stmtSenior.setString(2, game.getDate());
                stmtSenior.setInt(3, game_id);
                stmtSenior.executeUpdate();
            } else if (game.getSquad() instanceof JuniorSquad squad) {
                stmtJunior.setInt(1, getID("SELECT squad_id FROM junior_squads WHERE squad_name=?", squad.getSquadName()));
                stmtJunior.setString(2, game.getDate());
                stmtJunior.setInt(3, game_id);
                stmtJunior.executeUpdate();
            }

        } catch (ValidationException | SQLException e) {
            new CustomAlert("Save Game Error:", e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Loads a Squad (Senior, Junior, or Replacement) from the database.
     *
     * @param squad    The type of squad to return
     * @param squad_id The squad ID to look up
     * @return The loaded Squad object, or null on error
     */
    public static Squad loadSquad(Squad squad, int squad_id) throws ValidationException {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement stmtSeniorPlayers = connection.prepareStatement(
                        "SELECT loose_head_prop,hooker,tight_head_prop,second_row,second_row2,blind_side_flanker," +
                        "open_side_flanker,number_8,scrum_half,fly_half,left_wing,inside_centre,outside_center," +
                        "right_side,full_back FROM senior_squads WHERE squad_id=?");
                PreparedStatement stmtSeniorMeta = connection.prepareStatement(
                        "SELECT squad_name,cogroup_id,adteam_id,repteam_id FROM senior_squads WHERE squad_id=?");
                PreparedStatement stmtJuniorPlayers = connection.prepareStatement(
                        "SELECT loose_head_prop,hooker,tight_head_prop,scrum_half,fly_half,centre,wing FROM junior_squads WHERE squad_id=?");
                PreparedStatement stmtJuniorMeta = connection.prepareStatement(
                        "SELECT squad_name,cogroup_id,adteam_id,repteam_id FROM junior_squads WHERE squad_id=?");
                PreparedStatement stmtRepTeam = connection.prepareStatement(
                        "SELECT player_1,player_2,player_3,player_4,player_5 FROM replacement_team WHERE repteam_ID=?")
        ) {
            if (squad instanceof SeniorSquad) {
                ArrayList<Player> players = new ArrayList<>();
                stmtSeniorPlayers.setInt(1, squad_id);
                try (ResultSet rs = stmtSeniorPlayers.executeQuery()) {
                    for (int i = 1; i <= 15; i++)
                        players.add((Player) loadMember(Player.dummyPlayer(), rs.getInt(i)));
                } catch (SQLException e) {
                    new CustomAlert("Error Squad creation", "Could not load the requested squad").showAndWait();
                    e.printStackTrace();
                    return null;
                }
                stmtSeniorMeta.setInt(1, squad_id);
                try (ResultSet rs = stmtSeniorMeta.executeQuery()) {
                    return new SeniorSquad(players, rs.getString(1),
                            (ReplacementTeam) loadSquad(new ReplacementTeam(), rs.getInt(4)),
                            (AdminTeam) loadTeam(new AdminTeam(), rs.getInt(3)),
                            (CoachTeam) loadTeam(new CoachTeam(), rs.getInt(3)));
                } catch (SQLException e) {
                    new CustomAlert("Error Squad creation", "Could not load the requested squad").showAndWait();
                    e.printStackTrace();
                    return null;
                }
            } else if (squad instanceof JuniorSquad) {
                ArrayList<Player> players = new ArrayList<>();
                stmtJuniorPlayers.setInt(1, squad_id);
                try (ResultSet rs = stmtJuniorPlayers.executeQuery()) {
                    for (int i = 1; i <= 7; i++)
                        players.add((Player) loadMember(Player.dummyPlayer(), rs.getInt(i)));
                } catch (SQLException e) {
                    new CustomAlert("Error Squad creation", "Could not load the requested squad").showAndWait();
                    e.printStackTrace();
                    return null;
                }
                stmtJuniorMeta.setInt(1, squad_id);
                try (ResultSet rs = stmtJuniorMeta.executeQuery()) {
                    return new JuniorSquad(players, rs.getString(1),
                            (ReplacementTeam) loadSquad(new ReplacementTeam(), rs.getInt(4)),
                            (AdminTeam) loadTeam(new AdminTeam(), rs.getInt(3)),
                            (CoachTeam) loadTeam(new CoachTeam(), rs.getInt(3)));
                } catch (SQLException e) {
                    new CustomAlert("Error Squad creation", "Could not load the requested squad").showAndWait();
                    e.printStackTrace();
                    return null;
                }
            } else if (squad instanceof ReplacementTeam) {
                ArrayList<Player> players = new ArrayList<>();
                stmtRepTeam.setInt(1, squad_id);
                try (ResultSet rs = stmtRepTeam.executeQuery()) {
                    for (int i = 1; i <= 5; i++)
                        players.add((Player) loadMember(Player.dummyPlayer(), rs.getInt(i)));
                    return new ReplacementTeam(players);
                } catch (SQLException e) {
                    new CustomAlert("Error Squad creation", "Could not load the requested squad").showAndWait();
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (SQLException e) {
            new CustomAlert("Error Squad creation", "Could not load the requested squad").showAndWait();
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Loads an AdminTeam or CoachTeam from the database.
     *
     * @param memberTeam The type of team to return
     * @param team_id    The team ID to look up
     * @return The loaded MemberTeam, or null on error
     */
    public static MemberTeam loadTeam(MemberTeam memberTeam, int team_id) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement stmtAdmin = connection.prepareStatement(
                        "SELECT chairman,fixture_sec FROM squad_admin_team WHERE adteam_id=?");
                PreparedStatement stmtCoach = connection.prepareStatement(
                        "SELECT coach_1,coach_2,coach_3 FROM squad_coaches WHERE cogroup_id=?")
        ) {
            if (memberTeam instanceof AdminTeam) {
                stmtAdmin.setInt(1, team_id);
                try (ResultSet rs = stmtAdmin.executeQuery()) {
                    return new AdminTeam(
                            (NonPlayer) loadMember(new NonPlayer(), rs.getInt(1)),
                            (NonPlayer) loadMember(new NonPlayer(), rs.getInt(2)));
                } catch (SQLException e) {
                    new CustomAlert("Error Team creation", "Could not load the requested team").showAndWait();
                    e.printStackTrace();
                    return null;
                }
            }
            if (memberTeam instanceof CoachTeam) {
                stmtCoach.setInt(1, team_id);
                try (ResultSet rs = stmtCoach.executeQuery()) {
                    return new CoachTeam(
                            (NonPlayer) loadMember(new NonPlayer(), rs.getInt(1)),
                            (NonPlayer) loadMember(new NonPlayer(), rs.getInt(2)),
                            (NonPlayer) loadMember(new NonPlayer(), rs.getInt(3)));
                } catch (SQLException e) {
                    new CustomAlert("Error Team creation", "Could not load the requested team").showAndWait();
                    e.printStackTrace();
                    return null;
                }
            }
        } catch (ValidationException | SQLException e) {
            new CustomAlert("Error Team creation", "Could not load the requested team").showAndWait();
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Loads a Game record from the database (without score/outcome data).
     *
     * @param game_id The game ID to retrieve
     * @return The Game object, or null on error
     */
    public static Game loadNonUpdatedGame(int game_id) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT date,club_id,location_id FROM games WHERE game_id=?")
        ) {
            statement.setInt(1, game_id);
            try (ResultSet rs = statement.executeQuery()) {
                Game game = new Game();
                game.setGame_id(game_id);
                game.setPlayingClub(loadClub(rs.getInt(2)));
                game.setLocation(rs.getInt(3));
                game.setDate(rs.getString(1));
                return game;
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        } catch (ValidationException | SQLException e) {
            new CustomAlert("Game Object Error", e.getMessage()).showAndWait();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Updates an existing game with match outcome and scoring details.
     *
     * @param game The Game with updated data to save
     */
    public static void updateGame(Game game) {
        executeUpdateQuery(
                "UPDATE games SET nb_of_try=?,nb_of_penalty=?,nb_of_conversion=?,nb_of_drop_goal=?,opponent_score=?,outcome_id=? WHERE game_id=?",
                game.getNbTry(),
                game.getNbPenalty(),
                game.getNbConversion(),
                game.getNbDropGoal(),
                game.getOpponentScore(),
                getID("SELECT outcome_id FROM game_outcomes WHERE outcome=?", game.getOutcome()),
                game.getGame_id()
        );
    }

    /**
     * Loads a Club from the database by its ID.
     *
     * @param club_id The club ID to retrieve
     * @return The Club object, or null on error
     */
    public static Club loadClub(int club_id) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT name,address,telephone,email FROM clubs WHERE club_id=?")
        ) {
            statement.setInt(1, club_id);
            try (ResultSet rs = statement.executeQuery()) {
                Club club = new Club(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                club.setClub_id(club_id);
                return club;
            } catch (SQLException e) {
                new CustomAlert("Club Object Error", e.getMessage()).showAndWait();
                return null;
            }
        } catch (ValidationException | SQLException e) {
            new CustomAlert("Club Object Error", e.getMessage()).showAndWait();
            return null;
        }
    }

    /**
     * Returns the squad ID of a player, determined by their age (senior vs junior).
     * Returns 0 if the player is not assigned to any squad.
     *
     * @param player_id The player ID to check
     * @return The squad ID, or 0 if not found
     */
    public static int getPlayerSquadID(int player_id) {
        int age = 0;
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement stmtDob = connection.prepareStatement(
                        "SELECT date_of_birth FROM players WHERE player_id=?");
                PreparedStatement stmtSenior = connection.prepareStatement(
                        "SELECT squad_id FROM senior_squads WHERE ? IN (loose_head_prop,hooker,tight_head_prop," +
                        "second_row,second_row2,blind_side_flanker,open_side_flanker,number_8,scrum_half," +
                        "fly_half,left_wing,inside_centre,outside_center,right_side,full_back)");
                PreparedStatement stmtJunior = connection.prepareStatement(
                        "SELECT squad_id FROM junior_squads WHERE ? IN (loose_head_prop,hooker,tight_head_prop,scrum_half,fly_half,centre,wing)")
        ) {
            stmtDob.setInt(1, player_id);
            try (ResultSet rs = stmtDob.executeQuery()) {
                LocalDate date = LocalDate.parse(rs.getString(1), dt);
                age = Period.between(date, LocalDate.now()).getYears();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (age > 17) {
                stmtSenior.setInt(1, player_id);
                try (ResultSet rs = stmtSenior.executeQuery()) {
                    return rs.getInt(1) != 0 ? rs.getInt(1) : 0;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                stmtJunior.setInt(1, player_id);
                try (ResultSet rs = stmtJunior.executeQuery()) {
                    return rs.getInt(1) != 0 ? rs.getInt(1) : 0;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            new CustomAlert("Get the player's Squad ID", e.getMessage()).showAndWait();
            return 0;
        }
        return 0;
    }

    /**
     * Returns a Squad instance representing the type of squad a player belongs to (Senior or Junior).
     * Returns null if the player is not assigned to any squad.
     *
     * @param player_id The player ID to check
     * @return A SeniorSquad or JuniorSquad instance, or null
     */
    public static Squad getPlayerSquadType(int player_id) {
        int age = 0;
        DateTimeFormatter dt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement stmtDob = connection.prepareStatement(
                        "SELECT date_of_birth FROM players WHERE player_id=?");
                PreparedStatement stmtSenior = connection.prepareStatement(
                        "SELECT squad_id FROM senior_squads WHERE ? IN (loose_head_prop,hooker,tight_head_prop," +
                        "second_row,second_row2,blind_side_flanker,open_side_flanker,number_8,scrum_half," +
                        "fly_half,left_wing,inside_centre,outside_center,right_side,full_back)");
                PreparedStatement stmtJunior = connection.prepareStatement(
                        "SELECT squad_id FROM junior_squads WHERE ? IN (loose_head_prop,hooker,tight_head_prop,scrum_half,fly_half,centre,wing)")
        ) {
            stmtDob.setInt(1, player_id);
            try (ResultSet rs = stmtDob.executeQuery()) {
                LocalDate date = LocalDate.parse(rs.getString(1), dt);
                age = Period.between(date, LocalDate.now()).getYears();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (age > 17) {
                stmtSenior.setInt(1, player_id);
                try (ResultSet rs = stmtSenior.executeQuery()) {
                    return rs.getInt(1) != 0 ? new SeniorSquad() : null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                stmtJunior.setInt(1, player_id);
                try (ResultSet rs = stmtJunior.executeQuery()) {
                    return rs.getInt(1) != 0 ? new JuniorSquad() : null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            new CustomAlert("Get the player's Squad type", e.getMessage()).showAndWait();
            return null;
        }
        return null;
    }

    /**
     * Inserts a training session into the database and adds an entry in each player's training log.
     *
     * @param trainingSession The session to save
     * @param squad           The squad participating in the session
     * @return true if successful, false otherwise
     */
    public static boolean saveTrainingSession(TrainingSession trainingSession, Squad squad) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement stmtSession = connection.prepareStatement(
                        "INSERT INTO training_sessions (date,location_id,type_id) VALUES (?,?,?)");
                PreparedStatement stmtLog = connection.prepareStatement(
                        "INSERT INTO player_training_logs (profile_id,session_id) VALUES (?,?)");
                QueryResult qs1 = executeSelectQuery(
                        "SELECT session_id FROM training_sessions WHERE date=? AND location_id=?",
                        trainingSession.getDate(), trainingSession.getTrainingFacility())
        ) {
            if (squad == null) throw new ValidationException("The squad object is empty");
            if (qs1.getResultSet().getInt(1) != 0)
                throw new ValidationException("The facility is already booked that day");

            stmtSession.setString(1, trainingSession.getDate());
            stmtSession.setInt(2, trainingSession.getTrainingFacility());
            stmtSession.setInt(3, trainingSession.getTrainingType());
            stmtSession.executeUpdate();

            int session_id;
            try (QueryResult qs = executeSelectQuery("SELECT MAX(session_id) FROM training_sessions LIMIT 1")) {
                session_id = qs.getResultSet().getInt(1);
            } catch (SQLException e) {
                new CustomAlert("Save Training Session", e.getMessage()).showAndWait();
                e.printStackTrace();
                return false;
            }
            if (session_id == 0) throw new ValidationException("Wrong Session_id returned: 0");

            ArrayList<Player> allPlayers = new ArrayList<>();
            if (squad instanceof SeniorSquad s) {
                allPlayers.addAll(s.getSquadPlayers());
                allPlayers.addAll(s.getReplacementTeam().getReplacements());
            } else if (squad instanceof JuniorSquad j) {
                allPlayers.addAll(j.getSquadPlayers());
                allPlayers.addAll(j.getReplacementTeam().getReplacements());
            }

            for (Player player : allPlayers) {
                stmtLog.setInt(1, getID("SELECT profile_id FROM training_profiles WHERE player_id=?", player.getPlayerID()));
                stmtLog.setInt(2, session_id);
                stmtLog.executeUpdate();
            }
            return true;

        } catch (ValidationException | SQLException e) {
            new CustomAlert("Save Training Session", e.getMessage()).showAndWait();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Updates a player's training profile with new skill level values.
     * Only skills with a non-zero value in the levels list are updated.
     *
     * @param levels     List of skill level values (0 means skip that skill)
     * @param profile_id The profile ID to update
     * @return true if successful, false otherwise
     */
    public static boolean updateTrainingProfile(ArrayList<Integer> levels, int profile_id) {
        try {
            ArrayList<String> skillsToUpdate = new ArrayList<>();
            ArrayList<Integer> values = new ArrayList<>();
            String[] skillColumns = {"passing_skill", "running_skill", "support_skill", "tackling_skill", "decision_skill"};

            for (int i = 0; i < skillColumns.length; i++) {
                if (levels.get(i) != 0) {
                    skillsToUpdate.add(skillColumns[i]);
                    values.add(levels.get(i));
                }
            }

            if (skillsToUpdate.isEmpty())
                throw new ValidationException("There are no skills selected to update");

            StringBuilder query = new StringBuilder("UPDATE training_profiles SET ");
            for (int i = 0; i < skillsToUpdate.size(); i++) {
                query.append(skillsToUpdate.get(i)).append("=?");
                if (i < skillsToUpdate.size() - 1) query.append(",");
            }
            query.append(" WHERE profile_id=?");
            values.add(profile_id);

            if (executeUpdateQuery(query.toString(), values.toArray()))
                return true;
            else
                throw new ValidationException("The profile could not be updated");

        } catch (ValidationException e) {
            new CustomAlert("Update Profile Error", e.getMessage()).showAndWait();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Checks whether a player is part of a replacement team.
     *
     * @param player_id The player ID to check
     * @return true if the player is a replacement, false otherwise
     */
    public static boolean isReplacement(int player_id) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT repteam_id FROM replacement_team WHERE ? IN (player_1,player_2,player_3,player_4,player_5)")
        ) {
            statement.setInt(1, player_id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.getInt(1) != 0;
            }
        } catch (SQLException e) {
            new CustomAlert("Is Replacement", e.getMessage()).showAndWait();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returns the replacement team ID for a given player, or 0 if not found.
     *
     * @param player_id The player ID to check
     * @return The repteam_id, or 0
     */
    public static int getReplacementTeamID(int player_id) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT repteam_id FROM replacement_team WHERE ? IN (player_1,player_2,player_3,player_4,player_5)")
        ) {
            statement.setInt(1, player_id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            new CustomAlert("Get Replacement ID", e.getMessage()).showAndWait();
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Determines whether a replacement team belongs to a Senior or Junior squad.
     *
     * @param repTeamID The replacement team ID to check
     * @return A SeniorSquad or JuniorSquad instance, or null on error
     */
    public static Squad getReplacementSquadType(int repTeamID) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT squad_id FROM senior_squads WHERE repteam_id=?")
        ) {
            statement.setInt(1, repTeamID);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.getInt(1) != 0 ? new SeniorSquad() : new JuniorSquad();
            }
        } catch (SQLException e) {
            new CustomAlert("Replacement Squad type", e.getMessage()).showAndWait();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks whether a player is currently assigned to a squad.
     *
     * @param player The player ID to check
     * @return true if assigned, false otherwise
     */
    public static boolean playerIsAssignedToSquad(int player) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT is_assigned_to_squad FROM players WHERE player_id=?")
        ) {
            statement.setInt(1, player);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.getString(1).equals("YES");
            }
        } catch (SQLException e) {
            new CustomAlert("Is player assigned to squad", e.getMessage()).showAndWait();
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves a player's game performance rating to the database.
     *
     * @param profileID The player's training profile ID
     * @param gameID    The game ID
     * @param levelID   The performance level ID
     */
    public static void saveGamePerformance(int profileID, int gameID, int levelID) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO game_performances (profile_id,game_id,level_id) VALUES (?,?,?)")
        ) {
            statement.setInt(1, profileID);
            statement.setInt(2, gameID);
            statement.setInt(3, levelID);
            if (statement.executeUpdate() == 0)
                throw new ValidationException("No record was inserted");
        } catch (ValidationException | SQLException e) {
            new CustomAlert("Save game performance", e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the training profile for a given player.
     *
     * @param player The player to look up
     * @return The TrainingProfile object, or null on error
     */
    public static TrainingProfile getTrainingProfile(Player player) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT profile_id,passing_skill,running_skill,support_skill,tackling_skill,decision_skill " +
                        "FROM training_profiles WHERE player_id=?")
        ) {
            statement.setInt(1, player.getPlayerID());
            try (ResultSet rs = statement.executeQuery()) {
                TrainingProfile tp = new TrainingProfile();
                tp.setProfileID(rs.getInt(1));
                tp.setPassingLevel(TrainingProfile.getLevelDesc(rs.getInt(2)));
                tp.setRunningLevel(TrainingProfile.getLevelDesc(rs.getInt(3)));
                tp.setSupportLevel(TrainingProfile.getLevelDesc(rs.getInt(4)));
                tp.setTacklingLevel(TrainingProfile.getLevelDesc(rs.getInt(5)));
                tp.setDecisionLevel(TrainingProfile.getLevelDesc(rs.getInt(6)));
                return tp;
            }
        } catch (ValidationException | SQLException e) {
            new CustomAlert("Get training session", e.getMessage()).showAndWait();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a list of all training sessions attended by a given player.
     *
     * @param player The player to look up
     * @return An ArrayList of TrainingSession objects, or null on error
     */
    public static ArrayList<TrainingSession> getPlayerTrainingSessions(Player player) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement stmtLogs = connection.prepareStatement(
                        "SELECT session_id FROM player_training_logs WHERE profile_id=?");
                PreparedStatement stmtSession = connection.prepareStatement(
                        "SELECT date,location_id,type_id FROM training_sessions WHERE session_id=?")
        ) {
            ArrayList<TrainingSession> playerSessions = new ArrayList<>();
            stmtLogs.setInt(1, getID("SELECT profile_ID FROM training_profiles WHERE player_id=?", player.getPlayerID()));
            try (ResultSet logs = stmtLogs.executeQuery()) {
                while (logs.next()) {
                    stmtSession.setInt(1, logs.getInt(1));
                    try (ResultSet sessions = stmtSession.executeQuery()) {
                        playerSessions.add(new TrainingSession(sessions.getString(1), sessions.getInt(2), sessions.getInt(3)));
                    }
                }
            }
            return playerSessions;
        } catch (SQLException e) {
            new CustomAlert("Get training sessions", e.getMessage()).showAndWait();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Returns a list of all game performances for a given player.
     *
     * @param player The player to look up
     * @return An ArrayList of GamePerformance objects, or null on error
     */
    public static ArrayList<GamePerformance> getPlayerGamesPerformances(Player player) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(
                        "SELECT profile_id,game_id,level_description,date,DESCRIPTION,name " +
                        "FROM game_performances NATURAL JOIN performance_levels NATURAL JOIN games " +
                        "NATURAL JOIN game_location NATURAL JOIN clubs " +
                        "WHERE profile_id=(SELECT profile_id FROM training_profiles WHERE player_ID=?)")
        ) {
            ArrayList<GamePerformance> gamesPerfs = new ArrayList<>();
            statement.setInt(1, player.getPlayerID());
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    gamesPerfs.add(new GamePerformance(rs.getInt(1), rs.getInt(2),
                            rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
                }
            }
            return gamesPerfs;
        } catch (SQLException e) {
            new CustomAlert("Get Games Performances", e.getMessage()).showAndWait();
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Checks whether a Member (Player or NonPlayer) already exists in the database.
     *
     * @param member The member to check
     * @return true if found, false otherwise
     */
    public static boolean memberExists(Member member) {
        if (member instanceof Player pl) {
            try (QueryResult qs = executeSelectQuery(
                    "SELECT player_id FROM players WHERE first_name=? AND surname=? AND date_of_birth=?",
                    pl.getFirstName(), pl.getSurname(), pl.getDateOfBirth())) {
                return qs.getResultSet().next();
            } catch (ValidationException | SQLException e) {
                return false;
            }
        }
        if (member instanceof NonPlayer npl) {
            try (QueryResult qs = executeSelectQuery(
                    "SELECT member_id FROM non_players WHERE first_name=? AND surname=? AND telephone=?",
                    npl.getFirstName(), npl.getSurname(), npl.getTelephone())) {
                return qs.getResultSet().next();
            } catch (ValidationException | SQLException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Checks whether a Doctor or NextOfKin record already exists in the database.
     *
     * @param thirdParty The contact to check
     * @return true if found, false otherwise
     */
    public static boolean contactExists(ThirdParty thirdParty) {
        if (thirdParty instanceof Doctor doctor) {
            try (QueryResult qs = executeSelectQuery(
                    "SELECT doctor_id FROM player_doctors WHERE name=? AND surname=? AND telephone=?",
                    doctor.getFirstName(), doctor.getSurname(), doctor.getTelephone())) {
                return qs.getResultSet().next();
            } catch (ValidationException | SQLException e) {
                return false;
            }
        }
        if (thirdParty instanceof NextOfKin nok) {
            try (QueryResult qs = executeSelectQuery(
                    "SELECT kin_id FROM next_of_kin WHERE name=? AND surname=? AND telephone=?",
                    nok.getFirstName(), nok.getSurname(), nok.getTelephone())) {
                return qs.getResultSet().next();
            } catch (ValidationException | SQLException e) {
                return false;
            }
        }
        return false;
    }

    /**
     * Checks whether a NonPlayer is part of a coach or admin team.
     *
     * @param nonPlayer The member to check
     * @return true if found in a team, false otherwise
     */
    public static boolean isPartOfTeam(NonPlayer nonPlayer) {
        if (nonPlayer.getRole_id() == 1) {
            try (QueryResult qs = executeSelectQuery(
                    "SELECT cogroup_id FROM squad_coaches WHERE coach_1=? OR coach_2=? OR coach_3=?",
                    nonPlayer.getMember_id(), nonPlayer.getMember_id(), nonPlayer.getMember_id())) {
                return qs.getResultSet().next();
            } catch (ValidationException | SQLException e) {
                return false;
            }
        } else {
            try (QueryResult qs = executeSelectQuery(
                    "SELECT adteam_id FROM squad_admin_team WHERE chairman=? OR fixture_sec=?",
                    nonPlayer.getMember_id(), nonPlayer.getMember_id())) {
                return qs.getResultSet().next();
            } catch (ValidationException | SQLException e) {
                return false;
            }
        }
    }

    /**
     * Checks whether a squad already has a game recorded for a given date.
     *
     * @param date  The date to check
     * @param squad The squad to check
     * @return true if a game exists on that date, false otherwise
     */
    public static boolean isPlayingThatDay(String date, Squad squad) {
        if (squad instanceof SeniorSquad) {
            try (QueryResult qs = executeSelectQuery(
                    "SELECT game_id FROM senior_games_played WHERE date=?", date)) {
                return qs.getResultSet().next();
            } catch (ValidationException | SQLException e) {
                return false;
            }
        }
        if (squad instanceof JuniorSquad) {
            try (QueryResult qs = executeSelectQuery(
                    "SELECT game_id FROM junior_games_played WHERE date=?", date)) {
                return qs.getResultSet().next();
            } catch (ValidationException | SQLException e) {
                return false;
            }
        }
        return false;
    }

    // END OF CLASS
}