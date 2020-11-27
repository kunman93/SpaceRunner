package ch.zhaw.it.pm3.spacerunner.domain.spaceelement;


import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SpaceShip is the SpaceElement that the player controls. It moves vertically in a straight line and cannot leave the visible game-screen.
 * @author nachbric
 */
public class SpaceShip extends SpaceElement {

    private final Logger logger = Logger.getLogger(SpaceShip.class.getName());

    private final VelocityManager velocityManager = VelocityManager.getManager();
    private final VisualManager visualManager = VisualManager.getManager();

    /**
     * Initializes the variable position.
     * @param startPosition The starting position of the SpaceShip
     */
    public SpaceShip(Point2D.Double startPosition) {
        super(startPosition);
    }

    private void directMove(SpaceShipDirection direction, Point2D.Double position, long timeInMillis) {
        Point2D.Double velocity = null;
        try {
            velocity = velocityManager.getRelativeVelocity(this.getClass());
        } catch (VelocityNotSetException e) {
            logger.log(Level.SEVERE, "Velocity for {0} wasn't set", this.getClass());
        }

        if (direction == SpaceShipDirection.UP) {
            position.x -= timeInMillis / 1000.0 * velocity.x;
            position.y -= timeInMillis / 1000.0 * velocity.y;
        } else if (direction == SpaceShipDirection.DOWN) {
            position.x += timeInMillis / 1000.0 * velocity.x;
            position.y += timeInMillis / 1000.0 * velocity.y;
        }

        setRelativePosition(position);
    }

    /**
     * Moves the spaceship
     * @param direction The direction of movement (UP or DOWN)
     */
    public void moveSpaceShip(SpaceShipDirection direction, long timeInMillis) {
        double relativeHeight = 0;
        try {
            relativeHeight = visualManager.getElementRelativeHeight(this.getClass());
        } catch (VisualNotSetException e) {
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", this.getClass());
        }
        Point2D.Double position = getRelativePosition();
        switch (direction) {
            case UP:
                if (position.y <= 0.0) {
                    setRelativePosition(new Point2D.Double(position.x, 0.0));
                    return;
                }
                directMove(direction, position, timeInMillis);
                break;
            case DOWN:
                if (position.y + relativeHeight >= 1.0) {
                    setRelativePosition(new Point2D.Double(position.x, 1.0 - relativeHeight));
                    return;
                }
                directMove(direction, position, timeInMillis);
                break;
        }
    }
}
