package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.ValidationException;

/**
 * This class will hold the information of a game between a squad of the club and another club.
 */
public class Game {

    private Squad squad;
    private Club playingClub;
    private int location;
    private String outcome;
    private String date;
    private int nbTry;
    private int nbPenalty;
    private int nbConversion;
    private int nbDropGoal;
    private int game_id;
    private int squad_id;
    private int opponentScore;

    /**
     * Empty constructor to allow null object for tests
     */
    public Game(){}

    /**
     * Creates a Game object
     * @param squad The squad playing the game
     * @param club The opponent club
     * @param date the date of the match
     * @param location if the game is home or away
     * @throws ValidationException Error if data validation issue
     */
    public Game(Squad squad,Club club,String date,int location) throws ValidationException {
        setSquad(squad);
        setPlayingClub(club);
        setDate(date);
        setLocation(location);
    }

    /**
     * get the playing squad object
     * @return playing squad
     */
    public Squad getSquad() {
        return squad;
    }

    /**
     * set the playing squad
     * @param squad playing squad
     * @throws ValidationException Error if any issue
     */
    public void setSquad(Squad squad) throws ValidationException{
        if (squad!=null)
            this.squad = squad;
        else
            throw new ValidationException("The Squad object cannot be empty");
    }

    /**
     * get the opponent club
     * @return opponent club
     */
    public Club getPlayingClub() {
        return playingClub;
    }

    /**
     * set the playing club
     * @param playingClub playing club
     * @throws ValidationException Error if issues
     */
    public void setPlayingClub(Club playingClub) throws ValidationException{
        if (playingClub!=null)
            this.playingClub = playingClub;
        else
            throw new ValidationException("The Club object cannot be empty");
    }

    /**
     * return the location ID
     * @return location_id
     */
    public int getLocation() {
        return location;
    }

    /**
     * set the location id
     * @param location location_id
     * @throws ValidationException Error if issue
     */
    public void setLocation(int location) throws ValidationException{
        if (location==0)
            throw new ValidationException("The Game Location cannot be empty");
        else
            this.location = location;
    }

    /**
     * return the game outcome
     * @return outcome
     */
    public String getOutcome() {
        return outcome;
    }

    /**
     * set the game outcome
     * @param outcome
     */
    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    /**
     * get the match date
     * @return game date
     */
    public String getDate() {
        return date;
    }

    /**
     * set the match's date
     * @param date game date
     * @throws ValidationException Error if any issue
     */
    public void setDate(String date) throws ValidationException {
        if (date.equals(""))
            throw new ValidationException("The game date cannot be empty");
        else
            this.date=date;
    }

    /**
     * return the nb of Try
     * @return nb of Try
     */
    public int getNbTry() {
        return nbTry;
    }

    /**
     * Set the nb of Try
     * @param nbTry
     */
    public void setNbTry(int nbTry) {
        this.nbTry = nbTry;
    }

    /**
     * return the nb of Penalty
     * @return nb of penalty
     */
    public int getNbPenalty() {
        return nbPenalty;
    }

    /**
     * set the number of Penalties
     * @param nbPenalty
     */
    public void setNbPenalty(int nbPenalty) {
        this.nbPenalty = nbPenalty;
    }

    /**
     * return the nb of Conversions
     * @return nb of conversion
     */
    public int getNbConversion() {
        return nbConversion;
    }

    /**
     * set the number of conversions
     * @param nbConversion nb of conversions
     */
    public void setNbConversion(int nbConversion) {
        this.nbConversion = nbConversion;
    }

    /**
     * return the nb of Drop goals
     * @return nb of Drop goals
     */
    public int getNbDropGoal() {
        return nbDropGoal;
    }

    /**
     * set the number of drop goals
     * @param nbDropGoal nb of drop goals
     */
    public void setNbDropGoal(int nbDropGoal) {
        this.nbDropGoal = nbDropGoal;
    }

    /**
     * return the game id
     * @return game id
     */
    public int getGame_id() {return game_id;}

    /**
     * set the game id
     * @param game_id game id
     */
    public void setGame_id(int game_id) {this.game_id = game_id;}

    /**
     *  return the squad id
     * @return squad id
     */
    public int getSquad_id() {
        return squad_id;
    }

    /**
     * set the squad id
     * @param squad_id squad id
     */
    public void setSquad_id(int squad_id) {
        this.squad_id = squad_id;
    }

    /**
     * return the opponent score
     * @return opponent score
     */
    public int getOpponentScore() {
        return opponentScore;
    }

    /**
     * set the opponent score
     * @param opponentScore opponent score
     */
    public void setOpponentScore(int opponentScore) {
        this.opponentScore = opponentScore;
    }

    /**
     * display the object info
     * @return
     */
    @Override
    public String toString() {
        return "Game{" +
                "squad=" + squad +
                ", playingClub=" + playingClub +
                ", location=" + location +
                ", outcome='" + outcome + '\'' +
                ", date='" + date + '\'' +
                ", nbTry=" + nbTry +
                ", nbPenalty=" + nbPenalty +
                ", nbConversion=" + nbConversion +
                ", nbDropGoal=" + nbDropGoal +
                ", game_id=" + game_id +
                ", squad_id=" + squad_id +
                ", opponentScore=" + opponentScore +
                '}';
    }

    //END OF CLASS
}
