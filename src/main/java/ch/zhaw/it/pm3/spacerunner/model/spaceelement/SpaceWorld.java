package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SpaceWorld extends SpaceElement{
  public SpaceWorld(Point2D.Double startPosition) {
        super(startPosition);
    }

    private VisualManager visualManager = VisualManager.getManager();
    private VelocityManager velocityManager = VelocityManager.getManager();

    @Override
    public void move(long timeInMillis) {

        Logger logger = Logger.getLogger(SpaceWorld.class.getName());

        Point2D.Double position = getRelativePosition();

        //TODO: Fix background visualManager.getWidth()
        try {
            if(position.x + visualManager.getElementRelativeWidth(SpaceWorld.class) < 1){
                position.x = 0;
            }else{
                position.x += (timeInMillis/1000.0 *velocityManager.getRelativeVelocity(this.getClass()).x);
            }
        } catch (VisualNotSetException e) {
            //TODO: handle
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", SpaceWorld.class);
            e.printStackTrace();
        } catch (VelocityNotSetException e) {
            logger.log(Level.SEVERE, "Velocity for {0} wasn't set", this.getClass());
            e.printStackTrace();
        }
        setRelativePosition(position);
    }
}
