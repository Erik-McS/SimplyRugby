package com.application.simplyrugby.Control;

import com.application.simplyrugby.System.ObsListFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AddTrainingSession {
    @FXML
    private ComboBox<String> cbSquad,cbFacility,cbType;
    @FXML
    Button bCancel,bCreate;
    @FXML
    private VBox vbBox;
    @FXML
    private HBox hbBox;
    @FXML
    private Pane mainPane;

    public void initialize(){

        mainPane.getStyleClass().add("bckg1");
        vbBox.getStyleClass().add("bckg1");
        hbBox.getStyleClass().add("bckg1");
        cbSquad.getStyleClass().add("bckg5");
        cbFacility.getStyleClass().add("bckg5");
        cbType.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");
        bCreate.getStyleClass().add("bckg5");

        cbFacility.setItems(ObsListFactory.createObsList("TrainingFacilities"));
        cbFacility.getSelectionModel().select(0);
        cbType.setItems(ObsListFactory.createObsList("TrainingTypes"));
        cbType.getSelectionModel().select(0);


        bCancel.setOnAction((event)->{
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();
        });

    }
}
