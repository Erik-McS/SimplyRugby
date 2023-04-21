package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.ValidationException;

import java.util.ArrayList;

public class AdminTeam implements MemberTeam{

    private ArrayList<NonPlayer> adminTeam=new ArrayList<>();

    public AdminTeam(){}

    public AdminTeam(NonPlayer chairman,NonPlayer secretary) throws ValidationException{
        adminTeam.add(chairman);
        adminTeam.add(secretary);
    }
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

    public ArrayList<NonPlayer> getAdmins(){
        return adminTeam;
    }
}
