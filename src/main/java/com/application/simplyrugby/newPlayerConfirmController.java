package com.application.simplyrugby;

import com.application.simplyrugby.Control.Doctor;
import com.application.simplyrugby.Control.NextOfKin;
import com.application.simplyrugby.Control.Player;
import javafx.fxml.FXML;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class newPlayerConfirmController {

    @FXML
    private Pane confirmMainPane,nokPaneC,CdocPane,leftCPane;
            ;
    @FXML
    private Button bConfirmedPCreation,bConfirmCancel;
    @FXML
    private Label lName,lSurname,lAddress,lTel,lEmail,lDoB,lGender,lScrums,lCNameNoK,lCSurnameNoK,lCTelNoK,lCNameNOK,
                    lCSurnameNOK,lCTelNOK,lCNameDoc,lCSurnameDoc,lCTelDoc;
    @FXML
    private Player newPlayer;

    public void receivePlayerObject(Player newPlayer, NextOfKin nok, Doctor doc){

        if (newPlayer==null)
            System.out.println("newPlayer is empty");
        else {
            this.newPlayer=newPlayer;
            System.out.println("New Controller, received here properly: "+newPlayer.getFirstName());
            updateLabels();
            bConfirmedPCreation.setOnAction((event)->{
                System.out.println("ca marche");
            });
        }
    }
    public void updateLabels(){

        lName.setText(newPlayer.getFirstName());
        lSurname.setText(newPlayer.getSurname());
        lAddress.setText(newPlayer.getAddress());
        lDoB.setText(newPlayer.getDateOfBirth());
        lTel.setText(newPlayer.getTelephone());
        lEmail.setText(newPlayer.getEmail());
        lGender.setText(newPlayer.getGender());
        lScrums.setText(Integer.toString(newPlayer.getScrumsNumber()));

    }
    public void initialize(){

        confirmMainPane.getStyleClass().add("bckg5");
        leftCPane.getStyleClass().add("bckg2");
        nokPaneC.getStyleClass().add("bckg3");
        CdocPane.getStyleClass().add("bckg4");
        bConfirmCancel.getStyleClass().add("bckg5");
        bConfirmedPCreation.getStyleClass().add("bckg5");


       if (newPlayer!=null)
           System.out.println("Object is here ");
        else
           System.out.println("Not here");

        bConfirmCancel.setOnAction((event)->{
            Stage stage=(Stage) bConfirmCancel.getScene().getWindow();
            stage.close();
        });
        //setName();
    }


}
