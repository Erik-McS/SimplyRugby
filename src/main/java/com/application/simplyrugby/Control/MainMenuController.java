package com.application.simplyrugby.Control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MainMenuController {

    @FXML
    private ImageView mainLogo;
    @FXML
    private Button memberMgtButton,playerMgntButton,gamesMgntButton,exitButton;
    @FXML
    private AnchorPane mainPane;
    @FXML
    protected void exitApp(){System.exit(0);}

    public void initialize(){

        mainPane.getStyleClass().add("bckg1");
        memberMgtButton.getStyleClass().add("bckg5");
        playerMgntButton.getStyleClass().add("bckg5");
        gamesMgntButton.getStyleClass().add("bckg5");
        exitButton.getStyleClass().add("bckg5");

        memberMgtButton.setOnAction((event)->{

            try{

                FXMLLoader loader=new FXMLLoader(MainMenuController.class.getResource("/com/application/simplyrugby/member.fxml"));
                Parent root=loader.load();

                Scene scene=new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"),"CSS File not found").toExternalForm());
                Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setTitle("Simply Rugby Club: Member Management");
                stage.setScene(scene);
                stage.show();

            }
            catch(IOException e){e.printStackTrace();}


        });

        gamesMgntButton.setOnAction((event)->{
            try{

                FXMLLoader loader=new FXMLLoader(MainMenuController.class.getResource("/com/application/simplyrugby/games.fxml"));
                Parent root=loader.load();

                Scene scene=new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"),"CSS File not found").toExternalForm());
                Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setTitle("Simply Rugby Club: Games Management");
                stage.setScene(scene);
                stage.show();

            }
            catch(IOException e){e.printStackTrace();}
        });

        playerMgntButton.setOnAction((event)->{
            try{

                FXMLLoader loader=new FXMLLoader(MainMenuController.class.getResource("/com/application/simplyrugby/players.fxml"));
                Parent root=loader.load();

                Scene scene=new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"),"CSS File not found").toExternalForm());
                Stage stage=(Stage)((Node)event.getSource()).getScene().getWindow();
                stage.setTitle("Simply Rugby Club: Games Management");
                stage.setScene(scene);
                stage.show();

            }
            catch(IOException e){e.printStackTrace();}
        });
    }
}




