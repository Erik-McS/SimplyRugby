package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.Player;
import com.application.simplyrugby.Model.TrainingProfile;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.QueryResult;
import com.application.simplyrugby.System.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.sql.SQLException;

/**
 * controller to manage rhe create Profile panel
 */
public class CreateProfileController {

    @FXML
    private Pane mainPane;
    @FXML
    private ComboBox<String > cbPlayerProfile;
    @FXML
    private Button bCreateProfile,bCancel;

    /**
     * initialise the window
     */
    public void initialize(){

        mainPane.getStyleClass().add("bckg1");
        bCancel.getStyleClass().add("bckg5");
        bCreateProfile.getStyleClass().add("bckg5");

        bCancel.setOnAction((event)->{
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();
        });

        try(
                QueryResult queryResult=DBTools.executeSelectQuery("SELECT PLAYER_ID,FIRST_NAME|| ' ' || SURNAME FROM players " +
                "WHERE NOT EXISTS (" +
                "SELECT * FROM training_profiles WHERE training_profiles.player_id=players.player_id)")
                )
        {
            cbPlayerProfile.getItems().add("Select a player from the list");
            while (queryResult.getResultSet().next()){
                cbPlayerProfile.getItems().add(queryResult.getResultSet().getString(2));
            }
            cbPlayerProfile.getSelectionModel().select(0);
        }catch (ValidationException|SQLException e){
            e.printStackTrace();
            CustomAlert alert=new CustomAlert("Profile List Error",e.getMessage());
            alert.showAndWait();
        }

        bCreateProfile.setOnAction((event)->{

            if (cbPlayerProfile.getSelectionModel().getSelectedIndex()!=0){

                String[] pl=cbPlayerProfile.getValue().split(" ");
                Player selectedPlayer=(Player) DBTools.loadMember(Player.dummyPlayer(),
                        DBTools.getID("SELECT player_id FROM players WHERE first_name='"+pl[0]+"' AND surname='"+pl[1]+"'"));

                try{
                    TrainingProfile newTP=new TrainingProfile(selectedPlayer);

                    newTP.saveTrainingProfile();
                    CustomAlert alert=new CustomAlert("Training Profile","The training profile for "+selectedPlayer.getFirstName()+" "+selectedPlayer.getSurname()+" has been created.");

                    alert.showAndWait();
                    Stage stage=(Stage) cbPlayerProfile.getScene().getWindow();
                    stage.close();
                }catch(ValidationException e){
                    CustomAlert alert=new CustomAlert("Error: Training Profile Creation",e.getMessage());
                    e.printStackTrace();
                    alert.showAndWait();
                }
            }
            else {

                CustomAlert alert=new CustomAlert("Error","Please select a Player form the list");
                alert.showAndWait();
            }
        });
    }
// END OF CLASS
}
