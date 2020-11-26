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
     * Tests if one DoubleCoinsPowerUp which was "collected" is activated and removed correctly after the power-up time
     * limit is expired. It also tests if the coin-multiplier is correct when multiple DoubleCoinsPowerUps
     * were collected.
     */
    @Test
    void addOneDoubleCoinsPowerUpTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.addPowerUp(doubleCoinsPowerUp);
        assertEquals(1, activePowerUps.size());
        assertEquals(1, activatedPowerUpManager.getCoinMultiplicator());

        try {
            Thread.sleep(doubleCoinsPowerUp.getActiveTime() + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(activePowerUps.isEmpty());
    }

    /**
     * Tests if the two DoubleCoinsPowerUps which were "collected" are activated and removed correctly after the
     * power-up time limit is expired. It also tests if the coin-multiplier is correct when multiple
     * DoubleCoinsPowerUps were collected.
     */
    @Test
    void addTwoDoubleCoinsPowerUpTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.addPowerUp(doubleCoinsPowerUp);
        activatedPowerUpManager.addPowerUp(doubleCoinsPowerUp);
        assertEquals(1, activePowerUps.size());
        assertEquals(2, activatedPowerUpManager.getCoinMultiplicator());

        try {
            Thread.sleep(doubleCoinsPowerUp.getActiveTime() + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(activePowerUps.isEmpty());
    }

    /**
     * Tests if the three DoubleCoinsPowerUps which were "collected" are activated and removed correctly after
     * the power-up time limit is expired. It also tests if the coin-multiplier is correct when multiple
     * DoubleCoinsPowerUps were collected.
     */
    @Test
    void addThreeDoubleCoinsPowerUpTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.addPowerUp(doubleCoinsPowerUp);
        activatedPowerUpManager.addPowerUp(doubleCoinsPowerUp);
        activatedPowerUpManager.addPowerUp(doubleCoinsPowerUp);
        assertEquals(1, activePowerUps.size());
        assertEquals(3, activatedPowerUpManager.getCoinMultiplicator());

        try {
            Thread.sleep(doubleCoinsPowerUp.getActiveTime() + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(activePowerUps.isEmpty());
    }

    /**
     * Tests if the two DoubleCoinsPowerUps which were not consecutively "collected" are activated and removed correctly
     * after the power-up time limit is expired. It also tests if the coin-multiplier is correct when multiple
     * DoubleCoinsPowerUps were collected.
     */
    @Test
    void addTwoDoubleCoinsPowerUpNoWithPauseInBetweenTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.addPowerUp(doubleCoinsPowerUp);

        try {
            Thread.sleep(doubleCoinsPowerUp.getActiveTime()/2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        activatedPowerUpManager.addPowerUp(doubleCoinsPowerUp);

        try {
            Thread.sleep((long) (doubleCoinsPowerUp.getActiveTime() * (3.0/4.0)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(activePowerUps.isEmpty());


        assertEquals(1, activePowerUps.size());
        assertEquals(2, activatedPowerUpManager.getCoinMultiplicator());

        try {
            Thread.sleep((long) (doubleCoinsPowerUp.getActiveTime() + (1.0/4.0)) + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(activePowerUps.isEmpty());
    }

    /**
     * Tests if one ShieldPowerUp which was "collected" is activated.
     */
    @Test
    void addOneShieldPowerUpTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.addPowerUp(shieldPowerUp);
        assertEquals(1, activePowerUps.size());
        assertTrue(activatedPowerUpManager.hasShield());
    }

    /**
     * Tests if the two ShieldPowerUps which were "collected" are activated.
     */
    @Test
    void addTwoShieldPowerUpTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.addPowerUp(shieldPowerUp);
        activatedPowerUpManager.addPowerUp(shieldPowerUp);
        assertEquals(1, activePowerUps.size());
        assertTrue(activatedPowerUpManager.hasShield());
    }

    /**
     * Tests if the two power ups (DoubleCoinsPowerUp and ShieldPowerUp) which were "collected" are activated and
     * removed correctly after the power-up time limit is expired. It also tests if the coin-multiplier is correct
     * when multiple DoubleCoinsPowerUps were collected.
     */
    @Test
    void addOneDoubleCoinsPowerUpAndOneShieldPowerUpTest(){
        Map<Class<? extends PowerUp>, PowerUp> activePowerUps = activatedPowerUpManager.getActivePowerUps();
        assertTrue(activePowerUps.isEmpty());
        activatedPowerUpManager.addPowerUp(doubleCoinsPowerUp);
        activatedPowerUpManager.addPowerUp(shieldPowerUp);
        assertEquals(2, activePowerUps.size());
        assertEquals(1, activatedPowerUpManager.getCoinMultiplicator());

        try {
            Thread.sleep(doubleCoinsPowerUp.getActiveTime() + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(1, activePowerUps.size());
        assertTrue(activatedPowerUpManager.hasShield());
    }

    /**
     * This test simulates if the collected shield is removed properly after a collision occurred.
     */
    @Test
    void removeShieldWhenOneShieldPowerUpWasCollectedTest(){
        activatedPowerUpManager.addPowerUp(shieldPowerUp);
        activatedPowerUpManager.removeShield();
        assertFalse(activatedPowerUpManager.hasShield());
    }

    /**
     * This test simulates if the collected shields is removed properly after a collision occurred.
     */
    @Test
    void removeShieldWhenTwoShieldPowerUpsWereCollectedTest(){
        activatedPowerUpManager.addPowerUp(shieldPowerUp);
        activatedPowerUpManager.addPowerUp(shieldPowerUp);
        activatedPowerUpManager.removeShield();
        assertFalse(activatedPowerUpManager.hasShield());
    }
}