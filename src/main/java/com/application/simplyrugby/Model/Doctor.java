package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.DBTools;

/**
 * Class to hold a player's doctor information.<br>
 * It inherits from the ThirdParty Interface and provides methods to insert and select a doctor from the database.
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

    /**
     * Returns the doctor ID.
     * @return Doctor ID
     */
    public int getDoctorID() {
        return doctorID;
    }

    /**
     * Set the doctorID
     * @param doctorID Doctor ID
     */
    public void setDoctorID(int doctorID) {
        this.doctorID = doctorID;
    }

    /**
     * Returns the doctor's first name
     * @return First name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the first name
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * return the surname
     * @return surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * set the surname
     * @param surname surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * return the telephone
     * @return telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * set the telephone
     * @param telephone telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    /**
     * get the object info
     * @return String.
     */
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
