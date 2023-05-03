package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.Player;
import com.application.simplyrugby.Model.TrainingProfile;
import com.application.simplyrugby.Model.TrainingSession;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ObsListFactory;
import com.application.simplyrugby.System.ValidationException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * class to manage the consult profile panel
 */
public class ConsultProfileController {

    @FXML
    private ComboBox<String> cbPlayer;
    @FXML
    private Label lTitle1,lTitle2,lTitle3,lSkill1,lSkill2,lSkill3,lSkill4,lSkill5,lRunning,lPassing,lSupport,lTackling,lDecision;
    @FXML
    private TableView<TrainingTableView> tvTrainingSessions;
    @FXML
    private TableColumn<TrainingTableView,String > rType,rDate,rFacility;
    @FXML
    private TableView<String> tvGamePerfs;
    @FXML
    private Button bExit;
    @FXML
    private Pane firstPane;
    private int player_id=0;
    public void initialize(){
        hideContent();
        cbPlayer.getStyleClass().add("bckg5");
        cbPlayer.setItems(ObsListFactory.createObsList("PlayerProfiles"));
        cbPlayer.getSelectionModel().select(0);
        cbPlayer.setOnAction((event)->{
            // try-catch if any issue
            /*try
            {*/
                // if a player is selected
                if (cbPlayer.getSelectionModel().getSelectedIndex()!=0) {
                    // show the nae content with a custom method
                    showContent();
                    // getting the player ID
                    String[] player=cbPlayer.getValue().split(" ");
                    player_id= DBTools.getID("SELECT player_id FROM players " +
                            "WHERE first_name='"+player[0]+"' AND surname ='"+player[1]+"'");
                    // getting its training profile
                    Player pl=(Player) DBTools.loadMember(Player.dummyPlayer(),player_id);
                    TrainingProfile trainingProfile=DBTools.getTrainingProfile(pl);

                    lRunning.setText(trainingProfile.getRunningLevel());
                    lPassing.setText(trainingProfile.getPassingLevel());
                    lSupport.setText(trainingProfile.getSupportLevel());
                    lTackling.setText(trainingProfile.getTacklingLevel());
                    lDecision.setText(trainingProfile.getDecisionLevel());

                    ObservableList<TrainingTableView> trainingLines= FXCollections.observableArrayList();
                    ArrayList<TrainingSession> playerSession=DBTools.getPlayerTrainingSessions(pl);

                    for (int i=0;i<playerSession.size();i++){
                        String date=playerSession.get(i).getDate();
                        String type=TrainingSession.getTypeDescription(playerSession.get(i).getTrainingType());
                        String location=TrainingSession.getFacility(playerSession.get(i).getTrainingFacility());
                        TrainingTableView tb=new TrainingTableView(type,date,location);
                        trainingLines.add(tb);
                    }
                    rType.setCellValueFactory(new PropertyValueFactory<>("type"));
                    rDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                    rFacility.setCellValueFactory(new PropertyValueFactory<>("facility"));
                    tvTrainingSessions.setItems(trainingLines);

                }
                else
                    hideContent();
                /*
            }
            catch (ValidationException e){
                CustomAlert alert=new CustomAlert("Consult a training Profile",e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            } */
        });


        bExit.setOnAction((event)->{
            firstPane.getChildren().clear();
        });
    // END OF INITIALIZE()
    }
    private void hideContent(){
        // using IntelliJ recommendation on looping the labels
        for (Label label : Arrays.asList(lTitle1, lTitle2, lTitle3, lSkill1, lSkill2, lSkill3, lSkill4, lSkill5, lRunning, lPassing, lSupport, lTackling, lDecision)) {
            label.setVisible(false);
        }
        tvGamePerfs.setVisible(false);
        tvTrainingSessions.setVisible(false);
    }
    private void showContent(){
        for (Label label : Arrays.asList(lTitle1, lTitle2, lTitle3, lSkill1, lSkill2, lSkill3, lSkill4, lSkill5, lRunning, lPassing, lSupport, lTackling, lDecision)) {
            label.setVisible(true);
        }
        tvGamePerfs.setVisible(true);
        tvTrainingSessions.setVisible(true);
    }

    public static class TrainingTableView{
        private SimpleStringProperty type;
        private SimpleStringProperty date;
        private SimpleStringProperty facility;

        public TrainingTableView(String type,String date,String facility){
            this.type=new SimpleStringProperty(type);
            this.date=new SimpleStringProperty(date);
            this.facility=new SimpleStringProperty(facility);
        }

        public String getType() {
            return type.get();
        }

        public SimpleStringProperty typeProperty() {
            return type;
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

        public void setDate(String date) {
            this.date.set(date);
        }

        public String getFacility() {
            return facility.get();
        }

        public SimpleStringProperty facilityProperty() {
            return facility;
        }

        public void setFacility(String facility) {
            this.facility.set(facility);
        }
    }
// END OF CLASS
}
