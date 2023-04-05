package com.application.simplyrugby.Control;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class MainMenuController {

    @FXML
    private ImageView mainLogo;
    @FXML
    private Button memberMgtButton,playerMgntButton,gamesMgntButton,exitButton;
    @FXML
    private AnchorPane mainPane;

    public void initialize(){
        mainPane.getStyleClass().add("bckg1");
        memberMgtButton.getStyleClass().add("bckg5");
        playerMgntButton.getStyleClass().add("bckg5");
        gamesMgntButton.getStyleClass().add("bckg5");
        exitButton.getStyleClass().add("bckg5");

    }

}
