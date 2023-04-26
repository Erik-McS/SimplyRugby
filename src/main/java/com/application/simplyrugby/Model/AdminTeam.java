package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.ValidationException;

import java.util.ArrayList;

/**
 * This class will hold and manipulate an admin team object, linked to a squad.
 */
public class AdminTeam implements MemberTeam{

    private ArrayList<NonPlayer> adminTeam=new ArrayList<>();

    /**
     * Allows null objects creation
     */
    public AdminTeam(){}

    /**
     * Create a team object.
     * @param chairman The Squad Chairman
     * @param secretary The Squad secretary
     * @throws ValidationException Error message
     */
    public AdminTeam(NonPlayer chairman,NonPlayer secretary) throws ValidationException{
        adminTeam.add(chairman);
        adminTeam.add(secretary);
    }

    /**
     * Add a member in the admin team
     * @param member The member to add
     * @throws ValidationException Error message
     */
    public void addMember(NonPlayer member)throws ValidationException {
        // checking if the team is not full already
        if (adminTeam.size()<2){
            if (member==null)
                throw new ValidationException("Empty Member record: null object");
            else if (member.getRole_id()==2)
                adminTeam.add(member);
            else if (member.getRole_id()==3)
                adminTeam.add(member);
            else
                throw new ValidationException("This member is not a chairman or secretary.");
        }
        else
            throw new ValidationException("This team is already full.");
    }

    @Override
    public MemberTeam loadTeam(MemberTeam teamType, int team_ID) {
        return null;
    }

    @Override
    public void saveTeam(MemberTeam team) {

    }

    /**
     * Returns an arraylist with the admin team members
     * @return The list of members
     */
    public ArrayList<NonPlayer> getAdmins(){
        return adminTeam;
    }
}
