package com.application.simplyrugby.System;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

public class CustomAlert extends Stage {

    private Label lTitle;
    private Label lMessage;
    private Button okButton;

    public CustomAlert(String title,String message){

        lTitle=new Label(title);
        lMessage=new Label(message);
        okButton=new Button("OK");


        HBox hbox=new HBox(okButton);
        hbox.setAlignment(Pos.CENTER);
        VBox vbox=new VBox(10,lTitle,lMessage,hbox);
        vbox.setPadding(new Insets(0,20,0,20));

        Scene scene=new Scene(vbox);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css"), "CSS not found").toExternalForm());
        vbox.getStyleClass().add("bckg3");
        //vbox.setAlignment(Pos.CENTER);
        okButton.getStyleClass().add("bckg5");
        lTitle.getStyleClass().add("whiteText");
        lTitle.setMinSize(15,15);
        lMessage.getStyleClass().add("whiteText");
        this.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        //setMinWidth(400);
        setMinHeight(150);

        okButton.setOnAction((event->{
            Stage stage=(Stage) okButton.getScene().getWindow();
            stage.close();
        }));
        setScene(scene);

    }

    @Override
    public void showAndWait() {
        super.showAndWait();
    }
}
