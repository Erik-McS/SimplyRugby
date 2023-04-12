package com.application.simplyrugby.Control;

import com.application.simplyrugby.System.DBTools;

/**
 * Class to hold a player's doctor information.<br>
 * It inherits from the ThirdParty Interface and provide methods to insert and select a doctor from the database.
 */
public class Doctor implements ThirdParty{

    private int doctorID;
    private String firstName;
    private String surname;
    private String telephone;

    @Override
    public boolean saveContact() {
        return DBTools.insertContact(this);
    }

    @Override
    public ThirdParty loadContact() {
        return null;
    }

    @Override
    public ThirdParty loadContact(int index) {
        Doctor temp=(Doctor) DBTools.selectContact(this,index);
        return temp;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
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

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "doctorID=" + doctorID +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
