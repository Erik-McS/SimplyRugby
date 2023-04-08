package com.application.simplyrugby.Control;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBToolsTests {
    public static void main(String[] args) {



        try{
                ResultSet rs= DBTools.executeSelectQuery("SELECT * FROM next_of_kin;");
                while (rs.next()){
                    System.out.println("yes");
                    System.out.println("result 1: "+ rs.getString("name"));
                }


        }catch (SQLException e){e.printStackTrace();}

    }
}
