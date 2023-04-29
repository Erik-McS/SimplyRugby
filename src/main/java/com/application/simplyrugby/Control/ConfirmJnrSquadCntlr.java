package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.JuniorSquad;
import com.application.simplyrugby.Model.Squad;
import com.application.simplyrugby.System.ObsListFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller for the confirmation of the Junior squad creation.
 */
public class ConfirmJnrSquadCntlr {

    @FXML
    Pane leftPane,upPane,hPane1,hPane2;
    @FXML
    private Button bCreate,bCancel;
    @FXML
    private TableView<SquadTableView> squadTable;
    @FXML
    private TableView<RepTableView> repTable;
    @FXML
    private TableView<CoachTableView> coachTable;
    @FXML
    private TableView<AdminTableView> adminTable;
    @FXML
    private Label squadNameLabel;
    @FXML
    private TableColumn<RepTableView,String > repName,repSurname;
    @FXML
    private TableColumn<CoachTableView,String> coachName,coachSurname;
    @FXML
    private TableColumn<AdminTableView,String> adminName,adminSurname,adminRole;
    @FXML
    private TableColumn<SquadTableView,String> squadRoles,squadName,squadSurname;


    /**
     * This methode receives the object created by the Create Squad panel and display them for confirmation
     * @param squad The created squad.
     */
    public void receiveTeams(Squad squad){
        //AdminTeam adminTeam, ReplacementTeam replacementTeam, CoachTeam coachTeam

        // here we will create a table view to show the squad.

        JuniorSquad dSquad=(JuniorSquad) squad;
        squadNameLabel.setText("Squad : "+dSquad.getSquadName());
        ObservableList<SquadTableView> squadRoleLines= FXCollections.observableArrayList();
        ObservableList<String> roles= ObsListFactory.createObsList("JuniorSquadRoles");

        for (int i=0;i<dSquad.getSquadList().size();i++){

            String role=roles.get(i);
            String name=dSquad.getSquadList().get(i).getFirstName();
            String surname=dSquad.getSquadList().get(i).getSurname();
            SquadTableView line=new SquadTableView(role,name,surname);
            squadRoleLines.add(line);
        }


        squadRoles.setCellValueFactory(new PropertyValueFactory<>("role"));

        squadName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        squadSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        squadTable.setItems(squadRoleLines);

        // This part creates the admin table view
        ObservableList<AdminTableView> adminTeamLines=FXCollections.observableArrayList();
        ObservableList<String> adminRoles =ObsListFactory.createObsList("AdminRoles");

        for (int i = 0; i<dSquad.getAdminTeam().getAdmins().size(); i++){
            String role =adminRoles.get(i);
            String name=dSquad.getAdminTeam().getAdmins().get(i).getFirstName();
            String surname=dSquad.getAdminTeam().getAdmins().get(i).getSurname();
            AdminTableView adminTableView=new AdminTableView(role,name,surname);
            adminTeamLines.add(adminTableView);
        }
        adminRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        adminName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        adminSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));


        adminTable.setItems(adminTeamLines);

        // this part will create the coach table view.
        ObservableList<CoachTableView> coachTeamLines=FXCollections.observableArrayList();

        for (int i=0;i<dSquad.getCoachTeam().getCoaches().size();i++){
            String name=dSquad.getCoachTeam().getCoaches().get(i).getFirstName();
            String surname=dSquad.getCoachTeam().getCoaches().get(i).getSurname();
            CoachTableView coachTableView=new CoachTableView(name,surname);
            coachTeamLines.add(coachTableView);
        }
        coachName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        coachSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        coachTable.setItems(coachTeamLines);

        ObservableList<RepTableView> repTeamLines=FXCollections.observableArrayList();

        for (int i = 0; i<dSquad.getReplacementTeam().getReplacements().size(); i++){
            String name=dSquad.getReplacementTeam().getReplacements().get(i).getFirstName();
            String surname=dSquad.getReplacementTeam().getReplacements().get(i).getSurname();
            RepTableView repTableView=new RepTableView(name,surname);
            repTeamLines.add(repTableView);
        }
        repName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        repSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        repTable.setItems(repTeamLines);

        // here we handle the insertion of the full squad and team in the DB
        bCreate.getStyleClass().add("bckg5");
        bCreate.setOnAction((event)->{
            Stage stage=(Stage) bCreate.getScene().getWindow();
            stage.close();
        });

        bCreate.setOnAction((event)->{
            dSquad.saveSquad();
            Stage stage=(Stage) bCreate.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Sets the colors and style of the window. And event handler for the cancel button
     */
    public void initialize(){

        leftPane.getStyleClass().add("bckg1");
        upPane.getStyleClass().add("bckg1");
        hPane1.getStyleClass().add("bckg1");
        hPane2.getStyleClass().add("bckg1");
        bCancel.getStyleClass().add("bckg5");


        bCancel.setOnAction((event)->{
            Stage stage=(Stage) bCancel.getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Nested class to hold the Squad players data.
     */
    public static class SquadTableView {
        private SimpleStringProperty firstname;
        private  SimpleStringProperty surname;
        private SimpleStringProperty role;
        public SquadTableView(String role, String name, String surname){
            this.role=new SimpleStringProperty(role);
            this.firstname=new SimpleStringProperty(name);
            this.surname=new SimpleStringProperty(surname);
        }

        public String getFirstname() {
            return firstname.get();
        }

        public SimpleStringProperty firstnameProperty() {
            return firstname;
        }

        public String getSurname() {
            return surname.get();
        }

        public SimpleStringProperty surnameProperty() {
            return surname;
        }

        public String getRole() {
            return role.get();
        }

        public SimpleStringProperty roleProperty() {
            return role;
        }
    }

    /**
     * Nested class to hold the Admin team data
     */
    public static class AdminTableView{

        private SimpleStringProperty firstname;
        private  SimpleStringProperty surname;
        private SimpleStringProperty role;

        public AdminTableView(String role, String name, String surname){
            this.role=new SimpleStringProperty(role);
            this.firstname=new SimpleStringProperty(name);
            this.surname=new SimpleStringProperty(surname);
        }

        public String getFirstname() {
            return firstname.get();
        }

        public SimpleStringProperty firstnameProperty() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname.set(firstname);
        }

        public String getSurname() {
            return surname.get();
        }

        public SimpleStringProperty surnameProperty() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname.set(surname);
        }

        public String getRole() {
            return role.get();
        }

        public SimpleStringProperty roleProperty() {
            return role;
        }

        public void setRole(String role) {
            this.role.set(role);
        }
    }
    public static class CoachTableView{

        private SimpleStringProperty firstname;
        private  SimpleStringProperty surname;


        public CoachTableView(String name, String surname){

            this.firstname=new SimpleStringProperty(name);
            this.surname=new SimpleStringProperty(surname);
        }

        public String getFirstname() {
            return firstname.get();
        }

        public SimpleStringProperty firstnameProperty() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname.set(firstname);
        }

        public String getSurname() {
            return surname.get();
        }

        public SimpleStringProperty surnameProperty() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname.set(surname);
        }
    }

    public static class RepTableView {

        private SimpleStringProperty firstname;
        private SimpleStringProperty surname;


        public RepTableView(String name, String surname) {

            this.firstname = new SimpleStringProperty(name);
            this.surname = new SimpleStringProperty(surname);
        }

        public String getFirstname() {
            return firstname.get();
        }

        public SimpleStringProperty firstnameProperty() {
            return firstname;
        }

        public void setFirstname(String firstname) {
            this.firstname.set(firstname);
        }

        public String getSurname() {
            return surname.get();
        }

        public SimpleStringProperty surnameProperty() {
            return surname;
        }

        public void setSurname(String surname) {
            this.surname.set(surname);
        }
    }
}
