package com.application.simplyrugby.System;

import com.application.simplyrugby.Model.Player;

public class DBToolsTests {
    public static void main(String[] args) {



        try{

            /*
                            ResultSet rs= DBTools.executeSelectQuery("SELECT * FROM next_of_kin;");
                while (rs.next()){
                    System.out.println("yes");
                    System.out.println("result 1: "+ rs.getString("name"));
                }
             */


            Player pl=new Player.PlayerBuilder().setFirstName("Erik").setSurname("Mcs").setAddress("adr").setDoB("birth")
                    .setGender("Male").setEmail("email").setIsAssignedToSquad("No").setDoctorID(1).setKinID(1).setProfileID(0).setScrumsNumber(1238456)
                    .setTelephone("tel").Builder();

            //pl.toString();

            CustomAlert cs=new CustomAlert("Record Created",pl.toString());
            cs.showAndWait();

        }catch (ValidationException e){e.printStackTrace();}

    }
}
