package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ValidationException;
import java.util.ArrayList;

/**
 * Class to store a Training Session
 */
public class TrainingProfile {

    private int playerID;
    private int profileID;
    private String passingLevel;
    private String runningLevel;
    private String supportLevel;
    private String tacklingLevel;
    private String decisionLevel;

    private ArrayList<Game> games=new ArrayList<>();
    private ArrayList<TrainingSession> trainingSessions=new ArrayList<>();

    public TrainingProfile(){


    }

    public TrainingProfile(Player player) throws ValidationException {
        setPlayerID(player.getPlayerID());
        String level="Poor";
        setDecisionLevel(level);
        setPassingLevel(level);
        setRunningLevel(level);
        setSupportLevel(level);

    }

    public void saveTrainingProfile() throws ValidationException{
        if (playerID!=0)
            DBTools.insertTrainingProfile(this);
        else
            throw new ValidationException("There is no player assigned to this profile");
    }


    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public String getPassingLevel() {
        return passingLevel;
    }

    public void setPassingLevel(String passingLevel) throws ValidationException{
        if (testLevelDesc(passingLevel))
            this.passingLevel=passingLevel;
        else
            throw new ValidationException("Incorrect Level Description");
    }

    public String getRunningLevel() {
        return runningLevel;
    }

    public void setRunningLevel(String runningLevel) throws ValidationException{
        if (testLevelDesc(runningLevel))
            this.runningLevel=runningLevel;
        else
            throw new ValidationException("Incorrect Level Description");
    }

    public String getSupportLevel() {
        return supportLevel;
    }

    public void setSupportLevel(String supportLevel) throws ValidationException{
        if (testLevelDesc(supportLevel))
            this.supportLevel=supportLevel;
        else
            throw new ValidationException("Incorrect Level Description");
    }

    public String getTacklingLevel() {
        return tacklingLevel;
    }

    public void setTacklingLevel(String tacklingLevel) throws ValidationException {
        if (testLevelDesc(tacklingLevel))
            this.tacklingLevel=tacklingLevel;
        else
            throw new ValidationException("Incorrect Level Description");    }

    public String getDecisionLevel() {
        return decisionLevel;
    }

    public void setDecisionLevel(String decisionLevel) throws ValidationException {
        if (testLevelDesc(decisionLevel))
            this.decisionLevel=decisionLevel;
        else
            throw new ValidationException("Incorrect Level Description");
    }

    public ArrayList<Game> getGames() {
        return games;
    }

    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    public ArrayList<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    public void setTrainingSessions(ArrayList<TrainingSession> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }
    public int getLevelID(String levelDesc) throws ValidationException{
        if (levelDesc.equals("Poor"))
            return 1;
        else if (levelDesc.equals("Developing"))
            return 2;
        else if (levelDesc.equals("Proficient"))
            return 3;
        else if(levelDesc.equals("Advanced"))
            return 4;
        else if(levelDesc.equals("Leading"))
            return 5;
        else
            throw new ValidationException("Incorrect Level Description");
        }
    private boolean testLevelDesc(String levelDesc){
        if (levelDesc.equals("Poor") || levelDesc.equals("Developing") || levelDesc.equals("Proficient")
                || levelDesc.equals("Advanced") || levelDesc.equals("Leading"))
            return true;
        else
            return false;
    }



    // END OF CLASS
}
