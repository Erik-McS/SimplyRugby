package com.application.simplyrugby.Testing;
import com.application.simplyrugby.Model.Player;
import com.application.simplyrugby.System.ValidationException;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.Assert;
public class TestPlayer {

    Player pl;

    @BeforeTest
    public void initialise(){
        pl=null;
    }
    // test the dummy constructor method
    @Test
    public void testDummyPlayer(){
        pl=Player.dummyPlayer();
        Assert.assertNotNull(pl);
        Assert.assertEquals(pl.getSurname(),"Dummy");
        Assert.assertEquals(pl.getFirstName(),"Dummy");
        Assert.assertEquals(pl.getAddress(),"dummy");
        Assert.assertEquals(pl.getDateOfBirth(),"");
        Assert.assertEquals(pl.getGender(),"Male");
        Assert.assertEquals(pl.getEmail(),"q@q.qqq");

    }
    // test the builder
    @Test
    public void testCorrectPlayer() throws ValidationException {
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();

        Assert.assertNotNull(pl);
        Assert.assertEquals(pl.getPlayerID(),1);
        Assert.assertEquals(pl.getSurname(),"Mcseveney");
        Assert.assertEquals(pl.getFirstName(),"Erik");
        Assert.assertEquals(pl.getAddress(),"123 Glasgow");
        Assert.assertEquals(pl.getGender(),"Male");
        Assert.assertEquals(pl.getTelephone(),"0772522334");
        Assert.assertEquals(pl.getDateOfBirth(),"24/12/1974");
        Assert.assertEquals(pl.getEmail(),"erik@erik.com");
        Assert.assertEquals(pl.getDoctorID(),1);
        Assert.assertEquals(pl.getKinID(),1);
        Assert.assertEquals(pl.getScrumsNumber(),111111);
        Assert.assertEquals(pl.isAssignedToSquad(),"NO");
    }

    // test incorrect name, exception expected
    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectName() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Er1k").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getFirstName(),"Er1k");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyName() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getFirstName(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectSurname() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcs111veney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();

        Assert.assertEquals(pl.getSurname(),"Mcseveney");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptySurname() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getSurname(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyAddress() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getAddress(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testLowLenghtTelephone() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("00000").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getTelephone(),"00000");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testHighLenghtTelephone() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("07725223344444").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getTelephone(),"07725223344444");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptytelephone() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getTelephone(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testInvalidTelephone() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("aaaaaa").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getTelephone(),"");

    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyGender() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getGender(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyDoB() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getDateOfBirth(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyEmail() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getEmail(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectEmail() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.c")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getEmail(),"erik@erik.c");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectKinID() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(0).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getKinID(),0);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectDoctorID() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(0).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getDoctorID(),0);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptySCRUMS() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(0).setIsAssignedToSquad("NO").Builder();
        Assert.assertEquals(pl.getScrumsNumber(),0);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectSquadFlag() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("aaa").Builder();
        Assert.assertEquals(pl.isAssignedToSquad(),"aaa");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptySquadFlag() throws ValidationException{
        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("").Builder();
    }

    @AfterTest
    public void close(){
        pl=null;
    }
}
