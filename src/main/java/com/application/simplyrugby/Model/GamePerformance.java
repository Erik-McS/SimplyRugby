package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.DBTools;

/**
 * Class to hold the game performance of a player via its profile. It is mainly used to get an object to populate the consult profile table
 */
public class GamePerformance {
    private int profile_id;
    private int game_id;
    private String performance;
    private String date;
    private String where;
    private String clubName;


    public GamePerformance(int profile_id, int game_id, String performance, String date, String where,String clubName) {
        this.profile_id = profile_id;
        this.game_id = game_id;
        this.performance = performance;
        this.date = date;
        this.where = where;
        this.clubName=clubName;
    }

    public int getProfile_id() {
        return profile_id;
    }

    public int getGame_id() {
        return game_id;
    }

    public String getPerformance() {
        return performance;
    }

    public String getDate() {
        return date;
    }

    public String getWhere() {
        return where;
    }

    public String getClubName() {
        return clubName;
    }
}
