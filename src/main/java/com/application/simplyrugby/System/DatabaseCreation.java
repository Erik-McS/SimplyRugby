package com.application.simplyrugby.System;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DatabaseCreation {

    // https://dba.stackexchange.com/questions/223267/in-sqlite-how-to-check-the-table-is-empty-or-not
    // String tableTest = "SELECT count(*) FROM (select 1 from players limit 1)";

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
    private String getTestString(String table){
        return "SELECT count(*) FROM (select 1 from "+table+" limit 1)";
    }
// END of CLASS
}