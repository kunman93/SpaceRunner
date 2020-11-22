package ch.zhaw.it.pm3.spacerunner.domain.spaceelement;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The background of the game.
 * @author islermic
 */
public class SpaceWorld extends SpaceElement {

    /**
     * Initializes the variable position.
     * @param startPosition The starting position of the SpaceWorld
     */
    public SpaceWorld(Point2D.Double startPosition) {
        super(startPosition);
    }

    private final VisualManager visualManager = VisualManager.getManager();
    private final VelocityManager velocityManager = VelocityManager.getManager();

    /**
     * Moves the SpaceWorld to make it seem as if the background is looping.
     * @param timeInMillis The time in milliseconds since the last time the SpaceWorld was moved. If timeInMillis is larger the SpaceWorld will be moved a larger distance to prevent element movement from changing at different framerates.
     */
    @Override
    public void move(long timeInMillis) {

        Logger logger = Logger.getLogger(SpaceWorld.class.getName());

        Point2D.Double position = getRelativePosition();

        try {
            if (position.x + visualManager.getElementRelativeWidth(SpaceWorld.class) < 1) {
                position.x = 0;
            } else {
                position.x += (timeInMillis / 1000.0 * velocityManager.getRelativeVelocity(this.getClass()).x);
            }
        } catch (VisualNotSetException e) {
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", SpaceWorld.class);
        } catch (VelocityNotSetException e) {
            logger.log(Level.SEVERE, "Velocity for {0} wasn't set", this.getClass());
        }
        setRelativePosition(position);
    }
}
