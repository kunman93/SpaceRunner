package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SpaceWorld extends SpaceElement{
    private static int spaceWorldHeight;
    private static int spaceWorldWidth;

    public SpaceWorld(Point startPosition, int width, int height) {
        super(startPosition);
        spaceWorldHeight = height;
        spaceWorldWidth = width;
        setElementHitbox();
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

    @Override
    protected void setElementHitbox() {
        setHeight(spaceWorldHeight);
        setWidth(spaceWorldWidth);
    }
}
