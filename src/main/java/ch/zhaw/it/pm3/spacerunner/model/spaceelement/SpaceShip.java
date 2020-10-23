package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;

public class SpaceShip extends SpaceElement {
    private boolean hasCrashed;
    private int verticalMovementSpeed;

    public SpaceShip(Point startPosition, int width, int length) throws Exception {
        super(startPosition, width, length);
    }

    public boolean getHasCrashed() {
        return hasCrashed;
    }

    public void crash() {
        hasCrashed = true;
    }

}
