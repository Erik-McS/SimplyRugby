package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.Member;
import com.application.simplyrugby.Model.NonPlayer;
import com.application.simplyrugby.Model.Player;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * class to manage the non player member deletion
 */
public class MemberDeletionController {
    @FXML
    private Button bConfirmDeletion,bCancel;
    @FXML
    private Label lMemberName;
    @FXML
    private Pane mainPane;

    /**
     * Function to receive the member object to delete
     * @param member member to delete
     */
    public void deleteMember(Member member){

        if (member instanceof Player player){

            lMemberName.setText(player.getFirstName()+" "+player.getSurname());
            bConfirmDeletion.setOnAction((event)->{

                try{
                    // that the player is not assigned in a squad.
                    if (!DBTools.playerIsAssignedToSquad(player.getPlayerID())){

                        DBTools.executeUpdateQuery("DELETE FROM players WHERE player_id="+player.getPlayerID());
                        Stage stage=(Stage) bConfirmDeletion.getScene().getWindow();
                        stage.close();
                    }
                    else
                        throw new ValidationException("This player is assigned to a Squad." +
                                "\nYou will need to recreate the squad with his/her replacement first" +
                                "\nbefore being able to delete this record.");
                }catch (ValidationException e){
                    CustomAlert alert =new CustomAlert("Delete Player Error:",e.getMessage());
                    alert.showAndWait();
                    Stage stage=(Stage) bConfirmDeletion.getScene().getWindow();
                    stage.close();
                }


            });
        }

        if (member instanceof NonPlayer nonPlayer){

            lMemberName.setText(nonPlayer.getFirstName()+" "+nonPlayer.getSurname());
            bConfirmDeletion.setOnAction((event)->{

                try{
                    if (!DBTools.isPartOfTeam((NonPlayer) member)){
                        DBTools.executeUpdateQuery("DELETE FROM non_players WHERE member_id="+nonPlayer.getMember_id());
                        Stage stage=(Stage) bConfirmDeletion.getScene().getWindow();
                        stage.close();
                    }
                    else
                        throw new ValidationException("This Club member is assigned to a Squad." +
                                "\nYou will need to recreate the squad with his/her replacement first" +
                                "\nbefore being able to delete this record.");

                }catch (ValidationException e){
                    CustomAlert alert =new CustomAlert("Delete Player Error:",e.getMessage());
                    alert.showAndWait();
                    Stage stage=(Stage) bConfirmDeletion.getScene().getWindow();
                    stage.close();
                }

            });
        }
    }

    /**
     * initialise the window
     */
    public void initialize(){
        bConfirmDeletion.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");
        mainPane.getStyleClass().add("bckg3");

        bCancel.setOnAction((event)->{
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();
        });
    }
}
