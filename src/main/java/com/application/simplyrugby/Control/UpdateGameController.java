package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.Game;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ObsListFactory;
import com.application.simplyrugby.System.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * This controller manages the Update game panel, where the game outcome and scores can be updated.
 */
public class UpdateGameController {

    @FXML
    private ComboBox <String>cbSeniorGame,cbJuniorGame;
    @FXML
    private RadioButton rbWon,rbLost,rbWonForfeit,rbLostForfeit,rbCancelled;
    @FXML
    private Spinner <Integer>spTry,spPenalty,spConversion,spDropGoal,spOppScore;
    @FXML
    private Button bUpdateGame,bCancel;
    @FXML
    private Pane firstPane,secondPane;
    @FXML
    private Label lOr;
    // local variables used to store selected data
    private String outcome;
    private Game game;

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

        // creating a Toggle group for the Game outcome
        ToggleGroup gameOutcome=new ToggleGroup();
        rbCancelled.setToggleGroup(gameOutcome);
        rbLost.setToggleGroup(gameOutcome);
        rbWon.setToggleGroup(gameOutcome);
        rbLostForfeit.setToggleGroup(gameOutcome);
        rbWonForfeit.setToggleGroup(gameOutcome);

        // setting up the spinners to get a value between 0 and 100;
        ArrayList<Spinner<Integer>> spinners=new ArrayList<>();
        Collections.addAll(spinners,spConversion,spDropGoal,spTry,spPenalty,spOppScore);
        int maxValue=100;
        for(Spinner<Integer> sp:spinners){
            SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory= new SpinnerValueFactory.IntegerSpinnerValueFactory(0,maxValue);
            sp.setValueFactory(valueFactory);
            sp.getStyleClass().add("bckg5");
        }

        // Here we create the event handler for the updateGame button.
        // It will gather the info and validate it, then pass it to the confirmation window
        bUpdateGame.setOnAction((event)->{
            //try-catch for Validation errors.
            try{
                // getting the game outcome selected.if, nothing is selected, we give an error message
                if (gameOutcome.getSelectedToggle()!=null){
                    RadioButton selectedOutcome =(RadioButton) gameOutcome.getSelectedToggle();
                    outcome=selectedOutcome.getText();
                    //System.out.println("outcome: "+outcome);
                }
                else
                    throw new ValidationException("A game outcome must be selected.");

                // now we get the squad and game data.
                // if the gam was played by a senior squad:
                if (cbSeniorGame.getSelectionModel().getSelectedIndex()!=0){
                    // getting the squad ID /date from the string of the comboBox.
                    String[] gameData= cbSeniorGame.getValue().split(" - ");
                    System.out.println("Squad ID: "+gameData[0]+" Date: "+gameData[3]);
                    int game_id=DBTools.getID("SELECT game_id FROM senior_games_played WHERE squad_id='"+gameData[0]+"' AND date='"+gameData[3]+"'");
                    //getting the game object
                    game=DBTools.loadNonUpdatedGame(game_id);
                    // checking if the game has been properly loaded form the DB.
                    if (game!=null){

                        game.setGame_id(game_id);
                        game.setSquad_id(Integer.parseInt(gameData[0]));
                        game.setOutcome(outcome);
                        game.setNbConversion(spConversion.getValue());
                        game.setNbDropGoal(spDropGoal.getValue());
                        game.setNbPenalty(spPenalty.getValue());
                        game.setNbTry(spTry.getValue());
                        game.setOpponentScore(spOppScore.getValue());
                        if (game.getOutcome().equals("Won") && (game.getNbTry()*5+ game.getNbPenalty()*3+ game.getNbConversion()*2
                                + game.getNbDropGoal()*3)<=spOppScore.getValue() )
                            throw new ValidationException("The squad scores cannot be lower that the opponent score in case of a Win");
                        System.out.println(game.toString());
                    }
                    else
                        throw new ValidationException("Error while getting the game");
                    // calling the confirmation window and passing it the game object.
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/application/simplyrugby/confirmUpdateGame.fxml"));
                    Parent root=loader.load();
                    // passing the game to the next window controller.
                    ConfirmGameUptCntlr controller=loader.getController();
                    controller.receiveGame(game);
                    // setting up the next window.
                    Scene scene=new Scene(root);
                    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"),"CSS not found").toExternalForm());
                    Stage stage=new Stage();
                    stage.setTitle("Updating a Game");
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
                    stage.setScene(scene);
                    stage.showAndWait();
                    firstPane.getChildren().clear();
                }
                // or if played by a junior squad.
                else if (cbJuniorGame.getSelectionModel().getSelectedIndex()!=0){

                    // getting the squad ID from the string of the comboBox.
                    String[] gameData= cbJuniorGame.getValue().split(" - ");
                    //getting the game object
                    int game_id=DBTools.getID("SELECT game_id FROM junior_games_played WHERE squad_id='"+gameData[0]+"' AND date='"+gameData[3]+"'");
                    System.out.println("gameID: "+game_id+" Date: "+gameData[3]);
                    game=DBTools.loadNonUpdatedGame(game_id);
                    // checking if the game has been properly loaded form the DB.
                    if (game!=null){
                        game.setGame_id(game_id);
                        game.setSquad_id(Integer.parseInt(gameData[0]));
                        game.setOutcome(outcome);
                        game.setNbConversion(spConversion.getValue());
                        game.setNbDropGoal(spDropGoal.getValue());
                        game.setNbPenalty(spPenalty.getValue());
                        game.setNbTry(spTry.getValue());
                        game.setOpponentScore(spOppScore.getValue());
                        if (game.getOutcome().equals("Won") && (game.getNbTry()*5+ game.getNbPenalty()*3+ game.getNbConversion()*2
                                + game.getNbDropGoal()*3)<=spOppScore.getValue() )
                            throw new ValidationException("The squad scores cannot be lower that the opponent score in case of a Win");
                        System.out.println(game.toString());
                    }
                    else
                        throw new ValidationException("Error while getting the game");
                    // calling the confirmation window and passing it the game object.
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/application/simplyrugby/confirmUpdateGame.fxml"));
                    Parent root=loader.load();
                    // passing the game to the next window controller.
                    ConfirmGameUptCntlr controller=loader.getController();
                    controller.receiveGame(game);
                    // setting up the next window.
                    Scene scene=new Scene(root);
                    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"),"CSS not found").toExternalForm());
                    Stage stage=new Stage();
                    stage.setTitle("Updating a Game");
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
                    stage.setScene(scene);
                    stage.showAndWait();
                    firstPane.getChildren().clear();
                }

            }catch (ValidationException  |IOException r){
                CustomAlert alert=new CustomAlert("Error:",r.getMessage());
                alert.showAndWait();
            }
        });
    }
//END OF CLASS
}
