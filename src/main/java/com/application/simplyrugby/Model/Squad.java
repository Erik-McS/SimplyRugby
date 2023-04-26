package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.ValidationException;

/**
 * Interface for the Club's squads, it will have two children class : SeniorSquad and juniorSquad
 */
public interface Squad {
    /**
     * Method to add a player in a Squad.
     * @param player The player to add to the squad
     * @throws ValidationException exception thrown if some of the tests done to add a player fails
     */
    public void addPlayer(Player player) throws ValidationException;

    /**
     * This method will create a Squad object from the database. the Squad object passed in parameters can be<br>
     * null as it is only needed by the DBTools method to know what kind of squad to look for(Junior or Senior)
     * @param squadType A squad object to determine the type.
     * @param squad_id The ID of the Squad looked for.
     * @return The selected Squad from the database
     */
    public Squad loadSquad(Squad squadType,int squad_id);

    /**
     * Method to save a Squad in the database.
     * .
     */
    public void saveSquad();

}
