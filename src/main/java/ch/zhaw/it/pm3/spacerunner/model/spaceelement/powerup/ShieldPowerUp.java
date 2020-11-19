package ch.zhaw.it.pm3.spacerunner.model.spaceelement.powerup;

import java.awt.geom.Point2D;

public class ShieldPowerUp extends PowerUp{
    private final int TIME_ACTIVE = 0;

    public ShieldPowerUp(Point2D.Double startPosition) {
        super(startPosition);
    }

    @Override
    public int getActiveTime() {
        return TIME_ACTIVE;
    }

    @Override
    public void activatePowerUp() {}
}
