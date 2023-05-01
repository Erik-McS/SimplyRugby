package com.application.simplyrugby.Control;

import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.ObsListFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;

/**
 * Class to manage the update profile function
 */
public class UpdateProfileController {

    @FXML
    private ComboBox<String> cbPlayer,cbRunning,cbPassing,cbSupport,cbTackling,cbDecision,cbTrainingSession,
            cbGame,cbPlayerPerf;
    @FXML
    private Button bUpdtProfile,bAddSession,bAddGame,bCancel;
    @FXML
    private Pane firstPane,skillsPane,sessionPane,gamePane;
    @FXML
    private Label lGamePerf,lCore,lTraining;

    /**
     * initialise the update Profile panel.
     */
    public void initialize(){
        // setting up styles
        bUpdtProfile.getStyleClass().add("bckg5");
        bAddGame.getStyleClass().add("bckg5");
        bAddSession.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");
        firstPane.getStyleClass().addAll("bckg1"," borderBlack");
        skillsPane.getStyleClass().addAll("borderBlack","bckg2");
        sessionPane.getStyleClass().addAll("borderBlack","bckg2");
        gamePane.getStyleClass().addAll("borderBlack","bckg2");
        ArrayList<ComboBox<String>> boxes=new ArrayList<>();
        Collections.addAll(boxes, cbPlayer,cbRunning,cbPassing,cbSupport,cbTackling,cbDecision,cbTrainingSession,
                cbGame,cbPlayerPerf);
        for(ComboBox<String> cbx:boxes)
        {
            cbx.getStyleClass().add("bckg5");
        }
        cbPlayer.setItems(ObsListFactory.createObsList("PlayerProfiles"));
        cbPlayer.getSelectionModel().select(0);

        boxes.clear();
        Collections.addAll(boxes,cbRunning,cbPassing,cbSupport,cbTackling,cbDecision);
        for(ComboBox<String> cbx:boxes)
        {
            cbx.setItems(ObsListFactory.createObsList("PerformanceLevels"));
            cbx.getSelectionModel().select(0);
        }
        // hiding the panes until a player is selectedv
        sessionPane.setVisible(false);
        skillsPane.setVisible(false);
        gamePane.setVisible(false);
        lCore.setVisible(false);
        lGamePerf.setVisible(false);
        lTraining.setVisible(false);

        cbPlayer.setOnAction((event)->{
            if (cbPlayer.getSelectionModel().getSelectedIndex()!=0){
                sessionPane.setVisible(true);
                skillsPane.setVisible(true);
                gamePane.setVisible(true);
                lCore.setVisible(true);
                lTraining.setVisible(true);
                lGamePerf.setVisible(true);
            }
            else {
                sessionPane.setVisible(false);
                skillsPane.setVisible(false);
                gamePane.setVisible(false);
                lCore.setVisible(false);
                lGamePerf.setVisible(false);
                lTraining.setVisible(false);
            }

        });
        // checking if a player has been selected before displaying the game list
        cbGame.setOnAction((event -> {
            if (cbPlayer.getSelectionModel().getSelectedIndex()!=0){}
            else{
                CustomAlert alert=new CustomAlert("Update a player profile","Please select a player first");
                alert.showAndWait();
            }
        }));

        cbTrainingSession.setOnAction((event)->{
            if (cbPlayer.getSelectionModel().getSelectedIndex()!=0){}
            else{
                CustomAlert alert=new CustomAlert("Update a player profile","Please select a player first");
                alert.showAndWait();
            }
        });
        // event handler for the cancel button
        bCancel.setOnAction((event)->{
            firstPane.getChildren().clear();
        });


    }
}
