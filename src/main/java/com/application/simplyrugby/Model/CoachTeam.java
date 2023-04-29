package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.ValidationException;

import java.util.ArrayList;

/**
 * This class will store a coach team a Squad coaches team.
 * It provides methods to load and save a coach team from the database.
 */
public class CoachTeam implements MemberTeam{
    // Arraylist to store the team members.
    private ArrayList<NonPlayer> coaches=new ArrayList<>();

    // Empty constructor, a null object is used in some methods, so we need to be able to create one.
    // This is due to the fact that we also use a custom constructor for this class.
    public CoachTeam(){}

    /**
     * Constructor to create a Coach team.
     * @param coach1 A coach to add to the team
     * @param coach2 A coach to add to the team
     * @param coach3 A coach to add to the team
     * @throws ValidationException Exception if validations fail
     */
    public CoachTeam(NonPlayer coach1,NonPlayer coach2,NonPlayer coach3) throws ValidationException{
        addMember(coach1);
        addMember(coach2);
        addMember(coach3);
    }

    @Override
    public void addMember(NonPlayer member) throws ValidationException {
        // test that the team is not empty
        if (coaches.size()<3){
            // and that the Coach object is not null
            if (member==null)
                throw new ValidationException("Empty Coach record: null object");
            else if (member.getRole_id()!=1)
                throw new ValidationException("This member is not a Coach");
            else
                coaches.add(member);
        }
        else
            throw new ValidationException("The Team is already full.");
    }

    @Override
    public MemberTeam loadTeam(MemberTeam teamType, int team_ID) {
        return null;
    }

    @Override
    public void saveTeam(MemberTeam team) {
    }

    public ArrayList<NonPlayer> getCoaches() {
        return coaches;
    }
}
