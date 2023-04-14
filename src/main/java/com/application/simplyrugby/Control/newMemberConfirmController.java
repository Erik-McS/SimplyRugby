package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.NonPlayer;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class newMemberConfirmController {
    @FXML
    private Pane confirmMainPane,leftCPane;
    @FXML
    private Label lName,lSurname,lAddress,lTel,lEmail;
    @FXML
    private Button bCreation,bCancel;

    public void receiveMember(NonPlayer member){

        try{

            if (member!=null){
                lName.setText(member.getFirstName());
                lSurname.setText(member.getSurname());
                lAddress.setText(member.getAddress());
                lTel.setText(member.getTelephone());
                lEmail.setText(member.getEmail());

                bCreation.setOnAction((event)->{
                   if (member.saveMember(member)){
                       // create a confirmation window
                       CustomAlert cs=new CustomAlert("Player Record Created",
                               "Player : "+member.getFirstName()+" "+member.getSurname()+
                               "\nRole: "+ DBTools.getRole(member.getRole_id()));
                       cs.showAndWait();
                       Stage stage=(Stage) bCreation.getScene().getWindow();
                       stage.close();
                   }
                   else {
                       // create a confirmation window""
                       CustomAlert cs=new CustomAlert("Error","The Member record could not be created");
                       cs.showAndWait();
                       Stage stage=(Stage) bCreation.getScene().getWindow();
                       stage.close();
                   }
                });
            }
            else
                throw new ValidationException("No NonPlayer object passed to the controller.");
        }
        catch (ValidationException e){
            CustomAlert alert=new CustomAlert("Error: New Member Confirm",e.getMessage());
            alert.showAndWait();
        }

    }

    public void initialize(){

        confirmMainPane.getStyleClass().add("bckg5");
        leftCPane.getStyleClass().add("bckg2");
        bCancel.getStyleClass().add("bckg5");
        bCreation.getStyleClass().add("bckg5");

        bCancel.setOnAction((event)->{
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();
        });

    }
}
