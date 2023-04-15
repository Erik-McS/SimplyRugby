package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.NonPlayer;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ObsListFactory;
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

public class DeleteMemberController {

    @FXML
    private Pane mainPane;
    @FXML
    private Button bDeleteMember,bCancel;
    @FXML
    private ComboBox <String >cbMember;

    public void initialize(){

        mainPane.getStyleClass().add("bckg1");
        bCancel.getStyleClass().add("bckg5");
        bDeleteMember.getStyleClass().add("bckg5");
        cbMember.getStyleClass().add("bckg5");

        cbMember.setItems(ObsListFactory.createObsList(new NonPlayer()));
        cbMember.getSelectionModel().select(0);
        bCancel.setOnAction((event)->{
            // https://9to5answer.com/how-to-close-a-java-window-with-a-button-click-javafx-project
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();
        });

        bDeleteMember.setOnAction((event)->{
            if (cbMember.getSelectionModel().getSelectedIndex()!=0){
                NonPlayer member;
                try {
                    FXMLLoader loader =new FXMLLoader(getClass().getResource("/com/application/simplyrugby/confirmMemberDeletion.fxml"));
                    Parent root=loader.load();

                    MemberDeletionController controller=loader.getController();
                    String memberName=cbMember.getValue().toString();
                    String[] name=memberName.split(" ");

                    member=(NonPlayer)  DBTools.loadMember(new NonPlayer(),
                            DBTools.getID("SELECT member_id FROM non_players WHERE first_name='"+name[0]+"' AND surname='"+name[1]+"'"));
                    member.toString();

                    DBTools.closeConnections();

                    controller.deleteMember(member);

                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"), "CSS not found").toExternalForm());
                    Stage stage1 = new Stage();
                    stage1.setTitle("Confirm player deletion");
                    stage1.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
                    stage1.setScene(scene);
                    stage1.showAndWait();
                    Stage stage2=(Stage) bDeleteMember.getScene().getWindow();
                    stage2.close();

                }catch (IOException e){
                    CustomAlert alert=new CustomAlert("Error Member Deletion",e.getMessage());
                    alert.showAndWait();
                    e.printStackTrace();
                }

            }
            else{
                CustomAlert alert=new CustomAlert("Error","Please select a player in the list first");
                alert.showAndWait();
            }
        });
    }
}
