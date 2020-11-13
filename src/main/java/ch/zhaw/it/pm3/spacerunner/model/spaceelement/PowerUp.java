package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.controller.PowerUpType;

import java.awt.*;

public class PowerUp extends SpaceElement {
    private static int powerUpHeight;
    private static int powerUpWidth;
    private PowerUpType type;

    public PowerUp(Point startPosition, PowerUpType type) {
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
