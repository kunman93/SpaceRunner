package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

import java.awt.*;

public class SpaceWorld extends SpaceElement{
  public SpaceWorld(Point startPosition) {
        super(startPosition);
    }

    private VisualManager visualManager = VisualManager.getInstance();

    @Override
    public void move() {
        Point position = getCurrentPosition();

        //TODO: Fix background visualManager.getWidth()
        try {
            if(position.x + visualManager.getElementPixelWidth(SpaceWorld.class) - 960 < 0){
                position.x = 0;
            }else{
                position.x += getVelocity().x;
            }
        } catch (VisualNotSetException e) {
            //TODO: handle
            e.printStackTrace();
        }

        setCurrentPosition(position);
    }
}
