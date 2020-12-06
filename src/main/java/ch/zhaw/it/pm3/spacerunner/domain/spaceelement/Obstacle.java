package ch.zhaw.it.pm3.spacerunner.domain.spaceelement;

import java.awt.geom.Point2D;

/**
 * Obstacle is an abstract class. All classes which extend Obstacle are SpaceElements that the SpaceShip can collide with to end the game.
 *
 * @author nachbric
 */
public abstract class Obstacle extends SpaceElement {

    /**
     * Initializes the variable position.
     *
     * @param startPosition The starting position of the Obstacle
     */
    public Obstacle(Point2D.Double startPosition) {
        super(startPosition);
    }
}
