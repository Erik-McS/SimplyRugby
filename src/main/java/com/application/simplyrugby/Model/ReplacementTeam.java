package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.ValidationException;

import java.util.ArrayList;

/**
 * Class to store a replacement team added to a squad.
 */
public class ReplacementTeam implements Squad{
    // private array to contain the players
    private ArrayList<Player> replacementTeam=new ArrayList<>();
    //empty constructor to create null objects.
    public ReplacementTeam(){}

    /**
     * Constructor for the replacement team. takes an array of players.
     * it will test that each player is a non-null object.
     * @param repTeam the replacement team to create.
     * @throws ValidationException Exception if any errors.
     */
    public ReplacementTeam(ArrayList<Player> repTeam) throws ValidationException{
        if (repTeam!=null){
            if (repTeam.size()>4){
                // checking that the player objects are not empty, using the addPlayer method.
                for (Player pl:repTeam){
                    addPlayer(pl);
                }
            }
            else
                throw new ValidationException("The replacement team array is incomplete");
        }
        else
            throw new ValidationException("The replacement team array is empty");
    }

    @Override
    public void addPlayer(Player player) throws ValidationException {

        if (player!=null){

            if (replacementTeam.size()<5){
                replacementTeam.add(player);
            }
            else
                throw new ValidationException("The team is already full.");

        }
        else
            throw new ValidationException("The player record is empty: Null object");
    }

    @Override
    public Squad loadSquad(Squad squadType, int squad_id) {
        return null;
    }

    @Override
    public void saveSquad(Squad squad) {

    }
}
