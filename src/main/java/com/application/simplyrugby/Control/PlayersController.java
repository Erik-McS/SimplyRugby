package com.application.simplyrugby.Control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Class to manage the player management panel
 */
public class PlayersController {

    @FXML
    private Pane mainPane,menuPane;
    @FXML
    private Button squadMgnt,playerMgnt,createSession,mainMenu;

    /**
     * initialise the window
     */
    public void initialize(){

        menuPane.getStyleClass().addAll("bckg1","borderBlack");
        mainPane.getStyleClass().addAll("bckg1","borderBlack");
        squadMgnt.getStyleClass().add("bckg5");
        playerMgnt.getStyleClass().add("bckg5");
        createSession.getStyleClass().add("bckg5");
        mainMenu.getStyleClass().add("bckg5");

        mainMenu.setOnAction((event)->{
            try{

                FXMLLoader loader=new FXMLLoader(PlayersController.class.getResource("/com/application/simplyrugby/main_menu.fxml"));
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

        squadMgnt.setOnAction((event)->{

            try{

                mainPane.getChildren().clear();
                FXMLLoader loader=new FXMLLoader(PlayersController.class.getResource("/com/application/simplyrugby/squadManagement.fxml"));
                Parent root=loader.load();
                mainPane.getChildren().add(root);

            }catch(IOException e){e.printStackTrace();}
        });

        playerMgnt.setOnAction((event)->{

            try{

                mainPane.getChildren().clear();
                FXMLLoader loader=new FXMLLoader(PlayersController.class.getResource("/com/application/simplyrugby/playerManagement.fxml"));
                Parent root=loader.load();
                mainPane.getChildren().add(root);

            }catch(IOException e){e.printStackTrace();}
        });

        createSession.setOnAction((event)->{
            try{

                FXMLLoader loader =new FXMLLoader(MembershipMenuController.class.getResource("/com/application/simplyrugby/createTrainingSession.fxml"));
                Parent root=loader.load();
                Scene scene=new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"),"CSS not found").toExternalForm());
                Stage stage=new Stage();
                stage.setTitle("Create a Training Session");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
                stage.setScene(scene);
                stage.show();

            }catch(IOException e){e.printStackTrace();}
        });
    }
}


