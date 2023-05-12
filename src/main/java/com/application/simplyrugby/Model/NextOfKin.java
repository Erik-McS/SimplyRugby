package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ValidationException;

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

        return DBTools.selectContact(this,index);

    }

    public NextOfKin(){}

    public NextOfKin(String firstName,String surname,String telephone) throws ValidationException{
        setFirstName(firstName);
        setSurname(surname);
        setTelephone(telephone);
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
    public void setKinID(int kinID) throws ValidationException{
        if (kinID!=0)
            this.kinID = kinID;
        else
            throw new ValidationException("Invalid KinID");
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
    public void setFirstName(String firstName) throws ValidationException{

        // test if the field is empty
        if (firstName.equals("")){
            throw new ValidationException("Name cannot be empty");
        }
        // if not, test the data format, if invalid, will throw an exception
        else{
            String nameValidation="(\\p{Upper})(\\p{Lower}){1,12}";
            if (firstName.matches(nameValidation)){
                this.firstName = firstName;

            }
            else
                throw new ValidationException("Invalid Name format, please enter a valid name.");
        }
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
     * return the object info
     * @return info
     */
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
