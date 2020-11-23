package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PlayerProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceUtilTest {

    private PersistenceUtil persistenceUtil = PersistenceUtil.getUtil();

    @BeforeEach
    void setUp() throws IOException {
        PlayerProfile.TEST = true;

        Path testJSON = Path.of("profile.json");
        if(Files.exists(testJSON)){
            Files.delete(testJSON);
        }
    }

    /**
     *  test save profile
     */
    @Test
    void testSaveProfile(){

        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setCoins(20000);
        playerProfile.setFps(35);
        playerProfile.setVolume(55);

        persistenceUtil.saveProfile(playerProfile);
        Path path = Path.of(GameFile.PROFILE.getFileName());
        assertTrue(Files.exists(path));

    }

    /**
     *  test save profile with null -> IllegalArgumentException
     */
    @Test
    void testSaveProfileWithNull(){
        assertThrows(IllegalArgumentException.class, () -> persistenceUtil.saveProfile(null));
        Path path = Path.of(GameFile.PROFILE.getFileName());
        assertFalse(Files.exists(path));
    }

    /**
     *  test load profile
     */
    @Test
    void testLoadProfile(){

        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setCoins(20060);
        playerProfile.setFps(65);
        playerProfile.setVolume(45);

        persistenceUtil.saveProfile(playerProfile);
        PlayerProfile playerProfileLoaded = persistenceUtil.loadProfile();

        assertEquals(playerProfile, playerProfileLoaded);
    }

    /**
     *  test load profile without an existing json file
     *  --> default profile should be loaded!
     */
    @Test
    void testLoadProfileWithoutExistingProfile(){
        PlayerProfile playerProfile = new PlayerProfile(); //Creating default profile!

        PlayerProfile playerProfileLoaded = persistenceUtil.loadProfile();

        assertEquals(playerProfile, playerProfileLoaded);
    }

    /**
     * Test saving data with the test model and then loading with extended test model
     */
    @Test
    void testSaveDataAndLoadIntoClassWithMoreFields() throws IOException {
        Path path = Path.of("testModel.json");
        TestDataModel testDataModel = new TestDataModel(13);
        persistenceUtil.serializeAndSaveData(path.toString(), testDataModel);

        assertTrue(Files.exists(path));
        TestDataModelExtended testDataModelExtended = persistenceUtil.loadAndDeserializeData(path.toString(), TestDataModelExtended.class);
        assertEquals(testDataModel.getTestNumber(), testDataModelExtended.getTestNumber());
        Files.delete(path);
    }

    /**
     * Test saving data with the extended test model and then loading with test model
     */
    @Test
    void testSaveDataAndLoadIntoClassWithLessFields() throws IOException {
        Path path = Path.of("testModel.json");
        TestDataModelExtended testDataModelExtended = new TestDataModelExtended(13, "Test", 55);
        persistenceUtil.serializeAndSaveData(path.toString(), testDataModelExtended);

        assertTrue(Files.exists(path));
        TestDataModel testDataModel = persistenceUtil.loadAndDeserializeData(path.toString(), TestDataModel.class);
        assertEquals(testDataModelExtended.getTestNumber(), testDataModel.getTestNumber());
        Files.delete(path);
    }






}
