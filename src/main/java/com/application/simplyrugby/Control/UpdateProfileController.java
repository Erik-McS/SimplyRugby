package com.application.simplyrugby.Control;

import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ObsListFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

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
        Collections.addAll(boxes,cbRunning,cbPassing,cbSupport,cbTackling,cbDecision,cbPlayerPerf);
        for(ComboBox<String> cbx:boxes)
        {
            cbx.setItems(ObsListFactory.createObsList("PerformanceLevels"));
            cbx.getSelectionModel().select(0);
        }
        // hiding the panes until a player is selected
        sessionPane.setVisible(false);
        skillsPane.setVisible(false);
        gamePane.setVisible(false);
        lCore.setVisible(false);
        lGamePerf.setVisible(false);
        lTraining.setVisible(false);

        cbPlayer.setOnAction((event)->{
            if (cbPlayer.getSelectionModel().getSelectedIndex()!=0){
                //sessionPane.setVisible(true);
                skillsPane.setVisible(true);
                gamePane.setVisible(true);
                lCore.setVisible(true);
                //lTraining.setVisible(true);
                lGamePerf.setVisible(true);
            }
            else {
                //sessionPane.setVisible(false);
                skillsPane.setVisible(false);
                gamePane.setVisible(false);
                lCore.setVisible(false);
                lGamePerf.setVisible(false);
                //lTraining.setVisible(false);
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

        // event handler for the the update player profile button
        bUpdtProfile.setOnAction((event)->{
            ArrayList <Integer>levels=new ArrayList<>();
            // inserting the comboBox values in an array.
            // the DBtools functions will only update the fields where a values was selected.
            // the values are added in the table columns order
            // passing,running,support,tackling, decision.
            levels.add(cbPassing.getSelectionModel().getSelectedIndex());
            levels.add(cbRunning.getSelectionModel().getSelectedIndex());
            levels.add(cbSupport.getSelectionModel().getSelectedIndex());
            levels.add(cbTackling.getSelectionModel().getSelectedIndex());
            levels.add(cbDecision.getSelectionModel().getSelectedIndex());

            String[] player=cbPlayer.getValue().split(" ");
            int profile_id= DBTools.getID("SELECT profile_id FROM training_profiles " +
                    "WHERE player_id=(SELECT player_id FROM players " +
                    "WHERE first_name='"+player[0]+"' AND surname ='"+player[1]+"')");
            System.out.println("Profile ID to update: "+profile_id);

            if(DBTools.updateTrainingProfile(levels,DBTools.getID("SELECT profile_id FROM training_profiles " +
                    "WHERE player_id=(SELECT player_id FROM players " +
                    "WHERE first_name='"+player[0]+"' AND surname ='"+player[1]+"')")))
            {
                CustomAlert alert = new CustomAlert("Update Profile: "+cbPlayer.getValue(), "The player profile has been updated");
                alert.showAndWait();
            }
            else {
                CustomAlert alert = new CustomAlert("Update Profile: "+cbPlayer.getValue(), "The player profile could not be updated");
                alert.showAndWait();
            }

        });

    }
}
