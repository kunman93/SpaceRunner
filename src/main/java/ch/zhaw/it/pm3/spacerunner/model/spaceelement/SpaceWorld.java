package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

import java.awt.*;
import java.awt.geom.Point2D;

public class SpaceWorld extends SpaceElement{
  public SpaceWorld(Point2D.Double startPosition) {
        super(startPosition);
    }

    private VisualManager visualManager = VisualManager.getInstance();
    private VelocityManager velocityManager = VelocityManager.getInstance();

    @Override
    public void move() { //long timeInMillis
        Point2D.Double position = getRelativePosition();

        //TODO: Fix background visualManager.getWidth()
        try {
            if(position.x + visualManager.getElementPixelWidth(SpaceWorld.class) - visualManager.getWidth() < 0){
                position.x = 0;
            }else{
                position.x += velocityManager.getRelativeVelocity(this.getClass()).x; //timeInMillis/1000 *
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
