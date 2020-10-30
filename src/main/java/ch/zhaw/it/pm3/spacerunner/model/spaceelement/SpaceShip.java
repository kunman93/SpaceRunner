package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpaceShip extends SpaceElement {
    private boolean hasCrashed;

    private static int spaceShipSpeed = 3;
    private static Point UP = new Point(0,-spaceShipSpeed);
    private static Point DOWN = new Point(0,spaceShipSpeed);
    private static BufferedImage visual; //TODO: why Rico?

    public SpaceShip(Point startPosition, int width, int length){
        super(startPosition, width, length);
    }

    public boolean hasCrashed() {
        return hasCrashed;
    }

    public void crash() {
        hasCrashed = true;
    }

    private void directMove(Point direction){
        setVelocity(direction);
        move();
    }

    public void directMoveUp(){
        directMove(UP);
    }

    public void directMoveDown(){
        directMove(DOWN);
    }

    public static void setSpaceShipSpeed(int spaceShipSpeed){
        SpaceShip.spaceShipSpeed = spaceShipSpeed;
        UP = new Point(0,-spaceShipSpeed);
        DOWN = new Point(0,spaceShipSpeed);
    }

    public static int getSpaceShipSpeed(){
        return spaceShipSpeed;
    }

    public static void setVisual(BufferedImage visual){
        SpaceShip.visual = visual;
    }

    //TODO: why Rico?
    @Override
    public BufferedImage getVisual() throws VisualNotSetException{
        if (visual == null) {
            throw new VisualNotSetException("Visual for SpaceShip was not set!");
        }
        return visual;
    }

}
