package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.Member;
import com.application.simplyrugby.Model.NonPlayer;
import com.application.simplyrugby.Model.Player;
import com.application.simplyrugby.System.DBTools;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class MemberDeletionController {
    @FXML
    private Button bConfirmDeletion,bCancel;
    @FXML
    private Label lMemberName;
    @FXML
    private Pane mainPane;

    public void deleteMember(Member member){

        if (member instanceof Player player){

            lMemberName.setText(player.getFirstName()+" "+player.getSurname());

            bConfirmDeletion.setOnAction((event)->{
                DBTools.executeQuery("DELETE FROM players WHERE player_id="+player.getPlayerID());
                Stage stage=(Stage) bConfirmDeletion.getScene().getWindow();
                stage.close();
            });
        }

        if (member instanceof NonPlayer nonPlayer){

            lMemberName.setText(nonPlayer.getFirstName()+" "+nonPlayer.getSurname());
            bConfirmDeletion.setOnAction((event)->{
                DBTools.executeQuery("DELETE FROM non_players WHERE member_id="+nonPlayer.getMember_id());
                Stage stage=(Stage) bConfirmDeletion.getScene().getWindow();
                stage.close();
            });
        }

    }

    public void initialize(){
        bConfirmDeletion.getStyleClass().add("bgck5");
        bCancel.getStyleClass().add("bgck5");
        mainPane.getStyleClass().add("bckg3");

        bCancel.setOnAction((event)->{

            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();
        });
    }
}