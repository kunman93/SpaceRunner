package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.controller.PowerUpType;

import java.awt.*;
import java.awt.geom.Point2D;

public class PowerUp extends SpaceElement {
    private static int powerUpHeight;
    private static int powerUpWidth;
    private PowerUpType type;

    public PowerUp(Point2D.Double startPosition, PowerUpType type) {
        super(startPosition);
        setElementHitbox();
        this.type = type;
    }

    public PowerUpType getType() {
        return type;
    }

    public void setType(PowerUpType type) {
        this.type = type;
    }

    @Override
    protected void setElementHitbox() {
        setHeight(powerUpHeight);
        setWidth(powerUpWidth);
    }

    public static void setClassHitbox(int height, int width) {
        powerUpHeight = height;
        powerUpWidth = width;
    }
}
