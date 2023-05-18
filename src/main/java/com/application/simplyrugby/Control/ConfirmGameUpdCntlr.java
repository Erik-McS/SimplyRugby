package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.Game;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.DBTools;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller for the Confirm Game update window.
 */
public class ConfirmGameUpdCntlr {

    @FXML
    private Pane upperPane,lowerPane;
    @FXML
    private Label lOutcome,lnbTry,lOppScore,lnbPenalty,lnbConversion,lnbDropGoal,lTotalScore;
    @FXML
    private Button bUpdateGame,bCancel;

    /**
     * Method used to pass the game object to update from the previous panel
     * @param game the game to update
     */
    public void receiveGame(Game game){

        lOutcome.setText(game.getOutcome());
        lnbConversion.setText(Integer.toString(game.getNbConversion()));
        lnbDropGoal.setText(Integer.toString(game.getNbDropGoal()));
        lnbPenalty.setText(Integer.toString(game.getNbPenalty()));
        lnbTry.setText(Integer.toString(game.getNbTry()));
        lOppScore.setText(Integer.toString(game.getOpponentScore()));
        lTotalScore.setText(Integer.toString(game.getNbTry()*5+ game.getNbPenalty()*3+ game.getNbConversion()*2
                + game.getNbDropGoal()*3));

        bUpdateGame.setOnAction((event)->{
            DBTools.updateGame(game);
            CustomAlert alert =new CustomAlert("Update Game:","The game has been updated successfully.");
            alert.showAndWait();
            Stage stage=(Stage) bUpdateGame.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * initialise the window
     */
    public void initialize(){

        // setting up styles
        bUpdateGame.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");
        upperPane.getStyleClass().add("bckg3");
        lowerPane.getStyleClass().add("bckg1");

        // event handler for the cancel button.
        bCancel.setOnAction((event)->{
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();
        });
    }
}
