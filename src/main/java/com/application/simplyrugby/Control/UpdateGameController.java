package com.application.simplyrugby.Control;

import com.application.simplyrugby.System.ObsListFactory;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

/**
 * This controller manages the Update game panel, where the game outcome and scores can be updated.
 */
public class UpdateGameController {

    @FXML
    private ComboBox <String>cbSeniorGame,cbJuniorGame;
    @FXML
    private RadioButton rbWon,rbLost,rbWonForfeit,rbLostForfeit,rbCancelled;
    @FXML
    private Spinner <Integer>spTry,spPenaltyTry,spPenalty,spConversion,spDropGoal;
    @FXML
    private Button bUpdateGame,bCancel;
    @FXML
    private Pane firstPane,secondPane;
    @FXML
    Label lOr;

    public void initialize(){

        firstPane.getStyleClass().add("bckg2");
        secondPane.getStyleClass().add("bckg3");
        bCancel.getStyleClass().add("bckg5");
        bUpdateGame.getStyleClass().add("bckg5");

        cbSeniorGame.setItems(ObsListFactory.createObsList("SeniorGames"));
        cbSeniorGame.getSelectionModel().select(0);
        bCancel.setOnAction((event)->{
            firstPane.getChildren().clear();
        });


    }

}
