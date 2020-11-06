package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;

public class Asteroid extends Obstacle {
    private static int asteroidHeight;
    private static int asteroidWidth;

    public Asteroid(Point startPosition) {
        super(startPosition);
        setElementHitbox();
    }

    @Override
    protected void setElementHitbox() {
        setHeight(asteroidHeight);
        setWidth(asteroidWidth);
    }

    public static void setClassHitbox(int height, int width) {
        asteroidHeight = height;
        asteroidWidth = width;
    }
}
