package com.application.simplyrugby;

import com.application.simplyrugby.System.DBTools;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateProfileController {

    @FXML
    private Pane mainPane;
    @FXML
    private ComboBox cbPlayerProfile;
    @FXML
    private Button bCreateProfile,bCancel;

    ResultSet result;
    public void initialize(){

        mainPane.getStyleClass().add("bckg1");
        bCancel.getStyleClass().add("bckg5");
        bCreateProfile.getStyleClass().add("bckg5");

        bCancel.setOnAction((event)->{
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();
        });

        try{
            result= DBTools.executeSelectQuery("SELECT FIRST_NAME|| ' ' || SURNAME FROM players WHERE PROFILE_ID IS NULL");
            while (result.next()){
                cbPlayerProfile.getItems().add(result.getString(1));
            }
            cbPlayerProfile.getSelectionModel().select(1);
        }catch (SQLException e){e.printStackTrace();}


    }
}
