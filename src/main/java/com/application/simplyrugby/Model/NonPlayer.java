package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to store the club's non-playing members, which include coaches, secretaries and chairman
 */
public class NonPlayer implements Member{

    private String firstName;
    private String surname;
    private String address;
    private String telephone;
    private String email;
    private int member_id;
    private int role_id;

    public NonPlayer(){}

    public NonPlayer(int member_id,String firstName,String surname,String address,String telephone,String email,int role_id) throws ValidationException{
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
    public Member loadMember(Member member,int id) {
        return DBTools.loadMember(member,id);
    }

    /**
     * Function to get the member_id
     * @return the member ID.
     */
    public int getMember_id() {
        return member_id;
    }
    /**
     * Function to get the role_ID.
     * @return Member role_ID.
     */
    public int getRole_id() {
        return role_id;
    }

    /**
     * Set the Member's role ID.
     * @param role_id Member's role ID, refer to the non_players_roles table for more info on roles
     */
    public void setRole_id(int role_id) {
        this.role_id = role_id;
    }

    /**
     * Set the member_ID. used by the DBTools to create a member object from the DB.
     * @param member_id The member ID
     */
    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }
    /**
     * Function to get the surname.
     * @return Member surname.
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Method to set the member firstname
     * @param firstName First name of the member
     * @throws ValidationException exception if the format is invalid or the field empty
     */
    public void setFirstName(String firstName) throws ValidationException{
        // test if the field is empty
        if (firstName.equals("")){
            throw new ValidationException("Name cannot be empty");
        }
        // if not, test the data format, if invalid, will throw an exception
        else{
            String nameValidation="(\\p{Upper})(\\p{Lower}){1,12}";
            if (firstName.matches(nameValidation))
                this.firstName = firstName;
            else
                throw new ValidationException("Invalid Name format, please enter a valid name.");
        }
    }

    /**
     * Function to get the surname.
     * @return Member surname.
     */
    public String getSurname() {
        return surname;
    }
    /**
     * Function to set the Surname
     * @param surname The member's surname
     * @throws ValidationException Surname cannot be empty
     */
    public void setSurname(String surname) throws ValidationException{

        if (surname.equals(""))
            throw new ValidationException("Surname cannot be empty");
        else{
            // basic regex to test surname
            String validation="(\\p{Upper})(\\p{Alpha}){1,15}";
            if (surname.matches(validation)) {
                this.surname = surname;
            }
            else
                throw new ValidationException("Invalid Surname Format, please enter a valid surname");
        }
    }

    /**
     * Get the member's address
     * @return Address
     */
    public String getAddress() {
        return address;
    }
    /**
     * Function to set the address
     * @param address The Member's address
     * @throws ValidationException Address cannot be empty
     */
    public void setAddress(String address) throws ValidationException{

        if (address.equals(""))
            throw new ValidationException("Address cannot be empty");
        else{
            this.address=address;
        }
    }

    /**
     * Returns the member's telephone.
     * @return Telephone
     */
    public String getTelephone() {
        return telephone;
    }
    /**
     * Function to set the telephone
     * @param telephone The member's telephone
     * @throws ValidationException Telephone cannot be empty
     */
    public void setTelephone(String telephone) throws ValidationException{

        if (telephone.equals(""))
            throw new ValidationException("Telephone cannot be empty");
        else{
            // testing if the tel format is valid
            String regex="[0-9]{9,11}";
            if (telephone.matches(regex))
                this.telephone=telephone;
            else
                throw new ValidationException("Phone numbers can only contains 9-11 digits");
        }
    }

    /**
     * Function to get the email.
     * @return Member's email. b
     */
    public String getEmail() {
        return email;
    }
    /**
     * Function to set the member's email<br>
     * @see <a href="https://www.baeldung.com/java-email-validation-regex">Regex for emails</a>
     * @param email The member's email
     * @throws ValidationException Email cannot be empty
     */
    public void setEmail(String email) throws ValidationException{

        if (email.equals(""))
            throw new ValidationException("Email cannot be empty");
        else{
            String validation="^[\\w-\\.\\_]+@([\\w-]+\\.)+[\\w-]{2,4}$";
            Pattern pattern=Pattern.compile(validation);
            Matcher matcher=pattern.matcher(email);
            if (matcher.matches())
                this.email=email;
            else
                throw new ValidationException("Invalid email.");
        }
    }

    @Override
    public String toString() {
        return "NonPlayer{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", member_id=" + member_id +
                ", role_id=" + role_id +
                '}';
    }
}
