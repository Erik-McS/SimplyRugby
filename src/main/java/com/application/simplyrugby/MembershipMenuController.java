package com.application.simplyrugby;

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

public class MembershipMenuController {

    @FXML
    private Button addPlayer,deletePlayer,addMember,deleteMember,mainMenu;
    @FXML
    private Pane menuPane,mainPane;
    public void initialize(){

        menuPane.getStyleClass().add("bckg1");
        mainPane.getStyleClass().add("bckg1");
        addPlayer.getStyleClass().add("bckg5");
        deletePlayer.getStyleClass().add("bckg5");
        addMember.getStyleClass().add("bckg5");
        deleteMember.getStyleClass().add("bckg5");
        mainMenu.getStyleClass().add("bckg5");

        addPlayer.setOnAction((event)->{

            try{
                // clear the main area
                mainPane.getChildren().clear();
                // create a loader to store the add player pane
                FXMLLoader loader =new FXMLLoader(MembershipMenuController.class.getResource("addPlayer.fxml"));
                // lood the add player pane into a node
                Parent root=loader.load();
                // add the pane
                mainPane.getChildren().add(root);
            }
            catch(IOException e){e.printStackTrace();}

        });

        deletePlayer.setOnAction((event)->{

            try{

                FXMLLoader loader =new FXMLLoader(MembershipMenuController.class.getResource("deletePlayer.fxml"));
                Parent root=loader.load();
                Scene scene=new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css"),"CSS not found").toExternalForm());
                Stage stage=new Stage();
                //Stage stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setTitle("Delete a Player");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
                stage.setScene(scene);
                stage.show();

            }catch(IOException e){e.printStackTrace();}
        });

        addMember.setOnAction((event)->{

            try{
                mainPane.getChildren().clear();
                // create a loader to store the add player pane
                FXMLLoader loader =new FXMLLoader(MembershipMenuController.class.getResource("addMember.fxml"));
                // lood the add player pane into a node
                Parent root=loader.load();
                // add the pane
                mainPane.getChildren().add(root);
            }catch (IOException e){e.printStackTrace();}

        });

        deleteMember.setOnAction((event)->{

            try{

                FXMLLoader loader =new FXMLLoader(MembershipMenuController.class.getResource("deleteMember.fxml"));
                Parent root=loader.load();
                Scene scene=new Scene(root);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css"),"CSS not found").toExternalForm());
                Stage stage=new Stage();
                //Stage stage=(Stage) ((Node)event.getSource()).getScene().getWindow();
                stage.setTitle("Delete a Member");
                stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
                stage.setScene(scene);
                stage.show();

            }catch(IOException e){e.printStackTrace();}
        });
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
