package com.application.simplyrugby;

import com.application.simplyrugby.Control.Doctor;
import com.application.simplyrugby.Control.NextOfKin;
import com.application.simplyrugby.Control.Player;
import com.application.simplyrugby.System.DBTools;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * This class manages the Add Member function.it allows to add a player,its next of kin and doctor.<br>
 * it is linked to the addPlayer.fxml file.
 * @author Erik McSeveney
 */
public class AddPlayerController {

    // we declare here the fmxl elements so that they can be manipulated by the controller.
    @FXML
    private Pane mainPane,leftPane,nokPane,docPane;
    @FXML
    private TextField txName,txSurname,txTelephone,txEmail,txScrumsNumber,txNameNOK,txSurnameNOK,txTelNOK,
            txNameDoctor,txSurnameDoctor,txTelDoctor;
    @FXML
    private DatePicker dpDateOfBirth;
    @FXML
    private RadioButton rbMale,rbFemale;
    @FXML
    private Button bCreatePlayer,bCancel,bAddConsentForm;
    @FXML
    private ComboBox cbNOK,cbDoctor;
    @FXML
    private Button tbAddNOK,tbCreateNOK,tbAddDoctor,tbCreateDoctor;
    @FXML
    private TextArea txAddress;
    @FXML
    private Label lNameNoK,lSurnameNoK,lTelNoK,lTitleNOK,lTitleDoc,lNameDoc,lSurnameDoc,lTelDoc;
    private ResultSet queryResult;
    @FXML NextOfKin nok1;
    @FXML
    boolean nokSelected,docSelected;
    boolean nokListChanged=false;
    boolean docListChanged=false;
    /**
     * the initialize method set the style of the window/pane.<br>
     * it uses the styles.css file to color the background and buttons.<br>
     * all events handlers use lambda functions.
     */
    public void initialize(){
        nok1=new NextOfKin();
        // set Pane background color
        mainPane.getStyleClass().add("bckg5");
        leftPane.getStyleClass().add("bckg2");
        nokPane.getStyleClass().add("bckg3");
        docPane.getStyleClass().add("bckg4");
        // set Buttons style.
        bCreatePlayer.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");
        bAddConsentForm.getStyleClass().add("bckg5");
        tbAddDoctor.getStyleClass().add("bckg5");
        tbAddNOK.getStyleClass().add("bckg5");
        tbAddNOK.setVisible(false);
        tbCreateDoctor.getStyleClass().add("bckg5");
        tbAddDoctor.setVisible(false);
        tbCreateNOK.getStyleClass().add("bckg5");
        // set a group for the radio buttons so only one can be checked.
        ToggleGroup group=new ToggleGroup();
        rbFemale.setToggleGroup(group);
        rbMale.setToggleGroup(group);

        // add a function to clear the main pane
        bCancel.setOnAction((event)->{
            mainPane.getChildren().clear();
        });

        // we add the content of the Next of Kin table to the comboBox
        try {
            // we get the table content in the resultset
            queryResult= DBTools.executeSelectQuery("SELECT name,surname FROM next_of_kin");
            // add the first entry of the list.
            cbNOK.getItems().add("Select from list");
            // set it as the default entry
            cbNOK.getSelectionModel().select(0);
            // adding every entry of the NoK table
            while (queryResult.next()){
                String name=queryResult.getString(1)+" "+queryResult.getString(2);
                cbNOK.getItems().add(name);
            }

        }catch (SQLException e){e.printStackTrace();}
        // we are listening to the ComboBoxes to see if an entry is selected.
        // if so, twe hide the textfields used to create a NoK
        cbNOK.setOnAction((event)->{

            if (cbNOK.getSelectionModel().getSelectedIndex()!=0){
                tbAddNOK.setVisible(true);
                txNameNOK.setVisible(false);
                txSurnameNOK.setVisible(false);
                txTelNOK.setVisible(false);
                lNameNoK.setVisible(false);
                lSurnameNoK.setVisible(false);
                lTelNoK.setVisible(false);
                tbCreateNOK.setVisible(false);
                lTitleNOK.setVisible(false);
                nokListChanged=true;
            }
            else {
                if (nokListChanged)
                    tbAddNOK.setVisible(false);
                txNameNOK.setVisible(true);
                txSurnameNOK.setVisible(true);
                txTelNOK.setVisible(true);
                lNameNoK.setVisible(true);
                lSurnameNoK.setVisible(true);
                lTelNoK.setVisible(true);
                tbCreateNOK.setVisible(true);
                lTitleNOK.setVisible(true);
                nokListChanged=false;
            }
        });
        // same process/logic for the Doctors comboBox

        try {
            queryResult=DBTools.executeSelectQuery("SELECT name,surname FROM player_doctors");
            cbDoctor.getItems().add("Select from list");
            cbDoctor.getSelectionModel().select(0);
            while (queryResult.next()){
                String name=queryResult.getString(1)+" "+queryResult.getString(2);
                cbDoctor.getItems().add(name);
            }
        }
        catch (SQLException e){e.printStackTrace();}

        cbDoctor.setOnAction((event->{

            if (cbDoctor.getSelectionModel().getSelectedIndex()!=0){
                tbAddDoctor.setVisible(true);
                txNameDoctor.setVisible(false);
                txSurnameDoctor.setVisible(false);
                txTelDoctor.setVisible(false);
                lTitleDoc.setVisible(false);
                lNameDoc.setVisible(false);
                lSurnameDoc.setVisible(false);
                lTelDoc.setVisible(false);
                tbCreateDoctor.setVisible(false);
                docListChanged=true;
            }
            else{
                if (docListChanged)
                    tbAddDoctor.setVisible(false);
                txNameDoctor.setVisible(true);
                txSurnameDoctor.setVisible(true);
                txTelDoctor.setVisible(true);
                lTitleDoc.setVisible(true);
                lNameDoc.setVisible(true);
                lSurnameDoc.setVisible(true);
                lTelDoc.setVisible(true);
                tbCreateDoctor.setVisible(true);
                docListChanged=false;

            }
        }));
        tbAddNOK.setOnAction((event)->{

            try{

                queryResult= DBTools.executeSelectQuery("SELECT name,surname,telephone FROM next_of_kin WHERE kin_id="+cbNOK.getSelectionModel().getSelectedIndex());
                nok1.setFirstName(queryResult.getString(1));
                nok1.setSurname(queryResult.getString(2));
                nok1.setTelephone(queryResult.getString(3));
                System.out.println(nok1.toString());

            }catch(SQLException e){e.printStackTrace();}

        });
        bCreatePlayer.setOnAction((event)->{
            Player nPlayer;
            NextOfKin nok=nok1;
            Doctor doc=null;
            boolean formIsValid=false;
            String gender="";
            LocalDate date;
            DateTimeFormatter dt=DateTimeFormatter.ofPattern("dd/MM/yyyy");

            if (group.getSelectedToggle()!=null){
                RadioButton selected=(RadioButton) group.getSelectedToggle();
                gender=selected.getText();
            }
            else
                gender="Male";
            try {
                if (dpDateOfBirth.getValue()!=null) {
                    date = dpDateOfBirth.getValue();
                    nPlayer=new Player.PlayerBuilder().setPlayerID(0).setFirstName(txName.getText()).setSurname(txSurname.getText()).setAddress(txAddress.getText())
                            .setDoB(date.format(dt)).setEmail(txEmail.getText()).setGender(gender).setScrumsNumber(Integer.parseInt(txScrumsNumber.getText()))
                            .setTelephone(txTelephone.getText()).setIsAssignedToSquad(false).setDoctorID(1).setKinID(1).setProfileID(0).Builder();
                    formIsValid=true;
                    }
                    else
                        throw new ValidationException("Date of Birth cannot be empty");

                }catch (ValidationException e){
                    nPlayer=null;
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText(e.getMessage());
                    System.out.println(e.getMessage());
                    alert.showAndWait();
                }

                if (formIsValid) {
                    try {
                        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("newPlayerConfirm.fxml"));
                        Parent root = loader1.load();
                        newPlayerConfirmController newPlayerConfirmController = loader1.getController();
                        newPlayerConfirmController.receivePlayerObject(nPlayer,nok,doc);


                        Scene scene = new Scene(root);
                        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css"), "CSS not found").toExternalForm());
                        Stage stage = new Stage();
                        //https://genuinecoder.com/javafx-communication-between-controllers/

                        stage.setTitle("Confirm Player Details");
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
                        stage.setScene(scene);
                        //stage.setUserData(newPlayer);
                        stage.show();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("error ??");
                    }
                }
        });
    }
// END OF CLASS
}
