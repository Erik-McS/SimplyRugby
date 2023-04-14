package com.application.simplyrugby.Control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class AddMemberController {

    @FXML
    private TextField txName,txSurname,txTelephone,txEmail;
    @FXML
    private TextArea taAddress;
    @FXML
    private Button bCreateMember,bCancel;
    @FXML
    private Pane  mainPane,secondPane;

    public void initialize(){
        bCreateMember.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");
        mainPane.getStyleClass().add("bckg2");
        secondPane.getStyleClass().add("bckg3");

        bCancel.setOnAction((event)->{

            mainPane.getChildren().clear();
        });
    }
}
