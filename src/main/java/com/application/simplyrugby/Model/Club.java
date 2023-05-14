package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        if (telephone.equals(""))
            throw new ValidationException("Telephone cannot be empty");
        else{
            // testing if the tel format is valid
            String regex="[0-9]{9,11}";
            if (telephone.matches(regex)) {
                this.telephone = telephone;
            }
            else
                throw new ValidationException("Phone numbers can only contains 9-11 digits");
        }
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
            throw new ValidationException("Email cannot be empty");
        else{
            String validation="^[\\w-\\.\\_]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            Pattern pattern=Pattern.compile(validation);
            Matcher matcher=pattern.matcher(email);
            if (matcher.matches()) {
                this.email = email;
            }
            else
                throw new ValidationException("Invalid email Format");
        }
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
    public void setClub_id(int club_id) throws ValidationException{

        if (club_id!=0)
            this.club_id = club_id;
        else
            throw new ValidationException("Incorrect Club ID");
    }
}
