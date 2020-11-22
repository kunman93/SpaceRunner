package ch.zhaw.it.pm3.spacerunner.domain.spaceelement;

import java.awt.geom.Point2D;

/**
 * Asteroid is a type of obstacle in the Space-Runner Game, which moves diagonally.
 * @author nachbric
 */
public class Asteroid extends Obstacle {

    /**
     * Initializes the variable position.
     * @param startPosition The starting position of the Asteroid
     */
    public Asteroid(Point2D.Double startPosition) {
        super(startPosition);
    }

}
