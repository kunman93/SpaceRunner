package ch.zhaw.it.pm3.spacerunner.model.spaceelement;



import ch.zhaw.it.pm3.spacerunner.controller.SpaceShipDirection;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

import java.awt.geom.Point2D;

public class SpaceShip extends SpaceElement {
    private boolean hasCrashed;
    private VelocityManager velocityManager = VelocityManager.getManager();
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
        Point2D.Double position = getRelativePosition();
        switch (direction) {
            case UP:
                if (position.y <= 0.0){
                    return;
                }
                directMove(direction, position, timeInMillis);
                break;
            case DOWN:
                try {
                    //TODO: fix spaceship out of view
                    if (position.y + visualManager.getElementRelativeHeight(this.getClass()) >= 1.0) {
                        return;
                    }
                } catch (VisualNotSetException e) {
                    //TODO: handle
                    e.printStackTrace();
                }
                directMove(direction, position, timeInMillis);
                break;
        }
    }

}
