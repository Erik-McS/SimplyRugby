package com.application.simplyrugby.System;

import com.application.simplyrugby.Control.*;

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
    /**
     *
     */
    private DBTools(){}
    private static final String DBURL="JDBC:sqlite:SimplyRugbyDB.db";
    public static void databaseConnect(){

        try {
            // loading sqlite/JDBC drivers
            Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
               NoSuchMethodException | InvocationTargetException e){
            e.printStackTrace();
        }
    }

    public static boolean executeQuery(String query){
        databaseConnect();
        // try with resource:
        // https://www.geeksforgeeks.org/try-with-resources-feature-in-java/
        try(
                Connection connect=DriverManager.getConnection(DBURL);
                PreparedStatement statement=connect.prepareStatement(query)
                )
        {
            statement.executeUpdate();
            return true;}
        catch (SQLException e){e.printStackTrace();return false;}

    }

    public static ResultSet executeSelectQuery(String query) {
        databaseConnect();
        try {
            Connection connection = DriverManager.getConnection(DBURL);
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void insertMember(Member member){
        databaseConnect();

        if (member instanceof Player){
            Player player=(Player) member;
            String s="INSERT INTO players (first_name,surname,address,date_of_birth,gender,telephone,email,scrums_number,doctor_id,kin_id," +
                    "is_assigned_to_squad,profile_id) " +
                    "VALUES ('"+player.getFirstName()+"','"+player.getSurname()+"','"+player.getAddress()+"','"+player.getDateOfBirth()
                    +"','"+player.getGender()+"','"+player.getTelephone()+"','"+player.getEmail()+"','"+player.getScrumsNumber()+
                    "','"+player.getDoctorID()+"','"+player.getKinID()+"','"+player.isAssignedToSquad()+"','"+player.getProfileID()+"')";
            System.out.println(s);
            executeQuery(s);
        }

        if (member instanceof NonPlayer){}
    }

    public static Member selectMember(){return null;}

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
                e.printStackTrace();
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
            }catch (SQLException e){e.printStackTrace();}
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
                e.printStackTrace();
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
            }catch (SQLException e){e.printStackTrace();}
        }
        return null;
    }

// END OF CLASS
}
