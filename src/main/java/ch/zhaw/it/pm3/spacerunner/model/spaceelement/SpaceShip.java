package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.HorizontalSpeed;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.VerticalSpeed;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpaceShip extends SpaceElement {
    private boolean hasCrashed;

    private static Point UP = new Point(HorizontalSpeed.ZERO.getSpeed(), -VerticalSpeed.SPACE_SHIP.getSpeed());
    private static Point DOWN = new Point(HorizontalSpeed.ZERO.getSpeed(), VerticalSpeed.SPACE_SHIP.getSpeed());
    private static BufferedImage visual;

    public SpaceShip(Point startPosition, int width, int height){
        super(startPosition, width, height);
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

    /**
     * sets the spaceship speed for UP and DOWN
     * @param spaceShipSpeed
     */
    public void setSpaceShipSpeed(int spaceShipSpeed){
        UP = new Point(0,-spaceShipSpeed);
        DOWN = new Point(0,spaceShipSpeed);
    }

    public static void setVisual(BufferedImage visual){
        SpaceShip.visual = visual;
    }

    @Override
    public BufferedImage getVisual() throws VisualNotSetException{
        if (visual == null) {
            throw new VisualNotSetException("Visual for SpaceShip was not set!");
        }
        return visual;
    }

}
