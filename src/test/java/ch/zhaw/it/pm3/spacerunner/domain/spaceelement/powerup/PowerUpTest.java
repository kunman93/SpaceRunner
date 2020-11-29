package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests methods in PowerUp.
 * @author kunnuman
 */
public class PowerUpTest {
    DoubleCoinsPowerUp doubleCoinsPowerUp;
    ShieldPowerUp shieldPowerUp;
    PowerUpListener powerUpListener;

    /**
     * Initializes the PowerUps and PowerUpListeners
     */
    @BeforeEach
    void setUp() {
        doubleCoinsPowerUp = new DoubleCoinsPowerUp(new Point2D.Double(1.0, 0.5));
        shieldPowerUp = new ShieldPowerUp(new Point2D.Double(1.0, 0.5));
        powerUpListener = new PowerUpListener() {
            @Override
            public void powerUpTimerChanged(double timeLeft) { }

            @Override
            public void powerUpFinished(PowerUp powerUp) {
                powerUp.removeListener(this);
            }
        };
    }

    /**
     * Tests if PowerUps are initialized with the correct multiplier and if they are incremented correctly. Tests also
     * if the powerUpTimer is reset properly.
     */
    @Test
    void incrementPowerUpMultiplierTest() {
        assertEquals(1, doubleCoinsPowerUp.getMultiplier());
        assertEquals(1, shieldPowerUp.getMultiplier());

        Set<PowerUpListener> powerUpListeners = doubleCoinsPowerUp.getPowerUpListeners();
        assertTrue(powerUpListeners.isEmpty());
        doubleCoinsPowerUp.addListener(powerUpListener);
        assertEquals(1,powerUpListeners.size());
        doubleCoinsPowerUp.createPowerUpTimer();

        try {
            Thread.sleep((long) (doubleCoinsPowerUp.getActiveTime() * (1.0/2.0)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(1,powerUpListeners.size());

        doubleCoinsPowerUp.incrementPowerUpMultiplier();
        shieldPowerUp.incrementPowerUpMultiplier();

        assertEquals(2, doubleCoinsPowerUp.getMultiplier());
        assertEquals(2, shieldPowerUp.getMultiplier());

        try {
            Thread.sleep((long) (doubleCoinsPowerUp.getActiveTime() * (3.0/4.0)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(1,powerUpListeners.size());

        try {
            Thread.sleep((long) (doubleCoinsPowerUp.getActiveTime() * (1.0/4.0) + 1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(powerUpListeners.isEmpty());
    }

    /**
     * Tests if the powerUpTimer and the powerUpTimerTasks are created and executed properly.
     */
    @Test
    void createPowerUpTimerTaskTest() {
        Set<PowerUpListener> powerUpListeners = doubleCoinsPowerUp.getPowerUpListeners();
        assertTrue(powerUpListeners.isEmpty());
        doubleCoinsPowerUp.addListener(powerUpListener);
        assertEquals(1,powerUpListeners.size());
        doubleCoinsPowerUp.createPowerUpTimer();
        try {
            Thread.sleep(doubleCoinsPowerUp.getActiveTime() + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(powerUpListeners.isEmpty());
    }

    /**
     * Tests if the DoubleCoinPowerUp-activation works properly.
     */
    @Test
    void activateDoubleCoinPowerUpTest(){
        Set<PowerUpListener> powerUpListeners = doubleCoinsPowerUp.getPowerUpListeners();
        assertTrue(powerUpListeners.isEmpty());
        doubleCoinsPowerUp.addListener(powerUpListener);
        assertEquals(1,powerUpListeners.size());
        doubleCoinsPowerUp.activatePowerUp();
        try {
            Thread.sleep(doubleCoinsPowerUp.getActiveTime() + 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(powerUpListeners.isEmpty());
    }
}
