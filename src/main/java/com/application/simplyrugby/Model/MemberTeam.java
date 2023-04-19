package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.ValidationException;

/**
 * Interface to describe a Admin or Coach team
 */
public interface MemberTeam {
    /**
     * Method to add a non-playing member in the team.
     * @param member The member to add
     * @throws ValidationException Error message to display is any issues
     */
    public void addMember(NonPlayer member) throws ValidationException;

    /**
     * Method to load a team from the database.
     * @param teamType Tested to see what kind of team needs loaded and sent back
     * @param team_ID the team_id of the team we are looking for
     * @return An object containing the team.
     */
    public MemberTeam loadTeam(MemberTeam teamType,int team_ID);

    /**
     * Method to save a team in the database.
     * @param team The team to save.
     */
    public void saveTeam(MemberTeam team);
}
