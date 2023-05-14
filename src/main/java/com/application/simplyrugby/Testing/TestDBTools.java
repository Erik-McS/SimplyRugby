package com.application.simplyrugby.Testing;
import com.application.simplyrugby.Model.NonPlayer;
import com.application.simplyrugby.Model.Player;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.QueryResult;
import com.application.simplyrugby.System.ValidationException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDBTools {

    @BeforeTest
    public void init(){
        DBTools.databaseConnect();
    }

    @Test
    public void testCorrectExecuteUpdateQuery(){
        String query="INSERT INTO clubs (name,address,telephone,email)" +
                " VALUES ('AC Cardonald','123 Glasgow','0141112233','cardo@cardo.com')";
        Assert.assertTrue(DBToolsNoJavaFX.executeUpdateQuery(query));
    }

    @Test
    public void testIncorrectExecuteUpdateQuery(){
        String query="INSERT INTO clu (name,address,telephone,email)" +
                " VALUES ('AC Cardonald','123 Glasgow','0141112233','cardo@cardo.com')";
        Assert.assertFalse(DBToolsNoJavaFX.executeUpdateQuery(query));
    }

    @Test
    public void testCorrectExecuteSelectQuery(){
        String query="SELECT name,surname FROM next_of_kin WHERE kin_id=1";
        String name="Isabella";
        String surname="Jones";
        try(QueryResult qs=DBTools.executeSelectQuery(query) )
        {
            try (ResultSet rs=qs.getResultSet()){
                Assert.assertNotNull(rs);
                Assert.assertEquals(rs.getString(1),name);
                Assert.assertEquals(rs.getString(2),surname);
            }
            catch (ValidationException | SQLException e){e.printStackTrace();}
        }catch (SQLException e){e.printStackTrace();}
    }

    @Test
    public void testIncorrectExecuteSelectQuery(){
        String query="SELECT name,surname FROM next_of_ WHERE kin_id=1";
        try(QueryResult qs=DBToolsNoJavaFX.executeSelectQuery(query) )
        {
            Assert.assertNull(qs);
        }catch (SQLException e){e.printStackTrace();}
    }

    @Test
    public void testCorrectGetID(){
        String query="SELECT kin_id FROM next_of_kin WHERE name='Isabella' AND surname='Jones'";
        Assert.assertEquals(DBTools.getID(query),1);
    }

    @Test
    public void testIncorrectGetID(){
        String query="SELECT kin_id FROM next_of_kin WHERE name='Isabella' AND surname='Jonesyy'";
        Assert.assertEquals(DBTools.getID(query),0);
    }

    @Test
    public void testCorrectPlayerInsertMember() throws ValidationException{
        Player pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertTrue(DBTools.insertMember(pl));
    }

    @Test
    public void testIncorrectPlayerInsertMember(){
        Assert.assertFalse(DBTools.insertMember(null));
    }

    @Test
    public void testCorrectNonPlayerInsertMember() throws ValidationException{
        NonPlayer np=new NonPlayer(1,"Erik","Froment","234 Glasgow","0141112233","erik@erik.com",1);
        Assert.assertTrue(DBTools.insertMember(np));

    }

    @Test
    public void testIncorrectNonPlayerInsertMember() throws ValidationException{
        Assert.assertFalse(DBTools.insertMember(null));
    }

    @Test
    public void testCorrectPlayerLoadMember(){
        Player pl=(Player) DBToolsNoJavaFX.loadMember(Player.dummyPlayer(),1);
        String name="Abbie";
        String surname="Morrison";
        Assert.assertEquals(pl.getFirstName(),name);
        Assert.assertEquals(pl.getSurname(),surname);
    }

    @Test
    public void testIncorrectPlayerLoadMember(){

        Assert.assertNull(DBToolsNoJavaFX.loadMember(Player.dummyPlayer(),1000));
    }

    @Test
    public void testCorrectNonPlayerLoadMember(){
        NonPlayer pl=(NonPlayer) DBToolsNoJavaFX.loadMember(new NonPlayer(),1);
        String name="Oliver";
        String surname="Campbell";
        Assert.assertEquals(pl.getFirstName(),name);
        Assert.assertEquals(pl.getSurname(),surname);
    }

    @Test
    public void testIncorrectNonPlayerLoadMember(){
        Assert.assertNull(DBToolsNoJavaFX.loadMember(new NonPlayer(),1000));
    }

}
