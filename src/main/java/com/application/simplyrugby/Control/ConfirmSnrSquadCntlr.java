package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.*;
import com.application.simplyrugby.System.ObsListFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Controller for the confirmation of the squad creation.
 */
public class ConfirmSnrSquadCntlr {

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
    private TableColumn<RepTableView,String > repName,repSurname;
    private TableColumn<CoachTableView,String> coachName,coachSurname;
    private TableColumn<AdminTableView,String> adminName,adminSurname,adminRole;
    private TableColumn<SquadTableView,String> squadRoles,squadName,squadSurname;


    /**
     * This methode receives the object created by the Create Squad panel and display them for confirmation
     * @param squad The created squad.
     * @param adminTeam The created admin team
     * @param replacementTeam the created replacement team.
     * @param coachTeam the created coach team.
     */
    public void receiveTeams(Squad squad, AdminTeam adminTeam, ReplacementTeam replacementTeam, CoachTeam coachTeam){
        // here we will create a table view to show the squad.
        SeniorSquad dSquad=(SeniorSquad)squad;
        ObservableList<SquadTableView> squadRoleLines= FXCollections.observableArrayList();
        ObservableList<String> roles=ObsListFactory.createObsList("SeniorSquadRoles");

        for (int i=0;i<dSquad.getSquadList().size();i++){

            String role=roles.get(i);
            String name=dSquad.getSquadList().get(i).getFirstName();
            String surname=dSquad.getSquadList().get(i).getSurname();
            SquadTableView line=new SquadTableView(role,name,surname);
            squadRoleLines.add(line);
        }

        squadRoles=new TableColumn<>("Role");
        squadRoles.setCellValueFactory(new PropertyValueFactory<>("role"));
        squadName=new TableColumn<>("Name");
        squadName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        squadSurname=new TableColumn<>("Surname");
        squadSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));

        squadTable.getColumns().addAll(squadRoles,squadName,squadSurname);
        squadTable.setItems(squadRoleLines);
        System.out.println("squad view created");

        // this part create the admin table view
        ObservableList<AdminTableView> adminTeamLines=FXCollections.observableArrayList();
        ObservableList<String> adminRoles =ObsListFactory.createObsList("AdminRoles");

        for (int i=0;i<adminTeam.getAdminTeam().size();i++){
            String role =adminRoles.get(i);
            String name=adminTeam.getAdminTeam().get(i).getFirstName();
            String surname=adminTeam.getAdminTeam().get(i).getSurname();
            AdminTableView adminTableView=new AdminTableView(role,name,surname);
            adminTeamLines.add(adminTableView);
        }
        adminRole=new TableColumn<>("Role");
        adminRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        adminName=new TableColumn<>("Name");
        adminName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        adminSurname=new TableColumn<>("Surname");
        adminSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        adminTable.getColumns().addAll(adminRole,adminName,adminSurname);

        //auto resize tableview
        // https://stackoverflow.com/questions/28428280/how-to-set-column-width-in-tableview-in-javafx

        adminTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        adminTable.setItems(adminTeamLines);
        System.out.println("Admin table created");

        // this part will create the coach table view.
        ObservableList<CoachTableView> coachTeamLines=FXCollections.observableArrayList();

        for (int i=0;i<coachTeam.getCoaches().size();i++){
            String name=coachTeam.getCoaches().get(i).getFirstName();
            String surname=coachTeam.getCoaches().get(i).getSurname();
            CoachTableView coachTableView=new CoachTableView(name,surname);
            coachTeamLines.add(coachTableView);
        }
        coachName=new TableColumn<>("Name");
        coachName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        coachSurname=new TableColumn<>("Surname");
        coachSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        coachTable.getColumns().addAll(coachName,coachSurname);
        coachTable.setItems(coachTeamLines);
        System.out.println("Coach View created");

        ObservableList<RepTableView> repTeamLines=FXCollections.observableArrayList();

        for (int i=0;i<replacementTeam.getReplacementTeam().size();i++){
            String name=replacementTeam.getReplacementTeam().get(i).getFirstName();
            String surname=replacementTeam.getReplacementTeam().get(i).getSurname();
            RepTableView repTableView=new RepTableView(name,surname);
            repTeamLines.add(repTableView);
        }

        //repName=new TableColumn<>("Name");
        repName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        //repSurname=new TableColumn<>("Surname");
        repSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        repTable.getColumns().addAll(repName,repSurname);
        repTable.setItems(repTeamLines);

        bCreate.getStyleClass().add("bckg5");
        bCreate.setOnAction((event)->{

            System.out.println("clicked");
        });

    }

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

    public static class RepTableView{

        private SimpleStringProperty firstname;
        private  SimpleStringProperty surname;


        public RepTableView(String name, String surname){

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
// END OF CLASS
}
