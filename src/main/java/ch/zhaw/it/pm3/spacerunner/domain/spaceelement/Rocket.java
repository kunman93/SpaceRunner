package ch.zhaw.it.pm3.spacerunner.domain.spaceelement;

import java.awt.geom.Point2D;

/**
 * Rocket is a type of obstacle in the Space-Runner Game, which moves horizontally in a straight line.
 * @author hirsceva
 */
public class Rocket extends Obstacle {
    public Rocket(Point2D.Double startPosition) {
        super(startPosition);
    }
}
