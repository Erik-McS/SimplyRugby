package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.*;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ObsListFactory;
import com.application.simplyrugby.System.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * Controller class for the Create Senior Squad panel.
 */
public class CreateJuniorSquadController {

    @FXML
    ComboBox<String> cblooseHP, cbThightHead, cbHooker,cbScrumhalf,
            cbFlyhalf, cbincentre, cbrightwing, cbplayer1, cbplayer2, cbplayer3, cbplayer4, cbplayer5,
            cbCoach1,cbCoach2,cbCoach3,cbChairman,cbSec;
    @FXML
    Button bCreateSquad, bCancel;
    @FXML
    Pane firstPane;
    @FXML
    TextField txSquadName;

    /**
     * Initialisation of the panel.
     */
    public void initialize() {
        // setting up the colors
        firstPane.getStyleClass().add("bckg1");
        bCancel.getStyleClass().add("bckg5");
        bCreateSquad.getStyleClass().add("bckg5");
        // Event handler for the cancel button.
        bCancel.setOnAction((event) -> {
            firstPane.getChildren().clear();
        });
        // we create an array list with all the Squad player ComboBoxes of the pane.
        // they are adding order to match the DB table columns.
        ArrayList<ComboBox<String>> playerBoxes = new ArrayList<>();
        Collections.addAll(playerBoxes, cblooseHP, cbHooker, cbThightHead, cbScrumhalf,
                cbFlyhalf,cbincentre, cbrightwing);
        // we create an array list with all the Replacement player ComboBoxes of the pane.
        ArrayList<ComboBox<String>> repPlayerTeam=new ArrayList<>();
        Collections.addAll(repPlayerTeam,cbplayer1, cbplayer2, cbplayer3, cbplayer4, cbplayer5);
        // we create an array list with all the Coaches ComboBoxes of the pane.
        ArrayList<ComboBox<String>> coachBoxes = new ArrayList<>();
        Collections.addAll(coachBoxes,cbCoach1,cbCoach2,cbCoach3);
       // ArrayList<ComboBox<String>> adminBoxes = new ArrayList<>();
        //Collections.addAll(coachBoxes,cbChairman,cbSec);

        // we go through the Squad players ComboBoxes to edit them
        int i=0;
        for (ComboBox<String> cbx : playerBoxes) {
            // add color style
            cbx.getStyleClass().add("bckg5");
            // get a list from the factory with the playes from the database.
            cbx.setItems(ObsListFactory.createObsList("JuniorAvailablePlayers"));
            // set the default choice.
            cbx.getSelectionModel().select(0);
            i++;
        }

        // same logic that the squad players
        for (ComboBox<String> cbx : repPlayerTeam) {
            cbx.getStyleClass().add("bckg5");
            cbx.setItems(ObsListFactory.createObsList("JuniorAvailablePlayers"));
            cbx.getSelectionModel().select(0);
        }
        // same that above.
        for (ComboBox<String> cbx : coachBoxes) {
            cbx.getStyleClass().add("bckg5");
            cbx.setItems(ObsListFactory.createObsList("Coaches"));
            cbx.getSelectionModel().select(0);
        }
        // the Admin team will be done 'manually' as there are only 2 of them.
        // the chairman ComboBox
        cbChairman.getStyleClass().add("bckg5");
        cbChairman.setItems(ObsListFactory.createObsList("Chairmen"));
        cbChairman.getSelectionModel().select(0);
        // the secretary ComboBox
        cbSec.getStyleClass().add("bckg5");
        cbSec.setItems(ObsListFactory.createObsList("FixtSect"));
        cbSec.getSelectionModel().select(0);

        // Event Handler for the Create button.
        bCreateSquad.setOnAction((event)->{

            // catching exceptions.
            try {

                // Arraylists to contain the selected persons
                // Squad player arraylist
                ArrayList<Player> squadArray=new ArrayList<>();
                // replacement team arrayList.
                ArrayList<Player> repTeam=new ArrayList<>();
                // this array will contain all the index added in the previous two arrays.
                // this will be used to check for duplicates in the ComboBoxes.
                ArrayList<Integer> duplicates=new ArrayList<>();
                // creating the squad player arraylist
                for (ComboBox<String> cbx:playerBoxes){
                    // checking a player is selected in the comboBox
                    if (cbx.getSelectionModel().getSelectedIndex()==0)
                        throw new ValidationException("One of the Squad role(s) has no player selected");
                    else {
                        // checking if the player index is not in the selected index's arraylist.
                        if (!duplicates.contains(cbx.getSelectionModel().getSelectedIndex())){
                            // getting the player name
                            String name=cbx.getValue();
                            // splitting it in name and surname
                            String[] playerNames=name.split(" ");
                            // Creating and adding the player object in the Squad array.
                            // The database is queried to make sure we have the right player_ID.
                            squadArray.add((Player) Player.dummyPlayer().loadMember(Player.dummyPlayer(), DBTools.getID("SELECT player_id FROM players WHERE first_name='"+playerNames[0]+"' AND surname='"+playerNames[1]+"'")));
                            // adding the selected player index in the duplicate arraylist.
                            duplicates.add(cbx.getSelectionModel().getSelectedIndex());
                        }
                        else
                            throw new ValidationException("Some of the selected players are duplicates, please check your selection");
                    }
                }


                // same logic for the replacement team.
                for (ComboBox<String> cbx:repPlayerTeam){

                    if (cbx.getSelectionModel().getSelectedIndex()==0)
                        throw new ValidationException("One of the Replacement slot has no player selected");
                    else {
                        if (!duplicates.contains(cbx.getSelectionModel().getSelectedIndex())){
                            String name=cbx.getValue();
                            String[] playerNames=name.split(" ");
                            repTeam.add((Player) Player.dummyPlayer().loadMember(Player.dummyPlayer(), DBTools.getID("SELECT player_id FROM players WHERE first_name='"+playerNames[0]+"' AND surname='"+playerNames[1]+"'")));
                            duplicates.add(cbx.getSelectionModel().getSelectedIndex());
                        }
                        else
                            throw new ValidationException("Some of the selected players are duplicates, please check your selection");
                    }
                }
                // creating a replacement team object.
                ReplacementTeam replacementTeam=new ReplacementTeam(repTeam);

                // we clear the duplicate array
                duplicates.clear();

                ArrayList<NonPlayer> coaches=new ArrayList<>();
                for (ComboBox<String> cbx:coachBoxes){
                    if (cbx.getSelectionModel().getSelectedIndex()==0)
                        throw new ValidationException("Missing one of the Coaches.");
                    else if (!duplicates.contains(cbx.getSelectionModel().getSelectedIndex())){
                        String name=cbx.getValue();
                        String[] coachNames=name.split(" ");
                        NonPlayer np=new NonPlayer();
                        coaches.add((NonPlayer) np.loadMember(np,DBTools.getID("SELECT member_id FROM non_players WHERE first_name='"+coachNames[0]+"' AND surname='"+coachNames[1]+"'")));
                    }
                }
                CoachTeam coachTeam=new CoachTeam(coaches.get(0),coaches.get(1),coaches.get(2));
                duplicates.clear();
                ArrayList<NonPlayer> admins=new ArrayList<>();



                if (cbChairman.getSelectionModel().getSelectedIndex()!=0 && cbSec.getSelectionModel().getSelectedIndex()!=0){

                    String[] chairName=cbChairman.getValue().split(" ");
                    String[] secName=cbSec.getValue().split(" ");
                    NonPlayer np=new NonPlayer();
                    admins.add((NonPlayer) np.loadMember(np,DBTools.getID("SELECT member_id FROM non_players WHERE first_name='"+chairName[0]+"' AND surname='"+chairName[1]+"'")));
                    admins.add((NonPlayer) np.loadMember(np,DBTools.getID("SELECT member_id FROM non_players WHERE first_name='"+secName[0]+"' AND surname='"+secName[1]+"'")));

                }
                else
                    throw new ValidationException("One of the Admin is empty");

                AdminTeam adminTeam=new AdminTeam(admins.get(0),admins.get(1));

                // creating the JuniorSquad object with the just created Squad arraylist.
                String squadName=txSquadName.getText();
                if (txSquadName.getText().equals(""))
                    throw new ValidationException("The Squad name field is empty, please choose a name");

                JuniorSquad juniorSquad=new JuniorSquad(squadArray,txSquadName.getText(),replacementTeam,adminTeam,coachTeam);
                // loading the resources for the confirmation window
                FXMLLoader loader= new FXMLLoader(getClass().getResource("/com/application/simplyrugby/ConfirmNewJuniorSquad.fxml"));
                Parent root=loader.load();
                // getting the Confirmation window controller. will be used to pass the 3 teams created here.
                ConfirmJnrSquadCntlr controller=loader.getController();
                // passing all the teams object to the next window.
                controller.receiveTeams(juniorSquad);
                //controller.receiveTeams(juniorSquadTest);
                Scene scene=new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"),"CSS not found").toExternalForm());
                Stage stage=new Stage();
                stage.setTitle("Create a Junior Squad");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
                stage.setScene(scene);
                stage.showAndWait();
                firstPane.getChildren().clear();

            }catch (ValidationException | IOException e){
                CustomAlert alert =new CustomAlert("Error",e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
            }

        });

    }


}
