package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.manager.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.manager.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

import java.awt.geom.Point2D;

public class SpaceWorld extends SpaceElement{
  public SpaceWorld(Point2D.Double startPosition) {
        super(startPosition);
    }

    private VisualManager visualManager = VisualManager.getManager();
    private VelocityManager velocityManager = VelocityManager.getManager();

    @Override
    public void move(long timeInMillis) {
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
            e.printStackTrace();
        } catch (VelocityNotSetException e) {
            e.printStackTrace();
        }

        setRelativePosition(position);
    }
}
