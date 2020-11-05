package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpaceWorld extends SpaceElement{

    public SpaceWorld(Point startPosition) {
        super(startPosition);
    }

    @Override
    public void move() {
        Point position = getCurrentPosition();

        //TODO: How do we get this value?
        //TODO: We can make it fixed
        int viewport = 960;

        if(position.x + getWidth() - viewport < 0){
            position.x = 0;
        }else{
            position.x += getVelocity().x;
        }

        setCurrentPosition(position);
    }
}
