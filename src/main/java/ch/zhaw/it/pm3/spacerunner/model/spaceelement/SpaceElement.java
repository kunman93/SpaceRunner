package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class SpaceElement {

    //todo: Idee: Object die alle variablen (position, width, length) umfasst
    private Point position;
    private int height;
    private int width;
    private static BufferedImage visual;

    public SpaceElement(Point startPosition, int height, int length) throws Exception {
        if(visual == null){
            //TODO: handle
            throw new Exception();
        }
        this.height = height;
        this.width = length;
        this.position = startPosition;
    }

    public static void setVisual(BufferedImage visual){
        SpaceElement.visual = visual;
    }

    public Point getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void move(Point movementPoint) {
        position.setLocation(position.getX() + movementPoint.getX(), position.getY() + movementPoint.getY());
    }

    public BufferedImage getVisual() {
        return visual;
    }
}
