package com.application.simplyrugby;

import com.application.simplyrugby.Control.ConfirmSnrSquadCntlr;
import com.application.simplyrugby.Model.*;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.DatabaseCreation;
import com.application.simplyrugby.System.ValidationException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class UnitTesting extends Application {

    public void start(Stage stage) throws IOException {


        try {

            ArrayList<Player> squadTest=new ArrayList<>();
            ArrayList<Player> repTest=new ArrayList<>();
            //Player pl=Player.dummyPlayer();
            for (int i=1;i<=15;i++){
                squadTest.add((Player) Player.dummyPlayer().loadMember(Player.dummyPlayer(),i));
            }
            //SeniorSquad seniorSquad =new SeniorSquad(squadTest,"Squadname");

            for (int i=1;i<=5;i++){
                repTest.add((Player) Player.dummyPlayer().loadMember(Player.dummyPlayer(),i));
            }

            ReplacementTeam replacementTeam=new ReplacementTeam(repTest);

            NonPlayer c1=new NonPlayer(1,"Name","Surname","address","1111111111","eamil@a.asd",1);
            NonPlayer c2=new NonPlayer(1,"Name","Surname","address","1111111111","eamil@a.asd",1);
            NonPlayer c3=new NonPlayer(1,"Name","Surname","address","1111111111","eamil@a.asd",1);
            NonPlayer c4=new NonPlayer(1,"Name","Surname","address","1111111111","eamil@a.asd",1);
            NonPlayer c5=new NonPlayer(1,"Name","Surname","address","1111111111","eamil@a.asd",1);
            NonPlayer c6=new NonPlayer(1,"Name","Surname","address","1111111111","eamil@a.asd",1);
            NonPlayer c7=new NonPlayer(1,"Name","Surname","address","1111111111","eamil@a.asd",1);


            CoachTeam coachTeamTest=new CoachTeam(c1,c2,c3);
            AdminTeam adminTeamTest=new AdminTeam(c4,c5);

            URL url = getClass().getResource("/com/application/simplyrugby/ConfirmNewSeniorSquad.fxml");
            System.out.println("FXML URL: " + url);
            FXMLLoader loader= new FXMLLoader(getClass().getResource("/com/application/simplyrugby/ConfirmNewSeniorSquad.fxml"));
            Parent root=loader.load();
            // getting the Confirmation window controller. will be used to pass the 3 teams created here.
            ConfirmSnrSquadCntlr controller=loader.getController();
            // passing all the teams object to the next window.
            //controller.receiveTeams(seniorSquad,adminTeamTest,replacementTeam,coachTeamTest);

            Scene scene=new Scene(root);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"),"CSS not found").toExternalForm());
            //Ststage=new Stage();
            stage.setTitle("Create a Senior Squad");
            stage.getIcons().add(new Image(UnitTesting.class.getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
            stage.setScene(scene);
            stage.show();


        }

        catch (IOException | ValidationException e){

            CustomAlert alert=new CustomAlert("Error:",e.getMessage());
            e.printStackTrace();
            alert.showAndWait();
        }
    }

    public static void main(String[] args) {

}}
