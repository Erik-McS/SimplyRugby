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
        // setting up the panel style
        firstPane.getStyleClass().add("bckg2");
        secondPane.getStyleClass().add("bckg3");
        bCancel.getStyleClass().add("bckg5");
        bUpdateGame.getStyleClass().add("bckg5");
        cbJuniorGame.getStyleClass().add("bckg5");
        cbSeniorGame.getStyleClass().add("bckg5");

        // adding the lists to the comboBoxes.
        cbSeniorGame.setItems(ObsListFactory.createObsList("SeniorGames"));
        cbSeniorGame.getSelectionModel().select(0);
        cbJuniorGame.setItems(ObsListFactory.createObsList("JuniorGames"));
        cbJuniorGame.getSelectionModel().select(0);

        // event for the cancel button
        bCancel.setOnAction((event)->{
            firstPane.getChildren().clear();
        });

        // adding event to the comboBoxes so one disappears if a selection is made in the other.
        cbSeniorGame.setOnAction((event)->{
            if (cbSeniorGame.getSelectionModel().getSelectedIndex()!=0){
                cbJuniorGame.setVisible(false);
                lOr.setVisible(false);
            }
            else {
                cbJuniorGame.setVisible(true);
                lOr.setVisible(true);
            }
        });
        cbJuniorGame.setOnAction((event)->{
            if (cbJuniorGame.getSelectionModel().getSelectedIndex()!=0){
                cbSeniorGame.setVisible(false);
                lOr.setVisible(false);
            }
            else{
                cbSeniorGame.setVisible(true) ;
                lOr.setVisible(true);
            }
        });

    }

}
