package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests methods in PowerUp.
 * @author hirsceva
 */
public class PowerUpTest {
    DoubleCoinsPowerUp d;
    ShieldPowerUp s;

    /**
     * Initializes the PowerUps 'DoubleCoinsPowerUp d' and 'ShieldPowerUp s'.
     */
    @BeforeEach
    void setUp() {
        d = new DoubleCoinsPowerUp(new Point2D.Double(1.0, 0.5));
        s = new ShieldPowerUp(new Point2D.Double(1.0, 0.5));
    }

    /**
     * Tests if PowerUps are initialized with the correct multiplier and tests if they are incremented correctly.
     */
    @Test
    void incrementPowerUpMultiplierTest() {
        assertEquals(1, d.getMultiplier());
        assertEquals(1, s.getMultiplier());
        d.incrementPowerUpMultiplier();
        s.incrementPowerUpMultiplier();
        assertEquals(2, d.getMultiplier());
        assertEquals(2, s.getMultiplier());
    }

    /**
     * Tests if the PowerUps return the correct time that they are active (10000 for Double Coins and 0 for Shield).
     */
    @Test
    void getActiveTimeTest() {
        assertEquals(10000, d.getActiveTime());
        assertEquals(0, s.getActiveTime());
    }
}
