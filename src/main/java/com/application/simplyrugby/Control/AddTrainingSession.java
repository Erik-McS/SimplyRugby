package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.JuniorSquad;
import com.application.simplyrugby.Model.SeniorSquad;
import com.application.simplyrugby.Model.Squad;
import com.application.simplyrugby.Model.TrainingSession;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ObsListFactory;
import com.application.simplyrugby.System.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * controller to the add Training Session panel(s)
 */
public class AddTrainingSession {
    @FXML
    private ComboBox<String> cbSeniorSquad,cbJuniorSquad,cbFacility,cbType;
    @FXML
    Button bCancel,bCreate;
    @FXML
    private VBox vbBox;
    @FXML
    private HBox hbBox;
    @FXML
    private Pane mainPane;
    @FXML
    private DatePicker datePicker;

    /**
     * initialise the window
     */
    public void initialize(){
        // set styles
        mainPane.getStyleClass().add("bckg1");
        vbBox.getStyleClass().add("bckg1");
        hbBox.getStyleClass().add("bckg1");
        cbSeniorSquad.getStyleClass().add("bckg5");
        cbJuniorSquad.getStyleClass().add("bckg5");
        cbFacility.getStyleClass().add("bckg5");
        cbType.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");
        bCreate.getStyleClass().add("bckg5");
        // add lists to the comboBoxes
        cbFacility.setItems(ObsListFactory.createObsList("TrainingFacilities"));
        cbFacility.getSelectionModel().select(0);
        cbType.setItems(ObsListFactory.createObsList("TrainingTypes"));
        cbType.getSelectionModel().select(0);
        cbSeniorSquad.setItems(ObsListFactory.createObsList(new SeniorSquad()));
        cbSeniorSquad.getSelectionModel().select(0);
        cbJuniorSquad.setItems(ObsListFactory.createObsList(new JuniorSquad()));
        cbJuniorSquad.getSelectionModel().select(0);
        // setting event-handlers on the squads comboBoxes to make sure only one type of squad can be selected.
        cbJuniorSquad.setOnAction((event)->{
            if (cbJuniorSquad.getSelectionModel().getSelectedIndex()!=0)
                cbSeniorSquad.setVisible(false);
            else
                cbSeniorSquad.setVisible(true);
        });
        cbSeniorSquad.setOnAction((event)->{
            if (cbSeniorSquad.getSelectionModel().getSelectedIndex()!=0)
                cbJuniorSquad.setVisible(false);
            else cbJuniorSquad.setVisible(true);
        });
        // setting up the cancel button
        bCancel.setOnAction((event)->{
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();
        });
        // event handler for the Create button
        bCreate.setOnAction((event)->{
            try{
                LocalDate date;
                Squad squad=null;
                DateTimeFormatter dt=DateTimeFormatter.ofPattern("dd/MM/yyyy");
                if (datePicker.getValue()!=null){
                    date=datePicker.getValue();
                }
                else
                    throw new ValidationException("A date for the session must be selected");
                // if a senior squad is selected
                if (cbSeniorSquad.getSelectionModel().getSelectedIndex()!=0) {
                    squad = DBTools.loadSquad(new SeniorSquad(), cbSeniorSquad.getSelectionModel().getSelectedIndex());
                }
                else if(cbJuniorSquad.getSelectionModel().getSelectedIndex()!=0){
                    squad=DBTools.loadSquad(new JuniorSquad(),cbJuniorSquad.getSelectionModel().getSelectedIndex());
                }
                DBTools.saveTrainingSession(new TrainingSession(date.format(dt),cbFacility.getSelectionModel().getSelectedIndex(),cbType.getSelectionModel().getSelectedIndex()),squad);
            }
            catch (ValidationException e){
                CustomAlert alert=new CustomAlert("Create a training session",e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        });
    }
}
