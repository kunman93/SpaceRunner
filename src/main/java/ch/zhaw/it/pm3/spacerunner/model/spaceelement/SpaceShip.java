package ch.zhaw.it.pm3.spacerunner.model.spaceelement;



import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpaceShip extends SpaceElement {

    private Logger logger = Logger.getLogger(SpaceShip.class.getName());

    private boolean hasCrashed;

    private final VelocityManager velocityManager = VelocityManager.getManager();
    private final VisualManager visualManager = VisualManager.getManager();


    public SpaceShip(Point2D.Double startPosition){
        super(startPosition);
    }

    public boolean hasCrashed() {
        return hasCrashed;
    }

    public void crash() {
        hasCrashed = true;
    }

    private void directMove(SpaceShipDirection direction, Point2D.Double position, long timeInMillis){
        Point2D.Double velocity = null;
        try {
            velocity = velocityManager.getRelativeVelocity(this.getClass());
        } catch (VelocityNotSetException e) {
            //TODO: handle
            logger.log(Level.SEVERE, "Velocity for {0} wasn't set", this.getClass());
            e.printStackTrace();
        }

        if(direction == SpaceShipDirection.UP){
            position.x -= timeInMillis/1000.0 * velocity.x;
            position.y -= timeInMillis/1000.0 * velocity.y;
        }else if(direction == SpaceShipDirection.DOWN){
            position.x += timeInMillis/1000.0 * velocity.x;
            position.y += timeInMillis/1000.0 * velocity.y;
        }

        setRelativePosition(position);
    }

    /**
     * Moves the spaceship
     *
     * @param direction The direction of movement (UP,DOWN or NONE)
     */
    public void moveSpaceShip(SpaceShipDirection direction, long timeInMillis) {
        double relativeHeight = 0;
        try {
            relativeHeight = visualManager.getElementRelativeHeight(this.getClass());
        } catch (VisualNotSetException e) {
            //TODO: Handle
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", this.getClass());
            e.printStackTrace();
        }
        Point2D.Double position = getRelativePosition();
        switch (direction) {
            case UP:
                if (position.y <= 0.0){
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
