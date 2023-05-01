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

    public static void main(String[] args) {
        DBTools.databaseConnect();

        System.out.println("Squad :" +DBTools.getPlayerSquadID(30));


}
}
