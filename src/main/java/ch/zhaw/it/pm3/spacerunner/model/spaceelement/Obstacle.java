package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.geom.Point2D;

public abstract class Obstacle extends SpaceElement {
    public Obstacle(Point2D.Double startPosition) {
        super(startPosition);
    }
}
