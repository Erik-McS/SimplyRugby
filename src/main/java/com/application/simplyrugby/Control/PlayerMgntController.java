package com.application.simplyrugby.Control;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Class to manage the Player management menu
 */
public class PlayerMgntController {

    @FXML
    private Pane firstPane,secondPane;
    @FXML
    private Button bCreateProfile,bUpdtProfile, bConsultProfile,bViewSquad;
    // TODO add a consult squad view
    /**
     * Initialise the window
     */
    public void initialize(){

        firstPane.getStyleClass().add("bckg2");
        bCreateProfile.getStyleClass().add("bckg5");
        bUpdtProfile.getStyleClass().add("bckg5");
        bConsultProfile.getStyleClass().add("bckg5");
        bViewSquad.getStyleClass().add("bckg5");

        bCreateProfile.setOnAction((event)->{
            try{

                FXMLLoader loader =new FXMLLoader(MembershipMenuController.class.getResource("/com/application/simplyrugby/createPlayerProfile.fxml"));
                Parent root=loader.load();
                Scene scene=new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"),"CSS not found").toExternalForm());
                Stage stage=new Stage();
                //Stage stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setTitle("Create a Training Profile");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
                stage.setScene(scene);
                stage.show();

            }catch(IOException e){e.printStackTrace();}

        });

        bUpdtProfile.setOnAction((event)->{
            try{
                secondPane.getChildren().clear();
                FXMLLoader loader=new FXMLLoader(SquadMgntController.class.getResource("/com/application/simplyrugby/updatePlayerProfile.fxml"));
                Parent root=loader.load();
                secondPane.getChildren().add(root);
            }catch (IOException e){e.printStackTrace();}
        });

        bConsultProfile.setOnAction((event)->{
            try{
                secondPane.getChildren().clear();
                FXMLLoader loader=new FXMLLoader(SquadMgntController.class.getResource("/com/application/simplyrugby/consultPlayerProfile.fxml"));
                Parent root=loader.load();
                secondPane.getChildren().add(root);
            }catch (IOException e){e.printStackTrace();}
        });
        bViewSquad.setOnAction((event)->{

            try{
                secondPane.getChildren().clear();
                FXMLLoader loader=new FXMLLoader(SquadMgntController.class.getResource("/com/application/simplyrugby/viewSquads.fxml"));
                Parent root=loader.load();
                secondPane.getChildren().add(root);
            }catch (IOException e){e.printStackTrace();}

        });
    }
//END OF CLASS
}
