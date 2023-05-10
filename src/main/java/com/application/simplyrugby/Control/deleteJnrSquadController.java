package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.JuniorSquad;
import com.application.simplyrugby.Model.SeniorSquad;
import com.application.simplyrugby.Model.Squad;
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
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * class to manage the delete junior squad window
 */
public class deleteJnrSquadController {
    @FXML
    private Button bDeleteSquad,bCancel;
    @FXML
    private Pane mainPane;
    @FXML
    private ComboBox <String>cbSquad;

    /**
     * initialise the window
     */
    public void initialize(){

        mainPane.getStyleClass().add("bckg1");
        bDeleteSquad.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");
        cbSquad.getStyleClass().add("bckg5");
        cbSquad.setItems(ObsListFactory.createObsList(new JuniorSquad()));
        // event handler to the cancel button
        bCancel.setOnAction((event)->{
            // https://9to5answer.com/how-to-close-a-java-window-with-a-button-click-javafx-project
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();

        });

        bDeleteSquad.setOnAction((event)->{

            try{
                // getting the selected squad, if none, error message
                if (cbSquad.getSelectionModel().getSelectedIndex()==0)
                    throw new ValidationException("Please select a squad to delete first");
                else{
                    // getting the squad data
                    Squad sSquad= DBTools.loadSquad(new SeniorSquad(),cbSquad.getSelectionModel().getSelectedIndex());
                    // preparing the next window.
                    FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/application/simplyrugby/ConfirmSquadDeletion.fxml"));
                    Parent root=loader.load();
                    // passing the squad data to the next controller
                    ConfirmSquadDeletion controller=loader.getController();
                    controller.receiveSquad(sSquad);
                    // displaying the confirmation window
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"), "CSS not found").toExternalForm());
                    Stage stage = new Stage();
                    stage.setTitle("Delete Squad");
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
                    stage.setScene(scene);
                    stage.showAndWait();
                    // closing this window.
                    Stage stage1=(Stage) bDeleteSquad.getScene().getWindow();
                    stage1.close();
                }

            }catch (IOException | ValidationException e){
                CustomAlert alert=new CustomAlert("Delete a Junior Squad error:",e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
            }

        });
    }
}
