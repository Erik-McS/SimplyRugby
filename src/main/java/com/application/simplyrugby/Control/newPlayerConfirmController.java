package com.application.simplyrugby.Control;

import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.Model.Doctor;
import com.application.simplyrugby.Model.NextOfKin;
import com.application.simplyrugby.Model.Player;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
// import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * THis class controls the Confirmation window for the Create Player function.
 */
public class newPlayerConfirmController {

    @FXML
    private Pane confirmMainPane,nokPaneC,CdocPane,leftCPane;
    @FXML
    private Button bConfirmedPCreation,bConfirmCancel;
    @FXML
    private Label lName,lSurname,lAddress,lTel,lEmail,lDoB,lGender,lScrums,lCNameNOK,
                    lCSurnameNOK,lCTelNOK,lCNameDoc,lCSurnameDoc,lCTelDoc;
    @FXML
    private Player newPlayer;
    private Doctor doc;
    private NextOfKin nok;

    /**
     * This method is an important method after initialize()<br>
     * it is called by the previous view to pass 3 objects to this controller, so it can use it to display additional <br>
     * information.
     * Due to initialisation and visibility between this method and initialize(), the createPlayer button will be coded <br>
     * in this method, as the objects cannot be seen by initialize().
     * @param newPlayer The player object created in the previous form.
     * @param nok The Next of Kin selected or created in the previous form.
     * @param doc The Doctor created or selected in the previous form.
     */
    public void receivePlayerObjects(Player newPlayer, NextOfKin nok, Doctor doc, Image consentForm,int age){
        // testing the objects exist

            // assigning the passed objects to local variables.
            this.newPlayer=newPlayer;
            this.doc=doc;
            this.nok=nok;
            // calling the function to update all the labels with the data provided by the 3 objects.
            updateLabels();
            // setting the event handler of the create Player button
            bConfirmedPCreation.setOnAction((event)->{
                // saving the new player in the database, the saveMember() will return true no error happened
                if (newPlayer.saveMember(newPlayer)){

                    // create a confirmation window
                    CustomAlert cs=new CustomAlert("Player Record Created",
                            "Player : "+newPlayer.getFirstName()+" "+newPlayer.getSurname());
                    cs.showAndWait();
                    Stage stage=(Stage) bConfirmedPCreation.getScene().getWindow();
                    stage.close();
                }
                // if error, display a message
                else {
                    // create a confirmation window""
                    CustomAlert cs = new CustomAlert("Error", "Error: the record could not be created");
                    cs.showAndWait();
                    Stage stage = (Stage) bConfirmedPCreation.getScene().getWindow();
                    stage.close();
                }
            });
    }

    /**
     * Function to display the info received by the controller.
     */
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

    /**
     * the initialize() function set up the styles of the different elements inside it. <br>
     * It also adds the event handler of the cancel button.
     */
    public void initialize(){
           // set the styles from the assigned css file
        confirmMainPane.getStyleClass().addAll("bckg5","borderBlack");
        leftCPane.getStyleClass().addAll("bckg2","borderBlack");
        nokPaneC.getStyleClass().addAll("bckg3","borderBlack");
        CdocPane.getStyleClass().addAll("bckg4","borderBlack");
        bConfirmCancel.getStyleClass().add("bckg5");
        bConfirmedPCreation.getStyleClass().add("bckg5");
        // event handler for the cancel button.
        bConfirmCancel.setOnAction((event)->{
        Stage stage=(Stage) bConfirmCancel.getScene().getWindow();
        stage.close();
        });
    }

// END OF CLASS
}
