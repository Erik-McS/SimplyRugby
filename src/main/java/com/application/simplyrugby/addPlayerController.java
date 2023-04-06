package com.application.simplyrugby;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class addPlayerController {

    @FXML
    Pane mainPane,leftPane,nokPane,docPane;
    @FXML
    TextField txName,txSurname,txTelephone,txEmail,txScrumsNumber,txNameNOK,txSurnameNOK,txTelNOK,
            txNameDoctor,txSurnameDoctor,txTelDoctor;
    @FXML
    DatePicker dpDateOfBirth;
    @FXML
    RadioButton rbMale,rbFemale;
    @FXML
    Button bCreatePlayer,bCancel,bAddConsentForm;
    @FXML
    ComboBox cbNOK,cbDoctor;
    @FXML
    ToggleButton tbAddNOK,tbCreateNOK,tbAddDoctor,tbCreateDoctor;
    @FXML
    TextArea txAdress;

    public void initialize(){

        // set Pane background color
        leftPane.getStyleClass().add("bckg2");
        nokPane.getStyleClass().add("bckg3");
        docPane.getStyleClass().add("bckg4");
        // set Buttons style.
        bCreatePlayer.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");
        bAddConsentForm.getStyleClass().add("bckg5");
        tbAddDoctor.getStyleClass().add("bckg5");
        tbAddNOK.getStyleClass().add("bckg5");
        tbCreateDoctor.getStyleClass().add("bckg5");
        tbCreateNOK.getStyleClass().add("bckg5");

        bCancel.setOnAction((event)->{
            mainPane.getChildren().clear();
        });

    }

}
