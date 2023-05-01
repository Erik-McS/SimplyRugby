package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.Club;
import com.application.simplyrugby.Model.Game;
import com.application.simplyrugby.Model.JuniorSquad;
import com.application.simplyrugby.Model.SeniorSquad;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This controller manages the Add Game function.
 */
public class AddGameController {

    @FXML
    private ComboBox<String> cbSquadSenior,cbSquadJunior,cbClub;
    @FXML
    private TextField txClub,txTelephone,txEmail;
    @FXML
    private TextArea taAddress;
    @FXML
    private RadioButton rbHome,rbAway;
    @FXML
    private Button bCreateGame,bCancel,bCreateClub;
    @FXML
    private Pane firstPane,secondPane;
    @FXML
    private Label lClubSelect,lClub1,lClub2,lClub3,lClub4,lClub5,lOr;
    @FXML
    private DatePicker dpDate;
    boolean clubIsInserted=false;
    private Club club;
    /**
     * Method to initialise the add Game panel
     */
    public void initialize(){
        // setting the item styles
        firstPane.getStyleClass().add("bckg2");
        secondPane.getStyleClass().add("bckg3");
        bCancel.getStyleClass().add("bckg5");
        bCreateGame.getStyleClass().add("bckg5");
        bCreateClub.getStyleClass().add("bckg5");
        cbClub.getStyleClass().add("bckg5");
        cbSquadSenior.getStyleClass().add("bckg5");
        cbSquadJunior.getStyleClass().add("bckg5");

        // creating a Toggle group for the location radio buttons, so only one value can be selected
        ToggleGroup group=new ToggleGroup();
        rbHome.setToggleGroup(group);
        rbAway.setToggleGroup(group);

        // Adding the values to the ComboBoxes
        cbSquadJunior.setItems(ObsListFactory.createObsList(new JuniorSquad()));
        cbSquadSenior.setItems(ObsListFactory.createObsList(new SeniorSquad()));
        cbClub.setItems(ObsListFactory.createObsList("Clubs"));
        cbSquadJunior.getSelectionModel().select(0);
        cbSquadSenior.getSelectionModel().select(0);
        cbClub.getSelectionModel().select(0);

        // if a club is selected, the fields to create one disappear
        cbClub.setOnAction((event)->{

            if (cbClub.getSelectionModel().getSelectedIndex()==0){
                txClub.setVisible(true);
                txTelephone.setVisible(true);
                taAddress.setVisible(true);
                txEmail.setVisible(true);
                bCreateClub.setVisible(true);
                lClub1.setVisible(true);
                lClub2.setVisible(true);
                lClub3.setVisible(true);
                lClub4.setVisible(true);
                lClub5.setVisible(true);

            }
            // and if the user put the list to the default options, show the club creation area again
            else{
                txTelephone.setVisible(false);
                txClub.setVisible(false);
                taAddress.setVisible(false);
                bCreateClub.setVisible(false);
                txEmail.setVisible(false);
                lClub1.setVisible(false);
                lClub2.setVisible(false);
                lClub3.setVisible(false);
                lClub4.setVisible(false);
                lClub5.setVisible(false);
            }
        });
        // if a junior squad is selected, the senior squad combobox disappear
        cbSquadJunior.setOnAction((event)->{
            if (cbSquadJunior.getSelectionModel().getSelectedIndex()==0){
                cbSquadSenior.setVisible(true);
                lOr.setVisible(true);
            }
            else
            {
                cbSquadSenior.setVisible(false);
                lOr.setVisible(false);
            }

        });
        // if a senior squad is selected, the senior squad combobox disappear
        cbSquadSenior.setOnAction((event)->{
            if (cbSquadSenior.getSelectionModel().getSelectedIndex()==0){
                cbSquadJunior.setVisible(true);
                lOr.setVisible(true);
            }
            else{
                cbSquadJunior.setVisible(false);
                lOr.setVisible(false);
            }
        });

        // here, once the createGame button is pressed, we will gather and test the data collected in the form
        bCreateGame.setOnAction((event)->{
            // try to catch any errors and display an error message if any.
            Game game;
            LocalDate date;
            int location;
            DateTimeFormatter dt=DateTimeFormatter.ofPattern("dd/MM/yyyy");
            try{
                // we check that team has been selected.
                if (cbSquadSenior.getSelectionModel().getSelectedIndex()==0 && cbSquadJunior.getSelectionModel().getSelectedIndex()==0)
                    throw new ValidationException("A Junior or Senior squad must be selected to create a game.");
                // if a Senior squad is selected.
                else if (cbSquadSenior.getSelectionModel().getSelectedIndex()!=0 && cbSquadJunior.getSelectionModel().getSelectedIndex()==0){
                    // testing a date has been picked
                    if (dpDate.getValue()!=null)
                        date=dpDate.getValue();
                    else
                        throw new ValidationException("A date for the game must be picked");
                    // testing that a location has been picked
                    if(group.getSelectedToggle()!=null){
                        // getting the selected button
                        RadioButton selected=(RadioButton) group.getSelectedToggle();
                        // testing the radio button value to determine which location id to save in the DB
                        if (selected.getText().equals("Home"))
                            location=1;
                        else
                            location=2;
                    }
                    else
                        throw new ValidationException("A location for the game must be picked");

                    // creating the Club object
                    // if selected from the comboBox
                    if (cbClub.getSelectionModel().getSelectedIndex()!=0){
                        club=DBTools.getClub(cbClub.getSelectionModel().getSelectedIndex());
                        clubIsInserted=true;
                    }

                    // we check if the club was created, if not flagged as such, gives an error message
                    // otherwise, it means the club object was created by the createClub button
                    if (!clubIsInserted)
                        throw new ValidationException("A rival club must either be selected or created");
                    else {

                        game=new Game(DBTools.loadSquad(new SeniorSquad(),cbSquadSenior.getSelectionModel().getSelectedIndex()),club,date.format(dt),location);
                        //preparing and showing the confirmation window
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/application/simplyrugby/ConfirmGameCreation.fxml"));
                        Parent root=loader.load();
                        ConfirmGameCreationCtlr controller=loader.getController();
                        // passing the info to the confirmation window
                        controller.receiveGame(game);

                        // setting the next view
                        Scene scene = new Scene(root);
                        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"), "CSS not found").toExternalForm());
                        Stage stage = new Stage();
                        stage.setTitle("Confirm Player Details");
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
                        stage.setScene(scene);
                        stage.showAndWait();
                        firstPane.getChildren().clear();
                    }
                // End of SeniorSquad parts
                }
                // otherwise it means it's a Junior squad
                else if (cbSquadJunior.getSelectionModel().getSelectedIndex()!=0)
                {
                    // testing a date has been picked
                    if (dpDate.getValue()!=null)
                        date=dpDate.getValue();
                    else
                        throw new ValidationException("A date for the game must be picked");
                    // testing that a location has been picked
                    if(group.getSelectedToggle()!=null){
                        // getting the selected button
                        RadioButton selected=(RadioButton) group.getSelectedToggle();
                        // testing the radio button value to determine which location id to save in the DB
                        if (selected.getText().equals("Home"))
                            location=1;
                        else
                            location=2;
                    }
                    else
                        throw new ValidationException("A location for the game must be picked");

                    // creating the Club object
                    // if selected from the comboBox
                    if (cbClub.getSelectionModel().getSelectedIndex()!=0){
                        club=DBTools.getClub(cbClub.getSelectionModel().getSelectedIndex());
                        clubIsInserted=true;
                    }
                    // we check if the club was created, if not flagged as such, gives an error message
                    // otherwise, it means the club object was created by the createClub button
                    if (!clubIsInserted)
                        throw new ValidationException("A rival club must either be selected or created");
                    else {
                        JuniorSquad jn=(JuniorSquad) DBTools.loadSquad(new JuniorSquad(),cbSquadJunior.getSelectionModel().getSelectedIndex());
                        game=new Game(jn,club,date.format(dt),location);
                        //preparing and showing the confirmation window
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/application/simplyrugby/ConfirmGameCreation.fxml"));
                        Parent root=loader.load();
                        ConfirmGameCreationCtlr controller=loader.getController();
                        // passing the info to the confirmation window
                        controller.receiveGame(game);
                        // setting the next view
                        Scene scene = new Scene(root);
                        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"), "CSS not found").toExternalForm());
                        Stage stage = new Stage();
                        stage.setTitle("Add Game");
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
                        stage.setScene(scene);
                        stage.showAndWait();
                        firstPane.getChildren().clear();
                    }
                    // End of JuniorSquad parts
                }
            }catch(IOException | ValidationException e){
                CustomAlert alert=new CustomAlert("Game Creation Error:",e.getMessage());
                //e.printStackTrace();
                alert.showAndWait();
            }
        });

        bCreateClub.setOnAction((event)->{
            try{
                String clubName;
                String clubAddress;
                String telephone;
                String clubEmail;
                // getting the Club name
                if (!txClub.getText().equals(""))
                    clubName=txClub.getText();
                else
                    throw new ValidationException("The Club name cannot be empty");
                // getting the club telephone.
                if (txTelephone.getText().equals(""))
                    throw new ValidationException("Telephone cannot be empty");
                else {
                    // testing if the tel format is valid
                    String regex = "[0-9]{9,11}";
                    if (txTelephone.getText().matches(regex))
                        telephone = txTelephone.getText();
                    else
                        throw new ValidationException("Phone numbers can only contains 9-11 digits");
                }

                //getting the club address
                if (taAddress.equals(""))
                    throw new ValidationException("Address filed cannot be empty");
                else
                    clubAddress=taAddress.getText();

                // getting the email

                if (txEmail.equals(""))
                    throw new ValidationException("Email cannot be empty");
                else {
                    String validation = "^[\\w-\\.\\_]+@([\\w-]+\\.)+[\\w-]{2,4}$";
                    Pattern pattern = Pattern.compile(validation);
                    Matcher matcher = pattern.matcher(txEmail.getText());
                    if (matcher.matches())
                        clubEmail = txEmail.getText();
                    else
                        throw new ValidationException("Invalid email.");
                }
                // creating and saving the Club object.
                Club club=new Club(clubName,clubAddress,telephone,clubEmail);
                DBTools.saveClub(club);
                CustomAlert alert =new CustomAlert("Club Creation","The club '"+clubName+"' has been created");
                alert.showAndWait();
                clubIsInserted=true;
                // we hide all the Club selection/creation items.
                txTelephone.setVisible(false);
                txClub.setVisible(false);
                taAddress.setVisible(false);
                bCreateClub.setVisible(false);
                lClub2.setVisible(false);
                lClub3.setVisible(false);
                lClub4.setVisible(false);
                lClub5.setVisible(false);
                bCreateClub.setVisible(false);
                cbClub.setVisible(false);
                lClub1.setText("The rival club is now selected.");

            }catch(ValidationException e){
                CustomAlert alert=new CustomAlert("No Squad Selected",e.getMessage());
                alert.showAndWait();
            }

        });

        bCancel.setOnAction((event->{
            firstPane.getChildren().clear();
            firstPane.getStyleClass().add("bckg1");
        }));
    }




}
