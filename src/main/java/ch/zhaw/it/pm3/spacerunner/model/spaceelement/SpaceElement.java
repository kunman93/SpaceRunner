package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualElement;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

import java.awt.*;
import java.awt.geom.Point2D;

public abstract class SpaceElement implements VisualElement {

    private VisualManager visualManager = VisualManager.getInstance();
    private VelocityManager velocityManager = VelocityManager.getInstance();
    //todo: Idee: Object die alle variablen (position, width, length) umfasst
    private Point position = new Point(0, 0);

    public SpaceElement(Point startPosition) {
        this.position = startPosition;
    }

    /**
     * will change the position by the current velocity
     */
    public void move() { //long timeInMillis
        Point velocity = null;
        try {
            velocity = velocityManager.getVelocity(this.getClass());
        } catch (VelocityNotSetException e) {
            //TODO: handle
            e.printStackTrace();
        }

        position.x += velocity.x; //timeInMillis/1000 *
        position.y += velocity.y; //timeInMillis/1000 *
    }


    public Point getCurrentPosition() {
        return position;
    }

    public void setCurrentPosition(Point position) {
        this.position = position;
    }

    /**
     * @return Returns Point where the SpaceElement will be after one move()
     */
    public Point getNextPosition(){
        Point velocity = null;
        try {
            velocity = velocityManager.getVelocity(this.getClass());
        } catch (VelocityNotSetException e) {
            //TODO: handle
            e.printStackTrace();
        }

        return new Point(position.x + velocity.x, position.y + velocity.y);
    }


    /**
     * @param s other SpaceElement
     * @return Returns true if SpaceElements Overlapp
     */
    public boolean doesCollide(SpaceElement s){
        try {
            return pointInObject(s.getCurrentPosition().x, s.getCurrentPosition().y, this)
                    || pointInObject(s.getCurrentPosition().x, s.getCurrentPosition().y + visualManager.getElementPixelHeight(s.getClass()), this)
                    || pointInObject(s.getCurrentPosition().x + visualManager.getElementPixelWidth(s.getClass()), s.getCurrentPosition().y, this)
                    || pointInObject(s.getCurrentPosition().x + visualManager.getElementPixelWidth(s.getClass()), s.getCurrentPosition().y + visualManager.getElementPixelHeight(s.getClass()), this)
                    || pointInObject(position.x, position.y, s)
                    || pointInObject(position.x, position.y + visualManager.getElementPixelHeight(this.getClass()), s)
                    || pointInObject(position.x + visualManager.getElementPixelWidth(this.getClass()), position.y, s)
                    || pointInObject(position.x + visualManager.getElementPixelWidth(this.getClass()), position.y + visualManager.getElementPixelHeight(this.getClass()), s);
        }catch(VisualNotSetException e){
            //TODO: handle
            e.printStackTrace();
            return true;
        }
    }

    private boolean pointInObject(float x, float y, SpaceElement s){
        try {
            return x > s.getCurrentPosition().x && x < s.getCurrentPosition().x + visualManager.getElementPixelWidth(s.getClass()) && y > s.getCurrentPosition().y && y < s.getCurrentPosition().y + visualManager.getElementPixelHeight(s.getClass());
        }catch(VisualNotSetException e){
            //TODO: handle
            e.printStackTrace();
            return false;
        }
    }

}
