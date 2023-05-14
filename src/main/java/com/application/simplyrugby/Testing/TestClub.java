package com.application.simplyrugby.Testing;
import com.application.simplyrugby.Model.Club;
import com.application.simplyrugby.System.ValidationException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.Assert;
public class TestClub {

    Club club;
    @BeforeTest
    public void init(){
        club=new Club();
    }

    @Test
    public void testCorrectClub() throws ValidationException {
        Club club1=new Club("AC Cardonald","123 Cardonald","0141223344","cardo@cardo.com");
        Assert.assertEquals(club1.getName(),"AC Cardonald");
        Assert.assertEquals(club1.getAddress(),"123 Cardonald");
        Assert.assertEquals(club1.getTelephone(),"0141223344");
        Assert.assertEquals(club1.getEmail(),"cardo@cardo.com");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyName() throws ValidationException{
        club.setName("");
        Assert.assertEquals(club.getName(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyAddress() throws ValidationException{
        club.setAddress("");
        Assert.assertEquals(club.getAddress(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyTelephone() throws ValidationException{
        club.setTelephone("");
        Assert.assertEquals(club.getTelephone(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectTelephone() throws ValidationException{
        club.setTelephone("aaaaaaaa");
        Assert.assertEquals(club.getTelephone(),"aaaaaaa");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyEmail() throws ValidationException{
        club.setEmail("");
        Assert.assertEquals(club.getEmail(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectEmail() throws ValidationException{
        club.setEmail("a@a.a");
        Assert.assertEquals(club.getEmail(),"a@a.a");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectClubID() throws ValidationException{
        club.setClub_id(0);
        Assert.assertEquals(club.getClub_id(),0);
    }

    @AfterTest
    public void close(){
        club=null;
    }
}
