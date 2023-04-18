package com.application.simplyrugby.Model;

import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ValidationException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to manipulate the players records.<br>
 * It uses the Builder design pattern, the constructor will be private and a nested class with a Builder.<br>
 * It will create the object and check the data.
 */
public class Player implements Member{

    // private fields specific to the Player class
    private String firstName;
    private String surname;
    private String address;
    private String telephone;
    private String email;
    private String gender;
    private String dateOfBirth;
    private int scrumsNumber;
    private int playerID;
    private int doctorID;
    private int kinID;
    //private int profileID;
    private String isAssignedToSquad;

    // Constructor of the player class. it will take the Builder object and assign its field.

    private Player(PlayerBuilder build){
        this.playerID=build.playerID;
        this.firstName=build.firstName;
        this.surname=build.surname;
        this.address=build.address;
        this.dateOfBirth=build.dateOfBirth;
        this.gender=build.gender;
        this.email=build.email;
        this.scrumsNumber=build.scrumsNumber;
        this.telephone=build.telephone;
        this.kinID=build.kinID;
        this.doctorID=build.doctorID;
        //this.profileID=build.profileID;
        this.isAssignedToSquad=build.isAssignedToSquad;
    }
    // empty constructor
    private Player(){
        this.playerID=0;
        this.firstName="Dummy";
        this.surname="Dummy";
        this.address="dummy";
        this.dateOfBirth="";
        this.gender="Male";
        this.email="q@q.qqq";
        this.scrumsNumber=1111111111;
        this.telephone="1111111111";
        this.kinID=0;
        this.doctorID=0;
        this.isAssignedToSquad="NO";
    }

    public static Player dummyPlayer(){
        return new Player();
    }
    //function to add a player record in the database.
    @Override
    public boolean saveMember(Member member) {
        return DBTools.insertMember(member);
    }
    // get a player record from the database.
    @Override
    public Member loadMember(Member member,int id) {
        return DBTools.loadMember(member,id);
    }

    /**
     * this Builder class will build a player object. <br>
     * it is in charge of the data validation.
     */
    public static class PlayerBuilder{
        // the class needs the same attributes as the Player Class
        private String firstName;
        private String surname;
        private String address;
        private String telephone;
        private String email;
        private String gender;
        private String dateOfBirth;
        private int scrumsNumber;
        private int playerID;
        private int doctorID;
        private int kinID;
        private String isAssignedToSquad;

        /**
         * function to set the firstname
         * @param firstName the player firstname
         * @return the builder object.
         * @throws ValidationException Name cannot be empty
         */
        public PlayerBuilder setFirstName(String firstName) throws ValidationException{

            if (firstName.equals("")){
                throw new ValidationException("Name cannot be empty");
            }
            // if not, test the data format, if invalid, will throw an exception
            else{
                String nameValidation="(\\p{Upper})(\\p{Lower}){1,12}";
                if (firstName.matches(nameValidation)) {
                    this.firstName = firstName;
                    return this;
                }
                else
                    throw new ValidationException("Invalid Name format, please enter a valid name.");
            }

        }
        /**
         * Function to set the Surname
         * @param surname The player's surname
         * @return The builder object
         * @throws ValidationException Surname cannot be empty
         */
        public PlayerBuilder setSurname(String surname)throws ValidationException{
            if (surname.equals(""))
                throw new ValidationException("Surname cannot be empty");
            else{
                // basic regex to test surname
                String validation="(\\p{Upper})(\\p{Alpha}){1,15}";
                if (surname.matches(validation)) {
                    this.surname = surname;
                    return this;
                }
                else
                    throw new ValidationException("Invalid Surname Format, please enter a valid surname");
            }
        }
        /**
         * Function to set the address
         * @param address The Player's address
         * @return The builder object
         * @throws ValidationException Address cannot be empty
         */
        public PlayerBuilder setAddress(String address) throws ValidationException{
            if (address.equals(""))
                throw new ValidationException("Address cannot be empty");
            else{
                this.address=address;
                return this;
            }
        }
        /**
         * Function to set the player's telephone
         * @param telephone The player's telephone
         * @return The builder object
         * @throws ValidationException Telephone cannot be empty
         */
        public PlayerBuilder setTelephone(String telephone) throws ValidationException{
            if (telephone.equals(""))
                throw new ValidationException("Telephone cannot be empty");
            else{
                // testing if the tel format is valid
                String regex="[0-9]{9,11}";
                if (telephone.matches(regex)) {
                    this.telephone = telephone;
                    return this;
                }
                else
                    throw new ValidationException("Phone numbers can only contains 9-11 digits");
            }
        }
        /**
         * Function to set the player's email
         * @param email The player's email
         * @return The builder object
         * @throws ValidationException Email cannot be empty or of incorrect format
         */
        public PlayerBuilder setEmail(String email)throws ValidationException{
            if (email.equals(""))
                throw new ValidationException("Email cannot be empty");
            else{
                String validation="^[\\w-\\.\\_]+@([\\w-]+\\.)+[\\w-]{2,4}$";
                Pattern pattern=Pattern.compile(validation);
                Matcher matcher=pattern.matcher(email);
                if (matcher.matches()) {
                    this.email = email;
                    return this;
                }
                else
                    throw new ValidationException("Invalid email.");
            }
        }
        /**
         * Function to set the player's gender
         * @param gender The player's gender
         * @return The builder object
         * @throws ValidationException A gender needs to be selected
         */
        public PlayerBuilder setGender(String gender)throws ValidationException{
            if (gender.equals(""))
                throw new ValidationException("Please select a gender");
            else {
                this.gender=gender;
                return this;
            }
        }
        /**
         * Function to set the Date of Birth
         * @param dateOfBirth The player's date of birth
         * @return The builder object
         * @throws ValidationException Date of Birth cannot be empty
         */
        public PlayerBuilder setDoB(String dateOfBirth) throws ValidationException{
            if (dateOfBirth.equals(""))
                throw new ValidationException("Date of Birth needs to be set");
            else {
                this.dateOfBirth=dateOfBirth;
                return this;
            }
        }
        /**
         * Function to set the SCRUMS cumber
         * @param scrumsNumber The player's SCRUMS number
         * @return the builder object
         * @throws ValidationException The SCRUMS number cannot be empty
         */
        public PlayerBuilder setScrumsNumber(int scrumsNumber)throws ValidationException{
            if (scrumsNumber==0)
                throw new ValidationException("SCRUMS number cannot be empty");
            else {
                this.scrumsNumber=scrumsNumber;
                return this;
            }
        }
        /**
         * Function to set the doctor ID
         * @param doctorID The player's doctor ID
         * @return The builder object
         * @throws ValidationException A doctor must be assigned to a player
         */
        public PlayerBuilder setDoctorID(int doctorID) throws ValidationException{
            if (doctorID==0)
                throw new ValidationException("A doctor needs to be assigned to the player");
            else{
                this.doctorID=doctorID;
                return this;
            }
        }
        /**
         * Function to set the Next of Kin
         * @param kinID The player's Next of Kin ID
         * @return The builder object
         * @throws ValidationException A NoK must be assigned do a player
         */
        public PlayerBuilder setKinID(int kinID) throws ValidationException{
            if (kinID==0)
                throw new ValidationException("A Next of Kin needs to be assigned to the player");
            else{
                this.kinID=kinID;
                return this;
            }
        }

        /**
         * Function to set the player ID, is not really used as the real ID is created by the database INSERT
         * <br> Can be set to 0
         * @param playerID The player's ID
         * @return The builder's object
         */
        public PlayerBuilder setPlayerID(int playerID){
                this.playerID=playerID;
                return this;
        }
        /**
         * Function to set the player's status. set to False by default.
         * @param isAssignedToSquad The player's status
         * @return The builder object
         */
        public PlayerBuilder setIsAssignedToSquad(String isAssignedToSquad) throws ValidationException{
            isAssignedToSquad=isAssignedToSquad.toUpperCase();
            if (isAssignedToSquad.equals("YES"))
                this.isAssignedToSquad=isAssignedToSquad;
            else if (isAssignedToSquad.equals("NO"))
                this.isAssignedToSquad=isAssignedToSquad;
            else
                throw new ValidationException("Incorrect squad assignment flag value");
            return this;
        }

        /**
         * This function build the Player object and returns it to the caller.
         * @return The player object
         * @throws ValidationException throws an error if any problems
         */
        public Player Builder() throws ValidationException
        {
            // set the default values for the non-mandatory fields
            this.setIsAssignedToSquad("NO");
            //this.profileID=0;

            return new Player(this);
        }

    }

    @Override
    public String toString() {
        return "Player{" +
                "firstName='" + firstName +
                "\nsurname='" + surname +
                "\naddress='" + address +
                "\ntelephone='" + telephone +
                "\nemail='" + email +
                "\ngender='" + gender +
                "\ndateOfBirth='" + dateOfBirth +
                "\nscrumsNumber=" + scrumsNumber +
                "\nplayerID=" + playerID +
                "\ndoctorID=" + doctorID +
                "\nkinID=" + kinID +
                "\nisAssignedToSquad=" + isAssignedToSquad +
                '}';
    }

    /**
     * Method to set the player firstname
      * @param firstName First name of the player
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
     * Function to set the Surname
     * @param surname The player's surname
     * @throws ValidationException Surname cannot be empty
     */
    public void setSurname(String surname)throws ValidationException{

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
     * Function to set the address
     * @param address The Player's address
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
     * Function to set the player's telephone
     * @param telephone The player's telephone
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
     * Function to set the player's email<br>
     * @see <a href="https://www.baeldung.com/java-email-validation-regex">Regex for emails</a>
     * @param email The player's email
     * @throws ValidationException Email cannot be empty
     */
    public void setEmail(String email)throws ValidationException{
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

    /**
     * Function to set the player's gender
     * @param gender The player's gender
     * @throws ValidationException A gender needs to be selected
     */
    public void setGender(String gender)throws ValidationException{
        if (gender.equals(""))
            throw new ValidationException("Please select a gender");
        else {
            this.gender=gender;
        }
    }

    /**
     * Function to set the Date of Birth
     * @param dateOfBirth The player's date of birth
     * @throws ValidationException Date of Birth cannot be empty
     */
    public void setDoB(String dateOfBirth) throws ValidationException{
        if (dateOfBirth.equals(""))
            throw new ValidationException("Date of Birth needs to be set");
        else {
            this.dateOfBirth=dateOfBirth;
        }
    }

    /**
     * Function to set the SCRUMS cumber
     * @param scrumsNumber The player's SCRUMS number
     * @throws ValidationException The SCRUMS number cannot be empty
     */
    public void setScrumsNumber(int scrumsNumber)throws ValidationException{
        if (scrumsNumber==0)
            throw new ValidationException("SCRUMS number cannot be empty");
        else if(!((Integer)scrumsNumber instanceof Integer)){
            throw new ValidationException("SCRUMS number cannot be letters");
        }
        else {
            this.scrumsNumber=scrumsNumber;
        }
    }
    /**
     * Function to set the doctor ID
     * @param doctorID The player's doctor ID
     * @throws ValidationException A doctor must be assigned to a player
     */
    public void setDoctorID(int doctorID) throws ValidationException{
        if (doctorID==0)
            throw new ValidationException("A doctor needs to be assigned to the player");
        else{
            this.doctorID=doctorID;
        }
    }

    /**
     * Function to set the Next of Kin
     * @param kinID The player's Next of Kin ID
     * @throws ValidationException A NoK must be assigned do a player
     */
    public void setKinID(int kinID) throws ValidationException{
        if (kinID==0)
            throw new ValidationException("A Next of Kin needs to be assigned to the player");
        else{
            this.kinID=kinID;
        }
    }

    /**
     * Function to set the player ID, is not really used as the real ID is created by the database INSERT
     * <br> Can be set to 0
     * @param playerID The player's ID
     */
    public void setPlayerID(int playerID){
        this.playerID=playerID;
    }

    /**
     * Function to set the player's ID, it is usually set to 0 as the profile is created later.
     * @param profileID The player's ID
     */


    /**
     * Function to set the player's status. set to False by default.
     * @param isAssignedToSquad The player's status
     */
    public void setIsAssignedToSquad(String isAssignedToSquad) throws ValidationException{
        if (isAssignedToSquad.equals("YES"))
            this.isAssignedToSquad=isAssignedToSquad;
        else if (isAssignedToSquad.equals("NO"))
            this.isAssignedToSquad=isAssignedToSquad;
        else
            throw new ValidationException("Incorrect squad assignment flag value");
    }

    /**
     * Function to get the player's name.
     * @return Player's name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Function to get the player's surname.
     * @return Player's surname.
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Function to get the player's address.
     * @return Player's address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * Function to get the player's telephone.
     * @return Player's telephone.
     */
    public String getTelephone() {
        return telephone;
    }

    /**
     * Function to get the player's email.
     * @return Player's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Function to get the player's gender.
     * @return Player's gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Function to get the player's date of birth.
     * @return Player's date of birth.
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Function to get the player's SCRUMS number.
     * @return Player's SCRUMS number.
     */
    public int getScrumsNumber() {
        return scrumsNumber;
    }

    /**
     * Function to get the player's ID.
     * @return Player's ID.
     */
    public int getPlayerID() {
        return playerID;
    }

    /**
     * Function to get the player's doctor ID.
     * @return Doctor's ID
     */
    public int getDoctorID() {
        return doctorID;
    }

    /**
     * Function to get the player's next of kin ID.
     * @return NoK ID
     */
    public int getKinID() {
        return kinID;
    }



    /**
     * Function to return the Squad's status of the player.
     * @return Squad status-
     */
    public String isAssignedToSquad() {
        return isAssignedToSquad;
    }

    // END OF CLASS
}
