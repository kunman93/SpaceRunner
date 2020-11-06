package ch.zhaw.it.pm3.spacerunner.model.spaceelement;



import java.awt.*;

public class SpaceShip extends SpaceElement {
    private boolean hasCrashed;
    private VelocityManager velocityManager = VelocityManager.getInstance();

    public SpaceShip(Point startPosition){
        super(startPosition);
    }

    public boolean hasCrashed() {
        return hasCrashed;
    }

    public void crash() {
        hasCrashed = true;
    }

    private void directMove(boolean invert){
        Point velocity = null;
        try {
            velocity = velocityManager.getVelocity(this.getClass());
        } catch (VelocityNotSetException e) {
            //TODO: handle
            e.printStackTrace();
        }

        Point position = getCurrentPosition();

        if(invert){
            position.x -= velocity.x;
            position.y -= velocity.y;
        }else{
            position.x += velocity.x;
            position.y += velocity.y;
        }

        setCurrentPosition(position);
    }

    public void directMoveUp(){
        directMove(true);
    }

    public void directMoveDown(){
        directMove(false);
    }

}
