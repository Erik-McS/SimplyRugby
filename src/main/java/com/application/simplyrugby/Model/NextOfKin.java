package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.DBTools;

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

    public int getKinID() {
        return kinID;
    }

    public void setKinID(int kinID) {
        this.kinID = kinID;
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
        return "NextOfKin{" +
                "kinID=" + kinID +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
