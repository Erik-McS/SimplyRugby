package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.JuniorSquad;
import com.application.simplyrugby.System.ObsListFactory;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class deleteJnrSquadController {



    @FXML
    private Button bDeleteSquad,bCancel;
    @FXML
    private Pane mainPane;
    @FXML
    private ComboBox cbSquad;

    public void initialize(){

        mainPane.getStyleClass().add("bckg1");
        bDeleteSquad.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");
        cbSquad.getStyleClass().add("bckg5");
        cbSquad.setItems(ObsListFactory.createObsList(new JuniorSquad()));

        bCancel.setOnAction((event)->{
            // https://9to5answer.com/how-to-close-a-java-window-with-a-button-click-javafx-project
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();

        });
    }
}
