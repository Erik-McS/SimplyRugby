package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ValidationException;

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
        return (Doctor) DBTools.selectContact(this,index);
    }

    public Doctor(){}
    public Doctor(String firstName,String surname,String telephone) throws ValidationException{
        setFirstName(firstName);
        setSurname(surname);
        setTelephone(telephone);
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
    public void setDoctorID(int doctorID) throws ValidationException {
        if (doctorID!=0)
            this.doctorID = doctorID;
        else
            throw new ValidationException("Incorrect Doctor ID");

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
    public void setFirstName(String firstName) throws ValidationException {

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
}
