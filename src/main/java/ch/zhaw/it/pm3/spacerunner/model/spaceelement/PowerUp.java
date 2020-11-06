package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;

public class PowerUp extends SpaceElement {
    private static int powerUpHeight;
    private static int powerUpWidth;

    public PowerUp(Point startPosition) {
        super(startPosition);
        setElementHitbox();
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
