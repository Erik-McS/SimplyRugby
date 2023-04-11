package com.application.simplyrugby.Control;

import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ValidationException;

/**
 * Class to manipulate teh players records<br>
 * uses the Builder design pattern, the constructor will be private and a nested class with a Builder<br>
 * will create the object
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
    private int profileID;
    private boolean isAssignedToSquad;

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
        this.profileID=build.profileID;
        this.isAssignedToSquad=build.isAssignedToSquad;
    }

    //function to add a player record in the database.
    @Override
    public void saveMember(Member member) {
        DBTools.insertMember(member);
    }
    // get a player record from the database.
    @Override
    public Member loadMember() {
        return null;
    }

    /**
     * this Buildr class will build a player object. <br>
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
        private int profileID;
        private boolean isAssignedToSquad;

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
            else{
                this.firstName=firstName;
                return this;
            }
        }

        /**
         * Function to set the Surname
         * @param surname The player's surname
         * @return Th builder object
         * @throws ValidationException Surname cannot be empty
         */
        public PlayerBuilder setSurname(String surname)throws ValidationException{
            if (surname.equals(""))
                throw new ValidationException("Surname cannot be empty");
            else{
                this.surname=surname;
                return this;
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
                this.telephone=telephone;
                return this;
            }
        }

        /**
         * Function to set the player's email
         * @param email The player's email
         * @return The builder object
         * @throws ValidationException Email cannot be empty
         */
        public PlayerBuilder setEmail(String email)throws ValidationException{
            if (email.equals(""))
                throw new ValidationException("Email cannot be empty");
            else{
                this.email=email;
                return this;
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
         * Function to set the player's ID, it is usually set to 0 as the profile is created later.
         * @param profileID The player's ID
         * @return The builder object
         */
        public PlayerBuilder setProfileID(int profileID){
                this.profileID=profileID;
                return this;
        }

        /**
         * Function to set the player's status. set to False by default.
         * @param isAssignedToSquad The player's status
         * @return The builder object
         */
        public PlayerBuilder setIsAssignedToSquad(boolean isAssignedToSquad){
                this.isAssignedToSquad=isAssignedToSquad;
                return this;
        }

        /**
         * This function build the Player object and returns it to the caller.
         * @return The player object
         * @throws ValidationException
         */
        public Player Builder() throws ValidationException
        {

            this.isAssignedToSquad=false;
            this.profileID=0;
            this.playerID=0;
            return new Player(this);


        }

    }

    @Override
    public String toString() {
        return "Player{" +
                "firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", scrumsNumber=" + scrumsNumber +
                ", playerID=" + playerID +
                ", doctorID=" + doctorID +
                ", kinID=" + kinID +
                ", profileID=" + profileID +
                ", isAssignedToSquad=" + isAssignedToSquad +
                '}';
    }

    public void setFirstName(String firstName) throws ValidationException{
        if (firstName.equals("")){
            throw new ValidationException("Name cannot be empty");
        }
        else{
            this.firstName=firstName;
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
            this.surname=surname;
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
            this.telephone=telephone;
        }
    }
    /**
     * Function to set the player's email
     * @param email The player's email
     * @throws ValidationException Email cannot be empty
     */
    public void setEmail(String email)throws ValidationException{
        if (email.equals(""))
            throw new ValidationException("Email cannot be empty");
        else{
            this.email=email;
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
    public void setProfileID(int profileID){
        this.profileID=profileID;
    }

    /**
     * Function to set the player's status. set to False by default.
     * @param isAssignedToSquad The player's status
     */
    public void setIsAssignedToSquad(boolean isAssignedToSquad){
        this.isAssignedToSquad=isAssignedToSquad;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public int getScrumsNumber() {
        return scrumsNumber;
    }

    public int getPlayerID() {
        return playerID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public int getKinID() {
        return kinID;
    }

    public int getProfileID() {
        return profileID;
    }

    public boolean isAssignedToSquad() {
        return isAssignedToSquad;
    }

    // END OF CLASS
}
