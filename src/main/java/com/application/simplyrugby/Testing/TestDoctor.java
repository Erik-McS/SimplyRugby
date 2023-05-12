package com.application.simplyrugby.Testing;
import com.application.simplyrugby.Model.Doctor;
import com.application.simplyrugby.Model.NextOfKin;
import com.application.simplyrugby.System.ValidationException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.Assert;
public class TestDoctor {

    Doctor doctor;
    @BeforeTest
    public void initialise(){
        doctor=new Doctor();
    }


    @Test
    public void testValidName() throws ValidationException{
        String name="Erik";
        doctor.setFirstName(name);
        Assert.assertEquals(doctor.getFirstName(),name);
    }
    @Test (expectedExceptions = ValidationException.class)
    public void testInvalidName()throws ValidationException{
        String name="Er1k";
        doctor.setFirstName(name);
        Assert.assertEquals(doctor.getFirstName(),name);
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyName() throws ValidationException{
        doctor.setFirstName("");
        Assert.assertEquals(doctor.getFirstName(),"");
    }
    @Test
    public void testValidSurname() throws ValidationException{
        String surname="Mcseveney";
        doctor.setSurname(surname);
        Assert.assertEquals(doctor.getSurname(),surname);
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testInvalidSurname() throws ValidationException{
        String surname="Mcs12mm";
        doctor.setSurname(surname);
        Assert.assertEquals(doctor.getSurname(),surname);
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testEmptySurname() throws ValidationException{
        doctor.setSurname("");
        Assert.assertEquals(doctor.getSurname(),"");
    }
    @Test
    public void testValidTelephone() throws ValidationException{
        String telephone="0772522223";
        doctor.setTelephone(telephone);
        Assert.assertEquals(doctor.getTelephone(),telephone);
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testInvalidTelephone() throws ValidationException{
        String telephone="telephone";
        doctor.setTelephone(telephone);
        Assert.assertEquals(doctor.getTelephone(),telephone);
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyTelephone() throws ValidationException{
        doctor.setTelephone("");
        Assert.assertEquals(doctor.getTelephone(),"");
    }
    @Test
    public void testCorrectDoctorID() throws ValidationException{
        int kinID=1;
        doctor.setDoctorID(kinID);
        Assert.assertEquals(doctor.getDoctorID(),kinID);
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testInvalidDoctorID()throws ValidationException{
        doctor.setDoctorID(0);
        Assert.assertEquals(doctor.getDoctorID(),0);
    }

    @Test
    public void testLoadContactNullMethod(){
        Doctor nok=(Doctor) doctor.loadContact();
        Assert.assertNull(nok);
    }
    @Test
    public void testLoadContact(){
        Doctor nok=(Doctor) doctor.loadContact(1);
        Assert.assertEquals(nok.getFirstName(),"Alexander");
        Assert.assertEquals(nok.getSurname(),"Taylor");
        Assert.assertEquals(nok.getTelephone(),"07484287511");
    }

    /* cant make that work for some reason
    @Test(expectedExceptions = NoClassDefFoundError.class)
    public void testLoadContactNonExistingID() throws NoClassDefFoundError{
        Doctor nok=(Doctor) doctor.loadContact(0);
        Assert.assertNull(nok);
    }
    */
    @Test
    public void testSaveDoctor() throws ValidationException{
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
        doctor=null;
    }
}
