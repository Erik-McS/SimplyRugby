package com.application.simplyrugby;

import com.application.simplyrugby.Control.Doctor;
import com.application.simplyrugby.Control.NextOfKin;
import com.application.simplyrugby.Control.Player;
import javafx.fxml.FXML;

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
    private Doctor doc;
    private NextOfKin nok;

    public void receivePlayerObjects(Player newPlayer, NextOfKin nok, Doctor doc){

        if (newPlayer==null)
            System.out.println("newPlayer is empty");
        else if (nok==null)
            System.out.println("NoK is empty");
        else if (doc==null)
            System.out.println("Doc is empty");
        else {
            this.newPlayer=newPlayer;
            this.doc=doc;
            this.nok=nok;
            System.out.println("New Controller, received here properly: "+newPlayer.getFirstName());
            System.out.println(newPlayer.toString());
            System.out.println(nok.toString());
            System.out.println(doc.toString());
            updateLabels();
            bConfirmedPCreation.setOnAction((event)->{
                System.out.println("ca marche");
                newPlayer.saveMember(newPlayer);
                /*
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("The following record has been created:\n"+newPlayer.toString());
                alert.showAndWait();
                 */
                CustomAlert cs=new CustomAlert("Record Created",newPlayer.toString());
                cs.showAndWait();
                Stage stage=(Stage) bConfirmedPCreation.getScene().getWindow();
                stage.close();
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
        lCNameNOK.setText(nok.getFirstName());
        lCSurnameNOK.setText(nok.getSurname());
        lCTelNOK.setText(nok.getTelephone());
        lCNameDoc.setText(doc.getFirstName());
        lCSurnameDoc.setText(doc.getSurname());
        lCTelDoc.setText(doc.getTelephone());
    }
    public void initialize(){

        confirmMainPane.getStyleClass().addAll("bckg5","borderBlack");
        leftCPane.getStyleClass().addAll("bckg2","borderBlack");
        nokPaneC.getStyleClass().addAll("bckg3","borderBlack");
        CdocPane.getStyleClass().addAll("bckg4","borderBlack");
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
