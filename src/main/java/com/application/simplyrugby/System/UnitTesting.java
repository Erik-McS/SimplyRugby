package com.application.simplyrugby.System;

import com.application.simplyrugby.Control.Player;
import javafx.scene.control.Alert;

public class UnitTesting {
    public static void main(String[] args) {

        Player newPlayer;

        try {
            newPlayer=new Player.PlayerBuilder().setPlayerID(0).setFirstName("Erik").setSurname("Erik").setAddress("Erik")
                    .setDoB("Erik").setEmail("Erik").setGender("Erik").setScrumsNumber(1)
                    .setTelephone("Erik").setIsAssignedToSquad("No").setDoctorID(1).setKinID(1).setProfileID(1).Builder();
            System.out.println(newPlayer.toString());
        }catch (ValidationException e){
            System.out.println("Error");

            System.out.println(e.getMessage());

        }
    }
}
