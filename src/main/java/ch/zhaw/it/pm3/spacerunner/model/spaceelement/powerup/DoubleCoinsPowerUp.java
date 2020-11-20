package ch.zhaw.it.pm3.spacerunner.model.spaceelement.powerup;

import java.awt.geom.Point2D;

public class DoubleCoinsPowerUp extends PowerUp {
    private final int TIME_ACTIVE = 10000;

    public DoubleCoinsPowerUp(Point2D.Double startPosition) {
        super(startPosition);
    }

    @Override
    public int getActiveTime() {
        return TIME_ACTIVE;
    }

    @Override
    public void activatePowerUp() {
        createPowerUpTimer();
    }
}
