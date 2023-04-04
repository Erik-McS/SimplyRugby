package com.application.simplyrugby.Control;

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
    //private static String dbURL="JDBC:sqlite:SimplyRugbyDB";
    public static void databaseConnect(){

        try {
            // loading sqlite/JDBC drivers
            Class.forName("org.sqlite.JDBC").getDeclaredConstructor().newInstance();
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException |
               NoSuchMethodException | InvocationTargetException e){
            e.printStackTrace();
        }

        try{
            // connecting to the test database
            // this will create the file if it doesn't exist already
            Connection connection = DriverManager.getConnection(DBURL);
        }catch (SQLException e){e.printStackTrace();}
    }

    public static void createTables() {

        try{
            Scanner sc = new Scanner(new File("CreateDatabase.csv"));
            sc.useDelimiter(";");
            while (sc.hasNext()) {
                String s = sc.next();
                System.out.println("Created: \n" + s);
                executeQuery(s);
            }
        }
        catch (FileNotFoundException e){e.printStackTrace();}

    }
    private static void executeQuery(String query){
        databaseConnect();
        //PreparedStatement statement;

        try{
            Connection connect=DriverManager.getConnection(DBURL);
            PreparedStatement statement=connect.prepareStatement(query);
            try{
                statement.executeUpdate();
                statement.close();
            }
            catch (SQLException e){e.printStackTrace();}
            finally {
                statement.close();
                connect.close();
            }

        }
        catch (SQLException e){e.printStackTrace();}
    }
// END OF CLASS
}
