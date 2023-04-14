package com.application.simplyrugby.Control;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

public class AddGameController {

    @FXML
    private ComboBox cbSquad,cbClub;
    @FXML
    private TextField txClub,txTelephone;
    @FXML
    private TextArea taAddress;
    @FXML
    private RadioButton rbHome,rbAway;
    @FXML
    private Button bCreateGame,bCancel;
    @FXML
    private Pane firstPane,secondPane;

    public void initialize(){

        firstPane.getStyleClass().add("bckg2");
        secondPane.getStyleClass().add("bckg3");
        bCancel.getStyleClass().add("bckg5");
        bCreateGame.getStyleClass().add("bckg5");

        ToggleGroup group=new ToggleGroup();
        rbHome.setToggleGroup(group);
        rbAway.setToggleGroup(group);

        bCancel.setOnAction((event->{

            firstPane.getChildren().clear();
            firstPane.getStyleClass().add("bckg1");
        }));
    }




}
