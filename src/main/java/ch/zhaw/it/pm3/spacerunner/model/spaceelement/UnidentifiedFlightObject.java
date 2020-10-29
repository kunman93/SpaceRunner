package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UnidentifiedFlightObject extends Obstacle {
    private boolean changeDirection = false;

    public UnidentifiedFlightObject(Point startPosition, int width, int length) {
        super(startPosition, width, length);
    }

    @Override
    public void move() {
        //TODO: sinus curve for example
        Point currentPosition = getCurrentPosition();
        //TODO: access Canvas height?
        int heightLowerLimit = 200;
        int heightUpperLimit = 20;

        if(reachedLowerThreshold(currentPosition, heightLowerLimit) && !changeDirection){
            //TODO: maybe use move()
            currentPosition.y++;
        }else {
            changeDirection = true;
            //TODO: maybe use move()
            currentPosition.y--;
            if(!reachedUpperThreshold(currentPosition, heightUpperLimit)){
                changeDirection = false;
            }
        }

        currentPosition.x--;
        setCurrentPosition(currentPosition);
    }

    private boolean reachedUpperThreshold(Point currentPosition, int heightLowerLimit) {
        return currentPosition.y > heightLowerLimit;
    }

    private boolean reachedLowerThreshold(Point currentPosition, int heightUpperLimit) {
        return currentPosition.y < heightUpperLimit;
    }
}
