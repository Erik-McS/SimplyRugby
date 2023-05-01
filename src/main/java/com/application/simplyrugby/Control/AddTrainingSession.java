package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.JuniorSquad;
import com.application.simplyrugby.Model.SeniorSquad;
import com.application.simplyrugby.System.ObsListFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    }
}
