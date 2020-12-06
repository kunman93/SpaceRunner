package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util;

import ch.zhaw.it.pm3.spacerunner.domain.ContentId;
import ch.zhaw.it.pm3.spacerunner.domain.PlayerProfile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class JsonPersistenceUtilTest {

    private JsonPersistenceUtil jsonPersistenceUtil = JsonPersistenceUtil.getUtil();

    @BeforeEach
    void setUp() throws IOException {
        GameFile.TEST = true;
        PlayerProfile.TEST = true;

        Path testJSON = Path.of(GameFile.PROFILE.getFileName());
        if (Files.exists(testJSON)) {
            Files.delete(testJSON);
        }
    }


    /**
     * test save profile
     */
    @Test
    void testSaveProfile() {

        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setCoins(20000);
        playerProfile.setFps(35);
        playerProfile.setVolume(55);

        jsonPersistenceUtil.saveProfile(playerProfile);
        Path path = Path.of(GameFile.PROFILE.getFileName());
        assertTrue(Files.exists(path));

    }

    /**
     * test save profile with null -> IllegalArgumentException
     */
    @Test
    void testSaveProfileWithNull() {
        assertThrows(IllegalArgumentException.class, () -> jsonPersistenceUtil.saveProfile(null));
        Path path = Path.of(GameFile.PROFILE.getFileName());
        assertFalse(Files.exists(path));
    }

    /**
     * test load profile
     */
    @Test
    void testLoadProfile() {

        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setCoins(20060);
        playerProfile.setFps(65);
        playerProfile.setVolume(45);

        jsonPersistenceUtil.saveProfile(playerProfile);
        PlayerProfile playerProfileLoaded = jsonPersistenceUtil.loadProfile();

        assertEquals(playerProfile, playerProfileLoaded);
    }

    /**
     * test load profile without an existing json file
     * --> default profile should be loaded!
     */
    @Test
    void testLoadProfileWithoutExistingProfile() {
        PlayerProfile playerProfile = new PlayerProfile(); //Creating default profile!

        PlayerProfile playerProfileLoaded = jsonPersistenceUtil.loadProfile();

        assertEquals(playerProfile, playerProfileLoaded);
    }

    /**
     * Test saving data with the test model and then loading with extended test model
     */
    @Test
    void testSaveDataAndLoadIntoClassWithMoreFields() throws IOException {
        Path path = Path.of("testModel.json");
        TestDataModel testDataModel = new TestDataModel(13);
        jsonPersistenceUtil.serializeAndSaveData(path.toString(), testDataModel);

        assertTrue(Files.exists(path));
        TestDataModelExtended testDataModelExtended = jsonPersistenceUtil.loadAndDeserializeData(path.toString(), TestDataModelExtended.class);
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
        jsonPersistenceUtil.serializeAndSaveData(path.toString(), testDataModelExtended);

        assertTrue(Files.exists(path));
        TestDataModel testDataModel = jsonPersistenceUtil.loadAndDeserializeData(path.toString(), TestDataModel.class);
        assertEquals(testDataModelExtended.getTestNumber(), testDataModel.getTestNumber());
        Files.delete(path);
    }


    @Test
    void isContentPurchasedTest() {
        assertFalse(jsonPersistenceUtil.isContentPurchased(ContentId.DOUBLE_DURATION_COIN_UPGRADE));
        assertFalse(jsonPersistenceUtil.isContentPurchased(ContentId.POWER_UP_CHANCE_MULTIPLIER));

        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.addContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        jsonPersistenceUtil.saveProfile(playerProfile);
        assertTrue(jsonPersistenceUtil.isContentPurchased(ContentId.POWER_UP_CHANCE_MULTIPLIER));
    }

    @Test
    void isContentActiveTest() {
        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.addContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        jsonPersistenceUtil.saveProfile(playerProfile);

        assertFalse(jsonPersistenceUtil.isContentActive(ContentId.DOUBLE_DURATION_COIN_UPGRADE));
        assertFalse(jsonPersistenceUtil.isContentActive(ContentId.POWER_UP_CHANCE_MULTIPLIER));

        jsonPersistenceUtil.activateContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        assertTrue(jsonPersistenceUtil.isContentActive(ContentId.POWER_UP_CHANCE_MULTIPLIER));
    }

    @Test
    void buyContentTestWithoutEnoughCoins() {
        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setCoins(3000);
        jsonPersistenceUtil.saveProfile(playerProfile);

        assertThrows(IllegalArgumentException.class, () -> {
            jsonPersistenceUtil.buyContent(ContentId.DOUBLE_DURATION_COIN_UPGRADE, 4000);
        });
        playerProfile = jsonPersistenceUtil.loadProfile();
        assertEquals(3000, playerProfile.getCoins());
        assertFalse(playerProfile.getPurchasedContentIds().contains(ContentId.DOUBLE_DURATION_COIN_UPGRADE));
    }

    @Test
    void buyContentTestWithNullAsContent() {
        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setCoins(3000);
        jsonPersistenceUtil.saveProfile(playerProfile);

        assertThrows(IllegalArgumentException.class, () -> {
            jsonPersistenceUtil.buyContent(null, 12);
        });
        playerProfile = jsonPersistenceUtil.loadProfile();
        assertEquals(3000, playerProfile.getCoins());
    }

    @Test
    void buyContentTestWithNegativePrice() {
        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setCoins(3000);
        jsonPersistenceUtil.saveProfile(playerProfile);

        assertThrows(IllegalArgumentException.class, () -> {
            jsonPersistenceUtil.buyContent(ContentId.DOUBLE_DURATION_COIN_UPGRADE, -1);
        });
        playerProfile = jsonPersistenceUtil.loadProfile();
        assertEquals(3000, playerProfile.getCoins());
        assertFalse(playerProfile.getPurchasedContentIds().contains(ContentId.DOUBLE_DURATION_COIN_UPGRADE));
    }

    @Test
    void buyContentTest() {
        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setCoins(3000);
        jsonPersistenceUtil.saveProfile(playerProfile);

        jsonPersistenceUtil.buyContent(ContentId.DOUBLE_DURATION_COIN_UPGRADE, 1000);

        playerProfile = jsonPersistenceUtil.loadProfile();
        assertEquals(2000, playerProfile.getCoins());
        assertTrue(playerProfile.getPurchasedContentIds().contains(ContentId.DOUBLE_DURATION_COIN_UPGRADE));
    }

    @Test
    void playerHasEnoughCoinsToBuyTestTestWithNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            jsonPersistenceUtil.playerHasEnoughCoinsToBuy(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            jsonPersistenceUtil.playerHasEnoughCoinsToBuy(Integer.MIN_VALUE);
        });
    }

    @Test
    void playerHasEnoughCoinsToBuyTest() {
        assertFalse(jsonPersistenceUtil.playerHasEnoughCoinsToBuy(1000));

        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setCoins(3000);
        jsonPersistenceUtil.saveProfile(playerProfile);

        assertTrue(jsonPersistenceUtil.playerHasEnoughCoinsToBuy(1000));
    }

    @Test
    void getAmountOfCoinsNeededToBuyContentTest() {
        assertEquals(1000, jsonPersistenceUtil.getAmountOfCoinsNeededToBuyContent(1000));
        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setCoins(3000);
        jsonPersistenceUtil.saveProfile(playerProfile);
        assertEquals(0, jsonPersistenceUtil.getAmountOfCoinsNeededToBuyContent(1000));

    }


    @Test
    void getAmountOfCoinsNeededToBuyContentTestWithNegativePrice() {
        assertThrows(IllegalArgumentException.class, () -> {
            jsonPersistenceUtil.getAmountOfCoinsNeededToBuyContent(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            jsonPersistenceUtil.getAmountOfCoinsNeededToBuyContent(Integer.MIN_VALUE);
        });
    }

    @Test
    void activateContentWhenNotPurchased() {
        assertThrows(IllegalArgumentException.class, () -> {
            jsonPersistenceUtil.activateContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        });
    }

    @Test
    void activateContentAlreadyActive() {
        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.addContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        jsonPersistenceUtil.saveProfile(playerProfile);

        jsonPersistenceUtil.activateContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        jsonPersistenceUtil.activateContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);

        assertTrue(jsonPersistenceUtil.hasPowerUpChanceMultiplierUpgrade());
    }

    @Test
    void activateContent() {
        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.addContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        jsonPersistenceUtil.saveProfile(playerProfile);

        jsonPersistenceUtil.activateContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        assertTrue(jsonPersistenceUtil.hasPowerUpChanceMultiplierUpgrade());
    }

    @Test
    void deactivateContentTestWhenNotActive() {
        PlayerProfile playerProfile = new PlayerProfile();
        jsonPersistenceUtil.saveProfile(playerProfile);

        jsonPersistenceUtil.deactivateContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        assertFalse(jsonPersistenceUtil.hasPowerUpChanceMultiplierUpgrade());

    }

    @Test
    void deactivateContentTest() {
        PlayerProfile playerProfile = new PlayerProfile();
        jsonPersistenceUtil.saveProfile(playerProfile);

        assertFalse(jsonPersistenceUtil.hasPowerUpChanceMultiplierUpgrade());

        playerProfile.addContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        playerProfile.activateContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        jsonPersistenceUtil.saveProfile(playerProfile);

        jsonPersistenceUtil.deactivateContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);

        assertFalse(jsonPersistenceUtil.hasPowerUpChanceMultiplierUpgrade());

    }

    @Test
    void hasPowerUpChanceMultiplierUpgradeTest() {
        PlayerProfile playerProfile = new PlayerProfile();
        jsonPersistenceUtil.saveProfile(playerProfile);

        assertFalse(jsonPersistenceUtil.hasPowerUpChanceMultiplierUpgrade());

        playerProfile.addContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        playerProfile.activateContent(ContentId.POWER_UP_CHANCE_MULTIPLIER);
        jsonPersistenceUtil.saveProfile(playerProfile);

        assertTrue(jsonPersistenceUtil.hasPowerUpChanceMultiplierUpgrade());
    }

    @Test
    void hasDoubleDurationForCoinPowerUpTest() {
        PlayerProfile playerProfile = new PlayerProfile();
        jsonPersistenceUtil.saveProfile(playerProfile);

        assertFalse(jsonPersistenceUtil.hasDoubleDurationForCoinPowerUp());

        playerProfile.addContent(ContentId.DOUBLE_DURATION_COIN_UPGRADE);
        playerProfile.activateContent(ContentId.DOUBLE_DURATION_COIN_UPGRADE);
        jsonPersistenceUtil.saveProfile(playerProfile);

        assertTrue(jsonPersistenceUtil.hasDoubleDurationForCoinPowerUp());
    }

}
