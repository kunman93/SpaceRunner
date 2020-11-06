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

    /**
     * will change the position by the current velocity
     */
    public void move() {
        position.x += velocity.x;
        position.y += velocity.y;
    }

    /**
     * will change the position by the given point
     * @param direction
     */
    public void move(Point direction) {
        setVelocity(direction);
        position.x += velocity.x;
        position.y += velocity.y;
    }

    /**
     * adds direction to velocity if direction == null no changes to Velocity will be made
     * @param direction
     */
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

    /**
     * sets Velocity if direction == null then it will be set to 0
     * @param direction
     */
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

    /**
     * @return Returns Point where the SpaceElement will be after one move()
     */
    public Point getNextPosition(){
        return new Point(position.x + velocity.x, position.y + velocity.y);
    }

    public Point getVelocity(){
        return velocity;
    }


    /**
     * @param s other SpaceElement
     * @return Returns true if SpaceElements Overlapp
     */
    public boolean doesCollide(SpaceElement s){
        return pointInObject(s.getCurrentPosition().x, s.getCurrentPosition().y, this)
                || pointInObject(s.getCurrentPosition().x, s.getCurrentPosition().y + s.getHeight(), this)
                || pointInObject(s.getCurrentPosition().x +s.getWidth(), s.getCurrentPosition().y, this)
                || pointInObject(s.getCurrentPosition().x + s.getWidth(), s.getCurrentPosition().y + s.getHeight(), this)
                || pointInObject(position.x, position.y, s)
                || pointInObject(position.x, position.y + height, s)
                || pointInObject(position.x + width, position.y, s)
                || pointInObject(position.x + width, position.y + height, s)
        ;
    }

    private boolean pointInObject(float x, float y, SpaceElement s){
        return x > s.getCurrentPosition().x && x < s.getCurrentPosition().x + s.getWidth() && y > s.getCurrentPosition().y && y < s.getCurrentPosition().y + s.getHeight();
    }

}
