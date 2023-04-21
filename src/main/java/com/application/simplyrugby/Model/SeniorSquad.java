package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * This is the class to define a Senior Squad.<br>
 * it will take 15 players and use an ArrayList to store them.
 */
public class SeniorSquad implements Squad{
    // the arraylist to contains the squad.
    private ArrayList<Player> squadPlayers=new ArrayList<>();
    // empty constructor, to allow the creation of a null object.
    private String squadName;
    private ReplacementTeam replacementTeam;
    private AdminTeam adminTeam;
    private CoachTeam coachTeam;

    public SeniorSquad(){}

    /**
     * Constructor of the class Squad, it will take an arraylist as parameter to create the squad.<br>
     * the ArrayList passed must have 15 players in it.
     * @param squadPlayers The ArrayList containing the players
     * @throws ValidationException Exception if the validations fail
     */
    public SeniorSquad(ArrayList<Player> squadPlayers,String squadName,ReplacementTeam replacementTeam,AdminTeam adminTeam,CoachTeam coachTeam) throws ValidationException {
        // testing that the array is not empty
        if (squadPlayers!=null){
            // and that it has 15 players in it.
            if (squadPlayers.size()==15){
                // we need to check that none of the player objects are empty before adding them to the squad array
                // this will be checked by the addPlayer method.
                for(Player pl:squadPlayers){
                    addPlayer(pl);
                }
                this.squadName=squadName;
                this.replacementTeam=replacementTeam;
                this.coachTeam=coachTeam;
                this.adminTeam=adminTeam;
            }
            // otherwise, error message.
            else
                throw new ValidationException("The passed array do not contains 15 players.");
        }
        // error if the array is empty
        else
            throw new ValidationException("Invalid squad Arraylist");
    }

    @Override
    public void addPlayer(Player player) throws ValidationException{
        // test if the squad is not full
        if (squadPlayers.size()<15){
            // and that the player object is not empty
            if (player!=null){
                squadPlayers.add(player);
            }
            else
                throw new ValidationException("Invalid player record: null object");
        }
        else
            throw new ValidationException("This squad is already full");
    }

    @Override
    public Squad loadSquad(Squad squadType, int squad_id) {
        return null;
    }

    @Override
    public void saveSquad() {
        DBTools.saveSquad(this);
    }

    public ObservableList<Player> getSquadList(){
        ObservableList<Player> oList= FXCollections.observableArrayList();

        for(Player pl:squadPlayers){
            oList.add(pl);
        }
        return oList;
    }

    public String getSquadName() {
        return squadName;
    }

    public void setSquadName(String squadName) {
        this.squadName = squadName;
    }

    public ReplacementTeam getReplacementTeam() {
        return replacementTeam;
    }

    public AdminTeam getAdminTeam() {
        return adminTeam;
    }

    public CoachTeam getCoachTeam() {
        return coachTeam;
    }

    public ArrayList<Player> getSquadPlayers() {
        return squadPlayers;
    }
}

