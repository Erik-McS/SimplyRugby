package com.application.simplyrugby.System;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class will test if the database is present on the machine.<br>
 * By default, it will be placed at the folder root.
 * If the database do not exist, it will create it and populate it with some data
 * @author Erik McSeveney
 */
public class DatabaseCreation {

    // https://dba.stackexchange.com/questions/223267/in-sqlite-how-to-check-the-table-is-empty-or-not
    // String tableTest = "SELECT count(*) FROM (select 1 from players limit 1)";

    /**
     * The constructor will test if the DB exist and call the methods if needed.
     */
    public DatabaseCreation() {

        File db = new File("SimplyRugbyDB.db");
        if (db.isFile())
            System.out.println("Database exists");
        else {
            System.out.println("Database do not exists");
            createTables();
            insertData();
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
                DBTools.executeQuery(s);
                //System.out.println("Database Created:  \n"+s);
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
                DBTools.executeQuery(s);
            }
            System.out.println("Database Created\n");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


// END of CLASS
}