package com.application.simplyrugby.System;

import com.application.simplyrugby.Control.*;
import com.application.simplyrugby.CustomAlert;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

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
            CustomAlert alert=new CustomAlert("Error",e.getMessage());
            alert.showAndWait();
        }
    }

    /**
     * Method to execute an INSERT, CREATE or UPDATE<br>
     * will return true if the execution is successful or false if not
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
            // return sucessful
            return true;}
        catch (SQLException e){
            // issue during execution, return false
            CustomAlert alert=new CustomAlert("Error",e.getMessage());
            alert.showAndWait();
            return false;}
    }



    /**
     * Function to execute a
     * @param query
     * @return
     */
    public static ResultSet executeSelectQuery(String query) {
        databaseConnect();
        try {
            connection = DriverManager.getConnection(DBURL);
            statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            return rs;
        } catch (SQLException e) {
            CustomAlert alert=new CustomAlert("Error",e.getMessage());
            alert.showAndWait();
            closeConnections();
        }
        return null;
    }
    public static int getID(String query) {
        databaseConnect();
        try {
            connection = DriverManager.getConnection(DBURL);
            statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();

            return rs.getInt(1);
        } catch (SQLException e) {
            CustomAlert alert=new CustomAlert("Error",e.getMessage());
            alert.showAndWait();
            closeConnections();
        }
        return 0;
    }
    public static boolean insertMember(Member member){
        databaseConnect();

        if (member instanceof Player){
            Player player=(Player) member;
            String s="INSERT INTO players (first_name,surname,address,date_of_birth,gender,telephone,email,scrums_number,doctor_id,kin_id," +
                    "is_assigned_to_squad,profile_id) " +
                    "VALUES ('"+player.getFirstName()+"','"+player.getSurname()+"','"+player.getAddress()+"','"+player.getDateOfBirth()
                    +"','"+player.getGender()+"','"+player.getTelephone()+"','"+player.getEmail()+"','"+player.getScrumsNumber()+
                    "','"+player.getDoctorID()+"','"+player.getKinID()+"','"+player.isAssignedToSquad()+"','"+player.getProfileID()+"')";
            //System.out.println(s);
            return executeQuery(s);
        }

        if (member instanceof NonPlayer){}
        return false;
    }

    public static Member loadMember(Member member,int memberID){
         databaseConnect();

        if (member instanceof Player){
            String query="SELECT player_id,first_name,surname,address,date_of_birth,gender,telephone" +
                    ",email,scrums_number,doctor_id,kin_id,is_assigned_to_squad,profile_id FROM players WHERE player_id="+memberID;
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
                    )
            {
                ResultSet rs=statement.executeQuery();
                return new Player.PlayerBuilder().setPlayerID(rs.getInt(1)).setFirstName(rs.getString(2)).setSurname(rs.getString(3))
                        .setAddress(rs.getString(4)).setDoB(rs.getString(5)).setGender(rs.getString(6)).setTelephone(rs.getString(7))
                        .setEmail(rs.getString(8)).setScrumsNumber(rs.getInt(9)).setDoctorID(rs.getInt(10)).setKinID(rs.getInt(11))
                        .setIsAssignedToSquad(rs.getString(12)).setProfileID(rs.getInt(13))
                        .Builder();

            }
            catch (SQLException | ValidationException e){
                CustomAlert alert=new CustomAlert("Error",e.getMessage());
                alert.showAndWait();
                return null;}
        }

        if (member instanceof NonPlayer){
            String query="SELECT member_id,first_name,surname,address,telephone" +
                    ",email,role_id FROM players WHERE member_id="+memberID;
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
            )
            {
             ResultSet rs=statement.executeQuery();
             return new NonPlayer(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4)
                     ,rs.getString(5),rs.getString(6),rs.getInt(7));
            }
            catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error",e.getMessage());
                alert.showAndWait();
            }
        }

        return null;
    }

    public static boolean insertContact(ThirdParty person){
        if (person instanceof NextOfKin){
            NextOfKin nok=(NextOfKin) person;
            return executeQuery("INSERT INTO next_of_kin (name,surname,telephone) VALUES ('"+nok.getFirstName()+"','"+nok.getSurname()+"','"+nok.getTelephone()+"')");
        }
        if (person instanceof Doctor){
            Doctor doc=(Doctor) person;
            return executeQuery("INSERT INTO player_doctors (name,surname,telephone) VALUES ('"+doc.getFirstName()+"','"+doc.getSurname()+"','"+doc.getTelephone()+"')");
        }
        return false;
    }

    public static ThirdParty selectContact(ThirdParty tp,String query){
        databaseConnect();
        if (tp instanceof NextOfKin){
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
                    )
            {
                NextOfKin nok=(NextOfKin)tp;
                ResultSet rs = statement.executeQuery();
                nok.setKinID(rs.getInt(1));
                nok.setFirstName(rs.getString(2));
                nok.setSurname(rs.getString(3));
                nok.setTelephone(rs.getString(4));
                return nok;
            } catch (SQLException e) {
                CustomAlert alert=new CustomAlert("Error",e.getMessage());
                alert.showAndWait();
                return null;
            }
        }
        if (tp instanceof Doctor){
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
                    )
            {
                Doctor doc=(Doctor) tp;
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

    public static ThirdParty selectContact(ThirdParty tp,int index){
        databaseConnect();
        String query="SELECT kin_id,name,surname,telephone FROM next_of_kin WHERE kin_id="+index;

        if (tp instanceof NextOfKin){
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
            )
            {
                NextOfKin nok=(NextOfKin)tp;
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
        if (tp instanceof Doctor){
            try(
                    Connection connection = DriverManager.getConnection(DBURL);
                    PreparedStatement statement = connection.prepareStatement(query)
            )
            {
                Doctor doc=(Doctor) tp;
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
            CustomAlert alert=new CustomAlert("Error",e.getMessage());
            alert.showAndWait();
        }
    }
// END OF CLASS
}
