package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager;

public enum VisualScaling {
    SPACE_SHIP(0.1),
    COIN(0.1),
    COIN_COUNT(0.05),
    UFO(0.15),
    ASTEROID(0.2),
    POWERUP(0.15),
    ROCKET(0.1);


    private double scaling;

    VisualScaling(double scaling) {
        this.scaling = scaling;
    }

   public double getScaling() {
        return scaling;
    }
}