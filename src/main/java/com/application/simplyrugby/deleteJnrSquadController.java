package com.application.simplyrugby;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class deleteJnrSquadController {



    @FXML
    private Button bDeleteSquad,bCancel;
    @FXML
    private Pane mainPane;
    @FXML
    private ComboBox cbSquad;

    public void initialize(){

        mainPane.getStyleClass().add("bckg1");
        bDeleteSquad.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");

        bCancel.setOnAction((event)->{
            // https://9to5answer.com/how-to-close-a-java-window-with-a-button-click-javafx-project
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();

        });
    }
}
