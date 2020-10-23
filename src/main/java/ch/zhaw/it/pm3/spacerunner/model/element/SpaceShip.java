package ch.zhaw.it.pm3.spacerunner.model.element;

import java.awt.*;

public class SpaceShip extends SpaceElement {
    private boolean hasCrashed;

    private static Point UP = new Point(0,-1);
    private static Point DOWN = new Point(0,1);

    public SpaceShip(Point startPosition, int width, int length, Image visual) throws Exception {
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
