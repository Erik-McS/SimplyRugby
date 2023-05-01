package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ValidationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 *
 This is the class to define a Senior Squad.
 It will take 15 players and use an ArrayList to store them.
 */
public class JuniorSquad implements Squad{

    // the arraylist to contain the squad.
    private ArrayList<Player> squadPlayers=new ArrayList<>();
    // empty constructor, to allow the creation of a null object.
    private String squadName;
    private ReplacementTeam replacementTeam;
    private AdminTeam adminTeam;
    private CoachTeam coachTeam;
    private int squad_id;

    public JuniorSquad(){}

    /**
     * Constructor of the class Junior Squad, it will take an arraylist as parameter to create the squad.<br>
     * the ArrayList passed must have 7 players in it.
     * @param squadPlayers The ArrayList containing the players
     * @throws ValidationException Exception if the validations fail
     */
    public JuniorSquad(ArrayList<Player> squadPlayers,String squadName,ReplacementTeam replacementTeam,AdminTeam adminTeam,CoachTeam coachTeam) throws ValidationException {
        // testing that the array is not empty
        if (squadPlayers!=null){
            // and that it has 7 players in it.
            if (squadPlayers.size()==7){
                // we need to check that none of the player objects are empty before adding them to the squad array
                // this will be checked by the addPlayer method.
                for(Player pl:squadPlayers){
                    addPlayer(pl);
                }
                this.squadName=squadName;
                this.replacementTeam=replacementTeam;
                this.coachTeam=coachTeam;
                this.adminTeam=adminTeam;
                this.squad_id=DBTools.getID("SELECT squad_id FROM junior_squads WHERE squad_name='"+this.squadName+"'");
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
        if (squadPlayers.size()<7){
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

    /**
     * Method to get the arraylist containing the squad members.
     * @return List of the squad members
     */
    public ObservableList<Player> getSquadList(){
        ObservableList<Player> oList= FXCollections.observableArrayList();

        for(Player pl:squadPlayers){
            oList.add(pl);
        }
        return oList;
    }

    /**
     * Returns the squad name
     * @return Name
     */
    public String getSquadName() {
        return squadName;
    }

    /**
     * Set the squad name.
     * @param squadName Name
     */
    public void setSquadName(String squadName) {
        this.squadName = squadName;
    }

    /**
     * Returns the squad's replacement team
     * @return Replacement team.
     */
    public ReplacementTeam getReplacementTeam() {
        return replacementTeam;
    }

    /**
     * Returns the squad's admin team
     * @return Admin team
     */
    public AdminTeam getAdminTeam() {
        return adminTeam;
    }

    /**
     * Returns the squad's Coach team.
     * @return Coach team
     */
    public CoachTeam getCoachTeam() {
        return coachTeam;
    }

    /**
     * Returns the squad's player's list
     * @return Players list.
     */
    public ArrayList<Player> getSquadPlayers() {
        return squadPlayers;
    }

    public int getSquad_id() {
        return squad_id;
    }

    public JuniorSquad setSquad_id(int squad_id) {
        this.squad_id = squad_id;
        return this;
    }

    /**
     * Display the object's info
     * @return info
     */
    @Override
    public String toString() {
        return "JuniorSquad{" +
                "squadPlayers=" + squadPlayers +
                ", squadName='" + squadName + '\'' +
                ", replacementTeam=" + replacementTeam +
                ", adminTeam=" + adminTeam +
                ", coachTeam=" + coachTeam +
                '}';
    }
}

