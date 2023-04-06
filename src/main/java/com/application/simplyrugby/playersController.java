package com.application.simplyrugby;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class playersController{

    @FXML
    Pane mainPane,menuPane;
    @FXML
    Button squadMgnt,playerMgnt,createSession,mainMenu;

    public void initialize(){

        menuPane.getStyleClass().add("bckg1");
        mainPane.getStyleClass().add("bckg1");
        squadMgnt.getStyleClass().add("bckg5");
        playerMgnt.getStyleClass().add("bckg5");
        createSession.getStyleClass().add("bckg5");
        mainMenu.getStyleClass().add("bckg5");

        mainMenu.setOnAction((event)->{
            try{

                FXMLLoader loader=new FXMLLoader(MainMenuController.class.getResource("main_menu.fxml"));
                Parent root=loader.load();

                Scene scene=new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css"),"CSS File not found").toExternalForm());
                Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setTitle("Simply Rugby Club: Main Menu");
                stage.setScene(scene);
                stage.show();

            }
            catch(IOException e){e.printStackTrace();}
        });

    }
}


