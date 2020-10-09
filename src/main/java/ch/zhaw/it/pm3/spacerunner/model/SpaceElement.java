package ch.zhaw.it.pm3.spacerunner.model;

import java.awt.*;

public abstract class SpaceElement {

    //todo: Idee: Object die alle variablen (position, width, length) umfasst
    private Point position;
    private int width;
    private int length;

    public SpaceElement(Point startPosition, int width, int length) {
        this.width = width;
        this.length = length;
        this.position = startPosition;
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

    public boolean move(int gameSpeed) {
        return false;
    }
}
