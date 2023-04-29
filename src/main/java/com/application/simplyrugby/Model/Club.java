package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.ValidationException;

/**
 * This call contains the information of an opponent club
 */
public class Club {
    private String name;
    private String address;
    private String telephone;
    private String email;
    private int club_id;

    /**
     * Empty constructor can be used to create an object that will be assigned a club from the database.
     */
    public Club(){}

    /**
     * Constructor for the class Club
     * @param name Name of the club
     * @param address Address of the club
     * @param tel Telephone of the club
     * @param email Email of the club.
     * @throws ValidationException Error message
     */
    public Club(String name,String address,String tel,String email) throws ValidationException {
        setName(name);
        setAddress(address);
        setTelephone(tel);
        setEmail(email);
    }

    /**
     * Returns the name of club
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the club
     * @param name Name of the club
     * @throws ValidationException Error Message
     */
    public void setName(String name) throws ValidationException{
        if (name.equals(""))
            throw new ValidationException("The club name cannot be empty");
        else
            this.name=name;
    }

    /**
     * Returns the address of the club
     * @return Address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address of the club
     * @param address Address
     * @throws ValidationException Error message
     */
    public void setAddress(String address) throws ValidationException{
        if (address.equals(""))
            throw new ValidationException("The club address cannot be empty");
        else
            this.address=address;
    }

    /**
     * Returns the telephone of the club
     * @return Telephone
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Set the telephone of the club
     * @param telephone Telephone
     * @throws ValidationException Error message
     */
    public void setTelephone(String telephone) throws ValidationException {
        if(telephone.equals(""))
            throw new ValidationException("The club telephone cannot be empty");
        else
            this.telephone=telephone;
    }

    /**
     * Returns the email of the club
     * @return Email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set the email of the club
     * @param email Email
     * @throws ValidationException Error message.
     */
    public void setEmail(String email) throws ValidationException{
        if (email.equals(""))
            throw new ValidationException("The club telephone cannot be empty");
        else
            this.email=email;
    }

    /**
     * Returns the club ID
     * @return Club ID
     */
    public int getClub_id() {
        return club_id;
    }

    /**
     * Set the ID of the club
     * @param club_id Club ID
     */
    public void setClub_id(int club_id) {
        this.club_id = club_id;
    }
}
