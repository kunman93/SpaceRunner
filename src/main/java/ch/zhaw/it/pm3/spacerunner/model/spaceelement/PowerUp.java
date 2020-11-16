package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.geom.Point2D;

public class PowerUp extends SpaceElement {
    private PowerUpType type;

    public PowerUp(Point2D.Double startPosition, PowerUpType type) {
        super(startPosition);
        this.type = type;
    }

    public PowerUpType getType() {
        return type;
    }

    public void setType(PowerUpType type) {
        this.type = type;
    }
}
