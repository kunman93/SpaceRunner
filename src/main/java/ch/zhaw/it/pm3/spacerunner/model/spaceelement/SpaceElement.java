package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualElement;

import java.awt.*;

public abstract class SpaceElement implements VisualElement {

    //todo: Idee: Object die alle variablen (position, width, length) umfasst
    private Point position = new Point(0, 0);
    private Point velocity = new Point(0, 0);
    private int height;
    private int width;

    public SpaceElement(Point startPosition) {
        this.width = width;
        this.height = height;
        this.position = startPosition;
    }

    protected void setHeight(int height) {
        this.height = height;
    }

    protected void setWidth(int width) {
        this.width = width;
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

    public void move(Point direction) {
        setVelocity(direction);
        position.x += velocity.x;
        position.y += velocity.y;
    }

    //TODO: remove accelerate and velocity => they are not used for their intended purposes
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

    public void setCurrentPosition(Point position) {
        this.position = position;
    }

    public Point getNextPosition(){
        return new Point(position.x + velocity.x, position.y + velocity.y);
    }

    public Point getVelocity(){
        return velocity;
    }

    public boolean doesCollide(SpaceElement s){
        return pointInObject(s.getCurrentPosition().x, s.getCurrentPosition().y)
                || pointInObject(s.getCurrentPosition().x, s.getCurrentPosition().y + s.getHeight())
                || pointInObject(s.getCurrentPosition().x +s.getWidth(), s.getCurrentPosition().y)
                || pointInObject(s.getCurrentPosition().x + s.getWidth(), s.getCurrentPosition().y + s.getHeight());
    }

    private boolean pointInObject(float x, float y){
        return x > position.x && x < position.x + width && y > position.y && y < position.y + height;
    }

    protected abstract void setElementHitbox();

    // Cant have abstract & static method
    //public abstract void setHitbox(int height, int width);
}
