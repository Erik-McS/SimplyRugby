package com.application.simplyrugby.Control;

public class Doctor implements ThirdParty{

    private int doctorID;
    private String firstName;
    private String surname;
    private String telephone;

    @Override
    public void saveContact(ThirdParty person) {

    }

    @Override
    public ThirdParty loadContact() {
        return null;
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
