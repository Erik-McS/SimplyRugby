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

public class ViewSquadsController {

    @FXML
    private ComboBox<String> cbJunior,cbSenior;
    @FXML
    private Button bView,bExit;
    @FXML
    private Pane secondPane;
    private Squad squad=null;

    public void initialize(){
        bView.getStyleClass().add("bckg5");
        bExit.getStyleClass().add("bckg5");
        cbJunior.getStyleClass().add("bckg5");
        cbSenior.getStyleClass().add("bckg5");

        cbSenior.setOnAction((event)->{
            if (cbSenior.getSelectionModel().getSelectedIndex()!=0)
                cbJunior.setVisible(false);
            else
                cbJunior.setVisible(true);
        });

        cbJunior.setOnAction((event)->{
            if (cbJunior.getSelectionModel().getSelectedIndex()!=0)
                cbSenior.setVisible(false);
            else
                cbSenior.setVisible(true);
        });

        cbJunior.setItems(ObsListFactory.createObsList(new JuniorSquad()));
        cbJunior.getSelectionModel().select(0);
        cbSenior.setItems(ObsListFactory.createObsList(new SeniorSquad()));
        cbSenior.getSelectionModel().select(0);
        bView.setOnAction((event)->{
            try{
                if (cbSenior.getSelectionModel().getSelectedIndex()!=0){
                    squad= DBTools.loadSquad(new SeniorSquad(),cbSenior.getSelectionModel().getSelectedIndex());
                }
                else if(cbJunior.getSelectionModel().getSelectedIndex()!=0){
                    squad=DBTools.loadSquad(new JuniorSquad(),cbJunior.getSelectionModel().getSelectedIndex());
                }
            }catch (ValidationException e){
                CustomAlert alert=new CustomAlert("View Squads",e.getMessage());
                alert.showAndWait();
            }

            try {
                FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/application/simplyrugby/viewSelectedSquad.fxml"));
                Parent root=loader.load();
                ViewSelectedSquadCtlr controller=loader.getController();
                Scene scene=new Scene(root);
                Stage stage = new Stage();
                stage.setTitle("View Squad");
                controller.receiveSquad(squad);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"),"CSS file not found").toExternalForm());
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
                stage.setScene(scene);

                stage.showAndWait();
                secondPane.getChildren().clear();
            }
            catch (IOException e){e.printStackTrace();}

        });

        bExit.setOnAction((event)->{
            secondPane.getChildren().clear();
        });
    }
}
