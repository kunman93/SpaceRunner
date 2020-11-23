package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.speed;

/**
 * Horizontal speed of elements per second relative to the view.
 * @author blattpet
 */
public enum HorizontalSpeed {
    ZERO(0),
    ASTEROID(0.85),
    COIN(0.7),
    BACKGROUND(0.7),
    UFO(0.7),
    POWER_UP(0.7),
    ROCKET(0.85);

    private double speed;

    HorizontalSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }
}
