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

    /**
     * Empty constructor to allow null objects
     */
    public TrainingProfile(){}

    /**
     * Constructor for a player's training profile
     * @param player The player
     * @throws ValidationException Error if issues
     */
    public TrainingProfile(Player player) throws ValidationException {
        setPlayerID(player.getPlayerID());
        String level="Poor";
        setDecisionLevel(level);
        setPassingLevel(level);
        setRunningLevel(level);
        setSupportLevel(level);
        setTacklingLevel(level);
    }

    /**
     * Save the profile in the database
     * @throws ValidationException Error if issue
     */
    public void saveTrainingProfile() throws ValidationException{
        if (playerID!=0)
            DBTools.insertTrainingProfile(this);
        else
            throw new ValidationException("There is no player assigned to this profile");
    }

    /**
     * get the player id
     * @return player id
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * set the player id
     * @param playerID player ID
     */
    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /**
     * get the profile ID
     * @return profile ID
     */
    public int getProfileID() {
        return profileID;
    }

    /**
     * set the profile ID
     * @param profileID profile id
     */
    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    /**
     * Get the Passing level
     * @return passing level
     */
    public String getPassingLevel() {
        return passingLevel;
    }

    /**
     * set the Passing level
     * @param passingLevel passing level
     * @throws ValidationException Error if issue
     */
    public void setPassingLevel(String passingLevel) throws ValidationException{
        if (testLevelDesc(passingLevel))
            this.passingLevel=passingLevel;
        else
            throw new ValidationException("Incorrect Level Description");
    }

    /**
     * get the running level
     * @return running level
     */
    public String getRunningLevel() {
        return runningLevel;
    }

    /**
     * set the running level
     * @param runningLevel running level
     * @throws ValidationException Error if issue
     */
    public void setRunningLevel(String runningLevel) throws ValidationException{
        if (testLevelDesc(runningLevel))
            this.runningLevel=runningLevel;
        else
            throw new ValidationException("Incorrect Level Description");
    }

    /**
     * Get the support level
     * @return support level
     */
    public String getSupportLevel() {
        return supportLevel;
    }

    /**
     * set the support level
     * @param supportLevel support level
     * @throws ValidationException Error if issue
     */
    public void setSupportLevel(String supportLevel) throws ValidationException{
        if (testLevelDesc(supportLevel))
            this.supportLevel=supportLevel;
        else
            throw new ValidationException("Incorrect Level Description");
    }

    /**
     * get the tackling level
     * @return tackling level
     */
    public String getTacklingLevel() {
        return tacklingLevel;
    }

    /**
     * set the tackling level
     * @param tacklingLevel tackling level
     * @throws ValidationException Error if issue
     */
    public void setTacklingLevel(String tacklingLevel) throws ValidationException {
        if (testLevelDesc(tacklingLevel))
            this.tacklingLevel=tacklingLevel;
        else
            throw new ValidationException("Incorrect Level Description");    }

    /**
     * get the decision-making level
     * @return decision level
     */
    public String getDecisionLevel() {
        return decisionLevel;
    }

    /**
     * set the decision-making level
     * @param decisionLevel decision level
     * @throws ValidationException Error if issue
     */
    public void setDecisionLevel(String decisionLevel) throws ValidationException {
        if (testLevelDesc(decisionLevel))
            this.decisionLevel=decisionLevel;
        else
            throw new ValidationException("Incorrect Level Description");
    }

    /**
     * Get an arrayList of the games played by the player
     * @return Games arrayList
     */
    public ArrayList<Game> getGames() {
        return games;
    }

    /**
     * assign an arrayList of games to the player game list
     * @param games ArrayList of games
     */
    public void setGames(ArrayList<Game> games) {
        this.games = games;
    }

    /**
     * get an arrayList of training sessions done by the player
     * @return
     */
    public ArrayList<TrainingSession> getTrainingSessions() {
        return trainingSessions;
    }

    /**
     * set the player's training session.
     * @param trainingSessions arrayList of training session
     */
    public void setTrainingSessions(ArrayList<TrainingSession> trainingSessions) {
        this.trainingSessions = trainingSessions;
    }

    /**
     * get the levelID based on the level description
     * @param levelDesc the level description
     * @return the level id
     * @throws ValidationException Error if exceptions.
     */
    public static int getLevelID(String levelDesc) throws ValidationException{
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

    /**
     * Method to return the level desciption based on the id
     * @param level level id
     * @return level description
     */
    public static String getLevelDesc(int level){
        switch (level){
            case 1:
                return "Poor";
            case 2:
                return "Developing";
            case 3:
                return "Proficient";
            case 4:
                return "Advanced";
            case 5:
                return "Leading";
            default:
                return "";
        }
    }

    /**
     * Test that the correct level descriptions are passed to a function
     * @param levelDesc the level description
     * @return true or false
     */
    private boolean testLevelDesc(String levelDesc){
        if (levelDesc.equals("Poor") || levelDesc.equals("Developing") || levelDesc.equals("Proficient")
                || levelDesc.equals("Advanced") || levelDesc.equals("Leading"))
            return true;
        else
            return false;
    }



    // END OF CLASS
}
