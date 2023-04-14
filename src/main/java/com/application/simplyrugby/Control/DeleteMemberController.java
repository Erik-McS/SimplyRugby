package com.application.simplyrugby.Control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class DeleteMemberController {

    @FXML
    private Pane mainPane;
    @FXML
    private Button bDeleteMember,bCancel;
    @FXML
    private ComboBox cbMember;

    public void initialize(){

        mainPane.getStyleClass().add("bckg1");
        bCancel.getStyleClass().add("bckg5");
        bDeleteMember.getStyleClass().add("bckg5");
        cbMember.getStyleClass().add("bckg5");

        bCancel.setOnAction((event)->{
            // https://9to5answer.com/how-to-close-a-java-window-with-a-button-click-javafx-project
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();
        });

    }
}
