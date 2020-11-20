package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.VerticalSpeed;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UFO extends Obstacle {

    private final Logger logger = Logger.getLogger(UFO.class.getName());

    private final VisualManager visualManager = VisualManager.getManager();
    private final VelocityManager velocityManager = VelocityManager.getManager();
    private double waveOffset = 0;

    public UFO(Point2D.Double startPosition) {
        super(startPosition);
    }

    public UFO(Point2D.Double startPosition, double waveOffset) {
        super(startPosition);
        this.waveOffset = waveOffset;
    }

    @Override
    public void move(long timeInMillis) {
        double currentXPos = getRelativePosition().x;
        Point2D.Double currentPos = new Point2D.Double(currentXPos, sinWave(currentXPos));
        setRelativePosition(currentPos);
        double nextXPos = getNextPosition().x;
        Point2D.Double nextPos = new Point2D.Double(nextXPos, sinWave(nextXPos));
        Point2D.Double velocity = new Point2D.Double(nextPos.x - currentPos.x, nextPos.y - currentPos.y);

        //TODO: Velocity should be considered inside here and not updated
        velocityManager.setVelocity(UFO.class, velocity);

        super.move(timeInMillis);
    }

    private double sinWave(double currentXPos) {
        try {
            return 0.35 * Math.sin(currentXPos * VerticalSpeed.UFO.getSpeed() + 1 + 2 * Math.PI * waveOffset) + 0.5 - 0.5 * visualManager.getElementRelativeHeight(UFO.class);
        } catch (VisualNotSetException e) {
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", UFO.class);
            e.printStackTrace();
        }
        return 0;
    }
}
