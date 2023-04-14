package com.application.simplyrugby.System;

import com.application.simplyrugby.Model.Doctor;
import com.application.simplyrugby.Model.NextOfKin;
import com.application.simplyrugby.Model.NonPlayer;
import com.application.simplyrugby.Model.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


import java.sql.ResultSet;
import java.sql.SQLException;

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
     * otherwise, it will display an error message.
     * @param o The object to get a list for.
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
                    String name= queryResult.getString(1)+" "+ queryResult.getString(2);
                    oList.add(name);
                }
                DBTools.closeConnections();
                return oList;
            }
            catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error creating the ComboBox",e.getMessage());
                DBTools.closeConnections();
                alert.showAndWait();
                e.printStackTrace();return null;}
        }

        if (o instanceof NonPlayer){
            try {
                queryResult =DBTools.executeSelectQuery("SELECT first_name,surname FROM non_players");
                oList.add("Select from list");

                while (queryResult.next()){
                    String name= queryResult.getString(1)+" "+ queryResult.getString(2);
                    oList.add(name);
                }
                DBTools.closeConnections();
                return oList;
            }
            catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error creating the ComboBox",e.getMessage());
                DBTools.closeConnections();
                alert.showAndWait();
                e.printStackTrace();return null;}

        }
        if (o instanceof NextOfKin){

            try {
                // we get the table content in the ResultSet
                queryResult = DBTools.executeSelectQuery("SELECT name,surname FROM next_of_kin");
                // add the first entry of the list.
                oList.add("Select from list");


                while (queryResult.next()){
                    String name= queryResult.getString(1)+" "+ queryResult.getString(2);
                    oList.add(name);
                }
                DBTools.closeConnections();
                return oList;
            }catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error creating the ComboBox",e.getMessage());
                DBTools.closeConnections();
                alert.showAndWait();
                e.printStackTrace();DBTools.closeConnections();return null;}
        }
        if (o instanceof Doctor){

            try {
                queryResult =DBTools.executeSelectQuery("SELECT name,surname FROM player_doctors");
                oList.add("Select from list");
                while (queryResult.next()){
                    String name= queryResult.getString(1)+" "+ queryResult.getString(2);
                    oList.add(name);
                }
                DBTools.closeConnections();
                return oList;
            }
            catch (SQLException e){
                CustomAlert alert=new CustomAlert("Error creating the ComboBox",e.getMessage());
                alert.showAndWait();
                DBTools.closeConnections();
                e.printStackTrace();return null;}
        }
        // if the object isn't a valid one, error message:
        CustomAlert alert=new CustomAlert("Error creating the ComboBox","Invalid Object passed to the function");
        alert.showAndWait();
        DBTools.closeConnections();
        return null;
    }
}
