package com.application.simplyrugby.System;

import com.application.simplyrugby.Model.*;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;


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
    public static boolean executeQuery(String query){
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
     * This function is used to the get ID from a table, using the provided SQL query string. <br>
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
            CustomAlert alert=new CustomAlert("Error while getting the requested ID",e.getMessage());
            alert.showAndWait();
            closeConnections();
        }
        return 0;
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
            return executeQuery("INSERT INTO players (first_name,surname,address,date_of_birth,gender,telephone,email,scrums_number,doctor_id,kin_id," +
                    "is_assigned_to_squad,profile_id) " +
                    "VALUES ('"+player.getFirstName()+"','"+player.getSurname()+"','"+player.getAddress()+"','"+player.getDateOfBirth()
                    +"','"+player.getGender()+"','"+player.getTelephone()+"','"+player.getEmail()+"','"+player.getScrumsNumber()+
                    "','"+player.getDoctorID()+"','"+player.getKinID()+"','"+player.isAssignedToSquad()+"','"+player.getProfileID()+"')");
        }
        // If it is a nonPlayer record:
        if (member instanceof NonPlayer nonPlayer){
            // execute the query
            return executeQuery("INSERT INTO non_players (first_name,surname,address,telephone,email,role_id) " +
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
                    ",email,scrums_number,doctor_id,kin_id,is_assigned_to_squad,profile_id FROM players WHERE player_id="+memberID;
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
                    )
            {
                // the ResultSet returned by the query is used to create the object that is sent back to the calling class.
                ResultSet rs=statement.executeQuery();
                return new Player.PlayerBuilder().setPlayerID(rs.getInt(1)).setFirstName(rs.getString(2)).setSurname(rs.getString(3))
                        .setAddress(rs.getString(4)).setDoB(rs.getString(5)).setGender(rs.getString(6)).setTelephone(rs.getString(7))
                        .setEmail(rs.getString(8)).setScrumsNumber(rs.getInt(9)).setDoctorID(rs.getInt(10)).setKinID(rs.getInt(11))
                        .setIsAssignedToSquad(rs.getString(12)).setProfileID(rs.getInt(13))
                        .Builder();
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
            return executeQuery("INSERT INTO next_of_kin (name,surname,telephone) VALUES ('"+nok.getFirstName()+"','"+nok.getSurname()+"','"+nok.getTelephone()+"')");
        }
        if (person instanceof Doctor doc){
            // inserting a Doctor record in DB
            return executeQuery("INSERT INTO player_doctors (name,surname,telephone) VALUES ('"+doc.getFirstName()+"','"+doc.getSurname()+"','"+doc.getTelephone()+"')");
        }
        return false;
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
// END OF CLASS
}
