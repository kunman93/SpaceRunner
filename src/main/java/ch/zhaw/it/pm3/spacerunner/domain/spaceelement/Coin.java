package ch.zhaw.it.pm3.spacerunner.domain.spaceelement;

import java.awt.geom.Point2D;

/**
 * Coin is a SpaceElement that can be collided with and picked up by the SpaceShip to increase the coin count.
 * Coin moves in a straight line.
 * @author nachbric
 */
public class Coin extends SpaceElement {

    public Coin(Point2D.Double startPosition) {
        super(startPosition);
    }

}
