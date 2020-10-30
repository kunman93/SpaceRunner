package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.HorizontalSpeed;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.VerticalSpeed;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UFO extends Obstacle {
    private boolean changeDirection = false;
    //TODO discuss how to set the speed and movement, eventually use strategy-Patter for different movements?
    private Point vectorUp = new Point(-HorizontalSpeed.UFO.getSpeed(),-VerticalSpeed.UFO.getSpeed());
    private Point vectorDown = new Point(-HorizontalSpeed.UFO.getSpeed(),VerticalSpeed.UFO.getSpeed());

    public UFO(Point startPosition, int width, int length) {
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
            descend();
        }else {
            ascend(currentPosition, topBorderLimitOfCanvas);
        }
    }

    @Override
    public void setVelocity(Point direction){
        vectorUp = new Point(direction.x, -direction.y);
        vectorDown = new Point(direction.x, direction.y);
    }

    private void ascend(Point currentPosition, int topBorderLimitOfCanvas) {
        changeDirection = true;
        super.setVelocity(vectorUp);
        super.move();
        if(reachedUpperThreshold(currentPosition, topBorderLimitOfCanvas)){
            changeDirection = false;
        }
    }

    private void descend() {
        super.setVelocity(vectorDown);
        super.move();
    }

    private boolean reachedUpperThreshold(Point currentPosition, int topBorderLimitOfCanvas) {
        return currentPosition.y < topBorderLimitOfCanvas;
    }

    private boolean reachedLowerThreshold(Point currentPosition, int bottomBorderLimitOfCanvas) {
        return currentPosition.y > bottomBorderLimitOfCanvas;
    }
}
