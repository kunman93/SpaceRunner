package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.HorizontalSpeed;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.VerticalSpeed;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UFO extends Obstacle {

    private boolean changeDirection = false;
    //TODO discuss how to set the speed and movement, eventually use strategy-Patter for different movements?
    private VisualManager visualManager = VisualManager.getInstance();
    private VelocityManager velocityManager = VelocityManager.getInstance();

    public UFO(Point startPosition) {
        super(startPosition);
    }

    @Override
    public void move() { //long timeInMillis
        //TODO: sinus curve for example
        Point currentPosition = getCurrentPosition();
        //TODO: access Canvas height and width? maybe as static variable
        int bottomBorderLimitOfCanvas = visualManager.getHeight();
        int topBorderLimitOfCanvas = 0;

        Point velocity = null;
        try {
            velocity = velocityManager.getVelocity(this.getClass());
        } catch (VelocityNotSetException e) {
            //TODO: handle
            e.printStackTrace();
        }

        if(!reachedLowerThreshold(currentPosition, bottomBorderLimitOfCanvas) && !changeDirection){
            descend(velocity, currentPosition);
        }else {
            ascend(velocity,currentPosition, topBorderLimitOfCanvas);
        }
    }

    private void ascend(Point velocity, Point currentPosition, int topBorderLimitOfCanvas) {
        changeDirection = true;

        currentPosition.x += velocity.x;
        currentPosition.y -= velocity.y;


        if(reachedUpperThreshold(currentPosition, topBorderLimitOfCanvas)){
            changeDirection = false;
        }
    }

    private void descend(Point velocity, Point currentPosition) {

        currentPosition.x += velocity.x;
        currentPosition.y += velocity.y;

    }

    private boolean reachedUpperThreshold(Point currentPosition, int topBorderLimitOfCanvas) {
        return currentPosition.y < topBorderLimitOfCanvas;
    }

    private boolean reachedLowerThreshold(Point currentPosition, int bottomBorderLimitOfCanvas) {
        return currentPosition.y > bottomBorderLimitOfCanvas;
    }
}
