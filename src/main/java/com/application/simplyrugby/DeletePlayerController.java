package com.application.simplyrugby;

import com.application.simplyrugby.Control.Player;
import com.application.simplyrugby.System.DBTools;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class DeletePlayerController {

    @FXML
    private Button bDeletePlayer,bCancel;
    @FXML
    private Pane mainPane;
    @FXML
    private ComboBox <String> cbPlayer;
    ResultSet queryResult;
    Player player;
     public void initialize(){

         mainPane.getStyleClass().add("bckg1");
         bDeletePlayer.getStyleClass().add("bckg5");
         bCancel.getStyleClass().add("bckg5");

         bCancel.setOnAction((event)->{
             // https://9to5answer.com/how-to-close-a-java-window-with-a-button-click-javafx-project
             Stage stage=(Stage) bCancel.getScene().getWindow();
             stage.close();
         });
         try {
             // we get the table content in the resultset
             queryResult= DBTools.executeSelectQuery("SELECT first_name,surname FROM players");
             // add the first entry of the list.
             cbPlayer.getItems().add("Select from list");
             // set it as the default entry
             cbPlayer.getSelectionModel().select(0);
             // adding every entry of the NoK table
             while (queryResult.next()){
                 String name=queryResult.getString(1)+" "+queryResult.getString(2);
                 cbPlayer.getItems().add(name);
             }
             DBTools.closeConnections();
         }catch (SQLException e){
             CustomAlert alert=new CustomAlert("Error",e.getMessage());
             alert.showAndWait();
             DBTools.closeConnections();}

         bDeletePlayer.setOnAction((event)->{

             if (cbPlayer.getSelectionModel().getSelectedIndex()!=0){

                 try {
                     FXMLLoader loader =new FXMLLoader(getClass().getResource("confirmMemberDeletion.fxml"));
                     Parent root=loader.load();

                     MemberDeletionController controller=loader.getController();
                     String playerName=cbPlayer.getValue();
                     String[] name=playerName.split(" ");
                     player=(Player)  DBTools.loadMember(Player.dummyPlayer(),
                             DBTools.getID("SELECT player_id FROM players WHERE first_name='"+name[0]+"' AND surname='"+name[1]+"'"));
                     DBTools.closeConnections();
                     //player.toString();
                     controller.deleteMember(player);

                     Scene scene = new Scene(root);
                     scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css"), "CSS not found").toExternalForm());
                     Stage stage1 = new Stage();
                     stage1.setTitle("Confirm player deletion");
                     stage1.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
                     stage1.setScene(scene);
                     stage1.showAndWait();
                     Stage stage2=(Stage) bDeletePlayer.getScene().getWindow();
                     stage2.close();

                 }catch (IOException e){e.printStackTrace();}

             }
             else{
                 CustomAlert alert=new CustomAlert("Error","Please select a player in the list first");
                 alert.showAndWait();
             }
         });
     }

}
