package com.application.simplyrugby.Testing;
import com.application.simplyrugby.Model.*;
import com.application.simplyrugby.System.*;

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

    public static ThirdParty selectContact(ThirdParty tp,String query){

        if (tp instanceof NextOfKin nok){
            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet rs = statement.executeQuery()
            )
            {
                // using the ResultSet to populate the NoK object and sending it back
                nok.setKinID(rs.getInt(1));
                nok.setFirstName(rs.getString(2));
                nok.setSurname(rs.getString(3));
                nok.setTelephone(rs.getString(4));
                return nok;
            } catch (ValidationException|SQLException e) {
                return null;
            }
        }
        // same process if the record is for a doctor.
        if (tp instanceof Doctor doc){
            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement(query);
                    ResultSet rs= statement.executeQuery()
            )
            {
                doc.setDoctorID(rs.getInt(1));
                doc.setFirstName(rs.getString(2));
                doc.setSurname(rs.getString(3));
                doc.setTelephone(rs.getString(4));
                return doc;
            }catch (ValidationException| SQLException e){
                return null;
            }
        }
        //not used.
        return null;
    }


    public static ThirdParty selectContact(ThirdParty tp,int index){

        if (tp instanceof NextOfKin nok){

            try(
                    Connection connection=ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement("SELECT kin_id,name,surname,telephone FROM next_of_kin WHERE kin_id=?")
            )
            {
                statement.setInt(1,index);
                try(ResultSet rs = statement.executeQuery())
                {
                    nok.setKinID(rs.getInt(1));
                    nok.setFirstName(rs.getString(2));
                    nok.setSurname(rs.getString(3));
                    nok.setTelephone(rs.getString(4));
                }catch (SQLException e){
                    return null;
                }
                return nok;
            } catch (ValidationException| SQLException e) {
                return null;
            }
        }
        if (tp instanceof Doctor doc){
            try(
                    Connection connection =ConnectionPooling.getDataSource().getConnection();
                    PreparedStatement statement = connection.prepareStatement("SELECT doctor_id,name,surname,telephone FROM player_doctors WHERE doctor_id=?")
            )
            {
                statement.setInt(1,index);
                try(
                        ResultSet rs= statement.executeQuery()
                )
                {
                    doc.setDoctorID(rs.getInt(1));
                    doc.setFirstName(rs.getString(2));
                    doc.setSurname(rs.getString(3));
                    doc.setTelephone(rs.getString(4));
                    return doc;
                }catch (ValidationException| SQLException e){
                    return null;
                }
            }catch (SQLException e){
                return null;
            }
        }
        return null;
    }

    public static String getRole(int roleID){
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT role_description FROM non_players_roles WHERE role_id=?")
        )
        {
            statement.setInt(1,roleID);
            try (ResultSet rs=statement.executeQuery())
            {
                return rs.getString(1);
            }
            catch (SQLException e){
                return null;
            }
        }
        catch (SQLException e){
            return null;
        }
    }

    public static Club  getClub(int club_id) {
        // getting the data from the database and creating the object.
        try (
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement = connection.prepareStatement("SELECT name,address,telephone,email FROM clubs WHERE club_id=?")

        ) {
            statement.setInt(1,club_id);
            try(ResultSet rs = statement.executeQuery())
            {
                Club club = new Club(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
                club.setClub_id(club_id);
                return club;
            }
            catch (NullPointerException|SQLException e){
                e.printStackTrace();
                return null;
            }
        }
        // error message if any issues
        catch (ValidationException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Squad loadSquad(Squad squad,int squad_id) throws ValidationException{
        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement1=connection.prepareStatement("SELECT loose_head_prop,hooker,tight_head_prop,second_row,second_row2,blind_side_flanker,open_side_flanker,number_8,scrum_half," +
                        "fly_half,left_wing,inside_centre,outside_center,right_side,full_back FROM senior_squads WHERE squad_id=?");
                PreparedStatement statement2=connection.prepareStatement("SELECT squad_name,cogroup_id,adteam_id,repteam_id FROM senior_squads WHERE squad_id=?");
                PreparedStatement statement3=connection.prepareStatement("SELECT loose_head_prop,hooker,tight_head_prop,scrum_half,fly_half,centre,wing FROM junior_squads WHERE squad_id=?");
                PreparedStatement statement4=connection.prepareStatement("SELECT squad_name,cogroup_id,adteam_id,repteam_id FROM junior_squads WHERE squad_id=?");
                PreparedStatement statement5 = connection.prepareStatement("SELECT player_1,player_2,player_3,player_4,player_5 FROM replacement_team WHERE repteam_ID=?")
        )
        {

            // if we want a SeniorSquad
            if (squad instanceof SeniorSquad){
                // arraylist to contain the players of the squad.
                ArrayList<Player> players =new ArrayList<>();
                // query to get all the players in the squad
                statement1.setInt(1,squad_id);
                try(ResultSet rs1=statement1.executeQuery()){
                    // assigning the players in the arraylist.
                    for(int i=1;i<=15;i++){
                        players.add((Player) loadMember(Player.dummyPlayer(),rs1.getInt(i)));
                    }
                }catch (NullPointerException|SQLException e){
                    e.printStackTrace();
                    return null;
                }

                // getting the squad name, and the IDs of the related teams. they will be used to get the corresponding team objects to create the Senior Squad.
                statement2.setInt(1,squad_id);
                try(ResultSet rs2=statement2.executeQuery())
                {
                    // we then create and return a SeniorSquad object
                    return new SeniorSquad(players,rs2.getString(1),
                            (ReplacementTeam) loadSquad(new ReplacementTeam(),rs2.getInt(4)),
                            (AdminTeam) DBTools.loadTeam(new AdminTeam(),rs2.getInt(3)),
                            (CoachTeam) DBTools.loadTeam(new CoachTeam(),rs2.getInt(3)));
                }catch (NullPointerException|SQLException e){
                   // CustomAlert alert=new CustomAlert("Error Squad creation","Could not load the requested squad");
                    e.printStackTrace();
                    //alert.showAndWait();
                    return null;
                }
            }
            // if we requested a JuniorSquad
            else if(squad instanceof JuniorSquad){
                // arraylist to contain the players of the squad.
                ArrayList<Player> players =new ArrayList<>();
                // assigning the players in the arraylist.
                statement3.setInt(1,squad_id);
                try(ResultSet rs3=statement3.executeQuery())
                {
                    for(int i=1;i<=7;i++){
                        players.add((Player) loadMember(Player.dummyPlayer(),rs3.getInt(i)));
                    }
                }catch (NullPointerException|SQLException e){
                    //CustomAlert alert=new CustomAlert("Error Squad creation","Could not load the requested squad");
                    e.printStackTrace();
                    //alert.showAndWait();
                    return null;
                }
                statement4.setInt(1,squad_id);
                try(ResultSet rs4=statement4.executeQuery())
                {
                    // we then create and return a SeniorSquad object
                    return new JuniorSquad(players,rs4.getString(1),
                            (ReplacementTeam) loadSquad(new ReplacementTeam(),rs4.getInt(4)),
                            (AdminTeam) DBTools.loadTeam(new AdminTeam(),rs4.getInt(3)),
                            (CoachTeam) DBTools.loadTeam(new CoachTeam(),rs4.getInt(3)));
                }catch (NullPointerException|SQLException e){
                    //CustomAlert alert=new CustomAlert("Error Squad creation","Could not load the requested squad");
                    e.printStackTrace();
                   // alert.showAndWait();
                    return null;
                }
            }
            // finally, if we need to get a replacement team. which is a kind of squad as it is composed of playing members.
            else if (squad instanceof ReplacementTeam) {

                ArrayList<Player> players = new ArrayList<>();
                statement5.setInt(1,squad_id);
                try(ResultSet rs5 = statement5.executeQuery())
                {
                    for(int i=1;i<=5;i++){
                        players.add((Player) loadMember(Player.dummyPlayer(),rs5.getInt(i)));
                    }
                    return new ReplacementTeam(players);
                }catch (NullPointerException|SQLException e){
                    //CustomAlert alert=new CustomAlert("Error Squad creation","Could not load the requested squad");
                    e.printStackTrace();
                    //alert.showAndWait();
                    return null;}

            }
        }catch (SQLException e){
            //CustomAlert alert=new CustomAlert("Error Squad creation","Could not load the requested squad");
            e.printStackTrace();
            //alert.showAndWait();
            return null;
        }
        return null;
    }

    public static MemberTeam loadTeam(MemberTeam memberTeam,int team_id){


        try(
                Connection connection=ConnectionPooling.getDataSource().getConnection();
                PreparedStatement statement1= connection.prepareStatement("SELECT chairman,fixture_sec FROM squad_admin_team WHERE adteam_id=?");
                PreparedStatement statement2= connection.prepareStatement("SELECT coach_1,coach_2,coach_3 FROM squad_coaches WHERE cogroup_id=?")
        ){
            // if the requested team is an AdminTeam
            if (memberTeam instanceof AdminTeam){
                statement1.setInt(1,team_id);
                try(ResultSet rs1= statement1.executeQuery())
                {
                    return new AdminTeam((NonPlayer) loadMember(new NonPlayer(),rs1.getInt(1)),(NonPlayer) loadMember(new NonPlayer(),rs1.getInt(2)));
                }catch (NullPointerException|SQLException e){
                    //CustomAlert alert = new CustomAlert("Error Team creation", "Could not load the requested team");
                    e.printStackTrace();
                    //alert.showAndWait();
                    return null;
                }
            }
            // if it's a coach
            if (memberTeam instanceof CoachTeam){
                statement2.setInt(1,team_id);
                try(ResultSet rs2= statement2.executeQuery())
                {
                    return new CoachTeam((NonPlayer) loadMember(new NonPlayer(),rs2.getInt(1)),(NonPlayer) loadMember(new NonPlayer(),rs2.getInt(2)),
                            (NonPlayer) loadMember(new NonPlayer(), rs2.getInt(3)));
                }catch (NullPointerException|SQLException e){
                    //CustomAlert alert = new CustomAlert("Error Team creation", "Could not load the requested team");
                    e.printStackTrace();
                    //alert.showAndWait();
                    return null;
                }
            }
        }catch (ValidationException | SQLException e){
            //CustomAlert alert = new CustomAlert("Error Team creation", "Could not load the requested team");
            e.printStackTrace();
           // alert.showAndWait();
            return null;
        }
        return null;
    }
}
