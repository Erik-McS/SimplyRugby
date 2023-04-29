package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.Game;
import com.application.simplyrugby.Model.JuniorSquad;
import com.application.simplyrugby.Model.SeniorSquad;
import com.application.simplyrugby.System.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.security.spec.ECField;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Controller for the Game creation confirmation panel
 */
public class ConfirmGameCreationCtlr {

    @FXML
    private Label lSquad,lClub,lDate,lLocation;
    @FXML
    private Button bCreateGame,bCancel;
    @FXML
    private Pane firstPane;
    public void receiveGame(Game game){

        if (game.getSquad() instanceof SeniorSquad){
            SeniorSquad sSquad=(SeniorSquad) game.getSquad();
            lSquad.setText("Playing Squad: "+sSquad.getSquadName());
            lClub.setText("Opposant Club: "+game.getPlayingClub().getName());
            if (game.getLocation()==1)
                lLocation.setText("Location: Home");
            else
                lLocation.setText("Location: Away");
            lDate.setText(game.getDate());
        }

        if (game.getSquad() instanceof JuniorSquad){
            JuniorSquad sSquad=(JuniorSquad) game.getSquad();
            lSquad.setText("Playing Squad: "+sSquad.getSquadName());
            lClub.setText("Opposant Club: "+game.getPlayingClub().getName());
            if (game.getLocation()==1)
                lLocation.setText("Location: Home");
            else
                lLocation.setText("Location: Away");
            lDate.setText(game.getDate());
        }
    bCreateGame.setOnAction((event)->{
        System.out.println("Create Game button clicked");
        DBTools.saveGame(game);
        Stage stage=(Stage) bCreateGame.getScene().getWindow();
        stage.close();

    });

    }

    public void initialize(){

        firstPane.getStyleClass().add("bckg1");
        bCreateGame.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");

        bCancel.setOnAction((event)->{

            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();

        });
    }
}
