package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the ActivatedPowerUpManager which is responsible to generate, activate and deactivate power-ups.
 * @author kunnuman
 */
class ActivatedPowerUpManagerTest {
    private DoubleCoinsPowerUp doubleCoinsPowerUp;
    private ShieldPowerUp shieldPowerUp;
    private final ActivatedPowerUpManager activatedPowerUpManager = new ActivatedPowerUpManager();

    /**
     * Sets Up the two Power-Ups: DoubleCoinsPowerUp and ShieldPowerUp.
     */
    @BeforeEach
    void setUp() {
        doubleCoinsPowerUp = new DoubleCoinsPowerUp(new Point2D.Double(1.0, 0.5));
        shieldPowerUp = new ShieldPowerUp(new Point2D.Double(1.0, 0.5));
    }

    /**
     * Tests if one DoubleCoinsPowerUp which was "collected" is added.
     */
    @Test
    void addPowerUpAddingOneDoubleCoinsPowerUpTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.activatePowerUp(doubleCoinsPowerUp);
        assertEquals(1, activePowerUps.size());
    }

    /**
     * Tests if the two DoubleCoinsPowerUps which were "collected" are added.
     */
    @Test
    void addPowerUpAddingTwoDoubleCoinsPowerUpTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.activatePowerUp(doubleCoinsPowerUp);
        activatedPowerUpManager.activatePowerUp(doubleCoinsPowerUp);
        assertEquals(1, activePowerUps.size());
    }

    /**
     * Tests if the three DoubleCoinsPowerUps which were "collected" are added.
     */
    @Test
    void addPowerUpAddingThreeDoubleCoinsPowerUpTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.activatePowerUp(doubleCoinsPowerUp);
        activatedPowerUpManager.activatePowerUp(doubleCoinsPowerUp);
        activatedPowerUpManager.activatePowerUp(doubleCoinsPowerUp);
        assertEquals(1, activePowerUps.size());
    }

    /**
     * Tests if one ShieldPowerUp which was "collected" is activated.
     */
    @Test
    void addPowerUpAddingOneShieldPowerUpTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.activatePowerUp(shieldPowerUp);
        assertEquals(1, activePowerUps.size());
        assertTrue(activatedPowerUpManager.hasShield());
    }

    /**
     * This test simulates that a spaceship has only one shield active even if multiple ShieldPowerUps were collected.
     */
    @Test
    void addPowerUpAddingTwoShieldPowerUpTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.activatePowerUp(shieldPowerUp);
        activatedPowerUpManager.activatePowerUp(shieldPowerUp);
        assertEquals(1, activePowerUps.size());
        assertTrue(activatedPowerUpManager.hasShield());
    }

    /**
     * Tests if the two power ups (DoubleCoinsPowerUp and ShieldPowerUp) which were "collected" are added.
     */
    @Test
    void addPowerUpAddingOneDoubleCoinsPowerUpAndOneShieldPowerUpTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.activatePowerUp(doubleCoinsPowerUp);
        activatedPowerUpManager.activatePowerUp(shieldPowerUp);
        assertEquals(2, activePowerUps.size());
        assertTrue(activatedPowerUpManager.hasShield());
    }

    /**
     * This test simulates if the collected shield is removed properly after a collision occurred.
     */
    @Test
    void removeShieldWhenOneShieldPowerUpWasCollectedTest(){
        activatedPowerUpManager.activatePowerUp(shieldPowerUp);
        assertTrue(activatedPowerUpManager.hasShield());
        activatedPowerUpManager.removeShield();
        assertFalse(activatedPowerUpManager.hasShield());
    }

    /**
     * This test simulates if the collected shield is removed properly after a collision occurred.
     */
    @Test
    void removeShieldWhenTwoShieldPowerUpsWereCollectedTest(){
        activatedPowerUpManager.activatePowerUp(shieldPowerUp);
        activatedPowerUpManager.activatePowerUp(shieldPowerUp);
        assertTrue(activatedPowerUpManager.hasShield());
        activatedPowerUpManager.removeShield();
        assertFalse(activatedPowerUpManager.hasShield());
    }

    /**
     * Tests if the coin multiplier is correct when no power-ups were collected.
     */
    @Test
    void getCoinMultiplierWhenNoPowerUpsWereCollectedTest(){
        assertEquals(0, activatedPowerUpManager.getCoinMultiplier());
    }

    /**
     * Tests if the coin multiplier is correct when DoubleCoinsPowerUp were collected.
     */
    @Test
    void getCoinMultiplierWhenDoubleCoinsPowerUpsWereCollectedTest(){
        activatedPowerUpManager.activatePowerUp(doubleCoinsPowerUp);
        assertEquals(1, activatedPowerUpManager.getCoinMultiplier());
        activatedPowerUpManager.activatePowerUp(doubleCoinsPowerUp);
        assertEquals(2, activatedPowerUpManager.getCoinMultiplier());
        activatedPowerUpManager.activatePowerUp(doubleCoinsPowerUp);
        assertEquals(3, activatedPowerUpManager.getCoinMultiplier());
    }

    /**
     * Tests if the coin multiplier is correct when ShieldPowerUps were collected.
     */
    @Test
    void getCoinMultiplierWhenShieldPowerUpsWereCollectedTest(){
        activatedPowerUpManager.activatePowerUp(shieldPowerUp);
        assertEquals(0, activatedPowerUpManager.getCoinMultiplier());
        activatedPowerUpManager.activatePowerUp(shieldPowerUp);
        assertEquals(0, activatedPowerUpManager.getCoinMultiplier());
        activatedPowerUpManager.activatePowerUp(shieldPowerUp);
        assertEquals(0, activatedPowerUpManager.getCoinMultiplier());
    }

    /**
     * Tests if deactivating power-ups works properly when no power-ups are active.
     */
    @Test
    void powerUpFinishedWhenNoPowerUpsAreActiveTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.powerUpFinished(doubleCoinsPowerUp);
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.powerUpFinished(shieldPowerUp);
        assertTrue(activePowerUps.isEmpty());
    }

    /**
     * Tests if deactivating DoubleCoinsPowerUp works properly when one doubleCoinsPowerUp is active.
     */
    @Test
    void powerUpFinishedWhenDoubleCoinsPowerUpIsActiveTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.activatePowerUp(doubleCoinsPowerUp);
        assertEquals(1, activePowerUps.size());
        activatedPowerUpManager.powerUpFinished(doubleCoinsPowerUp);
        assertTrue(activePowerUps.isEmpty());
    }

    /**
     * Tests if deactivating ShieldPowerUp works properly when one ShieldPowerUp is active.
     */
    @Test
    void powerUpFinishedWhenShieldPowerUpIsActiveTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.activatePowerUp(shieldPowerUp);
        assertEquals(1, activePowerUps.size());
        activatedPowerUpManager.powerUpFinished(shieldPowerUp);
        assertTrue(activePowerUps.isEmpty());
    }
}