package com.application.simplyrugby.Testing;
import com.application.simplyrugby.Model.Player;
import com.application.simplyrugby.Model.TrainingProfile;
import com.application.simplyrugby.System.ValidationException;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.Assert;

public class TestTrainingProfile {

    TrainingProfile tp;
    Player pl;

    @BeforeTest
    public void init() throws ValidationException {

        pl=new Player.PlayerBuilder().setPlayerID(1).setFirstName("Erik").setSurname("Mcseveney").setAddress("123 Glasgow")
                .setTelephone("0772522334").setGender("Male").setDoB("24/12/1974").setEmail("erik@erik.com")
                .setKinID(1).setDoctorID(1).setScrumsNumber(111111).setIsAssignedToSquad("NO").Builder();
        tp=new TrainingProfile(pl);
    }

    @Test
    public void testConstructor() throws ValidationException{
        TrainingProfile trainingProfile=new TrainingProfile(pl);
        String level="Poor";
        Assert.assertEquals(trainingProfile.getPlayerID(),pl.getPlayerID());
        Assert.assertEquals(trainingProfile.getPassingLevel(),level);
        Assert.assertEquals(trainingProfile.getDecisionLevel(),level);
        Assert.assertEquals(trainingProfile.getTacklingLevel(),level);
        Assert.assertEquals(trainingProfile.getRunningLevel(),level);
        Assert.assertEquals(trainingProfile.getSupportLevel(),level);
    }

    @Test (expectedExceptions = ValidationException.class)
    public void testIncorrectPassing() throws ValidationException{
        tp.setPassingLevel("111");
        Assert.assertEquals(tp.getPassingLevel(),"111");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyPassing() throws ValidationException{
        tp.setPassingLevel("");
        Assert.assertEquals(tp.getPassingLevel(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyTackling() throws ValidationException{
        tp.setTacklingLevel("");
        Assert.assertEquals(tp.getTacklingLevel(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectTackling() throws ValidationException{
        tp.setTacklingLevel("111");
        Assert.assertEquals(tp.getTacklingLevel(),"111");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectSupport() throws ValidationException{
        tp.setSupportLevel("111");
        Assert.assertEquals(tp.getSupportLevel(),"111");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptySupport() throws ValidationException{
        tp.setSupportLevel("");
        Assert.assertEquals(tp.getSupportLevel(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectRunning() throws ValidationException{
        tp.setRunningLevel("111");
        Assert.assertEquals(tp.getRunningLevel(),"111");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyRunning() throws ValidationException{
        tp.setRunningLevel("");
        Assert.assertEquals(tp.getRunningLevel(),"");
    }
    @Test(expectedExceptions = ValidationException.class)
    public void testIncorrectDecision() throws ValidationException{
        tp.setRunningLevel("111");
        Assert.assertEquals(tp.getRunningLevel(),"111");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyDecision() throws ValidationException{
        tp.setRunningLevel("");
        Assert.assertEquals(tp.getRunningLevel(),"");
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testEmptyLevelID() throws ValidationException{
        int i=TrainingProfile.getLevelID("");
        Assert.assertEquals(i,1);
    }

    @Test(expectedExceptions = ValidationException.class)
    public void testWrongLevelID() throws ValidationException{
        int i=TrainingProfile.getLevelID("aaaa");
        Assert.assertEquals(i,1);
    }

    @Test
    public void testPoorLevelID() throws ValidationException{
        int i=TrainingProfile.getLevelID("Poor");
        Assert.assertEquals(i,1);
    }

    @Test
    public void testDevelopingLevelID() throws ValidationException{
        int i=TrainingProfile.getLevelID("Developing");
        Assert.assertEquals(i,2);
    }

    @Test
    public void testProficientLevelID() throws ValidationException{
        int i=TrainingProfile.getLevelID("Proficient");
        Assert.assertEquals(i,3);
    }

    @Test
    public void testAdvancedLevelID() throws ValidationException{
        int i=TrainingProfile.getLevelID("Advanced");
        Assert.assertEquals(i,4);
    }

    @Test
    public void testLeadingLevelID() throws ValidationException{
        int i=TrainingProfile.getLevelID("Leading");
        Assert.assertEquals(i,5);
    }

    @Test
    public void testWrongLevelDesc() {
        String s=TrainingProfile.getLevelDesc(10);
        Assert.assertEquals(s,"");
    }

    @Test
    public void testLevelDesc1() {
        String s=TrainingProfile.getLevelDesc(1);
        Assert.assertEquals(s,"Poor");
    }

    @Test
    public void testLevelDesc2() {
        String s=TrainingProfile.getLevelDesc(2);
        Assert.assertEquals(s,"Developing");
    }

    @Test
    public void testLevelDesc3() {
        String s=TrainingProfile.getLevelDesc(3);
        Assert.assertEquals(s,"Proficient");
    }

    @Test
    public void testLevelDesc4() {
        String s=TrainingProfile.getLevelDesc(4);
        Assert.assertEquals(s,"Advanced");
    }

    @Test
    public void testLevelDesc5() {
        String s=TrainingProfile.getLevelDesc(5);
        Assert.assertEquals(s,"Leading");
    }

    @AfterTest
    public void close(){
        tp=null;
        pl=null;
    }
}
