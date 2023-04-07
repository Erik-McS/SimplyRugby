package com.application.simplyrugby;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class AddPlayerController {

    @FXML
    private Pane mainPane,leftPane,nokPane,docPane;
    @FXML
    private TextField txName,txSurname,txTelephone,txEmail,txScrumsNumber,txNameNOK,txSurnameNOK,txTelNOK,
            txNameDoctor,txSurnameDoctor,txTelDoctor;
    @FXML
    private DatePicker dpDateOfBirth;
    @FXML
    private RadioButton rbMale,rbFemale;
    @FXML
    private Button bCreatePlayer,bCancel,bAddConsentForm;
    @FXML
    private ComboBox cbNOK,cbDoctor;
    @FXML
    private ToggleButton tbAddNOK,tbCreateNOK,tbAddDoctor,tbCreateDoctor;
    @FXML
    private TextArea txAdress;

    public void initialize(){

        // set Pane background color
        mainPane.getStyleClass().add("bckg5");
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

        ToggleGroup group=new ToggleGroup();
        rbFemale.setToggleGroup(group);
        rbMale.setToggleGroup(group);

        bCancel.setOnAction((event)->{
            mainPane.getChildren().clear();
        });

    }

}
