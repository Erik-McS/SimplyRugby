package com.application.simplyrugby.Testing;

import com.application.simplyrugby.Model.NextOfKin;
import com.application.simplyrugby.System.ValidationException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.sql.SQLException;

public class TestNextOfKin {

    NextOfKin nextOfKin;

    @BeforeTest
    public void initialiseTest(){
        nextOfKin=new NextOfKin();
    }

    @Test
    public void testValidName() throws ValidationException{
        String name="Erik";
        nextOfKin.setFirstName(name);
        Assert.assertEquals(nextOfKin.getFirstName(),name);
    }
    @Test (expectedExceptions = ValidationException.class)
    public void testInvalidName()throws ValidationException{
        String name="Er1k";
        nextOfKin.setFirstName(name);
        Assert.assertEquals(nextOfKin.getFirstName(),name);
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyName() throws ValidationException{
        nextOfKin.setFirstName("");
        Assert.assertEquals(nextOfKin.getFirstName(),"");
    }
    @Test
    public void testValidSurname() throws ValidationException{
        String surname="Mcseveney";
        nextOfKin.setSurname(surname);
        Assert.assertEquals(nextOfKin.getSurname(),surname);
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testInvalidSurname() throws ValidationException{
        String surname="Mcs12mm";
        nextOfKin.setSurname(surname);
        Assert.assertEquals(nextOfKin.getSurname(),surname);
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testEmptySurname() throws ValidationException{
        nextOfKin.setSurname("");
        Assert.assertEquals(nextOfKin.getSurname(),"");
    }
    @Test
    public void testValidTelephone() throws ValidationException{
        String telephone="0772522223";
        nextOfKin.setTelephone(telephone);
        Assert.assertEquals(nextOfKin.getTelephone(),telephone);
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testInvalidTelephone() throws ValidationException{
        String telephone="telephone";
        nextOfKin.setTelephone(telephone);
        Assert.assertEquals(nextOfKin.getTelephone(),telephone);
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyTelephone() throws ValidationException{
        nextOfKin.setTelephone("");
        Assert.assertEquals(nextOfKin.getTelephone(),"");
    }
    @Test
    public void testCorrectKinID() throws ValidationException{
        int kinID=1;
        nextOfKin.setKinID(kinID);
        Assert.assertEquals(nextOfKin.getKinID(),kinID);
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testInvalidKinID()throws ValidationException{
        nextOfKin.setKinID(0);
        Assert.assertEquals(nextOfKin.getKinID(),0);
    }
    @Test
    public void testLoadContactNullMethod(){
        NextOfKin nok=(NextOfKin) nextOfKin.loadContact();
        Assert.assertNull(nok);
    }
    @Test
    public void testLoadContact(){
        NextOfKin nok=(NextOfKin) nextOfKin.loadContact(1);
        Assert.assertEquals(nok.getFirstName(),"Isabella");
        Assert.assertEquals(nok.getSurname(),"Jones");
        Assert.assertEquals(nok.getTelephone(),"07817927447");
    }
    @Test(expectedExceptions = ExceptionInInitializerError.class)
    public void testLoadContactNonExistingID() throws ExceptionInInitializerError{
        NextOfKin nok=(NextOfKin) nextOfKin.loadContact(0);
        Assert.assertNull(nok);
    }

    @Test
    public void testSaveNextOfKin() throws ValidationException{
        NextOfKin nextOfKin1=new NextOfKin("Frederic","Froment","0772533221");
        Assert.assertTrue(nextOfKin1.saveContact());
    }
    @Test
    public void testConstructor() throws ValidationException{
        String name="Frederic";
        String surname="Froment";
        String tel="0772533221";
        NextOfKin nextOfKin1=new NextOfKin(name,surname,tel);
        Assert.assertEquals(nextOfKin1.getFirstName(),name);
        Assert.assertEquals(nextOfKin1.getSurname(),surname);
        Assert.assertEquals(nextOfKin1.getTelephone(),tel);
    }
        @AfterTest
    public void close(){
        nextOfKin=null;
    }
}
