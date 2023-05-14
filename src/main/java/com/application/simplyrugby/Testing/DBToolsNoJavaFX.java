package com.application.simplyrugby.Testing;
import com.application.simplyrugby.Model.*;
import com.application.simplyrugby.System.ConnectionPooling;
import com.application.simplyrugby.System.CustomAlert;
import com.application.simplyrugby.System.QueryResult;
import com.application.simplyrugby.System.ValidationException;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Class duplicating methods from DBTools for automated testing.
 * This was necessary as testNG was throwing errors with javafx dialogs
 */
public class DBToolsNoJavaFX {

    public static boolean executeUpdateQuery(String query){

        // try with the connection and statement as resources
        try(
                Connection connect= ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement=connect.prepareStatement(query)
        )
        {
            // execute the query
            statement.executeUpdate();
            // return successful
            return true;}
        catch (SQLException e){
            e.printStackTrace();
            return false;}
    }

    public static QueryResult executeSelectQuery(String query) {

        try {
            // connecting to the database.
            Connection connection=ConnectionPooling.getDataSource().getConnection();
            PreparedStatement statement = connection.prepareStatement(query);
            //
            ResultSet rs = statement.executeQuery();
            return new QueryResult(rs,connection,statement);
        } catch (SQLException e) {
            return null;
        }
        //return null;
    }

    public static Member loadMember(Member member,int memberID){

        // if the record to search for is for a player:
        if (member instanceof Player){

            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement("SELECT player_id,first_name,surname,address,date_of_birth,gender,telephone" +
                            ",email,scrums_number,is_assigned_to_squad,doctor_id,kin_id FROM players WHERE player_id=?")

            )
            {
                // the ResultSet returned by the query is used to create the object that is sent back to the calling class.
                statement.setInt(1,memberID);

                try(ResultSet rs=statement.executeQuery())
                {
                    return new Player.PlayerBuilder().setPlayerID(rs.getInt(1)).setFirstName(rs.getString(2)).setSurname(rs.getString(3))
                            .setAddress(rs.getString(4)).setDoB(rs.getString(5)).setGender(rs.getString(6)).setTelephone(rs.getString(7))
                            .setEmail(rs.getString(8)).setScrumsNumber(rs.getInt(9)).setIsAssignedToSquad(rs.getString(10)).setDoctorID(rs.getInt(11))
                            .setKinID(rs.getInt(12)).Builder();
                }catch (SQLException e){
                    e.printStackTrace();
                    return null;
                }
            }
            catch (SQLException | ValidationException e){
                e.printStackTrace();
                return null;}
        }
        // if the record to search for is a NonPlayer.
        if (member instanceof NonPlayer){
            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement("SELECT member_id,first_name,surname,address,telephone" +
                            ",email,role_id FROM non_players WHERE member_id=?")
            )
            {
                statement.setInt(1,memberID);
                try(ResultSet rs=statement.executeQuery())
                {
                    return new NonPlayer(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4)
                            ,rs.getString(5),rs.getString(6),rs.getInt(7));
                }catch (SQLException e){
                    e.printStackTrace();
                    return null;
                }
            }
            catch (SQLException | ValidationException e){
                e.printStackTrace();
                return null;
            }
        }
        // not used.
        return null;
    }





    public static boolean memberExists(Member member){
        if (member instanceof Player pl){
            try(
                    QueryResult qs=executeSelectQuery("SELECT player_id FROM players WHERE first_name='"+pl.getFirstName()+"' " +
                            "AND surname='"+pl.getSurname()+"' AND date_of_birth='"+pl.getDateOfBirth()+"'")
            )
            {
                return qs.getResultSet().next();

            }catch (ValidationException|SQLException e){
                return false;
            }
        }
        else if (member instanceof NonPlayer npl){

            try(
                    QueryResult qs=executeSelectQuery("SELECT member_id FROM non_players WHERE first_name='"+npl.getFirstName()+"' " +
                            "AND surname='"+npl.getSurname()+"' AND telephone='"+npl.getTelephone()+"'")
            )
            {
                return qs.getResultSet().next();

            }catch (ValidationException|SQLException e){
                return false;
            }
        }
        return false;
    }
}
