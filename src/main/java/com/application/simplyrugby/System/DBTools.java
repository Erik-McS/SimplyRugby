package com.application.simplyrugby.System;

import com.application.simplyrugby.Model.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;


/**
 * Class to interact with the Database. this class will follow the Singleton design pattern
 * @author Erik McSeveney
 */
public class DBTools {

    private static Connection connection;
    private static PreparedStatement statement;
    private DBTools(){}
    private static final String DBURL="JDBC:sqlite:SimplyRugbyDB.db";

    /**
     * This method load the JDBC drivers and allow access to a database
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
     * it will use a Try with resources feature to make sure the connection and PreparedStatement are closed each time
     * @see <a href="https://www.geeksforgeeks.org/try-with-resources-feature-in-java/">Try with resources in Java</a>
     * @param query The query to execute
     * @return the result of the function
     */
    public static boolean executeUpdateQuery(String query){
        // load the drivers
        databaseConnect();
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
    public static ResultSet executeSelectQuery(String query) {
        // cLoad the drivers
        databaseConnect();
        // normal try-catch. the closeConnections() must be executed by the calling class.
        try {
            // connecting to the database.
            connection = DriverManager.getConnection(DBURL);
            // executing the query
            statement = connection.prepareStatement(query);
            //
            ResultSet rs = statement.executeQuery();
            return rs;
        } catch (SQLException e) {
            CustomAlert alert=new CustomAlert("Error",e.getMessage());
            alert.showAndWait();
            closeConnections();
        }
        return null;
    }

    /**
     * This function is used to get an ID from a table, using the provided SQL query string. <br>
     * this is used for example, to get a member ID with the name-surname selected from a combobox.<br>
     * the closeConnections() functions must be called in the calling class after use.
     * @param query the SQL query to search for an ID.
     * @return The requested ID.
     */
    public static int getID(String query) {
        databaseConnect();
        try {
            connection = DriverManager.getConnection(DBURL);
            statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            // return the ID found
            return rs.getInt(1);
        } catch (SQLException e) {
            //CustomAlert alert=new CustomAlert("Error while getting the requested ID",e.getMessage());
            //alert.showAndWait();
            closeConnections();
            return 0;
        }
        finally {
            closeConnections();
        }
    }

    /**
     * Function to insert a Player or NonPlayer in the database. <br>
     * the test is done using 'pattern variable in Java 16'. this was suggested by the IDE. it replaces the explicit cast I was doing.<br>
     * this will be used from now anywhere we test an object class.
     * @see <a href="https://www.baeldung.com/java-16-new-features">Pattern variables</a>
     * @param member The club member to save in the database.
     * @return True if successful, false otherwise.
     */
    public static boolean insertMember(Member member){
        databaseConnect();
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
         databaseConnect();
        // if the record to search for is for a player:
        if (member instanceof Player){
            String query="SELECT player_id,first_name,surname,address,date_of_birth,gender,telephone" +
                    ",email,scrums_number,is_assigned_to_squad,doctor_id,kin_id FROM players WHERE player_id="+memberID;
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
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
            String query="SELECT member_id,first_name,surname,address,telephone" +
                    ",email,role_id FROM non_players WHERE member_id="+memberID;
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
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
     * Same usage as selectContact(ThirdParty,SQL query), but this version look for a record by the person ID.
     * @param tp the object type to look for.
     * @param index the ID to look for.
     * @return the found record.
     */
    public static ThirdParty selectContact(ThirdParty tp,int index){
        databaseConnect();
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

    public static void closeConnections(){
        try{
            if (connection!=null)
                connection.close();
            if (statement!=null)
                statement.close();
        }catch (SQLException e){
            CustomAlert alert=new CustomAlert("Error Closing the connections",e.getMessage());
            alert.showAndWait();
        }
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
     * Sa ve a Squad(Junior or Senior in the database.
     * @param squad the Squad to insert.
     */
    public static void saveSquad(Squad squad){
        //databaseConnect();
        if (squad instanceof SeniorSquad){


            try {
                // opening the connection
                Connection connection = DriverManager.getConnection(DBURL);
                PreparedStatement statement;
                // variables to save the inner teams IDs.

                int cogroup_id;
                int adteam_id;
                int repteam_id;
                // the arraylists to contains them.
                ArrayList<Integer> repTeam=new ArrayList<>();
                ArrayList<Integer> coTeam=new ArrayList<>();
                ArrayList<Integer> adTeam=new ArrayList<>();

                // creating an array with the Player_IDs of the replacement team
                for (int i = 0; i<((SeniorSquad) squad).getReplacementTeam().getReplacements().size(); i++){
                    repTeam.add(((SeniorSquad) squad).getReplacementTeam().getReplacements().get(i).getPlayerID());
                }
                // preparing the query to insert the rep team
                statement = connection.prepareStatement("INSERT INTO replacement_team(PLAYER_1,PLAYER_2,PLAYER_3,PLAYER_4,PLAYER_5) VALUES (?,?,?,?,?)");
                // going through the rep team array to set each statement parameters
                for (int i=0;i<repTeam.size();i++){
                    statement.setInt(i+1,repTeam.get(i));
                }
                // inserting the rep team
                statement.executeUpdate();
                // getting the repteam ID for later use when inserting the squad entry
                repteam_id=getID("SELECT repteam_id FROM replacement_team WHERE player_1='"+repTeam.get(0)+"' AND player_2='"+repTeam.get(1)+"'");

                // same thing for the coach team, same logic as above.
                for (int i=0;i<((SeniorSquad) squad).getCoachTeam().getCoaches().size();i++){
                    coTeam.add(((SeniorSquad) squad).getCoachTeam().getCoaches().get(i).getMember_id());
                }
                statement=connection.prepareStatement("INSERT INTO squad_coaches(COACH_1,COACH_2,COACH_3) VALUES (?,?,?)");
                for (int i=0;i<coTeam.size();i++){
                    statement.setInt(i+1,coTeam.get(i));
                }
                statement.executeUpdate();
                cogroup_id=getID("SELECT cogroup_id FROM squad_coaches WHERE coach_1='"+coTeam.get(0)+"' AND coach_2='"+coTeam.get(1)+"'");

                // same for the admin team

                for (int i=0;i<((SeniorSquad) squad).getAdminTeam().getAdmins().size();i++){
                    adTeam.add(((SeniorSquad) squad).getAdminTeam().getAdmins().get(i).getMember_id());
                }
                statement=connection.prepareStatement("INSERT INTO squad_admin_team(CHAIRMAN,FIXTURE_SEC) VALUES (?,?)");
                for (int i=0;i<adTeam.size();i++){
                    statement.setInt(i+1,adTeam.get(i));
                }
                statement.executeUpdate();
                adteam_id=getID("SELECT adteam_id FROM squad_admin_team WHERE CHAIRMAN='"+adTeam.get(0)+"'");

                //now we insert the squad in the database.

                statement=connection.prepareStatement( "INSERT INTO senior_squads(squad_name,loose_head_prop,hooker,tight_head_prop,second_row,second_row2,blind_side_flanker,open_side_flanker,number_8,scrum_half," +
                        "fly_half,left_wing,inside_centre,outside_center,right_side,full_back,cogroup_id,adteam_id,repteam_id) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

                ArrayList<Integer> squadPlayers=new ArrayList<>();
                for (int i=0;i<((SeniorSquad) squad).getSquadPlayers().size();i++){
                    squadPlayers.add(((SeniorSquad) squad).getSquadPlayers().get(i).getPlayerID());
                }
                statement.setString(1,((SeniorSquad) squad).getSquadName());
                for (int i=0;i<squadPlayers.size();i++){
                    statement.setInt(i+2,squadPlayers.get(i));
                }
                statement.setInt(17,cogroup_id);
                statement.setInt(18,adteam_id);
                statement.setInt(19,repteam_id);

                statement.executeUpdate();

                for(Player player:((SeniorSquad) squad).getSquadPlayers()){
                    statement= connection.prepareStatement("UPDATE players SET is_assigned_to_squad='YES' WHERE player_id='"+player.getPlayerID()+"'");
                    statement.executeUpdate();
                }
            }
            catch (SQLException e){
                e.printStackTrace();
                closeConnections();
            }
            finally {
                closeConnections();
            }

        }

        else if(squad instanceof JuniorSquad){

            try{
                // opening the connection
                Connection connection = DriverManager.getConnection(DBURL);
                PreparedStatement statement;
                // variables to save the inner teams IDs.

                int cogroup_id;
                int adteam_id;
                int repteam_id;
                // the arraylists to contains them.
                ArrayList<Integer> repTeam=new ArrayList<>();
                ArrayList<Integer> coTeam=new ArrayList<>();
                ArrayList<Integer> adTeam=new ArrayList<>();

                // creating an array with the Player_IDs of the replacement team
                for (int i = 0; i<((JuniorSquad) squad).getReplacementTeam().getReplacements().size(); i++){
                    repTeam.add(((JuniorSquad) squad).getReplacementTeam().getReplacements().get(i).getPlayerID());
                }
                // preparing the query to insert the rep team
                statement = connection.prepareStatement("INSERT INTO replacement_team(PLAYER_1,PLAYER_2,PLAYER_3,PLAYER_4,PLAYER_5) VALUES (?,?,?,?,?)");
                // going through the rep team array to set each statement parameters
                for (int i=0;i<repTeam.size();i++){
                    statement.setInt(i+1,repTeam.get(i));
                }
                // inserting the rep team
                statement.executeUpdate();
                // getting the repteam ID for later use when inserting the squad entry
                repteam_id=getID("SELECT repteam_id FROM replacement_team WHERE player_1='"+repTeam.get(0)+"' AND player_2='"+repTeam.get(1)+"'");

                // same thing for the coach team, same logic as above.
                for (int i=0;i<((JuniorSquad) squad).getCoachTeam().getCoaches().size();i++){
                    coTeam.add(((JuniorSquad) squad).getCoachTeam().getCoaches().get(i).getMember_id());
                }
                statement=connection.prepareStatement("INSERT INTO squad_coaches(COACH_1,COACH_2,COACH_3) VALUES (?,?,?)");
                for (int i=0;i<coTeam.size();i++){
                    statement.setInt(i+1,coTeam.get(i));
                }
                statement.executeUpdate();
                cogroup_id=getID("SELECT cogroup_id FROM squad_coaches WHERE coach_1='"+coTeam.get(0)+"' AND coach_2='"+coTeam.get(1)+"'");

                // same for the admin team

                for (int i=0;i<((JuniorSquad) squad).getAdminTeam().getAdmins().size();i++){
                    adTeam.add(((JuniorSquad) squad).getAdminTeam().getAdmins().get(i).getMember_id());
                }
                statement=connection.prepareStatement("INSERT INTO squad_admin_team(CHAIRMAN,FIXTURE_SEC) VALUES (?,?)");
                for (int i=0;i<adTeam.size();i++){
                    statement.setInt(i+1,adTeam.get(i));
                }
                statement.executeUpdate();
                adteam_id=getID("SELECT adteam_id FROM squad_admin_team WHERE CHAIRMAN='"+adTeam.get(0)+"'");


                //now we insert the squad in the database.

                statement=connection.prepareStatement( "INSERT INTO junior_squads(squad_name,loose_head_prop,hooker,tight_head_prop,scrum_half," +
                        "fly_half,centre,wing,cogroup_id,adteam_id,repteam_id) VALUES (?,?,?,?,?,?,?,?,?,?,?)");


                ArrayList<Integer> squadPlayers=new ArrayList<>();
                for (int i=0;i<((JuniorSquad) squad).getSquadPlayers().size();i++){
                    squadPlayers.add(((JuniorSquad) squad).getSquadPlayers().get(i).getPlayerID());
                }
                statement.setString(1,((JuniorSquad) squad).getSquadName());
                for (int i=0;i<squadPlayers.size();i++){
                    statement.setInt(i+2,squadPlayers.get(i));
                }
                statement.setInt(9,cogroup_id);
                statement.setInt(10,adteam_id);
                statement.setInt(11,repteam_id);

                statement.executeUpdate();

                for(Player player:((JuniorSquad) squad).getSquadPlayers()){
                    statement= connection.prepareStatement("UPDATE players SET is_assigned_to_squad = 'YES' WHERE player_id='"+player.getPlayerID()+"'");
                    statement.executeUpdate();
                }
            }
            catch (SQLException e){
                CustomAlert alert=new CustomAlert("Save Squad Error:",e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
            }

        }

    }
    public static void saveAdminTeam(MemberTeam adminTeam){

    }
    public static void updatePlayerProfile(Player player){

    }
// END OF CLASS
}
