package ch.zhaw.it.pm3.spacerunner.model.spaceelement;



import ch.zhaw.it.pm3.spacerunner.controller.SpaceShipDirection;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

import java.awt.*;
import java.awt.geom.Point2D;

public class SpaceShip extends SpaceElement {
    private boolean hasCrashed;
    private VelocityManager velocityManager = VelocityManager.getInstance();
    private final VisualManager visualManager = VisualManager.getInstance();

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
                    setRelativePosition(new Point2D.Double(position.x - relativeHeight, 1.0));
                    return;
                }
                directMove(direction, position, timeInMillis);
                break;
        }
    }

}
