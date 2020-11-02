package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Asteroid extends Obstacle {
    private static BufferedImage visual;

    public Asteroid(Point startPosition, int width, int length) {
        super(startPosition, width, length);
    }

    public static void setVisual(BufferedImage visual){
        Asteroid.visual = visual;
    }

    @Override
    public BufferedImage getVisual() throws VisualNotSetException{
        if (visual == null) {
            throw new VisualNotSetException("Visual for Asteroid was not set!");
        }
        return visual;
    }

}
