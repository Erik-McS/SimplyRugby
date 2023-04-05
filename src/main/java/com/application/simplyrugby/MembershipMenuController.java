package com.application.simplyrugby;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class MembershipMenuController {

    @FXML
    Button addPlayer,deletePlayer,addMember,deleteMember,mainMenu;
    @FXML
    Pane menuPane,mainPane;
    public void initialize(){

        menuPane.getStyleClass().add("bckg1");
        mainPane.getStyleClass().add("bckg1");
        addPlayer.getStyleClass().add("bckg5");
        deletePlayer.getStyleClass().add("bckg5");
        addMember.getStyleClass().add("bckg5");
        deleteMember.getStyleClass().add("bckg5");
        mainMenu.getStyleClass().add("bckg5");

    }
}
