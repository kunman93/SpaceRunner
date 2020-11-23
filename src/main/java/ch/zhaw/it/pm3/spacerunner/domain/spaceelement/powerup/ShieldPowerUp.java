package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup;

import java.awt.geom.Point2D;

/**
 * The ShieldPowerUp is a power-up which gives the spaceship an extra shield.
 * The spaceship can collide once with an obstacle without losing the game when this power-up is active.
 * @author nachbric
 */
public class ShieldPowerUp extends PowerUp {
    private final int TIME_ACTIVE = 0;

    /**
     * Sets up the startPosition where ShieldPowerUp should appear in the game
     * @param startPosition The startPosition where the power-up should appear.
     */
    public ShieldPowerUp(Point2D.Double startPosition) {
        super(startPosition);
    }

    @Override
    public int getActiveTime() {
        return TIME_ACTIVE;
    }

    @Override
    public void activatePowerUp() {
    }
}
