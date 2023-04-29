package com.application.simplyrugby.Control;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.layout.Pane;

public class UpdateGameController {

    @FXML
    private ComboBox <String>cbGame;
    @FXML
    private RadioButton rbWon,rbLost,rbWonForfeit,rbLostForfeit,rbCancelled;
    @FXML
    private Spinner <Integer>spTry,spPenaltyTry,spPenalty,spConversion,spDropGoal;
    @FXML
    private Button bUpdateGame,bCancel;
    @FXML
    private Pane firstPane,secondPane;

    public void initialize(){

        firstPane.getStyleClass().add("bckg2");
        secondPane.getStyleClass().add("bckg3");
        bCancel.getStyleClass().add("bckg5");
        bUpdateGame.getStyleClass().add("bckg5");


        bCancel.setOnAction((event)->{
            firstPane.getChildren().clear();
        });
    }

}
