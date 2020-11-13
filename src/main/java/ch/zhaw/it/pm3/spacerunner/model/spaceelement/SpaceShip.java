package ch.zhaw.it.pm3.spacerunner.model.spaceelement;



import java.awt.*;
import java.awt.geom.Point2D;

public class SpaceShip extends SpaceElement {
    private boolean hasCrashed;
    private VelocityManager velocityManager = VelocityManager.getInstance();

    public SpaceShip(Point2D.Double startPosition){
        super(startPosition);
    }

    public boolean hasCrashed() {
        return hasCrashed;
    }

    public void crash() {
        hasCrashed = true;
    }

    private void directMove(boolean invert){
        Point2D.Double velocity = null;
        try {
            velocity = velocityManager.getRelativeVelocity(this.getClass());
        } catch (VelocityNotSetException e) {
            //TODO: handle
            e.printStackTrace();
        }

        Point2D.Double position = getRelativePosition();

        if(invert){
            position.x -= velocity.x;
            position.y -= velocity.y;
        }else{
            position.x += velocity.x;
            position.y += velocity.y;
        }

        setRelativePosition(position);
    }

    public void directMoveUp(){
        directMove(true);
    }

    public void directMoveDown(){
        directMove(false);
    }

}
