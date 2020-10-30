package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Asteroid extends Obstacle {
    //TODO visual is already in the superclass
    private static BufferedImage visual;
    private int asteroidSpeed = 1;
    private Point vectorAsteroid = new Point(-asteroidSpeed,asteroidSpeed);

    public Asteroid(Point startPosition, int width, int length) {
        super(startPosition, width, length);
    }

    @Override
    public void move() {
        setVelocity(vectorAsteroid);
        super.move();
    }
    //TODO: ask Rico why
    public static void setVisual(BufferedImage visual){
        Asteroid.visual = visual;
    }

    //TODO: ask Rico why
    @Override
    public BufferedImage getVisual() throws VisualNotSetException{
        if (visual == null) {
            throw new VisualNotSetException("Visual for Asteroid was not set!");
        }
        return visual;
    }

}
