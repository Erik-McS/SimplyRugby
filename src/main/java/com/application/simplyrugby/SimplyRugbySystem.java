package com.application.simplyrugby;

import com.application.simplyrugby.System.DatabaseCreation;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;
import java.util.Objects;

/**
 * This is the entry point of the program. this will launch the main menu window
 * @author Erik McSeveney
 */
public class SimplyRugbySystem extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Database creation object will test DB presence and create it if it doesn't exist
        DatabaseCreation db=new DatabaseCreation();
        // load the main menu fxml file
        FXMLLoader fxmlLoader = new FXMLLoader(SimplyRugbySystem.class.getResource("main_menu.fxml"));
        // set the scene for the menu
        Scene scene = new Scene(fxmlLoader.load(), 449, 589);
        // stage.initStyle(StageStyle.UNDECORATED);
        // add a css style file to use with the main menu window
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styles.css"),"CSS file not found").toExternalForm());
        // add the logo as an icon to the application.
        stage.getIcons().add(new Image(getClass().getResourceAsStream("logo.png")));
        // add a title to the application window
        stage.setTitle("Simply Rugby Club: Main Menu");
        // set the scene to the stage
        stage.setScene(scene);
        // show the main menu window.
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}