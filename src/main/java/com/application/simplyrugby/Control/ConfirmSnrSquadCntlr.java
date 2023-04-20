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

public class ConfirmSnrSquadCntlr {

    @FXML
    Pane leftPane,upPane,hPane1,hPane2;
    @FXML
    private Button bCreate,bCancel;
    @FXML
    private TableView<SquadTableView> squadTable,repTable;
    @FXML
    private TableView<NonPlayer> adminTable,coachTable;

    @FXML
    private TableColumn<Member,String > repName,repSurname,adminName,adminSurname,coachName,coachSurname;

    private TableColumn<SquadTableView,String> squadRoles,squadName,squadSurname;
    @FXML
    private TableColumn<String ,String> adminRole;
    public void receiveTeams(Squad squad, AdminTeam adminTeam, ReplacementTeam replacementTeam, CoachTeam coachTeam){
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
        //squadTable=new TableView<>(squadRoleLines);
        squadRoles=new TableColumn<>("Role");
        squadRoles.setCellValueFactory(new PropertyValueFactory<>("role"));
        squadName=new TableColumn<>("Name");
        squadName.setCellValueFactory(new PropertyValueFactory<>("firstname"));
        squadSurname=new TableColumn<>("Surname");
        squadSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));

        squadTable.getColumns().addAll(squadRoles,squadName,squadSurname);
        squadTable.setItems(squadRoleLines);
        bCreate.getStyleClass().add("bckg5");
        System.out.println("receivedTeams entered");

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
}
