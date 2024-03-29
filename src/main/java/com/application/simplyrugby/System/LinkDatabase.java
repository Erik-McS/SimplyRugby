package com.application.simplyrugby.System;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

/**
 * This class will test if the database is present on the machine.<br>
 * By default, it will be placed at the folder root.
 * If the database does not exist, it will create it and populate it with some data
 * @author Erik McSeveney
 */
public class LinkDatabase {
    /**
     * The constructor will test if the DB exist and call the methods if needed.
     */
    public LinkDatabase() {

        File db = new File("SimplyRugbyDB.db");
        if (db.isFile())
            System.out.println("Database exists");
        else {
            System.out.println("Database do not exists");
            createTables();
            insertData();
            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    ){
                Statement statement= connection.createStatement();
                statement.execute("PRAGMA journal_mode = wal;");
                statement.execute("PRAGMA synchronous = NORMAL;");
                statement.close();
            }catch (SQLException e){e.printStackTrace();}
        }
    }

    /**
     * This function will read the insertData.csv file and insert it in the database
     */
    private void insertData() {

        try {

            Scanner sc = new Scanner(new File("insertData.csv"));
            sc.useDelimiter(";");
            while (sc.hasNext()) {
                String s = sc.next();
                DBTools.executeUpdateQuery(s);
            }
        } catch (FileNotFoundException e) {
            e.getMessage();
        }
    }

    /**
     * This functions will read the CreateDatabase.csv file and create the tables.
     */
    private void createTables() {
        try {
            Scanner sc = new Scanner(new File("CreateDatabase.csv"));
            sc.useDelimiter(";");
            while (sc.hasNext()) {
                String s = sc.next();
                DBTools.executeUpdateQuery(s);
            }
            System.out.println("Database Created\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


// END of CLASS
}