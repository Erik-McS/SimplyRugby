package com.application.simplyrugby.Control;

import com.application.simplyrugby.System.DBTools;

public class NonPlayer implements Member{

    private String firstName;
    private String surname;
    private String address;
    private String telephone;
    private String email;

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
}
