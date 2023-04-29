package com.application.simplyrugby.Control;

import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.Model.Doctor;
import com.application.simplyrugby.Model.NextOfKin;
import com.application.simplyrugby.Model.Player;
import com.application.simplyrugby.System.ObsListFactory;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ValidationException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.io.File;

/**
 * This class manages the Add Member function.it allows adding a player, its next of kin and doctor.<br>
 * it is linked to the addPlayer.fxml file.
 * @author Erik McSeveney
 */
public class AddPlayerController {

    // we declare here the fmxl elements so that they can be manipulated by the controller.
    @FXML
    private Pane mainPane,leftPane,nokPane,docPane;
    @FXML
    private TextField txName,txSurname,txTelephone,txEmail,txScrumsNumber,txNameNOK,txSurnameNOK,txTelNOK,
            txNameDoctor,txSurnameDoctor,txTelDoctor;
    @FXML
    private DatePicker dpDateOfBirth;
    @FXML
    private RadioButton rbMale,rbFemale;
    @FXML
    private Button bCreatePlayer,bCancel,bAddConsentForm;
    @FXML
    private ComboBox<String> cbNOK,cbDoctor;
    @FXML
    private Button tbAddNOK,tbCreateNOK,tbAddDoctor,tbCreateDoctor;
    @FXML
    private TextArea txAddress;
    @FXML
    private Label lNameNoK,lSurnameNoK,lTelNoK,lTitleNOK,lTitleDoc,lNameDoc,lSurnameDoc,lTelDoc,nokSelect,docSelect;
    private NextOfKin nok1;
    private Doctor doc1;
    @FXML
    boolean nokSelected,docSelected;
    private boolean nokListChanged=false;
    private boolean docListChanged=false;
    // image object to get the consentform
    private Image image;
    private int age;

    /**
     * the initialize method set the style of the window/pane.<br>
     * it uses the styles.css file to color the background and buttons.<br>
     * all events handlers use lambda functions.
     */
    public void initialize(){
        nok1=new NextOfKin();
        doc1=new Doctor();

        // set Pane background color
        mainPane.getStyleClass().addAll("bckg5");
        leftPane.getStyleClass().addAll("bckg2","borderBlack");
        nokPane.getStyleClass().addAll("bckg3","borderBlack");
        docPane.getStyleClass().addAll("bckg4","borderBlack");
        // set Buttons style.
        bCreatePlayer.getStyleClass().add("bckg5");
        bCancel.getStyleClass().add("bckg5");
        bAddConsentForm.getStyleClass().add("bckg5");
        tbAddDoctor.getStyleClass().add("bckg5");
        tbAddNOK.getStyleClass().add("bckg5");
        tbAddNOK.setVisible(false);
        tbCreateDoctor.getStyleClass().add("bckg5");
        tbAddDoctor.setVisible(false);
        tbCreateNOK.getStyleClass().add("bckg5");
        // set a group for the radio buttons so only one can be checked.
        ToggleGroup group=new ToggleGroup();
        rbFemale.setToggleGroup(group);
        rbMale.setToggleGroup(group);
        // styling the ComboBoxes
        cbNOK.getStyleClass().add("bckg5");
        cbDoctor.getStyleClass().add("bckg5");
        dpDateOfBirth.getStyleClass().add("bckg5");
        bAddConsentForm.setVisible(false);
        // add a function to clear the main pane
        bCancel.setOnAction((event)->{
            mainPane.getChildren().clear();
        });

        // we add the content of the Next of Kin table to the comboBox
        cbNOK.setItems(ObsListFactory.createObsList(new NextOfKin()));
        cbNOK.getSelectionModel().select(0);

        // we are listening to the ComboBoxes to see if an entry is selected.
        // if so, we hide the textfields used to create a NoK
        // if the user selects the first item again, we display back the fields to create a NoK.
        cbNOK.setOnAction((event)->{

            if (cbNOK.getSelectionModel().getSelectedIndex()!=0){
                tbAddNOK.setVisible(true);
                txNameNOK.setVisible(false);
                txSurnameNOK.setVisible(false);
                txTelNOK.setVisible(false);
                lNameNoK.setVisible(false);
                lSurnameNoK.setVisible(false);
                lTelNoK.setVisible(false);
                tbCreateNOK.setVisible(false);
                lTitleNOK.setVisible(false);
                nokListChanged=true;
            }
            else {
                if (nokListChanged)
                    tbAddNOK.setVisible(false);
                txNameNOK.setVisible(true);
                txSurnameNOK.setVisible(true);
                txTelNOK.setVisible(true);
                lNameNoK.setVisible(true);
                lSurnameNoK.setVisible(true);
                lTelNoK.setVisible(true);
                tbCreateNOK.setVisible(true);
                lTitleNOK.setVisible(true);
                nokListChanged=false;
            }
        });
        // same process/logic for the Doctors comboBox

        cbDoctor.setItems(ObsListFactory.createObsList(new Doctor()));
        cbDoctor.getSelectionModel().select(0);

        cbDoctor.setOnAction((event->{

            if (cbDoctor.getSelectionModel().getSelectedIndex()!=0){
                tbAddDoctor.setVisible(true);
                txNameDoctor.setVisible(false);
                txSurnameDoctor.setVisible(false);
                txTelDoctor.setVisible(false);
                lTitleDoc.setVisible(false);
                lNameDoc.setVisible(false);
                lSurnameDoc.setVisible(false);
                lTelDoc.setVisible(false);
                tbCreateDoctor.setVisible(false);
                docListChanged=true;
            }
            else{
                if (docListChanged)
                    tbAddDoctor.setVisible(false);
                txNameDoctor.setVisible(true);
                txSurnameDoctor.setVisible(true);
                txTelDoctor.setVisible(true);
                lTitleDoc.setVisible(true);
                lNameDoc.setVisible(true);
                lSurnameDoc.setVisible(true);
                lTelDoc.setVisible(true);
                tbCreateDoctor.setVisible(true);
                docListChanged=false;

            }
        }));

        /* this is the event for adding a Doctor from the ComboBox
        as the items were added in order from the database, and the item at index 0 is the title, the index of a field is the primary key of the item
        in the database.
        */
        tbAddNOK.setOnAction((event)->{

                // getting the NoK object from the database.
                nok1=(NextOfKin) nok1.loadContact(cbNOK.getSelectionModel().getSelectedIndex());
                // displaying a confirmation
                lTitleNOK.setText("This Next of Kin is added to the player profile");
                lTitleNOK.setVisible(true);
                // hiding all the other fields
                displayOfNOKFields(0);
                // setting a fla to confirn a NoK has been selected.
                nokSelected=true;
        });
        /*
        This is the event for the createNoK button.
        It will assign the values from the fields to the Doctor object, insert it
        in the database, then get the correct doctor ID from it.
         */
        tbCreateNOK.setOnAction((event)->{
            // adding the data from the textfields to the NoK object
            nok1.setFirstName(txNameNOK.getText());
            nok1.setSurname(txSurnameNOK.getText());
            nok1.setTelephone(txTelNOK.getText());
            // saving the record in the database, if ok, will set the NoK flag to true.
            nokSelected=nok1.saveContact();
            // we are recreating the NoK object by getting the record from the database, this will allow us to get the correct nok_ID assigned by the database to the new record.
            nok1=(NextOfKin) DBTools.selectContact(nok1,"SELECT kin_id,name,surname,telephone FROM next_of_kin WHERE name='"+txNameNOK.getText()+"' AND surname='"+txSurnameNOK.getText()+"'");
            // displaying a confirmation that the NoK record is now selected
            lTitleNOK.setText("This Next of Kin is added to the player profile");
            // hiding all the fields.
            displayOfNOKFields(0);
        });

        /* this is the event for adding a Doctor from the ComboBox
        as the items were added in order from the database, and the item at index 0 is the title, the index of a field is the primary key of the item
        in the database.
        */
        tbAddDoctor.setOnAction((event)->{
            // getting the Doctor record from the database using the combobox index
            doc1=(Doctor) doc1.loadContact(cbDoctor.getSelectionModel().getSelectedIndex());
            // confirmation message
            lTitleDoc.setText("This Doctor has been added to the player profile");
            lTitleDoc.setVisible(true);
            // setting the doctor flag to true
            docSelected=true;
            displayOfDocFields(0);
        });
        /*
        This is the event for the createNoK button.
        It will assign the values from the fields to the Doctor object, insert it
        in the database, then get the correct doctor ID from it.
        */
        tbCreateDoctor.setOnAction((event->{
            // getting the info from the textfields
            doc1.setFirstName(txNameDoctor.getText());
            doc1.setSurname(txSurnameDoctor.getText());
            doc1.setTelephone(txTelDoctor.getText());
            // inserting the record in a database.
            docSelected= doc1.saveContact();
            doc1=(Doctor) DBTools.selectContact(doc1,"SELECT doctor_id,name,surname,telephone FROM player_doctors WHERE name='"+txNameDoctor.getText()+"' AND surname='"+txSurnameDoctor.getText()+"'");
            // display confirmation
            lTitleDoc.setText("This Doctor has been added to the player profile");
            displayOfDocFields(0);
        }));

        bAddConsentForm.setOnAction((event)->{
            // using a filechooser
            // https://docs.oracle.com/javafx/2/ui_controls/file-chooser.htm
            // creating a filechooser to get the consent form picture
            FileChooser consentForm=new FileChooser();
            consentForm.setTitle("Select the Consent form picture");
            // setting the allowed files format
           consentForm.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images","*.png","*.jpg","*.gif"));
           // getting the consentform image

            File consentFormImage=consentForm.showOpenDialog((Stage)bAddConsentForm.getScene().getWindow());

            if (consentFormImage!=null){
                image=new Image(consentFormImage.toURI().toString());
            }
        });
        // checking the date of birth value
        dpDateOfBirth.setOnAction((event)->{
            try {

                // getting today's date.
                LocalDate today = LocalDate.now();
                age = Period.between(dpDateOfBirth.getValue(), today).getYears();
                // making sure the player is old enough
                if (age <= 7)
                    throw new ValidationException("The player must be at least 7 years old, please check the selected birthdate.");
                else if (age <= 17) {
                    bAddConsentForm.setVisible(true);
                    CustomAlert alert = new CustomAlert("Consent form needed, the player is under 18 years old.", "Please make sure to upload the player's consent form");
                    alert.showAndWait();
                }
                else
                    bAddConsentForm.setVisible(false);

            }catch(ValidationException e){
                CustomAlert alert=new CustomAlert("DoB Error",e.getMessage());
                alert.showAndWait();
                }
        });
        bCreatePlayer.setOnAction((event)->{
            Player nPlayer;

            boolean formIsValid=false;
            String gender="";
            LocalDate date;
            DateTimeFormatter dt=DateTimeFormatter.ofPattern("dd/MM/yyyy");

            try {
                if (group.getSelectedToggle()!=null){
                    RadioButton selected=(RadioButton) group.getSelectedToggle();
                    gender=selected.getText();
                }
                else {
                    throw new ValidationException("Please select the player's Gender");

                }
            }catch (ValidationException e){
                nPlayer=null;
                CustomAlert alert=new CustomAlert("Error ",e.getMessage());
                e.printStackTrace();
                alert.showAndWait();
            }

            try {
                // if a DoB is selected
                if (dpDateOfBirth.getValue()!=null) {
                    // if the NoK flag is set to false, throw an exception
                    if (!nokSelected)
                        throw new ValidationException("A Next of Kin must be assigned to the player");
                    // if the Doctor flag is set to false, throw an exception
                    else if (!docSelected)
                        throw new ValidationException("A Doctor must be assigned to the player.");
                    else if(txScrumsNumber.getText()==null)
                        throw new ValidationException("The SCRUMS number cannot be empty.");
                    // if all ok, we get the remaining data and create the full player record
                    else {
                        // getting the DoB and calculating the age
                        // https://www.w3schools.blog/java-period-class
                        date = dpDateOfBirth.getValue();

                        /* creating the Player object. data validation is done by the Player class
                        if any issue with the data, it will throw a ValidationException and an error message
                         */
                        nPlayer=new Player.PlayerBuilder().setPlayerID(0).setFirstName(txName.getText()).setSurname(txSurname.getText()).setAddress(txAddress.getText())
                                .setDoB(date.format(dt)).setEmail(txEmail.getText()).setGender(gender).setScrumsNumber(Integer.parseInt(txScrumsNumber.getText()))
                                .setTelephone(txTelephone.getText()).setIsAssignedToSquad("NO").setDoctorID(doc1.getDoctorID()).setKinID(nok1.getKinID()).Builder();
                        // if all fine, we set the form flag to true, meaning the all the data is correct and captured for the next step
                        formIsValid=true;
                    }
                }
                // if the DoB is null, throw an exception
                else
                    throw new ValidationException("Date of Birth cannot be empty");
                }
            // any exception during data and form validation will be caught and display in an alert window.
            catch (NumberFormatException|ValidationException e){
                    nPlayer=null;
                    /*
                    testing whih exception is raised. the NumberFormatException is raised by the SCRUMS number text field.
                     */
                    if (e instanceof NumberFormatException){
                        // creating a custom alert.
                        CustomAlert alert=new CustomAlert("Error","Invalid or empty SCRUMS number");
                        alert.showAndWait();
                    }
                    if (e instanceof ValidationException){
                        // creating a custom alert.
                        CustomAlert alert=new CustomAlert("Error",e.getMessage());
                        alert.showAndWait();
                    }
                }
                // if the form is validated, prepare and create here the validation window, where the user can view and confirm the Player record creation
                if (formIsValid) {
                    try {
                        // getting the view
                        FXMLLoader loader1 = new FXMLLoader(getClass().getResource("/com/application/simplyrugby/newPlayerConfirm.fxml"));
                        Parent root = loader1.load();
                        /*
                        Here I am using a technique to pass 3 objects to the confirmation windows: Player, NextOfKin and Doctor.
                        This is done by getting the controller of the next view and using a method defined in it to pass those objects to it.
                        The new window will be able to use those objects to display their content to the user.
                        sources:
                        https://stackoverflow.com/questions/14187963/passing-parameters-javafx-fxml
                        https://genuinecoder.com/javafx-communication-between-controllers/
                         */

                        newPlayerConfirmController newPlayerConfirmController = loader1.getController();
                        newPlayerConfirmController.receivePlayerObjects(nPlayer,nok1,doc1,image,age);

                        // setting the next view
                        Scene scene = new Scene(root);
                        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"), "CSS not found").toExternalForm());
                        Stage stage = new Stage();
                        stage.setTitle("Confirm Player Details");
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
                        stage.setScene(scene);
                        stage.showAndWait();
                        mainPane.getChildren().clear();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        });
    }

    // methods to hide or show the NoK and Doctor fields.
    private void displayOfNOKFields(int toggle){

        if (toggle==0){
            nokSelect.setVisible(false);
            tbAddNOK.setVisible(false);
            tbCreateNOK.setVisible(false);
            cbNOK.setVisible(false);
            txNameNOK.setVisible(false);
            txSurnameNOK.setVisible(false);
            txTelNOK.setVisible(false);
            lNameNoK.setVisible(false);
            lSurnameNoK.setVisible(false);
            lTelNoK.setVisible(false);
            tbCreateNOK.setVisible(false);


        }
        else if (toggle==1){
            nokSelect.setVisible(true);
            tbCreateNOK.setVisible(true);
            tbAddNOK.setVisible(true);
            cbNOK.setVisible(true);
            txNameNOK.setVisible(true);
            txSurnameNOK.setVisible(true);
            txTelNOK.setVisible(true);
            lNameNoK.setVisible(true);
            lSurnameNoK.setVisible(true);
            lTelNoK.setVisible(true);
            tbCreateNOK.setVisible(true);
            lTitleNOK.setVisible(true);
            nokListChanged=false;
        }
    }
    private void displayOfDocFields(int toggle){
        if (toggle==0){
            docSelect.setVisible(false);
            tbAddDoctor.setVisible(false);
            tbCreateDoctor.setVisible(false);
            cbDoctor.setVisible(false);
            txNameDoctor.setVisible(false);
            txSurnameDoctor.setVisible(false);
            txTelDoctor.setVisible(false);

            lNameDoc.setVisible(false);
            lSurnameDoc.setVisible(false);
            lTelDoc.setVisible(false);
            tbCreateDoctor.setVisible(false);

        }
        else if (toggle==1){
            tbAddDoctor.setVisible(true);
            tbCreateDoctor.setVisible(true);
            cbDoctor.setVisible(true);
            txNameDoctor.setVisible(true);
            txSurnameDoctor.setVisible(true);
            txTelDoctor.setVisible(true);
            lTitleDoc.setVisible(true);
            lNameDoc.setVisible(true);
            lSurnameDoc.setVisible(true);
            lTelDoc.setVisible(true);
            tbCreateDoctor.setVisible(true);
            docSelect.setVisible(true);
        }
    }


// END OF CLASS
}
