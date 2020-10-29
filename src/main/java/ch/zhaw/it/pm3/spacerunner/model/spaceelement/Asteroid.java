package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Asteroid extends Obstacle {
    private static BufferedImage visual;

    public Asteroid(Point startPosition, int width, int length) {
        super(startPosition, width, length);
    }

    @Override
    public void move() {
        Point currentPosition = getCurrentPosition();
        currentPosition.x--;
        currentPosition.y++;
        setCurrentPosition(currentPosition);
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
