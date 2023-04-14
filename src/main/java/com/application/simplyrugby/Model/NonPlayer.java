package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.DBTools;

public class NonPlayer implements Member{

    private String firstName;
    private String surname;
    private String address;
    private String telephone;
    private String email;
    private int member_id;
    private int role_id;

    public NonPlayer(){}

    public NonPlayer(int member_id,String firstName,String surname,String address,String telephone,String email,int role_id){
        setMember_id(member_id);
        setFirstName(firstName);
        setSurname(surname);
        setAddress(address);
        setTelephone(telephone);
        setEmail(email);
        setRole_id(role_id);
    }

    @Override
    public boolean saveMember(Member member) {
        return DBTools.insertMember(member);
    }

    @Override
    public Member loadMember() {
        return null;
    }

    public int getMember_id() {
        return member_id;
    }

    public int getRole_id() {
        return role_id;
    }

    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
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
