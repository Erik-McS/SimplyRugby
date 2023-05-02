package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.JuniorSquad;
import com.application.simplyrugby.Model.SeniorSquad;
import com.application.simplyrugby.System.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                /*
                    setting the game played comboBox will need to be done here, as we need the selected player id
                    this will use two DBTools functions:
                    one to get the player squad type and the other to get the player's squad id
                */
                String[] player=cbPlayer.getValue().split(" ");
                int player_id= DBTools.getID("SELECT player_id FROM players " +
                        "WHERE first_name='"+player[0]+"' AND surname ='"+player[1]+"'");

                ObservableList<String> gamesPlayed= FXCollections.observableArrayList();
                // first, we check if the player is in a squad
                if (!DBTools.playerIsAssignedToSquad(player_id)) {
                    gamePane.setVisible(false);
                    lGamePerf.setText("This player is not assigned to any squad.");
                }
                // we check if the player is in a replacement team
                if (!DBTools.isReplacement(player_id)){
                    // if not in a replacement team, we look in the squads
                    if (DBTools.getPlayerSquadType(player_id) instanceof SeniorSquad){
                        try(
                                Connection connection=ConnectionPooling.getDataSource().getConnection();
                                PreparedStatement statement=connection.prepareStatement("SELECT game_id,date FROM senior_games_played WHERE squad_id=?")
                        )
                        {
                            statement.setInt(1,DBTools.getPlayerSquadID(player_id));
                            try (ResultSet rs=statement.executeQuery();
                                 QueryResult qs=DBTools.executeSelectQuery("SELECT name FROM clubs " +
                                         "WHERE club_id=(SELECT club_id FROM games WHERE game_id='"+rs.getInt(1)+"')")
                            ){
                                while (rs.next()){
                                    gamesPlayed.add(rs.getInt(1)+" - "+rs.getString(2)+" - "+qs.getResultSet().getString(1));
                                }
                            }
                        }catch (ValidationException|SQLException e){
                            e.printStackTrace();
                        }
                        cbGame.setItems(gamesPlayed);
                    }
                    else if (DBTools.getPlayerSquadType(player_id) instanceof JuniorSquad){
                        try(
                                Connection connection=ConnectionPooling.getDataSource().getConnection();
                                PreparedStatement statement=connection.prepareStatement("SELECT game_id,date FROM junior_games_played WHERE squad_id=?")
                        )
                        {
                            statement.setInt(1,DBTools.getPlayerSquadID(player_id));
                            try (ResultSet rs=statement.executeQuery();
                                 QueryResult qs=DBTools.executeSelectQuery("SELECT name FROM clubs " +
                                         "WHERE club_id=(SELECT club_id FROM games WHERE game_id='"+rs.getInt(1)+"')")
                            ){
                                while (rs.next()){
                                    gamesPlayed.add(rs.getInt(1)+" - "+rs.getString(2)+" - "+qs.getResultSet().getString(1));
                                }
                            }
                        }catch (ValidationException|SQLException e){
                            e.printStackTrace();
                        }
                        cbGame.setItems(gamesPlayed);
                    }
                }
                // if in a replacement team, we look the games they played with their squads.
                else if(DBTools.isReplacement(player_id)){
                    // check the kind of squad the rep team is
                    // if in a senior squad.
                    if (DBTools.getReplacementSquadType(DBTools.getReplacementTeamID(player_id)) instanceof SeniorSquad)
                    {
                        try(
                                Connection connection=ConnectionPooling.getDataSource().getConnection();
                                PreparedStatement statement=connection.prepareStatement("SELECT game_id,date FROM senior_games_played WHERE squad_id=?");
                                QueryResult qs=DBTools.executeSelectQuery("SELECT squad_id from senior_squads WHERE repteam_id='"+DBTools.getReplacementTeamID(player_id)+"'");
                        )
                        {
                            System.out.println("Squad ID: "+qs.getResultSet().getInt(1));
                            statement.setInt(1,qs.getResultSet().getInt(1));
                            try(ResultSet rs=statement.executeQuery();
                                QueryResult qs1=DBTools.executeSelectQuery("SELECT name FROM clubs " +
                                        "WHERE club_id=(SELECT club_id FROM games WHERE game_id='"+rs.getInt(1)+"')")
                            )
                            {
                                if (rs!=null)
                                    System.out.println("no result");
                                while (rs.next()){
                                    int game_id=rs.getInt(1);
                                    gamesPlayed.add(rs.getInt(1)+" - "+rs.getString(2)+" - "+qs1.getResultSet().getString(1));
                            }
                            }catch (ValidationException|SQLException e){
                            e.printStackTrace();
                            }
                            cbGame.setItems(gamesPlayed);
                        }catch (ValidationException|SQLException e){e.printStackTrace();}
                    }
                    else {

                        try(
                                Connection connection=ConnectionPooling.getDataSource().getConnection();
                                PreparedStatement statement=connection.prepareStatement("SELECT game_id,date FROM junior_games_played WHERE squad_id=?");
                                QueryResult qs=DBTools.executeSelectQuery("SELECT squad_id from junior_squads WHERE repteam_id='"+DBTools.getReplacementTeamID(player_id)+"'");
                        )
                        {
                            statement.setInt(1,qs.getResultSet().getInt(1));
                            try(ResultSet rs=statement.executeQuery();
                                QueryResult qs1=DBTools.executeSelectQuery("SELECT name FROM clubs " +
                                        "WHERE club_id=(SELECT club_id FROM games WHERE game_id='"+rs.getInt(1)+"')")
                            )
                            {
                                while (rs.next()){
                                    gamesPlayed.add(rs.getInt(1)+" - "+rs.getString(2)+" - "+qs1.getResultSet().getString(1));
                                }
                            }catch (ValidationException|SQLException e){
                                e.printStackTrace();
                            }
                            cbGame.setItems(gamesPlayed);
                        }catch (ValidationException|SQLException e){}
                    }
                }
                // the player isn't in any squads, or no games for that squad exist, so no game to update
                else if(cbGame.getItems()==null){
                    gamePane.setVisible(false);
                    lGamePerf.setText("The player's squad hasn't played any game");
                }
                else
                    gamePane.setVisible(false);
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

        // event handler for the update player profile button
        bUpdtProfile.setOnAction((event)->{
            ArrayList <Integer>levels=new ArrayList<>();
            // inserting the comboBox values in an array.
            // the DBtools functions will only update the fields where a values was selected.
            // the values are added in the table columns order
            // passing, running,support,tackling, decision.
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
