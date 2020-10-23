package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class PersistenceUtilTest {


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
        playerProfile.setAudioEnabled(false);
        playerProfile.setCoins(20000);
        playerProfile.setFps(35);
        playerProfile.setVolume(55);

        PersistenceUtil.saveProfile(playerProfile);
        Path path = Path.of(GameFile.PROFILE.getFileName());
        assertTrue(Files.exists(path));

    }

    /**
     *  test load profile
     */
    @Test
    void testLoadProfile(){

        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setAudioEnabled(false);
        playerProfile.setCoins(20060);
        playerProfile.setFps(65);
        playerProfile.setVolume(45);

        PersistenceUtil.saveProfile(playerProfile);
        PlayerProfile playerProfileLoaded = PersistenceUtil.loadProfile();

        assertEquals(playerProfile, playerProfileLoaded);
    }

    /**
     *  test load profile without an existing json file
     *  --> default profile should be loaded!
     */
    @Test
    void testLoadProfileWithoutExistingProfile(){
        PlayerProfile playerProfile = new PlayerProfile(); //Creating default profile!

        PlayerProfile playerProfileLoaded = PersistenceUtil.loadProfile();

        assertEquals(playerProfile, playerProfileLoaded);
    }



}
