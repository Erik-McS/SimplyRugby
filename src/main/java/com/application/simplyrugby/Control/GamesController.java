package com.application.simplyrugby.Control;

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

public class GamesController {


    @FXML
    private Button addGame,updateGame,mainMenu;
    @FXML
    private Pane mainPane,menuPane;

    public void initialize(){

        menuPane.getStyleClass().add("bckg1");
        mainPane.getStyleClass().add("bckg1");
        addGame.getStyleClass().add("bckg5");
        updateGame.getStyleClass().add("bckg5");
        mainMenu.getStyleClass().add("bckg5");


        mainMenu.setOnAction((event)->{
            try{

                FXMLLoader loader=new FXMLLoader(MainMenuController.class.getResource("/com/application/simplyrugby/main_menu.fxml"));
                Parent root=loader.load();

                Scene scene=new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"),"CSS File not found").toExternalForm());
                Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setTitle("Simply Rugby Club: Main Menu");
                stage.setScene(scene);
                stage.show();

            }
            catch(IOException e){e.printStackTrace();}
        });

        addGame.setOnAction((event)->{

            try{
                mainPane.getChildren().clear();
                // create a loader to store the addPlayer pane
                FXMLLoader loader =new FXMLLoader(MembershipMenuController.class.getResource("/com/application/simplyrugby/addGame.fxml"));
                // load the add player pane into a node
                Parent root=loader.load();
                // add the pane
                mainPane.getChildren().add(root);
            }catch (IOException e){e.printStackTrace();}
        });

        updateGame.setOnAction((event)->{
            try{
                mainPane.getChildren().clear();
                // create a loader to store the addPlayer pane
                FXMLLoader loader =new FXMLLoader(MembershipMenuController.class.getResource("/com/application/simplyrugby/updateGame.fxml"));
                // load the add player pane into a node
                Parent root=loader.load();
                // add the pane
                mainPane.getChildren().add(root);
            }catch (IOException e){e.printStackTrace();}

        });
    }
}
