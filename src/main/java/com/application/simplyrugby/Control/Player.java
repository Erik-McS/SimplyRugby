package com.application.simplyrugby.Control;

import com.application.simplyrugby.System.DBTools;

/**
 * Class to manipulate teh players records
 */
public class Player implements Member{

    // private fields specific to the Player class
    private String firstName;
    private String surname;
    private String address;
    private String telephone;
    private String email;
    private String gender;
    private String dateOfBirth;
    private int scrumsNumber;
    private int playerID;
    private int doctorID;
    private int kinID;
    private int profileID;
    private boolean isAssignedToSquad;

    @Override
    public void saveMember(Member member) {
        DBTools.insertMember(member);
    }

    @Override
    public Member loadMember() {
        return null;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public int getScrumsNumber() {
        return scrumsNumber;
    }

    public void setScrumsNumber(int scrumsNumber) {
        this.scrumsNumber = scrumsNumber;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    public int getKinID() {
        return kinID;
    }

    public void setKinID(int kinID) {
        this.kinID = kinID;
    }

    public int getProfileID() {
        return profileID;
    }

    public void setProfileID(int profileID) {
        this.profileID = profileID;
    }

    public boolean isAssignedToSquad() {
        return isAssignedToSquad;
    }

    public void setAssignedToSquad(boolean assignedToSquad) {
        isAssignedToSquad = assignedToSquad;
    }
}
