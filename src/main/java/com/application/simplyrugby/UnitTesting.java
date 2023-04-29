package com.application.simplyrugby;

import com.application.simplyrugby.System.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UnitTesting {
    public class SimplyRugbySystem extends Application {
        @Override
        public void start(Stage stage) throws IOException {
            // Database creation object will test DB presence and create it if it doesn't exist
            DatabaseCreation db = new DatabaseCreation();
            // load the main menu fxml file
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/application/simplyrugby/main_menu.fxml"));
            // set the scene for the menu
            Scene scene = new Scene(fxmlLoader.load(), 449, 589);
            // add a css style file to use with the main menu window
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/application/simplyrugby/styles.css"), "CSS file not found").toExternalForm());
            // add the logo as an icon to the application.
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/com/application/simplyrugby/logo.png")));
            // add a title to the application window
            stage.setTitle("Simply Rugby Club: Main Menu");
            // set the scene to the stage
            stage.setScene(scene);
            // show the main menu window.
            stage.show();
        }
    }
    public static void main(String[] args) {
        DBTools.databaseConnect();

        try (
                Connection connection= ConnectionPooling.getDataSource().getConnection();
                QueryResult queryResult = DBTools.executeSelectQuery("SELECT name FROM games")
                )
        {
            ResultSet rs=queryResult.getResultSet();
            while(rs.next())
                System.out.println("Clubs: "+rs.getString(1));
        }
        catch (ValidationException | SQLException e){
            e.printStackTrace();
        }


}
}
