package ch.zhaw.it.pm3.spacerunner.domain.spaceelement;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualElement;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SpaceElement is the superclass to all Elements in the game (including SpaceShip, Coin, SpaceWorld and Obstacles)
 * It contains the code for initialization, movement and collision of these Elements.
 *
 * @author nachbric
 */
public abstract class SpaceElement implements VisualElement {

    private final Logger logger = Logger.getLogger(SpaceElement.class.getName());

    private final VisualManager visualManager = VisualManager.getManager();
    private final VelocityManager velocityManager = VelocityManager.getManager();
    private Point2D.Double position = new Point2D.Double(0, 0);

    /**
     * Initializes the variable position.
     *
     * @param startPosition The starting position of the element
     */
    public SpaceElement(Point2D.Double startPosition) {
        this.position = startPosition;
    }

    /**
     * Moves the element; the direction of which is determined by the velocity in VelocityManager.
     *
     * @param timeInMillis The time in milliseconds since the last time the element was moved.
     *                     If timeInMillis is larger the element will be moved a larger distance to prevent element movement from changing at different framerates.
     */
    public void move(long timeInMillis) {
        Point2D.Double velocity = null;
        try {
            velocity = velocityManager.getRelativeVelocity(this.getClass());
        } catch (VelocityNotSetException e) {
            logger.log(Level.SEVERE, "Velocity for {0} wasn't set", this.getClass());
        }

        position.x += (timeInMillis / 1000.0) * velocity.x;
        position.y += (timeInMillis / 1000.0) * velocity.y;
    }

    /**
     * @return The position of the element in relation to the height and width of the game-screen.
     */
    public Point2D.Double getRelativePosition() {
        return position;
    }

    /**
     * Sets the position of the element.
     *
     * @param position The position of the element in relation to the height and width of the game-screen.
     */
    public void setRelativePosition(Point2D.Double position) {
        this.position = position;
    }

    /**
     * @return Returns the relative position where the SpaceElement will be after one move(1000) (1 second of movement).
     */
    public Point2D.Double getNextPosition() {
        Point2D.Double velocity = null;
        try {
            velocity = velocityManager.getRelativeVelocity(this.getClass());
        } catch (VelocityNotSetException e) {
            logger.log(Level.SEVERE, "Velocity for {0} wasn't set");
        }

        return new Point2D.Double(position.x + velocity.x, position.y + velocity.y);
    }


    /**
     * Determines whether the SpaceElement is currently colliding with another SpaceElement.
     *
     * @param s The other SpaceElement
     * @return True if the two SpaceElements are colliding
     */
    public boolean doesCollide(SpaceElement s) {
        try {
            return pointInObject(s.getRelativePosition().x, s.getRelativePosition().y, this)
                    || pointInObject(s.getRelativePosition().x, s.getRelativePosition().y + visualManager.getElementRelativeHeight(s.getClass()), this)
                    || pointInObject(s.getRelativePosition().x + visualManager.getElementRelativeWidth(s.getClass()), s.getRelativePosition().y, this)
                    || pointInObject(s.getRelativePosition().x + visualManager.getElementRelativeWidth(s.getClass()), s.getRelativePosition().y + visualManager.getElementRelativeHeight(s.getClass()), this)
                    || pointInObject(position.x, position.y, s)
                    || pointInObject(position.x, position.y + visualManager.getElementRelativeHeight(this.getClass()), s)
                    || pointInObject(position.x + visualManager.getElementRelativeWidth(this.getClass()), position.y, s)
                    || pointInObject(position.x + visualManager.getElementRelativeWidth(this.getClass()), position.y + visualManager.getElementRelativeHeight(this.getClass()), s);
        } catch (VisualNotSetException e) {
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", this.getClass());
            return true;
        }
    }

    /**
     * Determines if a point is within the a SpaceElement.
     *
     * @param x The X value of the point
     * @param y The Y value of the point
     * @param s The SpaceElement
     * @return True if the points is within the SpaceElement.
     */
    private boolean pointInObject(double x, double y, SpaceElement s) {
        try {
            return x >= s.getRelativePosition().x
                    && x <= s.getRelativePosition().x + visualManager.getElementRelativeWidth(s.getClass())
                    && y >= s.getRelativePosition().y
                    && y <= s.getRelativePosition().y + visualManager.getElementRelativeHeight(s.getClass());
        } catch (VisualNotSetException e) {
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", this.getClass());
            return false;
        }
    }
}
