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
 * this static class has all the functions to interact with the database.<br>
 * During development, the class was optimised to prevent connection leaks that were causing database locks.<br>
 * to that effect, the class calls a HIKARICP class for connection pooling and extensive use of try-catch with resources.
 * @author Erik McSeveney
 */
public class DBTools {

    // private constructor to prevent objects creation
    private DBTools(){}

    /**
     * This method loads the JDBC drivers and allows access to a database
     */
    public static void databaseConnect(){
        try {
            // loading sqlite/JDBC drivers
            Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();

        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
               NoSuchMethodException | InvocationTargetException e){
            CustomAlert alert=new CustomAlert("Error loading the JDBC drivers",e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Method to execute an INSERT, CREATE or UPDATE statement.<br>
     * will return true if the execution is successful or false if not.<br>
     * it will use a Try with resource feature to make sure the connection and PreparedStatement are closed each time<br>
     * @see <a href="https://www.geeksforgeeks.org/try-with-resources-feature-in-java/">Try with resources in Java</a>
     * @param query The query to execute
     * @return the result of the function
     */
    public static boolean executeUpdateQuery(String query){

        // try with the connection and statement as resources
        try(
                Connection connect=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement=connect.prepareStatement(query)
                )
        {
            // execute the query
            statement.executeUpdate();
            // return successful
            return true;}
        catch (SQLException e){
            // issue during execution, return false
            CustomAlert alert=new CustomAlert("Error while trying to execute the query.",e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
            return false;}
    }

    /**
     * Function to execute a generic SELECT query.<br>
     * The class creates and returns a custom 'QueryResult' object. The object has a close() method that needs to be called<br>
     * to make sure the connection it used is closed properly.
     * @param query The SELECT query
     * @return the ResultSet requested.
     */
    public static QueryResult executeSelectQuery(String query) {

        try {
            // connecting to the database.
            Connection connection=ConnectionPooling.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            //
            ResultSet rs = statement.executeQuery();
            return new QueryResult(rs,connection,statement);
        } catch (SQLException e) {
            CustomAlert alert=new CustomAlert("Error Executing Query: ",e.getMessage());
            alert.showAndWait();
            return null;
        }
        //return null;
    }

    /**
     * This function is used to get an ID from a table, using the provided SQL query string. <br>
     * this is used, for example, to get a member ID with the name-surname selected from a combobox.<br>
     * the closeConnections() functions must be called in the calling class after use.
     * @param query the SQL query to search for an ID.
     * @return The requested ID.
     */
    public static int getID(String query) {

        try (
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet rs = statement.executeQuery()
                )
        {
            // return the ID found
            return rs.getInt(1);
        } catch (SQLException e) {
            CustomAlert alert=new CustomAlert("Error while getting the requested ID",e.getMessage());
            alert.showAndWait();
            return 0;
        }
    }

    /**
     * Function to insert a Player or NonPlayer in the database. <br>
     * the test is done using 'pattern variable in Java 16'. This was suggested by the IDE.
     * It replaces the explicit cast I was doing.<br>
     * this will be used from now anywhere we test an object class(when appropriate).
     * @see <a href="https://www.baeldung.com/java-16-new-features">Pattern variables</a>
     * @param member The club member to save in the database.
     * @return True if successful, false otherwise.
     */
    public static boolean insertMember(Member member){
        // testing what kind of member the function received.
        // if it is a Player record:
        if (member instanceof Player player){
            // Execute the query and return a boolean.
            if (!memberExists(player)){

                return executeUpdateQuery("INSERT INTO players (first_name,surname,address,date_of_birth,gender,telephone,email,scrums_number,is_assigned_to_squad,doctor_id,kin_id) " +
                        "VALUES " +
                        "('"+player.getFirstName()+"','"+player.getSurname()+"','"+ player.getAddress()+"','"+player.getDateOfBirth()+"','"+player.getGender()+"','"+player.getTelephone()
                        +"','"+player.getEmail()+"','"+player.getScrumsNumber()+"','"+player.isAssignedToSquad()+"','"+player.getDoctorID()+"','"+player.getKinID()+"')");
            }
            else
                return false;

        }
        // If it is a nonPlayer record:
        if (member instanceof NonPlayer nonPlayer){
            boolean exists=memberExists(member);
            // execute the query
            if (!exists){
                return executeUpdateQuery("INSERT INTO non_players (first_name,surname,address,telephone,email,role_id) " +
                        "VALUES('"+nonPlayer.getFirstName()+"','"+nonPlayer.getSurname()+"','"+nonPlayer.getAddress()+"','"+nonPlayer.getTelephone()+
                        "','"+nonPlayer.getEmail()+"','"+nonPlayer.getRole_id()+"')");
            }
            else
                return false;
        }
        // not used.
        return false;
    }

    /**
     * This function will look for a member of the club in the database from its memberID and create the corresponding object from it.<br>
     * the member object passed as parameter will be a test one, it will indicate to the function what kind of object to return.
     * @param member a test Player or NonPlayer object
     * @param memberID the memberID to look for.
     * @return The record found, sent back as a Player or NonPlayer object.
     */
    public static Member loadMember(Member member,int memberID){

        // if the record to search for is for a player:
        if (member instanceof Player){

            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement("SELECT player_id,first_name,surname,address,date_of_birth,gender,telephone" +
                            ",email,scrums_number,is_assigned_to_squad,doctor_id,kin_id FROM players WHERE player_id=?")

                    )
            {
                // the ResultSet returned by the query is used to create the object that is sent back to the calling class.
                statement.setInt(1,memberID);

                try(ResultSet rs=statement.executeQuery())
                {
                    return new Player.PlayerBuilder().setPlayerID(rs.getInt(1)).setFirstName(rs.getString(2)).setSurname(rs.getString(3))
                            .setAddress(rs.getString(4)).setDoB(rs.getString(5)).setGender(rs.getString(6)).setTelephone(rs.getString(7))
                            .setEmail(rs.getString(8)).setScrumsNumber(rs.getInt(9)).setIsAssignedToSquad(rs.getString(10)).setDoctorID(rs.getInt(11))
                            .setKinID(rs.getInt(12)).Builder();
                }catch (SQLException e){
                    // if any issue, display an error message
                    CustomAlert alert=new CustomAlert("Error while trying to create a Member Player object.",e.getMessage());
                    e.printStackTrace();
                    alert.showAndWait();
                    return null;
                }
            }
            catch (SQLException | ValidationException e){
                // if any issue, display an error message
                CustomAlert alert=new CustomAlert("Error while trying to create a Member Player object.",e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
                return null;}
        }
        // if the record to search for is a NonPlayer.
        if (member instanceof NonPlayer){
            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement("SELECT member_id,first_name,surname,address,telephone" +
                            ",email,role_id FROM non_players WHERE member_id=?")
            )
            {
                statement.setInt(1,memberID);
                try(ResultSet rs=statement.executeQuery())
                {
                    return new NonPlayer(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4)
                            ,rs.getString(5),rs.getString(6),rs.getInt(7));
                }catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error while trying to create a Member object.",e.getMessage());
                    e.printStackTrace();
                    alert.showAndWait();
                }
            }
            catch (SQLException | ValidationException e){
                CustomAlert alert=new CustomAlert("Error while trying to create a Member object.",e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
            }
        }
        // not used.
        return null;
    }

    /**
     * This function will insert a Next of Kin or Doctor record in the database.
     * @param person the person record to insert.
     * @return boolean as a result/
     */
    public static boolean insertContact(ThirdParty person){
        // testing the kind of record to insert. using pattern variable technique to cast.
        if (person instanceof NextOfKin nok){
            // inserting a Next of Kin record in DB
            if (!contactExists(nok))
                return executeUpdateQuery("INSERT INTO next_of_kin (name,surname,telephone) VALUES ('"+nok.getFirstName()+"','"+nok.getSurname()+"','"+nok.getTelephone()+"')");
            else
                return false;
        }
        if (person instanceof Doctor doc){
            // inserting a Doctor record in DB
            if (!contactExists(doc))
                return executeUpdateQuery("INSERT INTO player_doctors (name,surname,telephone) VALUES ('"+doc.getFirstName()+"','"+doc.getSurname()+"','"+doc.getTelephone()+"')");
            else
                return false;
        }
        return false;
    }

    /**
     * Function to create a profile entry in the database and link it to a player
     * @param tp The training profile
     * @return true of false, depending on SQL errors or not.
     */
    public static boolean insertTrainingProfile(TrainingProfile tp) {
        // insert the profile in the database. and test if ok
        try{
            return executeUpdateQuery("INSERT INTO training_profiles (passing_skill,running_skill,support_skill,tackling_skill,decision_skill,player_id)" +
                    " VALUES ('" + TrainingProfile.getLevelID(tp.getPassingLevel()) + "','" + TrainingProfile.getLevelID(tp.getRunningLevel()) + "','" + TrainingProfile.getLevelID(tp.getSupportLevel())
                    + "','" + TrainingProfile.getLevelID(tp.getTacklingLevel()) + "','" + TrainingProfile.getLevelID(tp.getDecisionLevel()) + "','"+tp.getPlayerID()+"')");
        }
        catch (ValidationException e){
            CustomAlert alert=new CustomAlert("Insert Training Profile",e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Function to search the database for a next of kin or doctor and send the record back as an object.<br>
     * This version will take the full SQL query to look for a record.
     * @param tp The object type to look for
     * @param query the SQL query to use
     * @return The NextOfKin or Doctor Object
     */
    public static ThirdParty selectContact(ThirdParty tp,String query){

        if (tp instanceof NextOfKin nok){
            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet rs = statement.executeQuery()
                    )
            {
                // using the ResultSet to populate the NoK object and sending it back
                nok.setKinID(rs.getInt(1));
                nok.setFirstName(rs.getString(2));
                nok.setSurname(rs.getString(3));
                nok.setTelephone(rs.getString(4));
                return nok;
            } catch (ValidationException|SQLException e) {
                // error message if any issues.
                CustomAlert alert=new CustomAlert("Error",e.getMessage());
                alert.showAndWait();
                return null;
            }
        }
        // same process if the record is for a doctor.
        if (tp instanceof Doctor doc){
            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet rs= statement.executeQuery()
                    )
            {
                doc.setDoctorID(rs.getInt(1));
                doc.setFirstName(rs.getString(2));
                doc.setSurname(rs.getString(3));
                doc.setTelephone(rs.getString(4));
                return doc;
            }catch (ValidationException| SQLException e){
                CustomAlert alert=new CustomAlert("Error",e.getMessage());
                alert.showAndWait();
            }
        }
        //not used.
        return null;
    }

    /**
     * Same usage as selectContact(ThirdParty, SQL query), but this version looks for a record by the person ID.
     * @param tp the object type to look for.
     * @param index the ID to look for.
     * @return the found record.
     */
    public static ThirdParty selectContact(ThirdParty tp,int index){

        if (tp instanceof NextOfKin nok){

            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement("SELECT kin_id,name,surname,telephone FROM next_of_kin WHERE kin_id=?")
            )
            {
                statement.setInt(1,index);
                try(ResultSet rs = statement.executeQuery())
                {
                    nok.setKinID(rs.getInt(1));
                    nok.setFirstName(rs.getString(2));
                    nok.setSurname(rs.getString(3));
                    nok.setTelephone(rs.getString(4));
                }catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error",e.getMessage());
                    alert.showAndWait();
                    return null;
                }
                return nok;
            } catch (ValidationException| SQLException e) {
                CustomAlert alert=new CustomAlert("Error",e.getMessage());
                alert.showAndWait();
                return null;
            }
        }
        if (tp instanceof Doctor doc){
            try(
                    Connection connection =ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement("SELECT doctor_id,name,surname,telephone FROM player_doctors WHERE doctor_id=?")
            )
            {
                statement.setInt(1,index);
                try(
                        ResultSet rs= statement.executeQuery()
                        )
                {
                    doc.setDoctorID(rs.getInt(1));
                    doc.setFirstName(rs.getString(2));
                    doc.setSurname(rs.getString(3));
                    doc.setTelephone(rs.getString(4));
                    return doc;
                }catch (ValidationException| SQLException e){
                    CustomAlert alert=new CustomAlert("Error",e.getMessage());
                    alert.showAndWait();
                    return null;
                }
            }catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error",e.getMessage());
                alert.showAndWait();
                return null;
            }
        }
        return null;
    }

    /**
     * Function to get the role description of a non-player member from its ID.
     * @param roleID The role ID to search for.
     * @return The role description.
     */
    public static String getRole(int roleID){
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT role_description FROM non_players_roles WHERE role_id=?")
                )
        {
            statement.setInt(1,roleID);
            try (ResultSet rs=statement.executeQuery())
            {
                return rs.getString(1);
            }
            catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error getting the Role description",e.getMessage());
                alert.showAndWait();
                return null;
            }
        }
        catch (SQLException e){
            CustomAlert alert=new CustomAlert("Error getting the Role description",e.getMessage());
            alert.showAndWait();
            return null;
        }
    }

    /**
     * Save a Squad(Junior or Senior) in the database.
     * @param squad the Squad to insert.
     */
    public static void saveSquad(Squad squad){

        if (squad instanceof SeniorSquad){
            try (
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement1 = connection.prepareStatement("INSERT INTO replacement_team(PLAYER_1,PLAYER_2,PLAYER_3,PLAYER_4,PLAYER_5) VALUES (?,?,?,?,?)");
                    PreparedStatement statement2=connection.prepareStatement("INSERT INTO squad_coaches(COACH_1,COACH_2,COACH_3) VALUES (?,?,?)");
                    PreparedStatement statement3=connection.prepareStatement("INSERT INTO squad_admin_team(CHAIRMAN,FIXTURE_SEC) VALUES (?,?)");
                    PreparedStatement statement4=connection.prepareStatement( "INSERT INTO senior_squads(squad_name,loose_head_prop,hooker,tight_head_prop,second_row,second_row2,blind_side_flanker,open_side_flanker,number_8,scrum_half," +
                            "fly_half,left_wing,inside_centre,outside_center,right_side,full_back,cogroup_id,adteam_id,repteam_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    PreparedStatement statement5= connection.prepareStatement("INSERT INTO training_profiles (passing_skill,running_skill,support_skill,tackling_skill,decision_skill,player_id) VALUES (1,1,1,1,1,?)")
                    ){

                // variables to save the inner teams IDs.
                int cogroup_id;
                int adteam_id;
                int repteam_id;
                // the arraylists to contain them.
                ArrayList<Integer> repTeam=new ArrayList<>();
                ArrayList<Integer> coTeam=new ArrayList<>();
                ArrayList<Integer> adTeam=new ArrayList<>();

                // creating an array with the Player_IDs of the replacement team
                for (int i = 0; i<((SeniorSquad) squad).getReplacementTeam().getReplacements().size(); i++){
                    repTeam.add(((SeniorSquad) squad).getReplacementTeam().getReplacements().get(i).getPlayerID());
                }
                // preparing the query to insert the rep team

                // going through the rep team array to set each statement parameter
                for (int i=0;i<repTeam.size();i++){
                    statement1.setInt(i+1,repTeam.get(i));
                    // also preparing the query to create the player training profile
                    statement5.setInt(1,repTeam.get(i));
                    // creating the player profile
                    statement5.executeUpdate();
                }
                // inserting the rep team
                statement1.executeUpdate();
                // getting the repteam ID for later use when inserting the squad entry
                repteam_id=getID("SELECT repteam_id FROM replacement_team WHERE player_1='"+repTeam.get(0)+"' AND player_2='"+repTeam.get(1)+"'");

                // same thing for the coach team, same logic as above.
                for (int i=0;i<((SeniorSquad) squad).getCoachTeam().getCoaches().size();i++){
                    coTeam.add(((SeniorSquad) squad).getCoachTeam().getCoaches().get(i).getMember_id());
                }

                for (int i=0;i<coTeam.size();i++){
                    statement2.setInt(i+1,coTeam.get(i));
                }
                statement2.executeUpdate();
                cogroup_id=getID("SELECT cogroup_id FROM squad_coaches WHERE coach_1='"+coTeam.get(0)+"' AND coach_2='"+coTeam.get(1)+"'");

                // same for the admin team

                for (int i=0;i<((SeniorSquad) squad).getAdminTeam().getAdmins().size();i++){
                    adTeam.add(((SeniorSquad) squad).getAdminTeam().getAdmins().get(i).getMember_id());
                }

                for (int i=0;i<adTeam.size();i++){
                    statement3.setInt(i+1,adTeam.get(i));
                }
                statement3.executeUpdate();
                adteam_id=getID("SELECT adteam_id FROM squad_admin_team WHERE CHAIRMAN='"+adTeam.get(0)+"'");

                //now we insert the squad in the database.
                ArrayList<Integer> squadPlayers=new ArrayList<>();
                for (int i=0;i<((SeniorSquad) squad).getSquadPlayers().size();i++){
                    squadPlayers.add(((SeniorSquad) squad).getSquadPlayers().get(i).getPlayerID());
                }
                statement4.setString(1,((SeniorSquad) squad).getSquadName());
                for (int i=0;i<squadPlayers.size();i++){
                    statement4.setInt(i+2,squadPlayers.get(i));
                    // preparing the profile insert query
                    statement5.setInt(1,squadPlayers.get(i));
                    // inserting the player profile
                    statement5.executeUpdate();
                }
                // assinging the associated replacement, coach and admin team.
                statement4.setInt(17,cogroup_id);
                statement4.setInt(18,adteam_id);
                statement4.setInt(19,repteam_id);
                statement4.executeUpdate();
                // updated the player squad status in the player table.
                for(Player player:((SeniorSquad) squad).getSquadPlayers()){
                    try (
                            Connection connection1=ConnectionPooling.getDataSource().getConnection();
                            PreparedStatement statement6= connection1.prepareStatement("UPDATE players SET is_assigned_to_squad='YES' WHERE player_id='"+player.getPlayerID()+"'")
                            ){
                        statement6.executeUpdate();
                    }
                    catch (SQLException w){w.printStackTrace();}
                }
            }
            catch (SQLException e){
                CustomAlert alert=new CustomAlert("Create Squad Error",e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
            }
        }
        // here we update the junior squad table.
        else if(squad instanceof JuniorSquad){
            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement1 = connection.prepareStatement("INSERT INTO replacement_team(PLAYER_1,PLAYER_2,PLAYER_3,PLAYER_4,PLAYER_5) VALUES (?,?,?,?,?)");
                    PreparedStatement statement2=connection.prepareStatement("INSERT INTO squad_coaches(COACH_1,COACH_2,COACH_3) VALUES (?,?,?)");
                    PreparedStatement statement3=connection.prepareStatement("INSERT INTO squad_admin_team(CHAIRMAN,FIXTURE_SEC) VALUES (?,?)");
                    PreparedStatement statement4=connection.prepareStatement( "INSERT INTO junior_squads(squad_name,loose_head_prop,hooker,tight_head_prop,scrum_half," +
                            "fly_half,centre,wing,cogroup_id,adteam_id,repteam_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
                    PreparedStatement statement6= connection.prepareStatement("INSERT INTO training_profiles (passing_skill,running_skill,support_skill,tackling_skill,decision_skill,player_id) VALUES (1,1,1,1,1,?)")
            ){

                // variables to save the inner teams IDs.
                int cogroup_id;
                int adteam_id;
                int repteam_id;
                // the arraylists to contain them.
                ArrayList<Integer> repTeam=new ArrayList<>();
                ArrayList<Integer> coTeam=new ArrayList<>();
                ArrayList<Integer> adTeam=new ArrayList<>();

                // creating an array with the Player_IDs of the replacement team
                for (int i = 0; i<((JuniorSquad) squad).getReplacementTeam().getReplacements().size(); i++){
                    repTeam.add(((JuniorSquad) squad).getReplacementTeam().getReplacements().get(i).getPlayerID());
                }
                // preparing the query to insert the rep team

                // going through the rep team array to set each statement parameter
                for (int i=0;i<repTeam.size();i++){
                    statement1.setInt(i+1,repTeam.get(i));
                    // creating the player profile
                    statement6.setInt(1,repTeam.get(i));
                    statement6.executeUpdate();
                }
                // inserting the rep team
                statement1.executeUpdate();
                // getting the repteam ID for later use when inserting the squad entry
                repteam_id=getID("SELECT repteam_id FROM replacement_team WHERE player_1='"+repTeam.get(0)+"' AND player_2='"+repTeam.get(1)+"'");

                // same thing for the coach team, same logic as above.
                for (int i=0;i<((JuniorSquad) squad).getCoachTeam().getCoaches().size();i++){
                    coTeam.add(((JuniorSquad) squad).getCoachTeam().getCoaches().get(i).getMember_id());
                }

                for (int i=0;i<coTeam.size();i++){
                    statement2.setInt(i+1,coTeam.get(i));
                }
                statement2.executeUpdate();
                cogroup_id=getID("SELECT cogroup_id FROM squad_coaches WHERE coach_1='"+coTeam.get(0)+"' AND coach_2='"+coTeam.get(1)+"'");

                // same for the admin team

                for (int i=0;i<((JuniorSquad) squad).getAdminTeam().getAdmins().size();i++){
                    adTeam.add(((JuniorSquad) squad).getAdminTeam().getAdmins().get(i).getMember_id());
                }

                for (int i=0;i<adTeam.size();i++){
                    statement3.setInt(i+1,adTeam.get(i));
                }
                statement3.executeUpdate();
                adteam_id=getID("SELECT adteam_id FROM squad_admin_team WHERE CHAIRMAN='"+adTeam.get(0)+"'");

                //now we insert the squad in the database.

                ArrayList<Integer> squadPlayers=new ArrayList<>();
                for (int i=0;i<((JuniorSquad) squad).getSquadPlayers().size();i++){
                    squadPlayers.add(((JuniorSquad) squad).getSquadPlayers().get(i).getPlayerID());
                }
                statement4.setString(1,((JuniorSquad) squad).getSquadName());
                for (int i=0;i<squadPlayers.size();i++){
                    statement4.setInt(i+2,squadPlayers.get(i));
                    statement6.setInt(1,squadPlayers.get(i));
                    statement6.executeUpdate();
                }
                statement4.setInt(9,cogroup_id);
                statement4.setInt(10,adteam_id);
                statement4.setInt(11,repteam_id);

                statement4.executeUpdate();

                for(Player player:((JuniorSquad) squad).getSquadPlayers()){
                    try(
                            Connection connection1=ConnectionPooling.getDataSource().getConnection();
                            PreparedStatement statement5= connection1.prepareStatement("UPDATE players SET is_assigned_to_squad = 'YES' WHERE player_id='"+player.getPlayerID()+"'")
                            )
                    {
                        statement5.executeUpdate();
                    }catch (SQLException e){e.printStackTrace();}

                }
            }
            catch (SQLException e){
                CustomAlert alert=new CustomAlert("Save Squad Error:",e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
            }
        }
    }

    /**
     * Method to load a club from the database.
     * @param club_id The Club ID to look for.
     * @return the club from the database as a Club object
     */
    public static Club  getClub(int club_id) {
        // getting the data from the database and creating the object.
        try (
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT name,address,telephone,email FROM clubs WHERE club_id=?")

        ) {
            statement.setInt(1,club_id);
            try(ResultSet rs = statement.executeQuery())
            {
                Club club = new Club(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                club.setClub_id(club_id);
                return club;
            }
            catch (NullPointerException|SQLException e){
                CustomAlert alert = new CustomAlert("Get Club error:", e.getMessage());
                alert.showAndWait();
                return null;
            }
        }
        // error message if any issues
        catch (ValidationException | SQLException e) {
            CustomAlert alert = new CustomAlert("Get Club error:", e.getMessage());
            alert.showAndWait();
            return null;
        }
    }
    /**
     * Saves a club in the database.
     * @param club The club to save.
     */
    public static void saveClub(Club club){

        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement= connection.prepareStatement("INSERT INTO clubs (name,address,telephone,email) VALUES (?,?,?,?)");
                QueryResult qs=executeSelectQuery("SELECT name FROM Clubs")
        ){

            if (qs!=null){
                // checking there are no name duplicates in the DB.
                while (qs.getResultSet().next()){
                    if (club.getName().equals(qs.getResultSet().getString(1)))
                        throw new ValidationException("A Club with this name already exists in the database");
                }
            }

            else
                throw new ValidationException("There are no Clubs saved in the database");
            //qs.close();
            // if not, we save the club in the DB.
            // setting each value in the statement
            statement.setString(1,club.getName());
            statement.setString(2, club.getAddress());
            statement.setString(3, club.getTelephone());
            statement.setString(4, club.getEmail());
            // executing the query, and testing that a row was created
            int rows=statement.executeUpdate();
            if (rows==0)
                throw new ValidationException("Save Club: No row was inserted");
        }catch(SQLException | ValidationException e){
            CustomAlert alert=new CustomAlert("Save Club Error:",e.getMessage());
            alert.showAndWait();
            e.printStackTrace();}
    }

    /**
     * This function will insert a game played by a squad in the database. <br>
     * It will update two tables: Games and the junior or senior played games.
     * @param game The game to save.
     */
    public static void saveGame(Game game){
       // ConnectionPooling.resetDatasource();
        int game_id=0;
        // First, we insert the game in the database.
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement=connection.prepareStatement("INSERT INTO games (date,club_id,location_id) VALUES (?,?,?)");
                PreparedStatement statement1=connection.prepareStatement("INSERT INTO senior_games_played VALUES (?,?,?)");
                PreparedStatement statement2=connection.prepareStatement("INSERT INTO junior_games_played VALUES (?,?,?)");

                )
        {
            statement.setString(1,game.getDate());
            statement.setInt(2,game.getPlayingClub().getClub_id());
            statement.setInt(3,game.getLocation());
            statement.executeUpdate();
            // getting the last gameID inserted
            try(QueryResult qs=executeSelectQuery("SELECT MAX(game_id) FROM games LIMIT 1"))
            {
                game_id=qs.getResultSet().getInt(1);
            }catch (SQLException e){
                CustomAlert alert=new CustomAlert("Save Game Error:",e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }

            // If the squad is a Senior squad, we need to update the intersection table Senior_games_played
            if (game.getSquad() instanceof SeniorSquad){
                SeniorSquad squad=(SeniorSquad) game.getSquad();
                statement1.setInt(1,getID("SELECT squad_id FROM senior_squads WHERE squad_name='"+squad.getSquadName()+"'"));
                statement1.setString(2, game.getDate());
                statement1.setInt(3,game_id);
                statement1.executeUpdate();
            }
            //else, the squad is junior.
            else{
                JuniorSquad squad=(JuniorSquad) game.getSquad();
                statement2.setInt(1,getID("SELECT squad_id FROM junior_squads WHERE squad_name='"+squad.getSquadName()+"'"));
                statement2.setString(2, game.getDate());
                statement2.setInt(3,game_id);
                statement2.executeUpdate();
            }

        }catch(ValidationException|SQLException e){
            CustomAlert alert=new CustomAlert("Save Game Error:",e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    /**
     * Function to load a Squad from the database and create an object from it.
     * @param squad The type of squad to return.
     * @param squad_id The squad ID to look for in the DB
     * @return the created Squad object
     */
    public static Squad loadSquad(Squad squad,int squad_id) throws ValidationException{
         try(
                 Connection connection=ConnectionPooling.getDataSource().getConnection();
                 PreparedStatement statement1=connection.prepareStatement("SELECT loose_head_prop,hooker,tight_head_prop,second_row,second_row2,blind_side_flanker,open_side_flanker,number_8,scrum_half," +
                         "fly_half,left_wing,inside_centre,outside_center,right_side,full_back FROM senior_squads WHERE squad_id=?");
                 PreparedStatement statement2=connection.prepareStatement("SELECT squad_name,cogroup_id,adteam_id,repteam_id FROM senior_squads WHERE squad_id=?");
                 PreparedStatement statement3=connection.prepareStatement("SELECT loose_head_prop,hooker,tight_head_prop,scrum_half,fly_half,centre,wing FROM junior_squads WHERE squad_id=?");
                 PreparedStatement statement4=connection.prepareStatement("SELECT squad_name,cogroup_id,adteam_id,repteam_id FROM junior_squads WHERE squad_id=?");
                 PreparedStatement statement5 = connection.prepareStatement("SELECT player_1,player_2,player_3,player_4,player_5 FROM replacement_team WHERE repteam_ID=?")
                 )
         {

             // if we want a SeniorSquad
            if (squad instanceof SeniorSquad){
                 // arraylist to contain the players of the squad.
                 ArrayList<Player> players =new ArrayList<>();
                 // query to get all the players in the squad
                statement1.setInt(1,squad_id);
                try(ResultSet rs1=statement1.executeQuery()){
                    // assigning the players in the arraylist.
                    for(int i=1;i<=15;i++){
                        players.add((Player) loadMember(Player.dummyPlayer(),rs1.getInt(i)));
                    }
                }catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error Squad creation","Could not load the requested squad");
                    e.printStackTrace();
                    alert.showAndWait();
                    return null;
                }

                 // getting the squad name, and the IDs of the related teams. they will be used to get the corresponding team objects to create the Senior Squad.
                 statement2.setInt(1,squad_id);
                try(ResultSet rs2=statement2.executeQuery())
                {
                    // we then create and return a SeniorSquad object
                    return new SeniorSquad(players,rs2.getString(1),
                            (ReplacementTeam) loadSquad(new ReplacementTeam(),rs2.getInt(4)),
                            (AdminTeam) loadTeam(new AdminTeam(),rs2.getInt(3)),
                            (CoachTeam) loadTeam(new CoachTeam(),rs2.getInt(3)));
                }catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error Squad creation","Could not load the requested squad");
                    e.printStackTrace();
                    alert.showAndWait();
                    return null;
                }
             }
             // if we requested a JuniorSquad
             else if(squad instanceof JuniorSquad){
                 // arraylist to contain the players of the squad.
                 ArrayList<Player> players =new ArrayList<>();
                 // assigning the players in the arraylist.
                 statement3.setInt(1,squad_id);
                 try(ResultSet rs3=statement3.executeQuery())
                 {
                     for(int i=1;i<=7;i++){
                         players.add((Player) loadMember(Player.dummyPlayer(),rs3.getInt(i)));
                     }
                 }catch (SQLException e){
                     CustomAlert alert=new CustomAlert("Error Squad creation","Could not load the requested squad");
                     e.printStackTrace();
                     alert.showAndWait();
                     return null;
                 }
                 statement4.setInt(1,squad_id);
                 try(ResultSet rs4=statement4.executeQuery())
                 {
                     // we then create and return a SeniorSquad object
                     return new JuniorSquad(players,rs4.getString(1),
                             (ReplacementTeam) loadSquad(new ReplacementTeam(),rs4.getInt(4)),
                             (AdminTeam) loadTeam(new AdminTeam(),rs4.getInt(3)),
                             (CoachTeam) loadTeam(new CoachTeam(),rs4.getInt(3)));
                 }catch (SQLException e){
                     CustomAlert alert=new CustomAlert("Error Squad creation","Could not load the requested squad");
                     e.printStackTrace();
                     alert.showAndWait();
                     return null;
                 }
             }
             // finally, if we need to get a replacement team. which is a kind of squad as it is composed of playing members.
             else if (squad instanceof ReplacementTeam) {

                         ArrayList<Player> players = new ArrayList<>();
                         statement5.setInt(1,squad_id);
                         try(ResultSet rs5 = statement5.executeQuery())
                         {
                             for(int i=1;i<=5;i++){
                                 players.add((Player) loadMember(Player.dummyPlayer(),rs5.getInt(i)));
                             }
                             return new ReplacementTeam(players);
                         }catch (SQLException e){
                             CustomAlert alert=new CustomAlert("Error Squad creation","Could not load the requested squad");
                             e.printStackTrace();
                             alert.showAndWait();
                             return null;}

             }
         }catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error Squad creation","Could not load the requested squad");
                e.printStackTrace();
                alert.showAndWait();
                return null;
            }
        return null;
    }

    /**
     * function to load an admin or coach team from the database and create<br>
     * the corresponding abject.
     * @param memberTeam The type of team we look for
     * @param team_id the ID of the team to look for
     * @return The requested team.
     */
    public static MemberTeam loadTeam(MemberTeam memberTeam,int team_id){


            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement1= connection.prepareStatement("SELECT chairman,fixture_sec FROM squad_admin_team WHERE adteam_id=?");
                    PreparedStatement statement2= connection.prepareStatement("SELECT coach_1,coach_2,coach_3 FROM squad_coaches WHERE cogroup_id=?")
                    ){
                // if the requested team is an AdminTeam
                if (memberTeam instanceof AdminTeam){
                    statement1.setInt(1,team_id);
                    try(ResultSet rs1= statement1.executeQuery())
                    {
                        return new AdminTeam((NonPlayer) loadMember(new NonPlayer(),rs1.getInt(1)),(NonPlayer) loadMember(new NonPlayer(),rs1.getInt(2)));
                    }catch (SQLException e){
                        CustomAlert alert = new CustomAlert("Error Team creation", "Could not load the requested team");
                        e.printStackTrace();
                        alert.showAndWait();
                        return null;
                    }
                }
                // if it's a coach
                if (memberTeam instanceof CoachTeam){
                    statement2.setInt(1,team_id);
                    try(ResultSet rs2= statement2.executeQuery())
                    {
                        return new CoachTeam((NonPlayer) loadMember(new NonPlayer(),rs2.getInt(1)),(NonPlayer) loadMember(new NonPlayer(),rs2.getInt(2)),
                                (NonPlayer) loadMember(new NonPlayer(), rs2.getInt(3)));
                    }catch (SQLException e){
                        CustomAlert alert = new CustomAlert("Error Team creation", "Could not load the requested team");
                        e.printStackTrace();
                        alert.showAndWait();
                        return null;
                    }
                }
            }catch (ValidationException | SQLException e){
                CustomAlert alert = new CustomAlert("Error Team creation", "Could not load the requested team");
                e.printStackTrace();
                alert.showAndWait();
                return null;
            }
            return null;
    }

    /**
     * Method to load a game from the database
     * @param game_id The game ID
     * @return The club Object
     */
    public static Game loadNonUpdatedGame(int game_id) {
        // managing resources/exceptions
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT date,club_id,location_id FROM games WHERE game_id=?")
        ) {
            // getting the requested game
            statement.setInt(1, game_id);
            try (ResultSet rs = statement.executeQuery()) {
                // creating a game with the data from the database
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
            CustomAlert alert = new CustomAlert("Game Object Error", e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
            return null;
        }
    }

    /**
     * This method will update an existing game with the match outcome and scores
     * @param game The game to update in the database.
     */
    public static void updateGame(Game game){
         try(
                 Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement= connection.prepareStatement("UPDATE games " +
                        "SET nb_of_try=?,nb_of_penalty=?,nb_of_conversion=?,nb_of_drop_goal=?,opponent_score=?,outcome_id=? WHERE game_id=?")
                 )
         {
            statement.setInt(1,game.getNbTry());
            statement.setInt(2,game.getNbPenalty());
            statement.setInt(3,game.getNbConversion());
            statement.setInt(4,game.getNbDropGoal());
            statement.setInt(5,game.getOpponentScore());
            statement.setInt(6,getID("SELECT outcome_id FROM game_outcomes WHERE outcome='"+game.getOutcome()+"'"));
            statement.setInt(7,game.getGame_id());
            int i= statement.executeUpdate();
             System.out.println("i: "+i);

         }catch (SQLException e){
             CustomAlert alert = new CustomAlert("Game Update Error", e.getMessage());
             e.printStackTrace();
             alert.showAndWait();
         }
    }

    /**
     * This method will create a Club object from the database.
     * @param club_id The club ID to retrieve
     * @return The requested Club object
     */
    public static Club loadClub(int club_id){
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement= connection.prepareStatement("SELECT name,address,telephone,email FROM clubs WHERE club_id=?")                )
        {
            statement.setInt(1,club_id);
            try(ResultSet rs= statement.executeQuery())
            {                Club club= new Club(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4));
                club.setClub_id(club_id);
                return club;
            }catch (SQLException e){
                CustomAlert alert=new CustomAlert("Club Object Error",e.getMessage());
                alert.showAndWait();
                return null;
            }
        }catch (ValidationException|SQLException e){
            CustomAlert alert=new CustomAlert("Club Object Error",e.getMessage());
            alert.showAndWait();
            return null;
        }
    }

    /**
     * Function to return the squad ID of a player<br>
     * if the squadID is 0, the player is not in any squad.
     * It will test the player DoB to determine which squad type
     * @param player_id Player ID
     * @return The Squad_ID
     */
    public static int getPlayerSquadID(int player_id){
        int age=0;
        LocalDate date;
        DateTimeFormatter dt=DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement=connection.prepareStatement("SELECT date_of_birth FROM players WHERE player_id=?");
                PreparedStatement statement1= connection.prepareStatement("SELECT squad_id FROM senior_squads WHERE ? " +
                        "IN (loose_head_prop, hooker, tight_head_prop, second_row, second_row2, blind_side_flanker, open_side_flanker, number_8, " +
                        "scrum_half, fly_half, left_wing, inside_centre, outside_center, right_side, full_back)");
                PreparedStatement statement2= connection.prepareStatement("SELECT squad_id FROM junior_squads WHERE ? " +
                        "IN (loose_head_prop,hooker,tight_head_prop,scrum_half,fly_half,centre,wing)")
                )
        {
            // getting the player date of birth
            statement.setInt(1,player_id);
            try(ResultSet rs=statement.executeQuery())
            {
                // calculating the age
                // https://www.w3schools.blog/java-period-class
                date=LocalDate.parse(rs.getString(1),dt);
                age= Period.between(date,LocalDate.now()).getYears();
            }catch (SQLException e){e.printStackTrace();}
            // if player > 17yo, we will look into the senior squads
            if (age>17){
                // looking for the player id in each role column
                statement1.setInt(1,player_id);
                try(ResultSet rs=statement1.executeQuery())
                {
                    // if found, return the squad id
                    if (rs.getInt(1)!=0)
                        return rs.getInt(1);
                    // otherwise return 0;
                    else
                        return 0;
                }
                catch (SQLException e){e.printStackTrace();}

            }
            // if under 17, we look into the junior squads
            else{
                statement.setInt(1,player_id);
                try(ResultSet rs=statement2.executeQuery())
                {
                    if (rs.getInt(1)!=0)
                        return rs.getInt(1);
                    else
                        return 0;
                }catch (SQLException e){e.printStackTrace();}
            }
        }catch (SQLException e){
            CustomAlert alert=new CustomAlert("Get the player's Squad ID",e.getMessage());
            alert.showAndWait();
            return 0;
        }
        return 0;
    }

    /**
     * Get the player's squad type.
     * @param player_id The player ID
     * @return A squad object of the corresponding type.
     */
    public static Squad getPlayerSquadType(int player_id){
        int age=0;
        LocalDate date;
        DateTimeFormatter dt=DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement=connection.prepareStatement("SELECT date_of_birth FROM players WHERE player_id=?");
                PreparedStatement statement1= connection.prepareStatement("SELECT squad_id FROM senior_squads WHERE ? " +
                        "IN (loose_head_prop, hooker, tight_head_prop, second_row, second_row2, blind_side_flanker, open_side_flanker, number_8, " +
                        "scrum_half, fly_half, left_wing, inside_centre, outside_center, right_side, full_back)");
                PreparedStatement statement2= connection.prepareStatement("SELECT squad_id FROM junior_squads WHERE ? " +
                        "IN (loose_head_prop,hooker,tight_head_prop,scrum_half,fly_half,centre,wing)")
        )
        {
            // getting the player date of birth
            statement.setInt(1,player_id);
            try(ResultSet rs=statement.executeQuery())
            {
                // calculating the age
                // https://www.w3schools.blog/java-period-class
                date=LocalDate.parse(rs.getString(1),dt);
                age= Period.between(date,LocalDate.now()).getYears();
            }catch (SQLException e){e.printStackTrace();}
            // if player > 17yo, we will look into the senior squads
            if (age>17){
                // looking for the player id in each role column
                statement1.setInt(1,player_id);
                try(ResultSet rs=statement1.executeQuery())
                {
                    int squad=rs.getInt(1);
                    // if found, return the squad id
                    if (rs.getInt(1)!=0)
                        return new SeniorSquad();
                        // otherwise return 0;
                    else
                        return null;
                }
                catch (SQLException e){e.printStackTrace();}
            }
            // if under 17, we look into the junior squads
            else{
                statement.setInt(1,player_id);
                try(ResultSet rs=statement2.executeQuery())
                {
                    if (rs.getInt(1)!=0)
                        return new JuniorSquad();
                    else
                        return null ;
                }catch (SQLException e){e.printStackTrace();}
            }
        }catch (SQLException e){
            CustomAlert alert=new CustomAlert("Get the player's Squad type",e.getMessage());
            alert.showAndWait();
            return null;
        }
        return null;

    }
    /**
     * insert a training session in the database, and create an entry in each player training log.
     * @param trainingSession the training session to save
     * @param squad the squad id of the participating squad.
     * @return If the session was created or not.
     */
    public static boolean saveTrainingSession(TrainingSession trainingSession,Squad squad){

        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement= connection.prepareStatement("INSERT INTO training_sessions (date,location_id,type_id) VALUES (?,?,?)");
                PreparedStatement statementTrainingLog= connection.prepareStatement("INSERT INTO player_training_logs (profile_id,session_id) VALUES (?,?)");
                QueryResult qs1=executeSelectQuery("SELECT session_id FROM training_sessions WHERE date='"+trainingSession.getDate()+"' AND location_id='"+trainingSession.getTrainingFacility()+"'")
                )
        {
            // checking that the squad object is correct
            if (squad==null)
                throw new ValidationException("The squad object is empty");

            // checking that the facility is not used that day.
            if (qs1.getResultSet().getInt(1)!=0)
                throw new ValidationException("The facility is already booked that day");

            // inserting the record
            statement.setString(1,trainingSession.getDate());
            statement.setInt(2,trainingSession.getTrainingFacility());
            statement.setInt(3,trainingSession.getTrainingType());
            statement.executeUpdate();
            // getting it session_id
            int session_id=0;
            try(QueryResult qs=executeSelectQuery("SELECT MAX(session_id) FROM training_sessions LIMIT 1"))
            {
                session_id=qs.getResultSet().getInt(1);
            }catch (SQLException e){
                CustomAlert alert=new CustomAlert("Get the player's Squad ID",e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
                return false;
            }
            if (session_id==0)
                throw new ValidationException("Wrong Session_id returned: 0");
            // if the training squad is senior
            if (squad instanceof SeniorSquad){
                SeniorSquad seniorSquad =(SeniorSquad)squad;
                // looping each player to add the training session to their profile training log.
                 for (Player player: seniorSquad.getSquadPlayers()){
                    statementTrainingLog.setInt(1,getID("SELECT profile_id FROM training_profiles WHERE player_id='"+player.getPlayerID()+"'"));
                    statementTrainingLog.setInt(2,session_id);
                    statementTrainingLog.executeUpdate();
                 }
                 // same to add the players from the replacement team
                for (Player player: seniorSquad.getReplacementTeam().getReplacements()){
                    statementTrainingLog.setInt(1,getID("SELECT profile_id FROM training_profiles WHERE player_id='"+player.getPlayerID()+"'"));
                    statementTrainingLog.setInt(2,session_id);
                    statementTrainingLog.executeUpdate();
                }
                return true;
            }

            else{
                JuniorSquad juniorSquad=(JuniorSquad) squad;
                for (Player player: juniorSquad.getSquadPlayers()){
                    statementTrainingLog.setInt(1,getID("SELECT profile_id FROM training_profiles WHERE player_id='"+player.getPlayerID()+"'"));
                    statementTrainingLog.setInt(2,session_id);
                    statementTrainingLog.executeUpdate();
                }
                for (Player player:juniorSquad.getReplacementTeam().getReplacements()){
                    statementTrainingLog.setInt(1,getID("SELECT profile_id FROM training_profiles WHERE player_id='"+player.getPlayerID()+"'"));
                    statementTrainingLog.setInt(2,session_id);
                    statementTrainingLog.executeUpdate();
                }
                return true;
            }
        }catch (ValidationException|SQLException e){
            CustomAlert alert=new CustomAlert("Get the player's Squad ID",e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
        return false;}
    }

    /**
     * Method to update a player's training profile with new performance level values.
     * @param levels The array containing the levels to update
     * @param profile_id the profile id to update
     * @return If the update was successful.
     */
    public static boolean updateTrainingProfile(ArrayList<Integer> levels,int profile_id){

        try
        {
            /*
            as there can be a different combination of skills to update, we will use two arrays to build a custom query.
            the first array will contain the name of the columns to update.
            if no skill was selected in the pane, the corresponding value in the level array is 0.
            testing if a level value is equal to 0 or not, we can build the list of skills to update and the new level.
            */
            ArrayList<String> skillsToUodate=new ArrayList<>();
            ArrayList<Integer> values =new ArrayList<>();
            //System.out.println("Size: "+levels.size());
            // building the two arrays.
            if (levels.get(0)!=0) {
                skillsToUodate.add("passing_skill");
                values.add(levels.get(0));
            }
            if (levels.get(1)!=0) {
                skillsToUodate.add("running_skill");
                values.add(levels.get(1));
            }
            if (levels.get(2)!=0) {
                skillsToUodate.add("support_skill");
                values.add(levels.get(2));
            }
            if (levels.get(3)!=0) {
                skillsToUodate.add("tackling_skill");
                values.add(levels.get(3));
            }
            if (levels.get(4)!=0) {
                skillsToUodate.add("decision_skill");
                values.add(levels.get(4));
            }
            // here we will create the sql query to use
            // if the skillsToUpdate has a 0 size, means that no skills were selected to update.
            if (skillsToUodate.size()!=0){
                String query="UPDATE training_profiles SET ";
                int i=0;
                for(String s:skillsToUodate){
                    query=query+s+"='"+values.get(i)+"',";
                    i++;
                }
                // take out the last comma and complete the query
                query=(query.substring(0,query.length()-1))+" WHERE profile_id='"+profile_id+"'";
                // testing that the update was done
                if (executeUpdateQuery(query)) {
                    System.out.println(query);
                    return true;
                }
                // if not, error message.
                else
                    throw new ValidationException("The profile could not be updated");
            }
            // if no skills to update, we display an error message.
            else
                throw new ValidationException("There are no skills selected to update");
        }catch (ValidationException e){
            CustomAlert alert=new CustomAlert("Update Profile Error",e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Check if the player is in a replacement team
     * @param player_id the player to check
     * @return True or False
     */
    public static boolean isReplacement(int player_id){
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement= connection.prepareStatement("SELECT repteam_id FROM replacement_team " +
                        "WHERE ? IN (player_1,player_2,player_3,player_4,player_5)")
                )
        {
            statement.setInt(1,player_id);
            try(ResultSet rs= statement.executeQuery()){
                if (rs.getInt(1)!=0)
                    return true;
                else
                    return false;
            }
        }catch (SQLException e){
            CustomAlert alert=new CustomAlert("Is Replacement",e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
            return false;}
    }

    /**
     * Get the replacement team ID of the player. iIf not found return 0.
     * @param player_id the player id
     * @return the repteam_ID
     */
    public static int getReplacementTeamID(int player_id){
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement= connection.prepareStatement("SELECT repteam_id FROM replacement_team " +
                        "WHERE ? IN (player_1,player_2,player_3,player_4,player_5)")
        )
        {
            statement.setInt(1,player_id);
            try(ResultSet rs= statement.executeQuery()){
                if (rs.getInt(1)!=0)
                    return rs.getInt(1);
                else
                    return 0;
            }
        }catch (SQLException e){
            CustomAlert alert=new CustomAlert("Get Replacement ID",e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
            return 0;}
    }

    /**
     * Check what kind of squad is a replacement team in
     * @param repTeamID the rep team to check
     * @return the type of squad.
     */
    public static Squad getReplacementSquadType(int repTeamID){
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement= connection.prepareStatement("SELECT squad_id from senior_squads WHERE repteam_id=?")
                )
        {
            // look for a squad id that has that replacement team
            statement.setInt(1,repTeamID);
            try(ResultSet rs=statement.executeQuery())
            {
                // if found in the senior squad table, sent a senior object
                if (rs.getInt(1)!=0)
                    return new SeniorSquad();
                // otherwise, it's a junior squad.
                else
                    return new JuniorSquad();
            }
        }catch (SQLException e){
            CustomAlert alert=new CustomAlert("Replacement Squad type",e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
            return null;}
    }

    /**
     * Function to check if the player is in a squad
     * @param player the player to check
     * @return true or false
     */
    public static boolean playerIsAssignedToSquad(int player){
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement= connection.prepareStatement("SELECT is_assigned_to_squad FROM players WHERE player_id=?");
                )
        {
            statement.setInt(1,player);
            try(ResultSet rs= statement.executeQuery())
            {
                if (rs.getString(1).equals("YES"))
                    return true;
                else
                    return false;
            }
        }catch (SQLException e){
            CustomAlert alert=new CustomAlert("Is player assigned to squad",e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
            return false;
        }
    }

    /**
     * Finction to save a game performance in the database
     * @param profileID the profile id of the player
     * @param gameID the game id to rate
     * @param levelID the performance level id
     */
    public static void saveGamePerformance(int profileID,int gameID,int levelID)
    {
        // try-with resource
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement= connection.prepareStatement("INSERT INTO game_performances (profile_id,game_id,level_id) " +
                    "VALUES (?,?,?)")
                )
        {
            // setting the prepared statement values
            statement.setInt(1,profileID);
            statement.setInt(2,gameID);
            statement.setInt(3,levelID);
            // checking the line was inserted
            if (statement.executeUpdate()==0)
                throw new ValidationException("No record was inserted");

        }catch (ValidationException|SQLException e){
            CustomAlert alert=new CustomAlert("Save game performance",e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
        }
    }

    /**
     * Method to get a training profile from the database
     * @param player The player to look for
     * @return the training profile
     */
    public static TrainingProfile getTrainingProfile(Player player) {
        try (
                Connection connection = ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT profile_id,passing_skill,running_skill,support_skill,tackling_skill,decision_skill FROM training_profiles " +
                        "WHERE player_id=?")
        ) {
            statement.setInt(1, player.getPlayerID());
            try (ResultSet rs = statement.executeQuery()) {
                TrainingProfile trainingProfile = new TrainingProfile();
                trainingProfile.setProfileID(rs.getInt(1));
                trainingProfile.setPassingLevel(TrainingProfile.getLevelDesc(rs.getInt(2)));
                trainingProfile.setRunningLevel(TrainingProfile.getLevelDesc(rs.getInt(3)));
                trainingProfile.setSupportLevel(TrainingProfile.getLevelDesc(rs.getInt(4)));
                trainingProfile.setTacklingLevel(TrainingProfile.getLevelDesc(rs.getInt(5)));
                trainingProfile.setDecisionLevel(TrainingProfile.getLevelDesc(rs.getInt(6)));
                return trainingProfile;
            }
        } catch (ValidationException | SQLException e) {
            CustomAlert alert = new CustomAlert("Get training session", e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
            return null;
        }
    }

    /**
     * Method to create an arraylist of the training sessions attended by a player
     * @param player the player
     * @return the session arraylist.
     */
    public static ArrayList<TrainingSession> getPlayerTrainingSessions(Player player){
        // preparing the connections and statement
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statementLogs= connection.prepareStatement("SELECT session_id FROM player_training_logs WHERE profile_id=?");
                PreparedStatement statementSession=connection.prepareStatement("SELECT date,location_id,type_id FROM training_sessions " +
                        "WHERE session_id=?")
                )
        {
            // declaring the sessions arrayList
            ArrayList<TrainingSession> playerSessions=new ArrayList<>();
            // looking ofr all the sessions attended by the player
            statementLogs.setInt(1,getID("SELECT profile_ID FROM training_profiles WHERE player_id='"+player.getPlayerID()+"'"));
            try(ResultSet logs=statementLogs.executeQuery())
            {
                // for each session attended, we will add the corresponding object to the arraylist
                while (logs.next())
                {
                    // getting the session details.
                    statementSession.setInt(1,logs.getInt(1));
                    try(ResultSet sessions=statementSession.executeQuery())
                    {
                        // creating the session object and adding it to the arrayList
                        playerSessions.add(new TrainingSession(sessions.getString(1),sessions.getInt(2),sessions.getInt(3)));
                    }
                }
            }
            return playerSessions;

        }catch (SQLException e){
            CustomAlert alert=new CustomAlert("Get training sessions",e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
            return null;
        }
    }
    public static ArrayList<GamePerformance> getPlayerGamesPerformances(Player player){
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statementGames=connection.prepareStatement("SELECT profile_id,game_id,level_description,date,DESCRIPTION,name " +
                        "FROM game_performances NATURAL JOIN performance_levels NATURAL JOIN games NATURAL JOIN game_location NATURAL JOIN clubs" +
                        " WHERE profile_id=(SELECT profile_id FROM training_profiles WHERE player_ID=?)");
                )
        {
            ArrayList<GamePerformance> gamesPerfs=new ArrayList<>();
            statementGames.setInt(1,player.getPlayerID());
            try(ResultSet rs= statementGames.executeQuery())
            {
                while (rs.next()){
                    gamesPerfs.add(new GamePerformance(rs.getInt(1),rs.getInt(2),rs.getString(3),rs.getString(4), rs.getString(5), rs.getString(6)));
                }
            }
            return gamesPerfs;
        }catch (SQLException e){
            CustomAlert alert=new CustomAlert("Get Games Performances",e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
            return null;
        }
    }

    /**
     * Method to test if a memeber already exists in the database
     * @param member the member to test
     * @return True or False
     */
    public static boolean memberExists(Member member){
        if (member instanceof Player pl){
            try(
                    QueryResult qs=executeSelectQuery("SELECT player_id FROM players WHERE first_name='"+pl.getFirstName()+"' " +
                            "AND surname='"+pl.getSurname()+"' AND date_of_birth='"+pl.getDateOfBirth()+"'")
                    )
            {
                return qs.getResultSet().next();

            }catch (ValidationException|SQLException e){
                return false;
            }
        }
        else if (member instanceof NonPlayer npl){

                try(
                        QueryResult qs=executeSelectQuery("SELECT member_id FROM non_players WHERE first_name='"+npl.getFirstName()+"' " +
                                "AND surname='"+npl.getSurname()+"' AND telephone='"+npl.getTelephone()+"'")
                )
                {
                    return qs.getResultSet().next();

                }catch (ValidationException|SQLException e){
                    return false;
                }
        }
        return false;
    }

    /**
     * Method to test if a doctor or next of kin already exists
     * @param thirdParty the contact to test
     * @return True or false
     */
    public static boolean contactExists(ThirdParty thirdParty){
        if (thirdParty instanceof Doctor doctor){
            try(
                    QueryResult qs=executeSelectQuery("SELECT doctor_id FROM player_doctors WHERE name='"+doctor.getFirstName()+"' " +
                            "AND surname='"+doctor.getSurname()+"' AND telephone='"+doctor.getTelephone()+"'")
            )
            {return qs.getResultSet().next();}catch (ValidationException|SQLException e){
                return false;
            }
        }
        if (thirdParty instanceof NextOfKin nextOfKin){
            try(
                    QueryResult qs=executeSelectQuery("SELECT kin_id FROM next_of_kin WHERE name='"+nextOfKin.getFirstName()+"' " +
                            "AND surname='"+nextOfKin.getSurname()+"' AND telephone='"+nextOfKin.getTelephone()+"'")
            )
            { return qs.getResultSet().next(); }catch (ValidationException|SQLException e){
                return false;
            }

        }
        return false;
    }
// END OF CLASS
}
