package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.NonPlayer;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.ObsListFactory;
import com.application.simplyrugby.System.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * Controller for the Add Member function
 */
public class AddMemberController {

    @FXML
    private TextField txName,txSurname,txTel,txEmail;
    @FXML
    private TextArea taAddress;
    @FXML
    private Button bCreateMember,bCancel;
    @FXML
    private Pane  mainPane,secondPane;
    @FXML
    private ComboBox<String> cbRole;
    public void initialize(){
        bCreateMember.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");
        mainPane.getStyleClass().add("bckg2");
        secondPane.getStyleClass().add("bckg3");

        bCancel.setOnAction((event)->{

            mainPane.getChildren().clear();
        });
        cbRole.setItems(ObsListFactory.createObsList("NonPlayerRoles"));
        cbRole.getSelectionModel().select(0);
        // add the event-handler to the createButton
        bCreateMember.setOnAction((event)->{

            try{
                if (cbRole.getSelectionModel().getSelectedIndex()==0)
                    throw new ValidationException("A role for the member need to be selected");
                else{
                    NonPlayer newMember=new NonPlayer(0,txName.getText(),txSurname.getText(),taAddress.getText(),txTel.getText(), txEmail.getText(),
                            cbRole.getSelectionModel().getSelectedIndex());

                    FXMLLoader loader=new FXMLLoader(getClass().getResource("/com/application/simplyrugby/newMemberConfirm.fxml"));
                    Parent root=loader.load();
                    // sending the member object to the confirmation window
                    newMemberConfirmController controller=loader.getController();
                    controller.receiveMember(newMember);
                    // calling the confirmation window.
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"), "CSS not found").toExternalForm());
                    Stage stage = new Stage();
                    stage.setTitle("Confirm Member Details");
                    stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
                    stage.setScene(scene);
                    stage.showAndWait();
                    mainPane.getChildren().clear();
                }
            }
            catch (IOException | ValidationException e){
                CustomAlert alert=new CustomAlert("Error Add Member panel.",e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
            }
        });
    }
}
