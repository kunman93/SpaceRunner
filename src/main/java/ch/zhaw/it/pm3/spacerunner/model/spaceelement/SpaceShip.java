package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpaceShip extends SpaceElement {
    private boolean hasCrashed;

    private static final int spaceShipSpeed = 3;
    private static final Point UP = new Point(0,-spaceShipSpeed);
    private static final Point DOWN = new Point(0,spaceShipSpeed);

    public SpaceShip(Point startPosition, int width, int length, BufferedImage visual) throws Exception {
        super(startPosition, width, length, visual);
    }

    public boolean getHasCrashed() {
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

}
