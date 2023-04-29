package com.application.simplyrugby.System;

import com.application.simplyrugby.Model.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;

/**
 * Class to interact with the Database. This class will follow the Singleton design pattern
 * @author Erik McSeveney
 */
public class DBTools {

    //private static Connection connection;
    //private static PreparedStatement statement;
    private DBTools(){}
    private static final String DBURL="JDBC:sqlite:SimplyRugbyDB.db";

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
     * Method to execute an INSERT, CREATE or UPDATE<br>
     * will return true if the execution is successful or false if not.<br>
     * it will use a Try with resource feature to make sure the connection and PreparedStatement are closed each time
     * @see <a href="https://www.geeksforgeeks.org/try-with-resources-feature-in-java/">Try with resources in Java</a>
     * @param query The query to execute
     * @return the result of the function
     */
    public static boolean executeUpdateQuery(String query){

        // try with the connection and statement as resources
        try(
                Connection connect=DriverManager.getConnection(DBURL);
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
     * the DBTools.closeConnections() must be called once the ResultSet that has been sent if not needed anymore.
     * @param query The SELECT query
     * @return the ResultSet requested.
     */
    public static QueryResult executeSelectQuery(String query) {

        try {
            // connecting to the database.
            Connection connection = DriverManager.getConnection(DBURL);
            // executing the query
            PreparedStatement statement = connection.prepareStatement(query);
            //
            ResultSet rs = statement.executeQuery();
            return new QueryResult(rs,connection,statement);
        } catch (SQLException e) {
            CustomAlert alert=new CustomAlert("Error",e.getMessage());
            alert.showAndWait();
        }
        return null;
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
                Connection connection = DriverManager.getConnection(DBURL);
                PreparedStatement statement = connection.prepareStatement(query)
                ){

            ResultSet rs = statement.executeQuery();
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
     * this will be used from now anywhere we test an object class.
     * @see <a href="https://www.baeldung.com/java-16-new-features">Pattern variables</a>
     * @param member The club member to save in the database.
     * @return True if successful, false otherwise.
     */
    public static boolean insertMember(Member member){
        // testing what kind of member the function received.
        // if it is a Player record:
        if (member instanceof Player player){
            // Execute the query and return a boolean.
            System.out.println("INSERT INTO players (first_name,surname,address,date_of_birth,gender,telephone,email,scrums_number,is_assigned_to_squad,doctor_id,kin_id " +
                    "VALUES " +
                    "('"+player.getFirstName()+"','"+player.getSurname()+"','"+ player.getAddress()+"','"+player.getDateOfBirth()+"','"+player.getGender()+"','"+player.getTelephone()
                    +"','"+player.getEmail()+"','"+player.getScrumsNumber()+"','"+player.isAssignedToSquad()+"','"+player.getDoctorID()+"','"+player.getKinID()+"')");
            return executeUpdateQuery("INSERT INTO players (first_name,surname,address,date_of_birth,gender,telephone,email,scrums_number,is_assigned_to_squad,doctor_id,kin_id) " +
                    "VALUES " +
                    "('"+player.getFirstName()+"','"+player.getSurname()+"','"+ player.getAddress()+"','"+player.getDateOfBirth()+"','"+player.getGender()+"','"+player.getTelephone()
                    +"','"+player.getEmail()+"','"+player.getScrumsNumber()+"','"+player.isAssignedToSquad()+"','"+player.getDoctorID()+"','"+player.getKinID()+"')");
        }
        // If it is a nonPlayer record:
        if (member instanceof NonPlayer nonPlayer){
            // execute the query
            return executeUpdateQuery("INSERT INTO non_players (first_name,surname,address,telephone,email,role_id) " +
                    "VALUES('"+nonPlayer.getFirstName()+"','"+nonPlayer.getSurname()+"','"+nonPlayer.getAddress()+"','"+nonPlayer.getTelephone()+
                    "','"+nonPlayer.getEmail()+"','"+nonPlayer.getRole_id()+"')");
        }
        // not used.
        return false;
    }

    /**
     * This function will look for a member of the club in the database from its memberID and create the corresponding object from it.<br>
     * the member object passed as parameter will be a dummy one, it will indicate to the function what kind of object to return.
     * @param member a dummy Player or NonPlayer object
     * @param memberID the mumberID to look for.
     * @return The record found, sent back as a Player or NonPlayer object.
     */
    public static Member loadMember(Member member,int memberID){

        // if the record to search for is for a player:
        if (member instanceof Player){

            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement("SELECT player_id,first_name,surname,address,date_of_birth,gender,telephone" +
                            ",email,scrums_number,is_assigned_to_squad,doctor_id,kin_id FROM players WHERE player_id="+memberID)
                    )
            {
                // the ResultSet returned by the query is used to create the object that is sent back to the calling class.
                ResultSet rs=statement.executeQuery();
                return new Player.PlayerBuilder().setPlayerID(rs.getInt(1)).setFirstName(rs.getString(2)).setSurname(rs.getString(3))
                        .setAddress(rs.getString(4)).setDoB(rs.getString(5)).setGender(rs.getString(6)).setTelephone(rs.getString(7))
                        .setEmail(rs.getString(8)).setScrumsNumber(rs.getInt(9)).setIsAssignedToSquad(rs.getString(10)).setDoctorID(rs.getInt(11))
                        .setKinID(rs.getInt(12)).Builder();
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
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement("SELECT member_id,first_name,surname,address,telephone" +
                            ",email,role_id FROM non_players WHERE member_id="+memberID)
            )
            {
             ResultSet rs=statement.executeQuery();
             return new NonPlayer(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4)
                     ,rs.getString(5),rs.getString(6),rs.getInt(7));
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
            return executeUpdateQuery("INSERT INTO next_of_kin (name,surname,telephone) VALUES ('"+nok.getFirstName()+"','"+nok.getSurname()+"','"+nok.getTelephone()+"')");
        }
        if (person instanceof Doctor doc){
            // inserting a Doctor record in DB
            return executeUpdateQuery("INSERT INTO player_doctors (name,surname,telephone) VALUES ('"+doc.getFirstName()+"','"+doc.getSurname()+"','"+doc.getTelephone()+"')");
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
         return executeUpdateQuery("INSERT INTO training_profiles (passing_skill,running_skill,support_skill,tackling_skill,decision_skill,player_id)" +
                " VALUES ('" + tp.getPassingLevel() + "','" + tp.getRunningLevel() + "','" + tp.getSupportLevel() + "','" + tp.getTacklingLevel() + "','" + tp.getDecisionLevel() + "','"+tp.getPlayerID()+"')");
    }

    /**
     * Function to search the database for a next of kin or doctor and send the record back as an object.<br>
     * This version will take the full SQL query to look for a record.
     * @param tp The object type to look for
     * @param query the SQL query to use
     * @return The NextOfKin or Doctor Object
     */
    public static ThirdParty selectContact(ThirdParty tp,String query){
        databaseConnect();

        if (tp instanceof NextOfKin nok){
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
                    )
            {
                // using the ResultSet to populate the NoK object and sending it back
                ResultSet rs = statement.executeQuery();
                nok.setKinID(rs.getInt(1));
                nok.setFirstName(rs.getString(2));
                nok.setSurname(rs.getString(3));
                nok.setTelephone(rs.getString(4));
                return nok;
            } catch (SQLException e) {
                // error message if any issues.
                CustomAlert alert=new CustomAlert("Error",e.getMessage());
                alert.showAndWait();
                return null;
            }
        }
        // same process if the record is for a doctor.
        if (tp instanceof Doctor doc){
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
                    )
            {
                ResultSet rs= statement.executeQuery();
                doc.setDoctorID(rs.getInt(1));
                doc.setFirstName(rs.getString(2));
                doc.setSurname(rs.getString(3));
                doc.setTelephone(rs.getString(4));
                return doc;
            }catch (SQLException e){
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
            String query="SELECT kin_id,name,surname,telephone FROM next_of_kin WHERE kin_id="+index;
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
            )
            {
                ResultSet rs = statement.executeQuery();
                nok.setKinID(rs.getInt(1));
                nok.setFirstName(rs.getString(2));
                nok.setSurname(rs.getString(3));
                nok.setTelephone(rs.getString(4));
                // nok.toString();
                return nok;
            } catch (SQLException e) {
                CustomAlert alert=new CustomAlert("Error",e.getMessage());
                alert.showAndWait();
                return null;
            }
        }
        if (tp instanceof Doctor doc){
            String query="SELECT doctor_id,name,surname,telephone FROM player_doctors WHERE doctor_id="+index;
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
            )
            {
                ResultSet rs= statement.executeQuery();
                doc.setDoctorID(rs.getInt(1));
                doc.setFirstName(rs.getString(2));
                doc.setSurname(rs.getString(3));
                doc.setTelephone(rs.getString(4));
                return doc;
            }catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error",e.getMessage());
                alert.showAndWait();
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
                Connection connection = DriverManager.getConnection(DBURL);
                PreparedStatement statement = connection.prepareStatement("SELECT role_description FROM non_players_roles WHERE role_id="+roleID)
                ){
            ResultSet rs=statement.executeQuery();
            return rs.getString(1);
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
        //databaseConnect();
        if (squad instanceof SeniorSquad){
            try (
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement1 = connection.prepareStatement("INSERT INTO replacement_team(PLAYER_1,PLAYER_2,PLAYER_3,PLAYER_4,PLAYER_5) VALUES (?,?,?,?,?)");
                    PreparedStatement statement2=connection.prepareStatement("INSERT INTO squad_coaches(COACH_1,COACH_2,COACH_3) VALUES (?,?,?)");
                    PreparedStatement statement3=connection.prepareStatement("INSERT INTO squad_admin_team(CHAIRMAN,FIXTURE_SEC) VALUES (?,?)");
                    PreparedStatement statement4=connection.prepareStatement( "INSERT INTO senior_squads(squad_name,loose_head_prop,hooker,tight_head_prop,second_row,second_row2,blind_side_flanker,open_side_flanker,number_8,scrum_half," +
                            "fly_half,left_wing,inside_centre,outside_center,right_side,full_back,cogroup_id,adteam_id,repteam_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    ){
                // opening the connection

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
                }
                statement4.setInt(17,cogroup_id);
                statement4.setInt(18,adteam_id);
                statement4.setInt(19,repteam_id);

                statement4.executeUpdate();
                PreparedStatement statement5=null;
                for(Player player:((SeniorSquad) squad).getSquadPlayers()){
                    statement5= connection.prepareStatement("UPDATE players SET is_assigned_to_squad='YES' WHERE player_id='"+player.getPlayerID()+"'");
                    statement5.executeUpdate();
                }
                statement5.close();
            }
            catch (SQLException e){
                CustomAlert alert=new CustomAlert("Create Squad Error",e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
            }


        }

        else if(squad instanceof JuniorSquad){

            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement1 = connection.prepareStatement("INSERT INTO replacement_team(PLAYER_1,PLAYER_2,PLAYER_3,PLAYER_4,PLAYER_5) VALUES (?,?,?,?,?)");
                    PreparedStatement statement2=connection.prepareStatement("INSERT INTO squad_coaches(COACH_1,COACH_2,COACH_3) VALUES (?,?,?)");
                    PreparedStatement statement3=connection.prepareStatement("INSERT INTO squad_admin_team(CHAIRMAN,FIXTURE_SEC) VALUES (?,?)");
                    PreparedStatement statement4=connection.prepareStatement( "INSERT INTO junior_squads(squad_name,loose_head_prop,hooker,tight_head_prop,scrum_half," +
                            "fly_half,centre,wing,cogroup_id,adteam_id,repteam_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)");
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
                }
                statement4.setInt(9,cogroup_id);
                statement4.setInt(10,adteam_id);
                statement4.setInt(11,repteam_id);

                statement4.executeUpdate();

                PreparedStatement statement5=null;
                for(Player player:((JuniorSquad) squad).getSquadPlayers()){
                    statement5= connection.prepareStatement("UPDATE players SET is_assigned_to_squad = 'YES' WHERE player_id='"+player.getPlayerID()+"'");
                    statement5.executeUpdate();
                }
                statement5.close();
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
                Connection connection = DriverManager.getConnection(DBURL);
                PreparedStatement statement = connection.prepareStatement("SELECT name,address,telephone,email FROM clubs WHERE club_id='" + club_id + "'");

        ) {
            ResultSet rs = statement.executeQuery();
            Club club = new Club(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
            club.setClub_id(club_id);
            return club;
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
        ResultSet rs;
        try(
                Connection connection=DriverManager.getConnection(DBURL);
                PreparedStatement statement= connection.prepareStatement("INSERT INTO clubs (name,address,telephone,email) VALUES (?,?,?,?)");
                QueryResult qs=executeSelectQuery("SELECT name FROM Clubs");
        ){

            if (qs!=null){
                rs=qs.getResultSet();
                // checking there are no name duplicates in the DB.
                while (rs.next()){
                    if (club.getName().equals(rs.getString(1)))
                        throw new ValidationException("A Club with this name already exists in the database");
                }
            }
            else
                throw new ValidationException("There are no Clubs saved in the database");
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

        try(
                Connection connection=DriverManager.getConnection(DBURL);
                PreparedStatement statement=connection.prepareStatement("INSERT INTO games (date,club_id,location_id) VALUES (?,?,?)");
                )
        {
            statement.setString(1,game.getDate());
            statement.setInt(2,game.getPlayingClub().getClub_id());
            statement.setInt(3,game.getLocation());
            statement.executeUpdate();
            connection.commit();
        }catch(SQLException e){
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
                 Connection connection=DriverManager.getConnection(DBURL);
                 PreparedStatement statement1=connection.prepareStatement("SELECT loose_head_prop,hooker,tight_head_prop,second_row,second_row2,blind_side_flanker,open_side_flanker,number_8,scrum_half," +
                         "fly_half,left_wing,inside_centre,outside_center,right_side,full_back FROM senior_squads WHERE squad_id='"+squad_id+"'");
                 PreparedStatement statement2=connection.prepareStatement("SELECT squad_name,cogroup_id,adteam_id,repteam_id FROM senior_squads WHERE squad_id='"+squad_id+"'");
                 PreparedStatement statement3=connection.prepareStatement("SELECT loose_head_prop,hooker,tight_head_prop,scrum_half,fly_half,centre,wing FROM senior_squads WHERE squad_id='"+squad_id+"'");
                 PreparedStatement statement4=connection.prepareStatement("SELECT squad_name,cogroup_id,adteam_id,repteam_id FROM senior_squads WHERE squad_id='"+squad_id+"'");
                 PreparedStatement statement5 = connection.prepareStatement("SELECT player_1,player_2,player_3,player_4,player_5 FROM replacement_team WHERE repteam_ID='" + squad_id + "'");
                 )
         {

             // need to review, test may not be necessary.
             if (squad==null)
                 throw new ValidationException("The Squad object cannot be null");
             // if we want a SeniorSquad
             else if (squad instanceof SeniorSquad){
                 // arraylist to contain the players of the squad.
                 ArrayList<Player> players =new ArrayList<>();
                 // query to get all the players in the squad
                 ResultSet rs=statement1.executeQuery();
                // assigning the players in the arraylist.
                 for(int i=1;i<=15;i++){
                     players.add((Player) loadMember(Player.dummyPlayer(),rs.getInt(i)));
                 }
                 // getting the squad name, and the IDs of the related teams. they will be used to get the corresponding team objects to create the Senior Squad.

                 rs=statement2.executeQuery();
                // we then create and return a SeniorSquad object
                 return new SeniorSquad(players,rs.getString(1),
                         (ReplacementTeam) loadSquad(new ReplacementTeam(),rs.getInt(4)),
                         (AdminTeam) loadTeam(new AdminTeam(),rs.getInt(3)),
                         (CoachTeam) loadTeam(new CoachTeam(),rs.getInt(3)));
             }
             // if we requested a JuniorSquad
             else if(squad instanceof JuniorSquad){
                 // arraylist to contain the players of the squad.
                 ArrayList<Player> players =new ArrayList<>();
                 // query to get all the players in the squad

                 ResultSet rs=statement3.executeQuery();
                 // assigning the players in the arraylist.
                 for(int i=1;i<=7;i++){
                     players.add((Player) loadMember(Player.dummyPlayer(),rs.getInt(i)));
                 }
                 // getting the squad name, and the IDs of the related teams. they will be used to get the corresponding team objects to create the Senior Squad.

                 rs=statement4.executeQuery();
                 // we then create and return a SeniorSquad object
                 return new JuniorSquad(players,rs.getString(1),
                         (ReplacementTeam) loadSquad(new ReplacementTeam(),rs.getInt(4)),
                         (AdminTeam) loadTeam(new AdminTeam(),rs.getInt(3)),
                         (CoachTeam) loadTeam(new CoachTeam(),rs.getInt(3)));

             }
             // finally, if we need to get a replacement team. which is a kind of squad as it is composed of playing members.
             else if (squad instanceof ReplacementTeam) {

                     ResultSet rs;
                     if (squad instanceof ReplacementTeam) {

                         ArrayList<Player> players = new ArrayList<>();
                         rs = statement5.executeQuery();
                         for(int i=1;i<=5;i++){
                             players.add((Player) loadMember(Player.dummyPlayer(),rs.getInt(i)));
                         }
                         return new ReplacementTeam(players);
                     }
             }
         }catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error Squad creation","Could not load the requested squad");
                e.printStackTrace();
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
        ResultSet rs;

            try(
                    Connection connection=DriverManager.getConnection(DBURL);
                    PreparedStatement statement1= connection.prepareStatement("SELECT chairman,fixture_sec FROM squad_admin_team WHERE adteam_id='"+team_id+"'");
                    PreparedStatement statement2= connection.prepareStatement("SELECT coach_1,coach_2,coach_3 FROM squad_coaches WHERE cogroup_id='"+team_id+"'");

                    ){
                // if the requested team is an AdminTeam
                if (memberTeam instanceof AdminTeam){

                    rs= statement1.executeQuery();
                    return new AdminTeam((NonPlayer) loadMember(new NonPlayer(),rs.getInt(1)),(NonPlayer) loadMember(new NonPlayer(),rs.getInt(2)));
                }
                // if it's a coach
                if (memberTeam instanceof CoachTeam){

                    rs= statement2.executeQuery();
                    return new CoachTeam((NonPlayer) loadMember(new NonPlayer(),rs.getInt(1)),(NonPlayer) loadMember(new NonPlayer(),rs.getInt(2)),
                            (NonPlayer) loadMember(new NonPlayer(), rs.getInt(3)));
                }
            }catch (ValidationException | SQLException e){
                CustomAlert alert = new CustomAlert("Error Team creation", "Could not load the requested team");
                e.printStackTrace();
                return null;
            }
            return null;
    }
    /**
     * Unused at the moment
     * @param player the profile to update.
     */
    public static void updatePlayerProfile(Player player){

    }
// END OF CLASS
}
