package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.DBTools;

/**
 * Class to hold a next of Kin record.
 */
public class NextOfKin implements ThirdParty{

    private int kinID;
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
        NextOfKin temp=(NextOfKin) DBTools.selectContact(this,index);
        return temp;

    }

    /**
     * Get the NoK ID
     * @return the kin_ID
     */
    public int getKinID() {
        return kinID;
    }

    /**
     * set the NoK id
     * @param kinID the kin id
     */
    public void setKinID(int kinID) {
        this.kinID = kinID;
    }

    /**
     * Get the NoK name
     * @return The name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Set the NoK name
     * @param firstName The first name;
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Get the NoK surname
     * @return The surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Set the NoK surname
     * @param surname The surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Get the NoK telephone
     * @return The telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Set the telephone
     * @param telephone The telephone
     */
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "NextOfKin{" +
                "kinID=" + kinID +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
