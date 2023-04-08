package com.application.simplyrugby;

import com.application.simplyrugby.Control.DBTools;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class manages the Add Player function.it allows to add a player,its next of kin and doctor.<br>
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
    private ToggleButton tbAddNOK,tbCreateNOK,tbAddDoctor,tbCreateDoctor;
    @FXML
    private TextArea txAdress;
    @FXML
    private Label lNameNoK,lSurnameNoK,lTelNoK,lTitleNOK,lTitleDoc,lNameDoc,lSurnameDoc,lTelDoc;
    private ResultSet queryResult;

    /**
     * the initialize method set the style of the window/pane.<br>
     * it uses the styles.css file to color the background and buttons.<br>
     * all events handlers use lambda functions.
     */
    public void initialize(){

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
        tbCreateDoctor.getStyleClass().add("bckg5");
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
            queryResult= DBTools.executeSelectQuery("SELECT name||' '|| surname FROM next_of_kin");
            // add the first entry of the list.
            cbNOK.getItems().add("Select from list");
            // set it as the default entry
            cbNOK.getSelectionModel().select(0);
            // adding every entry of the NoK table
            while (queryResult.next()){
                cbNOK.getItems().add(queryResult.getString(1));
            }

        }catch (SQLException e){e.printStackTrace();}
        // we are lsitening to the combobos to see if an entry is selected.
        // if so, twe hide the textfields used to create a NoK
        cbNOK.setOnAction((event)->{

            if (cbNOK.getSelectionModel().getSelectedIndex()!=0){
                txNameNOK.setVisible(false);
                txSurnameNOK.setVisible(false);
                txTelNOK.setVisible(false);
                lNameNoK.setVisible(false);
                lSurnameNoK.setVisible(false);
                lTelNoK.setVisible(false);
                tbCreateNOK.setVisible(false);
                lTitleNOK.setVisible(false);
            }
            else {
                txNameNOK.setVisible(true);
                txSurnameNOK.setVisible(true);
                txTelNOK.setVisible(true);
                lNameNoK.setVisible(true);
                lSurnameNoK.setVisible(true);
                lTelNoK.setVisible(true);
                tbCreateNOK.setVisible(true);
                lTitleNOK.setVisible(true);
            }
        });
        // same process/logic for the Doctors comboBox

        try {
            queryResult=DBTools.executeSelectQuery("SELECT name||' '||surname FROM player_doctors");
            cbDoctor.getItems().add("Select from list");
            cbDoctor.getSelectionModel().select(0);
            while (queryResult.next()){
                cbDoctor.getItems().add(queryResult.getString(1));
            }
        }
        catch (SQLException e){e.printStackTrace();}

        cbDoctor.setOnAction((event->{

            if (cbDoctor.getSelectionModel().getSelectedIndex()!=0){
                txNameDoctor.setVisible(false);
                txSurnameDoctor.setVisible(false);
                txTelDoctor.setVisible(false);
                lTitleDoc.setVisible(false);
                lNameDoc.setVisible(false);
                lSurnameDoc.setVisible(false);
                lTelDoc.setVisible(false);
                tbCreateDoctor.setVisible(false);
            }
            else{
                txNameDoctor.setVisible(true);
                txSurnameDoctor.setVisible(true);
                txTelDoctor.setVisible(true);
                lTitleDoc.setVisible(true);
                lNameDoc.setVisible(true);
                lSurnameDoc.setVisible(true);
                lTelDoc.setVisible(true);
                tbCreateDoctor.setVisible(true);

            }
        }));
    }

}
