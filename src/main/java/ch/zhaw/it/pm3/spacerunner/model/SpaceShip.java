package ch.zhaw.it.pm3.spacerunner.model;

import java.awt.*;

public class SpaceShip extends SpaceElement {
    private boolean hasCrashed;
    private int verticalMovementSpeed;
    private boolean hasPowerUp;

    public SpaceShip(Point startPosition, int width, int length) {
        super(startPosition, width, length);
    }

    public boolean getHasCrashed() {
        return hasCrashed;
    }

    public void crash() {
        hasCrashed = true;
    }

    public boolean getHasPowerup() {
        return hasPowerUp;
    }

    @Override
    public boolean move(int verticalMovement) { // 1,0,-1
        return false;
    }
}
