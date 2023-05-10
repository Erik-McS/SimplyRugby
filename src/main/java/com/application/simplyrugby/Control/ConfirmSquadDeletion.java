package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.JuniorSquad;
import com.application.simplyrugby.Model.SeniorSquad;
import com.application.simplyrugby.Model.Squad;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class ConfirmSquadDeletion {

    @FXML
    private Pane mainPane;
    @FXML
    private Label lSquad;
    @FXML
    private Button bCancel,bConfirm;

    public void receiveSquad(Squad squad){

        if (squad instanceof SeniorSquad){

            SeniorSquad seniorSquad=(SeniorSquad)squad;
            lSquad.setText(seniorSquad.getSquadName());
        }

        else {

            JuniorSquad juniorSquad=(JuniorSquad) squad;
            lSquad.setText(juniorSquad.getSquadName());
        }

    }

    public void initialize(){
        mainPane.getStyleClass().add("bckg1");
        bCancel.getStyleClass().add("bckg5");
        bConfirm.getStyleClass().add("bckg5");

        bCancel.setOnAction((event)->{

            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();
        });

    }
}
