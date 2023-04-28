package com.application.simplyrugby.System;

import com.application.simplyrugby.Model.Game;
import com.application.simplyrugby.Model.JuniorSquad;
import com.application.simplyrugby.Model.SeniorSquad;

import java.sql.*;

public class TempPaste {


    public static void saveGame(Game game) {
        /*

        databaseConnect();
        try(
                Connection connection1 = DriverManager.getConnection(DBURL);
                PreparedStatement statement1=connection1.prepareStatement("INSERT INTO games (date,club_id,location_id) VALUES (?,?,?)");

        ){

            // testing the squad object
            if (game.getSquad()==null)
                throw new ValidationException("The Squad object cannot be empty");
            else if (game.getSquad() instanceof SeniorSquad){

                // we will need to first insert the game in the Table Games, then retrieve the ID that has been created,
                // so we can use it to save the game with its squad in the correct Junior or senior Table
                //https://stackoverflow.com/questions/2127138/how-to-retrieve-the-last-autoincremented-id-from-a-sqlite-table

                // preparing the query
                statement1.setString(1, game.getDate());
                statement1.setInt(2,game.getPlayingClub().getClub_id());
                statement1.setInt(3,game.getLocation());

                // getting the number of rows created, in theory, 1.
                int check;
                check= statement1.executeUpdate();

                if (check!=0){
                    // getting the game_id just created
                    ResultSet rs=statement1.executeQuery("SELECT MAX(game_id) FROM games LIMIT 1");
                    int gameID=rs.getInt(1);
                    // casting the squad as a SeniorSquad object.
                    SeniorSquad sn=(SeniorSquad) game.getSquad();
                    // preparing and executing the insert query in senior_games_played.
                    //databaseConnect();
                    statement= connection.prepareStatement("INSERT INTO senior_games_played (squad_id,game_id,date) VALUES (?,?,?)");
                    statement.setInt(1,getID("SELECT squad_ID FROM senior_squads WHERE squad_name='"+sn.getSquadName()+"'"));
                    statement.setInt(2,gameID);
                    statement.setString(3,game.getDate());
                    // inserting and checking if ok.
                    check= statement.executeUpdate();
                    closeConnections();
                    if (check==0)
                        throw new ValidationException("The Squad/Game couldn't be created");
                }
            }
            // otherwise, it's a junior squad.
            else{
                statement=connection1.prepareStatement("INSERT INTO games (date,club_id,location_id) VALUES (?,?,?)");
                statement.setString(1, game.getDate());
                statement.setInt(2,game.getPlayingClub().getClub_id());
                statement.setInt(3,game.getLocation());
                // getting the number of rows created, in theory, 1.
                int check= statement.executeUpdate();
                if (check!=0) {
                    // getting the game_id just created
                    ResultSet rs = statement.executeQuery("SELECT MAX(game_id) FROM games LIMIT 1");
                    int gameID = rs.getInt(1);
                    // casting the squad as a SeniorSquad object.
                    JuniorSquad sn = (JuniorSquad) game.getSquad();
                    // preparing and executing the insert query in senior_games_played.
                    statement = connection.prepareStatement("INSERT INTO junior_games_played (squad_id,game_id,date) VALUES (?,?,?)");
                    statement.setInt(1, getID("SELECT squad_ID FROM senior_squads WHERE squad_name='" + sn.getSquadName() + "'"));
                    statement.setInt(2, gameID);
                    statement.setString(3, game.getDate());
                    // inserting and checking if ok.
                    int check2 = statement.executeUpdate();
                    if (check2 == 0)
                        throw new ValidationException("The Squad/Game couldn't be created");
                }
            }
        }catch (ValidationException | SQLException e)   {
            CustomAlert alert=new CustomAlert("Save Game Error:", e.getMessage());
            e.printStackTrace();
        }finally {
            closeConnections();
        }
    }

    */
    }
}