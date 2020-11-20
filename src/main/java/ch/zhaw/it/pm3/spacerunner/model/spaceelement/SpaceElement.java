package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualElement;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SpaceElement implements VisualElement {

    private final Logger logger = Logger.getLogger(SpaceElement.class.getName());

    private final VisualManager visualManager = VisualManager.getManager();
    private final VelocityManager velocityManager = VelocityManager.getManager();
    //todo: Idee: Object die alle variablen (position, width, length) umfasst
    private Point2D.Double position = new Point2D.Double(0, 0);

    public SpaceElement(Point2D.Double startPosition) {
        this.position = startPosition;
    }

    /**
     * will change the position by the current velocity
     */
    public void move(long timeInMillis) {
        Point2D.Double velocity = null;
        try {
            velocity = velocityManager.getRelativeVelocity(this.getClass());
        } catch (VelocityNotSetException e) {
            logger.log(Level.SEVERE, "Velocity for {0} wasn't set", this.getClass());
        }

        position.x += (timeInMillis/1000.0) * velocity.x;
        position.y += (timeInMillis/1000.0) * velocity.y;
    }


    public Point2D.Double getRelativePosition() {
        return position;
    }

    public void setRelativePosition(Point2D.Double position) {
        this.position = position;
    }

    /**
     * @return Returns Point where the SpaceElement will be after one move()
     */
    public Point2D.Double getNextPosition(){
        Point2D.Double velocity = null;
        try {
            velocity = velocityManager.getRelativeVelocity(this.getClass());
        } catch (VelocityNotSetException e) {
            logger.log(Level.SEVERE, "Velocity for {0} wasn't set");
        }

        return new Point2D.Double(position.x + velocity.x, position.y + velocity.y);
    }


    /**
     * @param s other SpaceElement
     * @return Returns true if SpaceElements Overlapp
     */
    public boolean doesCollide(SpaceElement s){
        try {
            return pointInObject(s.getRelativePosition().x, s.getRelativePosition().y, this)
                    || pointInObject(s.getRelativePosition().x, s.getRelativePosition().y + visualManager.getElementRelativeHeight(s.getClass()), this)
                    || pointInObject(s.getRelativePosition().x + visualManager.getElementRelativeWidth(s.getClass()), s.getRelativePosition().y, this)
                    || pointInObject(s.getRelativePosition().x + visualManager.getElementRelativeWidth(s.getClass()), s.getRelativePosition().y + visualManager.getElementRelativeHeight(s.getClass()), this)
                    || pointInObject(position.x, position.y, s)
                    || pointInObject(position.x, position.y + visualManager.getElementRelativeHeight(this.getClass()), s)
                    || pointInObject(position.x + visualManager.getElementRelativeWidth(this.getClass()), position.y, s)
                    || pointInObject(position.x + visualManager.getElementRelativeWidth(this.getClass()), position.y + visualManager.getElementRelativeHeight(this.getClass()), s);
        }catch(VisualNotSetException e){
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", this.getClass());
            return true;
        }
    }

    private boolean pointInObject(double x, double y, SpaceElement s){
        try {
            return x >= s.getRelativePosition().x && x <= s.getRelativePosition().x + visualManager.getElementRelativeWidth(s.getClass()) && y >= s.getRelativePosition().y && y <= s.getRelativePosition().y + visualManager.getElementRelativeHeight(s.getClass());
        }catch(VisualNotSetException e){
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", this.getClass());
            return false;
        }
    }
}
