package ch.zhaw.it.pm3.spacerunner.model.element;

import java.awt.*;

public abstract class SpaceElement {

    //todo: Idee: Object die alle variablen (position, width, length) umfasst
    private Point position;
    private int width;
    private int length;
    private static Image visual;

    public SpaceElement(Point startPosition, int width, int length) throws Exception {
        if(visual == null){
            //TODO: handle
            throw new Exception();
        }
        this.width = width;
        this.length = length;
        this.position = startPosition;
    }

    public static void setVisual(Image visual){
        SpaceElement.visual = visual;
    }

    public Point getPosition() {
        return position;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public void move(Point movementPoint) {

    }

    public Point getCurrentPosition() {
        return null;
    }

    public Image getVisuals() {
        return visual;
    }
}
