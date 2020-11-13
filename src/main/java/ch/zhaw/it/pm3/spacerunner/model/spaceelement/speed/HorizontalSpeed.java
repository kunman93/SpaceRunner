package ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed;

/**
 * Horizontal speed of elements per second relative to the view
 */
public enum HorizontalSpeed {
    ZERO(0),
    ASTEROID(1),
    COIN(0.7),
    BACKGROUND(0.7),
    UFO(0.7);


    private double speed;

    HorizontalSpeed(double speed){
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}
