package com.application.simplyrugby.Testing;

import com.application.simplyrugby.Model.NonPlayer;
import com.application.simplyrugby.System.ValidationException;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Class to do automated test for the NonPlayer class
 */
public class TestNonPlayer {

    // an object of a class to test
    NonPlayer np;
    // setting up the test
    @BeforeTest
    public void initialiseClass(){
        np=new NonPlayer();
    }

    // tests include the getter and setter of a variable.

    // testing a correct name
    @Test
    public void testName() throws ValidationException {
        String name="Erik";
        np.setFirstName(name);
        Assert.assertEquals(np.getFirstName(),name);
    }
    // testing an incorrect name, expecting an exception thrown
    @Test (expectedExceptions = ValidationException.class)
    public void testIncorrectNameFormat() throws ValidationException{
        String name="erik";
        np.setFirstName(name);
        Assert.assertEquals(np.getFirstName(),name);
    }
    // testing an empty name, expecting an exception thrown
    @Test (expectedExceptions = ValidationException.class)
    public void testEmptyName() throws ValidationException{
        np.setFirstName("");
        Assert.assertEquals(np.getFirstName(),"");
    }
    // test correct surname
    @Test
    public void testSurname() throws ValidationException{
        String sName="Mcseveney";
        np.setSurname(sName);
        Assert.assertEquals(np.getSurname(),sName);
    }
    // test invalid surname, expecting an exception thrown
    @Test (expectedExceptions = ValidationException.class)
    public void testInvalidSurname() throws ValidationException{
        String sName="Mc11veney";
        np.setSurname(sName);
        Assert.assertEquals(np.getSurname(),sName);
    }
    // test an empty surname, expecting an exception thrown
    @Test (expectedExceptions = ValidationException.class)
    public void testEmptySurname() throws ValidationException{
        String sName="";
        np.setSurname(sName);
        Assert.assertEquals(np.getSurname(),sName);
    }
    // address field only needs to be filled, but can't be empty
    // testing a correct entry
    @Test
    public void testCorrectAddress() throws ValidationException{
        String address="123 Galsgow Street";
        np.setAddress(address);
        Assert.assertEquals(np.getAddress(),address);
    }
    // testing an empty address, expecting an exception
    @Test (expectedExceptions = ValidationException.class)
    public void testEmptyAddress() throws ValidationException{
        np.setAddress("");
        Assert.assertEquals(np.getAddress(),"");
    }
    // telephone can only be number of 9-11 digits, cannot be empty
    // testing a correct entry
    @Test
    public void testCorrectTel() throws ValidationException{
        String tel="07725111111";
        np.setTelephone(tel);
        Assert.assertEquals(np.getTelephone(),tel);
    }
    // testing an empty tel, expecting an exception thrown.
    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyTel() throws ValidationException{
        np.setTelephone("");
        Assert.assertEquals(np.getTelephone(),"");
    }
    // testing an incorrect tel format, expecting an exception
    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectTel() throws ValidationException{
        np.setTelephone("telephone");
        Assert.assertEquals(np.getTelephone(),"telephone");
    }

    // test a valid email
    @Test
    public void testValidEmail() throws ValidationException{
        String email="erik@erik.com";
        np.setEmail(email);
        Assert.assertEquals(np.getEmail(),email);
    }
    // testing an empty email, exception expected
    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyEmail() throws ValidationException{
        np.setEmail("");
        Assert.assertEquals(np.getEmail(),"");
    }
    // testing an incorrect format, exception expected
    @Test(expectedExceptions = ValidationException.class)
    public void testInvalidEmail() throws ValidationException{
        String email="a@a.a";
        np.setEmail(email);
        Assert.assertEquals(np.getEmail(),email);
    }

    // testing the role id, cannot be 0.
    // test for correct role ID
    @Test
    public void testCorrectRoleID() throws ValidationException{
        np.setRole_id(1);
        Assert.assertEquals(np.getRole_id(),1);
    }
    // testing for a 0, exception expected
    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectRoleID() throws ValidationException{
        np.setRole_id(0);
        Assert.assertEquals(np.getRole_id(),0);
    }

    // same for the member ID
    // test for a correct one
    @Test
    public void testCorrectMemberID() throws ValidationException{
        np.setMember_id(1);
        Assert.assertEquals(np.getMember_id(),1);
    }
    // testing a 0, exception expected
    @Test(expectedExceptions = ValidationException.class)
    public void testInvalidMemberID() throws ValidationException{
        np.setMember_id(0);
        Assert.assertEquals(np.getMember_id(),0);
    }

    // testing the load member, checking we get the correct nonplayer from the database
    @Test
    public void testLoadExistingMember(){
        //getting the nonplayer from DB with member ID 1
        NonPlayer nonPlayer=(NonPlayer) np.loadMember(new NonPlayer(),1);
        Assert.assertEquals(nonPlayer.getFirstName(),"Oliver");
        Assert.assertEquals(nonPlayer.getSurname(),"Campbell");
        Assert.assertEquals(nonPlayer.getMember_id(),1);
        Assert.assertEquals(nonPlayer.getEmail(),"oliver.campbell@gmail.com");
    }

    // testing a nonplayer insertion record
    @Test
    public void testInsertNonPlayer() throws ValidationException{
        boolean result=true;
        int memberID=1;
        String name="Erik";
        String surname="Mcseveney";
        String address="123 Glasgow";
        String telephone="0772422334";
        String email="erik@erik.com";
        int roleID=1;
        NonPlayer player=new NonPlayer(memberID,name,surname,address,telephone,email,roleID);
        Assert.assertEquals(player.saveMember(player),result);

    }

    // finally, testing the custom constructor
    @Test
    public void testConstructor() throws ValidationException{
        int memberID=1;
        String name="Erik";
        String surname="Mcseveney";
        String address="123 Glasgow";
        String telephone="0772422334";
        String email="erik@erik.com";
        int roleID=1;
        NonPlayer player=new NonPlayer(memberID,name,surname,address,telephone,email,roleID);
        Assert.assertEquals(player.getMember_id(),memberID);
        Assert.assertEquals(player.getFirstName(),name);
        Assert.assertEquals(player.getSurname(),surname);
        Assert.assertEquals(player.getAddress(),address);
        Assert.assertEquals(player.getTelephone(),telephone);
        Assert.assertEquals(player.getEmail(),email);
        Assert.assertEquals(player.getRole_id(),roleID);
    }

    @AfterTest
    public void cleanup(){
        np=null;
    }
}
