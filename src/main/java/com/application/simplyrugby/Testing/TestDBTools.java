package com.application.simplyrugby.Testing;
import com.application.simplyrugby.Model.*;
import com.application.simplyrugby.System.DBTools;
import com.application.simplyrugby.System.QueryResult;
import com.application.simplyrugby.System.ValidationException;
import org.sqlite.core.DB;
import org.testng.Reporter;
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
        //

        Player pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        if (!DBTools.memberExists(pl))
            Assert.assertTrue(DBTools.insertMember(pl));
        else
            Reporter.log("Player already in the DB, please reset the DB.");

    }

    @Test
    public void testIncorrectPlayerInsertMember(){
        Assert.assertFalse(DBTools.insertMember(null));
    }

    @Test
    public void testCorrectNonPlayerInsertMember() throws ValidationException{
        NonPlayer np=new NonPlayer(1,"Erik","Froment","234 Glasgow","0141112233","erik@erik.com",1);
        if (!DBTools.memberExists(np))
            Assert.assertTrue(DBTools.insertMember(np));
        else
            Reporter.log("Non-Player already in the DB, please reset the DB.");

    }

    @Test
    public void testIncorrectNonPlayerInsertMember(){
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

    @Test(expectedExceptions = NullPointerException.class)
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

    @Test
    public void testSelectCorrectNextOfKinFromQuery(){
        String name="Isabella";
        String surname="Jones";
        String query="SELECT kin_id,name,surname,telephone FROM next_of_kin WHERE kin_id=1";
        NextOfKin nextOfKin=(NextOfKin) DBToolsNoJavaFX.selectContact(new NextOfKin(),query);
        Assert.assertNotNull(nextOfKin);
        Assert.assertEquals(nextOfKin.getFirstName(),name);
        Assert.assertEquals(nextOfKin.getSurname(),surname);
    }

    @Test
    public void testSelectCorrectDoctorFromQuery(){
        String name="Alexander";
        String surname="Taylor";
        String query="SELECT doctor_id,name,surname,telephone FROM player_doctors WHERE doctor_id=1";
        Doctor doctor=(Doctor) DBToolsNoJavaFX.selectContact(new Doctor(),query);
        Assert.assertNotNull(doctor);
        Assert.assertEquals(doctor.getFirstName(),name);
        Assert.assertEquals(doctor.getSurname(),surname);
    }

    @Test
    public void testSelectCorrectNextOfKinByIndex(){
        String name="Isabella";
        String surname="Jones";
        NextOfKin nextOfKin=(NextOfKin) DBToolsNoJavaFX.selectContact(new NextOfKin(),1);
        Assert.assertNotNull(nextOfKin);
        Assert.assertEquals(nextOfKin.getFirstName(),name);
        Assert.assertEquals(nextOfKin.getSurname(),surname);
    }
    @Test
    public void testSelectIncorrectNextOfKinByIndex(){
        NextOfKin nextOfKin=(NextOfKin) DBToolsNoJavaFX.selectContact(new NextOfKin(),1000);
        Assert.assertNull(nextOfKin);
    }

    @Test
    public void testSelectCorrectDoctorByIndex(){
        String name="Alexander";
        String surname="Taylor";
        Doctor doctor=(Doctor) DBToolsNoJavaFX.selectContact(new Doctor(),1);
        Assert.assertNotNull(doctor);
        Assert.assertEquals(doctor.getFirstName(),name);
        Assert.assertEquals(doctor.getSurname(),surname);
    }

    @Test
    public void testSelectIncorrectDoctorByIndex(){
        Doctor doctor=(Doctor) DBToolsNoJavaFX.selectContact(new Doctor(),1000);
        Assert.assertNotNull(doctor);
    }

    @Test
    public void testGetRoleCoach(){
        Assert.assertEquals(DBToolsNoJavaFX.getRole(1),"Coach");
    }
    @Test
    public void testGetRoleChairman(){
        Assert.assertEquals(DBToolsNoJavaFX.getRole(2),"Chairman");
    }

    @Test
    public void testGetRoleFixtSec(){
        Assert.assertEquals(DBToolsNoJavaFX.getRole(3),"Fixture Secretary");
    }

    @Test
    public void testCorrectGetClub(){
        String name="Glasgow Warriors";
        Club club=DBToolsNoJavaFX.getClub(1);
        Assert.assertEquals(club.getName(),name);
    }


    /*
    java.lang.NullPointerException: Cannot invoke "String.equals(Object)" because "name" is null

	at com.application.simplyrugby/com.application.simplyrugby.Model.Club.setName(Club.java:52)
	at com.application.simplyrugby/com.application.simplyrugby.Model.Club.<init>(Club.java:32)
	at com.application.simplyrugby/com.application.simplyrugby.Testing.DBToolsNoJavaFX.getClub(DBToolsNoJavaFX.java:228)
	at com.application.simplyrugby/com.application.simplyrugby.Testing.TestDBTools.testIncorrectClub(TestDBTools.java:213)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
	at java.base/java.lang.reflect.Method.invoke(Method.java:578)
	at org.testng@7.7.0/org.testng.internal.invokers.MethodInvocationHelper.invokeMethod(MethodInvocationHelper.java:139)
	at org.testng@7.7.0/org.testng.internal.invokers.TestInvoker.invokeMethod(TestInvoker.java:677)
	at org.testng@7.7.0/org.testng.internal.invokers.TestInvoker.invokeTestMethod(TestInvoker.java:221)
	at org.testng@7.7.0/org.testng.internal.invokers.MethodRunner.runInSequence(MethodRunner.java:50)
	at org.testng@7.7.0/org.testng.internal.invokers.TestInvoker$MethodInvocationAgent.invoke(TestInvoker.java:969)
	at org.testng@7.7.0/org.testng.internal.invokers.TestInvoker.invokeTestMethods(TestInvoker.java:194)
	at org.testng@7.7.0/org.testng.internal.invokers.TestMethodWorker.invokeTestMethods(TestMethodWorker.java:148)
	at org.testng@7.7.0/org.testng.internal.invokers.TestMethodWorker.run(TestMethodWorker.java:128)
     */
    @Test
    public void testIncorrectClub(){
        Assert.assertNull(DBToolsNoJavaFX.getClub(1000));
    }

    @Test
    public void testCorrectSeniorLoadSquad()throws ValidationException{
        String name="Senior Squad 1 The Lions";
        SeniorSquad seniorSquad=(SeniorSquad) DBToolsNoJavaFX.loadSquad(new SeniorSquad(),1);
        Assert.assertNotNull(seniorSquad);
        Assert.assertEquals(seniorSquad.getSquadName(),name);
    }
    @Test
    public void testIncorrectSeniorLoadSquad() throws ValidationException{
        Assert.assertNull(DBToolsNoJavaFX.loadSquad(new SeniorSquad(),1000));
    }

    @Test
    public void testCorrectAdminTeam(){
        AdminTeam adminTeam=(AdminTeam) DBToolsNoJavaFX.loadTeam(new AdminTeam(),1);
        Assert.assertNotNull(adminTeam);
        Assert.assertEquals(adminTeam.getAdmins().get(1).getSurname(),"Campbell");
    }
    @Test
    public void testIncorrectAdminTeam(){
        Assert.assertNull(DBToolsNoJavaFX.loadTeam(new AdminTeam(),100));
    }
}
