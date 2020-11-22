package ch.zhaw.it.pm3.spacerunner.domain.spaceelement;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.speed.VerticalSpeed;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UFO is a type of obstacle in the Space-Runner Game, which moves to the left in a wavey motion.
 * @author hirsceva
 */
public class UFO extends Obstacle {

    private final Logger logger = Logger.getLogger(UFO.class.getName());

    private final VisualManager visualManager = VisualManager.getManager();
    private double waveOffset = 0;

    /**
     * Initializes the variable position.
     * @param startPosition The starting position of the UFO
     */
    public UFO(Point2D.Double startPosition) {
        super(startPosition);
    }

    /**
     * Initializes the variable position and waveOffset.
     * @param startPosition The starting position of the UFO
     * @param waveOffset The number of wavelengths that the movement pattern of the UFO is shifted to the left by
     */
    public UFO(Point2D.Double startPosition, double waveOffset) {
        super(startPosition);
        this.waveOffset = waveOffset;
    }

    /**
     * Moves the UFO in a wavey motion (similar to a sine wave).
     * @param timeInMillis The time in milliseconds since the last time the element was moved. If timeInMillis is larger the element will be moved a larger distance to prevent element movement from changing at different framerates.
     */
    @Override
    public void move(long timeInMillis) {
        double currentXPos = getRelativePosition().x;
        Point2D.Double currentPos = new Point2D.Double(currentXPos, sinWave(currentXPos));
        double nextXPos = getNextPosition().x;
        Point2D.Double velocity = new Point2D.Double(nextXPos - currentXPos, sinWave(currentXPos) - sinWave(nextXPos));
        Point2D.Double nextPos = new Point2D.Double(currentPos.x + (timeInMillis/1000.0)*velocity.x, currentPos.y + (timeInMillis/1000.0)*velocity.y);
        setRelativePosition(nextPos);
    }

    private double sinWave(double xPos) {
        try {
            return 0.35 * Math.sin(xPos * VerticalSpeed.UFO.getSpeed() + 1 + 2 * Math.PI * waveOffset) + 0.5 - 0.5 * visualManager.getElementRelativeHeight(UFO.class);
        } catch (VisualNotSetException e) {
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", UFO.class);
            e.printStackTrace();
        }
        return 0;
    }
}
