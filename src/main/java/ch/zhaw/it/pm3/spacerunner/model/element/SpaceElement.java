package ch.zhaw.it.pm3.spacerunner.model.element;

import java.awt.*;

public abstract class SpaceElement {

    //todo: Idee: Object die alle variablen (position, width, length) umfasst
    private Point position;
    private Point velocity;
    private int width;
    private int length;
    private static Image visual;


    public SpaceElement(Point startPosition, int width, int length, Image visual) throws Exception {
        this.visual = visual;
        this.width = width;
        this.length = length;
        this.position = startPosition;
    }

    public static void setVisual(Image visual){
        SpaceElement.visual = visual;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public void move() {
        position.x += velocity.x;
        position.y += velocity.y;
    }

    public void accelerate(Point direction){
        if (direction == null){
            velocity.x += 0;
            velocity.y += 0;
        } else {
            velocity.x += direction.x;
            velocity.y += direction.y;
        }
    }

    public void setVelocity(Point direction){
        if (direction == null){
            velocity.x = 0;
            velocity.y = 0;
        } else {
            velocity.x = direction.x;
            velocity.y = direction.y;
        }
    }

    public Point getCurrentPosition() {
        return position;
    }

    public Point getNextPosition(){
        return new Point(position.x + velocity.x, position.y + velocity.y);
    }

    public Point getVelocity(){
        return velocity;
    }

    public Image getVisuals() {
        return visual;
    }
}
