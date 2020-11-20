package ch.zhaw.it.pm3.spacerunner.domain.spaceelement;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpaceWorld extends SpaceElement {
    public SpaceWorld(Point2D.Double startPosition) {
        super(startPosition);
    }

    private final VisualManager visualManager = VisualManager.getManager();
    private final VelocityManager velocityManager = VelocityManager.getManager();

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
