package com.application.simplyrugby.Model;

/**
 * Class used to store and manipulate training sessions
 */
public class TrainingSession {

    private String date;
    private int trainingType;
    private int trainingFacility;
    private int session_id;

    public TrainingSession(String date,int location,int type){
        this.date=date;
        this.trainingFacility=location;
        this.trainingType=type;
    }


    /**
     * Get the session date
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * set the session date
     * @param date date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * get the training type
     * @return training type
     */
    public int getTrainingType() {
        return trainingType;
    }

    /**
     * set the training type
     * @param trainingType training type
     */
    public void setTrainingType(int trainingType) {
        this.trainingType = trainingType;
    }

    /**
     * get the training facility
     * @return facility
     */
    public int getTrainingFacility() {
        return trainingFacility;
    }

    /**
     * set the training facility
     * @param trainingFacility training facility
     */
    public void setTrainingFacility(int trainingFacility) {
        this.trainingFacility = trainingFacility;
    }

    /**
     * get the session id
     * @return session id
     */
    public int getSession_id() {
        return session_id;
    }

    /**
     * set the session id
     * @param session_id session id
     */
    public void setSession_id(int session_id) {
        this.session_id = session_id;
    }

    /**
     * give the object information
     * @return string
     */
    @Override
    public String toString() {
        return "TrainingSession{" +
                "date='" + date + '\'' +
                ", trainingType='" + trainingType + '\'' +
                ", trainingFacility='" + trainingFacility + '\'' +
                ", session_id=" + session_id +
                '}';
    }

    /**
     * gives the location description
     * @param location location ID.
     * @return description
     */
    public static String getFacility(int location){
        switch (location){
            case 1:
                return "Main Field";
            case 2:
                return "Fitness Suite";
            case 3:
                return "Swimming Pool";
            default:
                return "";
        }
    }

    public static String getTypeDescription(int type) {
        switch (type) {
            case 1:
                return "Strength";
            case 2:
                return "Endurance";
            case 3:
                return "Core";
            case 4:
                return "Team play";
            case 5:
                return "Tactics";
            default:
                return "";
        }
    }
// END OF CLASS
}
