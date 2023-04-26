package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.ValidationException;

/**
 * This class will hold the information for a game between a squad of the club and another club.
 */
public class Game {

    private Squad squad;
    private Club playingClub;
    private int location;
    private String outcome;
    private String date;
    private int nbTry;
    private int nbPenaltyTry;
    private int nbPenalty;
    private int nbConversion;
    private int nbDropGoal;
    private int squad_id;

    public Game(){}

    public Game(Squad squad,Club club,String date,int location) throws ValidationException {
        setSquad(squad);
        setPlayingClub(club);
        setDate(date);
        setLocation(location);
    }

    public Squad getSquad() {
        return squad;
    }

    public void setSquad(Squad squad) throws ValidationException{
        if (squad!=null)
            this.squad = squad;
        else
            throw new ValidationException("The Squad object cannot be empty");
    }

    public Club getPlayingClub() {
        return playingClub;
    }

    public void setPlayingClub(Club playingClub) throws ValidationException{
        if (playingClub!=null)
            this.playingClub = playingClub;
        else
            throw new ValidationException("The Club object cannot be empty");
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) throws ValidationException{
        if (location==0)
            throw new ValidationException("The Game Location cannot be empty");
        else
            this.location = location;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) throws ValidationException {
        if (date.equals(""))
            throw new ValidationException("The game date cannot be empty");
        else
            this.date=date;
    }

    public int getNbTry() {
        return nbTry;
    }

    public void setNbTry(int nbTry) {
        this.nbTry = nbTry;
    }

    public int getNbPenaltyTry() {
        return nbPenaltyTry;
    }

    public void setNbPenaltyTry(int nbPenaltyTry) {
        this.nbPenaltyTry = nbPenaltyTry;
    }

    public int getNbPenalty() {
        return nbPenalty;
    }

    public void setNbPenalty(int nbPenalty) {
        this.nbPenalty = nbPenalty;
    }

    public int getNbConversion() {
        return nbConversion;
    }

    public void setNbConversion(int nbConversion) {
        this.nbConversion = nbConversion;
    }

    public int getNbDropGoal() {
        return nbDropGoal;
    }

    public void setNbDropGoal(int nbDropGoal) {
        this.nbDropGoal = nbDropGoal;
    }
}
