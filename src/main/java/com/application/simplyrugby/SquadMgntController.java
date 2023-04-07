package com.application.simplyrugby;

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

public class SquadMgntController {

    @FXML
    private Pane firstPane,secondPane;
    @FXML
    private Button bCreateSeniorSquad,bUpdtSeniorSquad,bDelSeniorSquad,bCreateJuniorSquad,bJUpdtJuniorSquad,bDelJuniorSquad;

    public void initialize(){

        firstPane.getStyleClass().add("bckg2");
        bCreateJuniorSquad.getStyleClass().add("bckg5");
        bCreateSeniorSquad.getStyleClass().add("bckg5");
        bUpdtSeniorSquad.getStyleClass().add("bckg5");
        bJUpdtJuniorSquad.getStyleClass().add("bckg5");
        bDelSeniorSquad.getStyleClass().add("bckg5");
        bDelJuniorSquad.getStyleClass().add("bckg5");

        bCreateSeniorSquad.setOnAction((event)->{

            try{

                secondPane.getChildren().clear();
                FXMLLoader loader=new FXMLLoader(SquadMgntController.class.getResource("createSeniorSquad.fxml"));
                Parent root=loader.load();
                secondPane.getChildren().add(root);

            }catch (IOException e){e.printStackTrace();}
        });

        bCreateJuniorSquad.setOnAction((event)->{

            try{

                secondPane.getChildren().clear();
                FXMLLoader loader=new FXMLLoader(SquadMgntController.class.getResource("createJuniorSquad.fxml"));
                Parent root=loader.load();
                secondPane.getChildren().add(root);

            }catch (IOException e){e.printStackTrace();}
        });

        bUpdtSeniorSquad.setOnAction((event)->{

            try{

                secondPane.getChildren().clear();
                FXMLLoader loader=new FXMLLoader(SquadMgntController.class.getResource("updateSeniorSquad.fxml"));
                Parent root=loader.load();
                secondPane.getChildren().add(root);

            }catch (IOException e){e.printStackTrace();}
        });

        bJUpdtJuniorSquad.setOnAction((event)->{

            try{

                secondPane.getChildren().clear();
                FXMLLoader loader=new FXMLLoader(SquadMgntController.class.getResource("updateJuniorSquad.fxml"));
                Parent root=loader.load();
                secondPane.getChildren().add(root);

            }catch (IOException e){e.printStackTrace();}
        });

        bDelSeniorSquad.setOnAction((event)->{
            try{
                FXMLLoader loader =new FXMLLoader(MembershipMenuController.class.getResource("deleteSeniorSquad.fxml"));
                Parent root=loader.load();
                Scene scene=new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css"),"CSS not found").toExternalForm());
                Stage stage=new Stage();
                //Stage stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setTitle("Delete a Senior Squad");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
                stage.setScene(scene);
                stage.show();
            }catch(IOException e){e.printStackTrace();}

        });

        bDelJuniorSquad.setOnAction((event)->{

            try{
                FXMLLoader loader =new FXMLLoader(MembershipMenuController.class.getResource("deleteJuniorSquad.fxml"));
                Parent root=loader.load();
                Scene scene=new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css"),"CSS not found").toExternalForm());
                Stage stage=new Stage();
                //Stage stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setTitle("Delete a Junior Squad");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
                stage.setScene(scene);
                stage.show();
            }catch(IOException e){e.printStackTrace();}
        });
    }
}
