package com.application.simplyrugby.System;

import com.application.simplyrugby.Model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class will create ObservableList arrays that will be used to populate ComboBoxes.<br>
 * it uses the Singleton and Factory pattern, it takes an object as parameter and test it to know what<br>
 * kind of list it must send back.
 * @see <a href="https://genuinecoder.com/javafx-observable-list-tutorial/">Observable lists</a>
 * @author Erik
 */
public class ObsListFactory {
    // making the class a singleton class. no objects allowed
    private ObsListFactory(){}

    /**
     * The function takes an object in parameter and will test it<br>
     * If the object is a Player, NonPLayer, Doctor or NextOfKIn, it will create and return a list from the database.<br>
     * It can also take a String that will act as a 'command', for example to get a list of non-player roles from the DB.<br>
     * otherwise, it will display an error message.
     * @param o The object to get a list for or a string command.
     * @return The compiled list.
     */
    public static ObservableList<String> createObsList(Object o){
        // the ResultSet to store queries results
        ResultSet queryResult;
        // declaring the observable list array.
        ObservableList<String> oList= FXCollections.observableArrayList();

        // testing the object and return the corresponding list.
        if (o instanceof Player){

            try {
                queryResult =DBTools.executeSelectQuery("SELECT first_name,surname FROM players");
                oList.add("Select from list");

                while (queryResult.next()){

                    oList.add(queryResult.getString(1)+" "+ queryResult.getString(2));
                }
                DBTools.closeConnections();
                return oList;
            }
            catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error creating the Players Obs List",e.getMessage());
                DBTools.closeConnections();
                alert.showAndWait();
                e.printStackTrace();return null;}
        }
        // same but fot non-players
        if (o instanceof NonPlayer){
            try {
                queryResult =DBTools.executeSelectQuery("SELECT first_name,surname,role_id,role_description FROM non_players NATURAL JOIN non_players_roles");
                oList.add("Select from list");

                while (queryResult.next()){

                    oList.add(queryResult.getString(1)+" "+ queryResult.getString(2)+" : "+queryResult.getString(4));
                }
                DBTools.closeConnections();
                return oList;
            }
            catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error creating the Non-players Obs List",e.getMessage());
                DBTools.closeConnections();
                alert.showAndWait();
                e.printStackTrace();return null;}

        }
        // creating a Next of kin list
        if (o instanceof NextOfKin){

            try {
                // we get the table content in the ResultSet
                queryResult = DBTools.executeSelectQuery("SELECT name,surname FROM next_of_kin");
                // add the first entry of the list.
                oList.add("Select from list");
                // add entried to the list.
                while (queryResult.next()){
                    oList.add(queryResult.getString(1)+" "+ queryResult.getString(2));
                }
                DBTools.closeConnections();
                return oList;
            }catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error creating the NextOfKin Obs List",e.getMessage());
                DBTools.closeConnections();
                alert.showAndWait();
                e.printStackTrace();DBTools.closeConnections();return null;}
        }
        //creating the doctor list
        if (o instanceof Doctor){
            try {
                queryResult =DBTools.executeSelectQuery("SELECT name,surname FROM player_doctors");
                oList.add("Select from list");
                while (queryResult.next()){
                    oList.add(queryResult.getString(1)+" "+ queryResult.getString(2));
                }
                DBTools.closeConnections();
                return oList;
            }
            catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error creating the Doctors Obs List",e.getMessage());
                alert.showAndWait();
                DBTools.closeConnections();
                e.printStackTrace();return null;}
        }
        // creating squads lists, done by testing the object in parameters to determine what kind of list is needed.
        if (o instanceof Squad){
            // if for a Senior Squad
            if (o instanceof SeniorSquad){
                try{
                    queryResult=DBTools.executeSelectQuery("SELECT squad_name FROM senior_squads");
                    oList.add("Select a Senior Squad");
                    while (queryResult.next()){
                        oList.add(queryResult.getString(1));
                    }
                    DBTools.closeConnections();
                    return oList;
                }catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error creating the Senior squads Obs List",e.getMessage());
                    alert.showAndWait();
                    DBTools.closeConnections();
                    e.printStackTrace();return null;
                }
            }
            // or a Junior Squad.
            if(o instanceof JuniorSquad){
                try{
                    queryResult=DBTools.executeSelectQuery("SELECT squad_name FROM junior_squads");
                    oList.add("Select a Junior Squad");
                    while (queryResult.next()){
                        oList.add(queryResult.getString(1));
                    }
                    DBTools.closeConnections();
                    return oList;
                }catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error creating the Junior Squads Obs List",e.getMessage());
                    alert.showAndWait();
                    DBTools.closeConnections();
                    e.printStackTrace();return null;
                }
            }
        }
        // if the object is a String, we test to see if it is a valid string request.
        if (o instanceof String s){
            // request to get the list of non-players roles
            if(s.equals("NonPlayerRoles")){
                try {
                    queryResult =DBTools.executeSelectQuery("SELECT role_description FROM non_players_roles");
                    oList.add("Select from list");
                    while (queryResult.next()){

                        oList.add(queryResult.getString(1));
                    }
                    DBTools.closeConnections();
                    return oList;
                }
                catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error creating the Nonplayers RolesObs List",e.getMessage());
                    alert.showAndWait();
                    DBTools.closeConnections();
                    e.printStackTrace();return null;}
            }
            // list of training types
            else if(s.equals("TrainingTypes")){
                try {
                    queryResult =DBTools.executeSelectQuery("SELECT type FROM training_session_types");
                    oList.add("Select from list");
                    while (queryResult.next()){

                        oList.add(queryResult.getString(1));
                    }
                    DBTools.closeConnections();
                    return oList;
                }
                catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error creating the Training Types Obs List",e.getMessage());
                    alert.showAndWait();
                    DBTools.closeConnections();
                    e.printStackTrace();return null;}
            }
            // list of training facilities
            else if(s.equals("TrainingFacilities")){
                try {
                    queryResult =DBTools.executeSelectQuery("SELECT description FROM training_locations");
                    oList.add("Select from list");
                    while (queryResult.next()){

                        oList.add(queryResult.getString(1));
                    }
                    DBTools.closeConnections();
                    return oList;
                }
                catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error creating the Facilities Obs List",e.getMessage());
                    alert.showAndWait();
                    DBTools.closeConnections();
                    e.printStackTrace();return null;}
            }
            // the list of all(Senior+Junior) players not assigned to a squad
            else if(s.equals("AvailablePlayers")){
                try {
                    queryResult =DBTools.executeSelectQuery("SELECT first_name,surname FROM players WHERE is_assigned_to_squad='NO'");
                    oList.add("Select from list");

                    while (queryResult.next()){
                        String name= queryResult.getString(1)+" "+ queryResult.getString(2);
                        oList.add(name);
                    }
                    DBTools.closeConnections();
                    return oList;
                }
                catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error creating the AvailablePlayers Obs List",e.getMessage());
                    DBTools.closeConnections();
                    alert.showAndWait();
                    e.printStackTrace();return null;}
            }
            // list of players over 17 not assigned to a squad
            else if(s.equals("SeniorAvailablePlayers")){
                try {
                    queryResult =DBTools.executeSelectQuery("SELECT first_name,surname,date_of_birth FROM players WHERE is_assigned_to_squad='NO'");
                    oList.add("Select from list");

                    while (queryResult.next()){
                        String name= queryResult.getString(1)+" "+ queryResult.getString(2);
                        // converting the database date to a LocaleDate obj
                        // https://howtodoinjava.com/java/date-time/localdate-parse-string/

                        DateTimeFormatter df=DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate date=LocalDate.parse(queryResult.getString(3),df);
                        // checking if the player is over 17yo
                        if( (Period.between(date,LocalDate.now()).getYears()) >17)
                            oList.add(name);
                    }
                    DBTools.closeConnections();
                    return oList;
                }
                catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error creating the AvailablePlayers Obs List",e.getMessage());
                    DBTools.closeConnections();
                    alert.showAndWait();
                    e.printStackTrace();return null;}
            }
            // list of players under 17 not assigned to a squad
            else if(s.equals("JuniorAvailablePlayers")){
                try {
                    queryResult =DBTools.executeSelectQuery("SELECT first_name,surname,date_of_birth FROM players WHERE is_assigned_to_squad='NO'");
                    oList.add("Select from list");

                    while (queryResult.next()){
                        String name= queryResult.getString(1)+" "+ queryResult.getString(2);
                        // converting the database date to a LocaleDate obj
                        // https://howtodoinjava.com/java/date-time/localdate-parse-string/

                        DateTimeFormatter df=DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate date=LocalDate.parse(queryResult.getString(3),df);
                        // checking if the player is over 17yo
                        if( (Period.between(date,LocalDate.now()).getYears()) <18)
                            oList.add(name);
                    }
                    DBTools.closeConnections();
                    return oList;
                }
                catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error creating the AvailablePlayers Obs List",e.getMessage());
                    DBTools.closeConnections();
                    alert.showAndWait();
                    e.printStackTrace();return null;}
            }
            // listing the coaches
            else if(s.equals("Coaches")){
                try {
                    queryResult =DBTools.executeSelectQuery("SELECT first_name,surname FROM non_players WHERE role_id=1");
                    oList.add("Select from list");

                    while (queryResult.next()){
                        String name= queryResult.getString(1)+" "+ queryResult.getString(2);
                        oList.add(name);
                    }
                    DBTools.closeConnections();
                    return oList;
                }
                catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error creating the Coaches Obs List",e.getMessage());
                    DBTools.closeConnections();
                    alert.showAndWait();
                    e.printStackTrace();return null;}
            }
            // the chairmen
            else if(s.equals("Chairmen")){
                try {
                    queryResult =DBTools.executeSelectQuery("SELECT first_name,surname FROM non_players WHERE role_id=2");
                    oList.add("Select from list");

                    while (queryResult.next()){
                        String name= queryResult.getString(1)+" "+ queryResult.getString(2);
                        oList.add(name);
                    }
                    DBTools.closeConnections();
                    return oList;
                }
                catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error creating the Chairman Obs List",e.getMessage());
                    DBTools.closeConnections();
                    alert.showAndWait();
                    e.printStackTrace();return null;}
            }
            // the fixture secretaries
            else if(s.equals("FixtSect")){
                try {
                    queryResult =DBTools.executeSelectQuery("SELECT first_name,surname FROM non_players WHERE role_id=3");
                    oList.add("Select from list");

                    while (queryResult.next()){
                        String name= queryResult.getString(1)+" "+ queryResult.getString(2);
                        oList.add(name);
                    }
                    DBTools.closeConnections();
                    return oList;
                }
                catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error creating the FixtSec Obs List",e.getMessage());
                    DBTools.closeConnections();
                    alert.showAndWait();
                    e.printStackTrace();return null;}
            }
            // getting a list of the roles in a Senior Squad
            else if(s.equals("SeniorSquadRoles")){
                ArrayList<String> roles=new ArrayList<>();
                roles.addAll(Arrays.asList("LOOSE HEAD PROP","HOOKER","TIGHT HEAD PROP","SECOND ROW","SECOND ROW2","BLIND SIDE FLANKER","OPEN SIDE FLANKER","NUMBER 8",
                        "SCRUM HALF","FLY HALF","LEFT WING","INSIDE CENTRE","OUTSIDE CENTRE","TIGHT SIDE","FULL BACK"));
                for (String role:roles){
                    oList.add(role);
                }
                return oList;
            }
            // getting a list of the roles in a Junior Squad
            else if(s.equals("JuniorSquadRoles")){
                ArrayList<String> roles=new ArrayList<>();
                roles.addAll(Arrays.asList("LOOSE HEAD PROP","TIGHT HEAD PROP","HOOKER","SCRUM HALF","FLY-HALF","CENTRE","RIGHT WING"));
                for (String role:roles){
                    oList.add(role);
                }

                return oList;
            }
            // list of the admin roles.
            else if(s.equals("AdminRoles")){
                ArrayList<String> roles=new ArrayList<>();
                roles.addAll(Arrays.asList("CHAIRMAN","FIXTURE SECRETARY"));
                for (String role:roles){
                    oList.add(role);
                }
                return oList;
            }
            // list of all the opponets clubs.
            else if(s.equals("Clubs")){
                try{
                    queryResult=DBTools.executeSelectQuery("SELECT name FROM clubs");
                    oList.add("Select the opposing Club");
                    while(queryResult.next()){
                        // add the club to the list
                        oList.add(queryResult.getString(1));
                    }
                    DBTools.closeConnections();
                    return oList;
                }catch (SQLException e){
                    CustomAlert alert=new CustomAlert("Error creating the Clubs Obs List",e.getMessage());
                    DBTools.closeConnections();
                    alert.showAndWait();
                    e.printStackTrace();return null;}
                }


            // and if not of any known command, display an error message.
            // for internal and testing uses
            else{
                CustomAlert alert=new CustomAlert("Error creating the Obs List","Invalid Command passed to the function");
                alert.showAndWait();
                DBTools.closeConnections();
            }
        }
        // if the object isn't a valid one, error message.
        // for internal and testing uses
        CustomAlert alert=new CustomAlert("Error creating the Obs List","Invalid Object passed to the function");
        alert.showAndWait();
        DBTools.closeConnections();
        return null;
    }
}
