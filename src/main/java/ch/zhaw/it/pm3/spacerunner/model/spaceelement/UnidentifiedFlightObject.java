package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UnidentifiedFlightObject extends Obstacle {
    private boolean changeDirection = false;
    //TODO discuss how to set the speed and movement, eventually use strategy-Patter for different movements?
    private int unidentifiedFlightObjectHorizontalSpeed = 5;
    private int unidentifiedFlightObjectVerticalSpeed = 5;
    private Point vectorUp = new Point(-unidentifiedFlightObjectHorizontalSpeed,-unidentifiedFlightObjectVerticalSpeed);
    private Point vectorDown = new Point(-unidentifiedFlightObjectHorizontalSpeed,unidentifiedFlightObjectVerticalSpeed);

    public UnidentifiedFlightObject(Point startPosition, int width, int length) {
        super(startPosition, width, length);
    }

    @Override
    public void move() {
        //TODO: sinus curve for example
        Point currentPosition = getCurrentPosition();
        //TODO: access Canvas height and width? maybe as static variable
        int bottomBorderLimitOfCanvas = 200;
        int topBorderLimitOfCanvas = 20;

        if(!reachedLowerThreshold(currentPosition, bottomBorderLimitOfCanvas) && !changeDirection){
            descend(vectorDown);
        }else {
            ascend(currentPosition, topBorderLimitOfCanvas);
        }
    }

    private void ascend(Point currentPosition, int topBorderLimitOfCanvas) {
        changeDirection = true;
        setVelocity(vectorUp);
        super.move();
        if(reachedUpperThreshold(currentPosition, topBorderLimitOfCanvas)){
            changeDirection = false;
        }
    }

    private void descend(Point vectorDown) {
        setVelocity(vectorDown);
        super.move();
    }

    private boolean reachedUpperThreshold(Point currentPosition, int topBorderLimitOfCanvas) {
        return currentPosition.y < topBorderLimitOfCanvas;
    }

    private boolean reachedLowerThreshold(Point currentPosition, int bottomBorderLimitOfCanvas) {
        return currentPosition.y > bottomBorderLimitOfCanvas;
    }
}
