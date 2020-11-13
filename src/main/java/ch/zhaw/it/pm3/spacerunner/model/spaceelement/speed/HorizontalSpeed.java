package ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed;

/**
 * Horizontal speed of elements per second relative to the view
 */
public enum HorizontalSpeed {
    ZERO(0),
    ASTEROID(0.02),
    COIN(0.01),
    BACKGROUND(0.01),
    UFO(0.02),
    POWERUP(0.01);


    private double speed;

    HorizontalSpeed(double speed){
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}
