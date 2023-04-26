package com.application.simplyrugby.Control;

import com.application.simplyrugby.Model.JuniorSquad;
import com.application.simplyrugby.Model.SeniorSquad;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.ObsListFactory;
import com.application.simplyrugby.System.ValidationException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This controller manages the Add Game function.
 */
public class AddGameController {

    @FXML
    private ComboBox cbSquadSenior,cbSquadJunior,cbClub;
    @FXML
    private TextField txClub,txTelephone;
    @FXML
    private TextArea taAddress;
    @FXML
    private RadioButton rbHome,rbAway;
    @FXML
    private Button bCreateGame,bCancel,bCreateClub;
    @FXML
    private Pane firstPane,secondPane;

    /**
     * Method to initialise the add Game panel
     */
    public void initialize(){
        // setting the items styles
        firstPane.getStyleClass().add("bckg2");
        secondPane.getStyleClass().add("bckg3");
        bCancel.getStyleClass().add("bckg5");
        bCreateGame.getStyleClass().add("bckg5");
        bCreateClub.getStyleClass().add("bckg5");
        cbClub.getStyleClass().add("bckg5");
        cbSquadSenior.getStyleClass().add("bckg5");
        cbSquadJunior.getStyleClass().add("bckg5");


        LocalDate date;
        DateTimeFormatter dt=DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // creating a Toggle group for the location radio buttons, so only one value can be selected
        ToggleGroup group=new ToggleGroup();
        rbHome.setToggleGroup(group);
        rbAway.setToggleGroup(group);

        // Adding the values to the ComboBoxes
        cbSquadJunior.setItems(ObsListFactory.createObsList(new JuniorSquad()));
        cbSquadSenior.setItems(ObsListFactory.createObsList(new SeniorSquad()));
        cbClub.setItems(ObsListFactory.createObsList("Clubs"));

        // if a club is selected, the fields to create one disappear
        cbClub.setOnAction((event)->{

            if (cbClub.getSelectionModel().getSelectedIndex()==0){
                txClub.setVisible(true);
                txTelephone.setVisible(true);
                taAddress.setVisible(true);
                bCreateClub.setVisible(true);
            }
            else{
                txTelephone.setVisible(false);
                txClub.setVisible(false);
                taAddress.setVisible(false);
                bCreateClub.setVisible(false);
            }
        });
        // if a junior squad is selected, the senior squad combobox disappear
        cbSquadJunior.setOnAction((event)->{
            if (cbSquadJunior.getSelectionModel().getSelectedIndex()==0)
                cbSquadSenior.setVisible(true);
            else
                cbSquadSenior.setVisible(false);
        });
        // if a senior squad is selected, the senior squad combobox disappear
        cbSquadSenior.setOnAction((event)->{
            if (cbSquadSenior.getSelectionModel().getSelectedIndex()==0)
                cbSquadJunior.setVisible(true);
            else
                cbSquadJunior.setVisible(false);
        });

        // here, once the create Game button is pressed, we will gather and test the data collected in the form
        bCreateGame.setOnAction((event)->{
            // try to catch any errors and display a error message if any.
            try{
                // we check that a team has been selected.
                if (cbSquadSenior.getSelectionModel().getSelectedIndex()==0 && cbSquadJunior.getSelectionModel().getSelectedIndex()==0)
                    throw new ValidationException("A Junior or Senior squad must be selected to create a game.");
                // if a Senior is selected.
                else if (cbSquadSenior.getSelectionModel().getSelectedIndex()==1 && cbSquadJunior.getSelectionModel().getSelectedIndex()==0){

                }
            }catch(ValidationException e){
                CustomAlert alert=new CustomAlert("No Squad Selected",e.getMessage());
            }



        });


        bCancel.setOnAction((event->{

            firstPane.getChildren().clear();
            firstPane.getStyleClass().add("bckg1");
        }));
    }




}
