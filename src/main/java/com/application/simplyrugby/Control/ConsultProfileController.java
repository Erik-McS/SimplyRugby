package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.GamePerformance;
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
    private TableColumn<GamesTableView,String> rgDate,rgTeam,rgWhere,rgPerf;
    @FXML
    private TableView<GamesTableView> tvGamePerfs;
    @FXML
    private Button bExit;
    @FXML
    private Pane firstPane;
    private int player_id=0;
    public void initialize(){
        hideContent();
        cbPlayer.getStyleClass().add("bckg5");
        bExit.getStyleClass().add("bckg5");
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
                    Player player1=(Player) DBTools.loadMember(Player.dummyPlayer(),player_id);
                    TrainingProfile trainingProfile=DBTools.getTrainingProfile(player1);

                    lRunning.setText(trainingProfile.getRunningLevel());
                    lPassing.setText(trainingProfile.getPassingLevel());
                    lSupport.setText(trainingProfile.getSupportLevel());
                    lTackling.setText(trainingProfile.getTacklingLevel());
                    lDecision.setText(trainingProfile.getDecisionLevel());
                    // setting up the lines to fill the rows with in the training table
                    ObservableList<TrainingTableView> trainingLines= FXCollections.observableArrayList();
                    ArrayList<TrainingSession> playerSession=DBTools.getPlayerTrainingSessions(player1);
                    // adding each line.
                    for (int i=0;i<playerSession.size();i++){
                        String date=playerSession.get(i).getDate();
                        String type=TrainingSession.getTypeDescription(playerSession.get(i).getTrainingType());
                        String location=TrainingSession.getFacility(playerSession.get(i).getTrainingFacility());
                        TrainingTableView tb=new TrainingTableView(type,date,location);
                        trainingLines.add(tb);
                    }
                    // setting the cells' types
                    rType.setCellValueFactory(new PropertyValueFactory<>("type"));
                    rDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                    rFacility.setCellValueFactory(new PropertyValueFactory<>("facility"));
                    // adding the rows
                    tvTrainingSessions.setItems(trainingLines);

                    ObservableList<GamesTableView> gamesLines=FXCollections.observableArrayList();
                    ArrayList<GamePerformance> games=DBTools.getPlayerGamesPerformances(player1);

                    for (int i=0;i<games.size();i++){
                        // add a game and its performance to the line array.
                        gamesLines.add(
                                new GamesTableView(games.get(i).getDate(),
                                        games.get(i).getClubName(),
                                        games.get(i).getWhere(),
                                        games.get(i).getPerformance())
                        );
                    }
                    rgDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                    rgTeam.setCellValueFactory(new PropertyValueFactory<>("vsTeam"));
                    rgWhere.setCellValueFactory(new PropertyValueFactory<>("where"));
                    rgPerf.setCellValueFactory(new PropertyValueFactory<>("performance"));
                    tvGamePerfs.setItems(gamesLines);
                }
                else
                    hideContent();

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

    /**
     * Nested class to handle the Training table data.
     */
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

    /**
     * Nested class to handle the Games performances data
     */
    public static class GamesTableView{
        private SimpleStringProperty date;
        private SimpleStringProperty vsTeam;
        private SimpleStringProperty where;
        private SimpleStringProperty performance;

        public GamesTableView(String date,String vsTeam,String where,String performance){
            this.date=new SimpleStringProperty(date);
            this.vsTeam=new SimpleStringProperty(vsTeam);
            this.where=new SimpleStringProperty(where);
            this.performance=new SimpleStringProperty(performance);
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

        public String getVsTeam() {
            return vsTeam.get();
        }

        public SimpleStringProperty vsTeamProperty() {
            return vsTeam;
        }

        public void setVsTeam(String vsTeam) {
            this.vsTeam.set(vsTeam);
        }

        public String getWhere() {
            return where.get();
        }

        public SimpleStringProperty whereProperty() {
            return where;
        }

        public void setWhere(String where) {
            this.where.set(where);
        }

        public String getPerformance() {
            return performance.get();
        }

        public SimpleStringProperty performanceProperty() {
            return performance;
        }

        public void setPerformance(String performance) {
            this.performance.set(performance);
        }
    }
// END OF CLASS
}
