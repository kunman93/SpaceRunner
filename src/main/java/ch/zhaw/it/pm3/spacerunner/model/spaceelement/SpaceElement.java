package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class SpaceElement {

    //todo: Idee: Object die alle variablen (position, width, length) umfasst
    private Point position = new Point(0, 0);
    private Point velocity = new Point(0, 0);
    private int height;
    private int width;
    private static BufferedImage visual;

    public SpaceElement(Point startPosition, int width, int height) {
        this.width = width;
        this.height = height;
        this.position = startPosition;
    }


    public int getHeight() {
        return height;
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

    public static void setVisual(BufferedImage visual){
        SpaceElement.visual = visual;
    }

    public BufferedImage getVisual() throws VisualNotSetException{
        if (visual == null) {
            throw new VisualNotSetException("Visual for SpaceElement was not set!");
        }
        return visual;
    }

    public Point getCurrentPosition() {
        return position;
    }

    public void setCurrentPosition(Point position) {
        this.position = position;
    }

    public Point getNextPosition(){
        return new Point(position.x + velocity.x, position.y + velocity.y);
    }

    public Point getVelocity(){
        return velocity;
    }
}
