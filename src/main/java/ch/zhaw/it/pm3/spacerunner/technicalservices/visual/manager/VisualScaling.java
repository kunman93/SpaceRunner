package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager;

/**
 * Scaling for visual elements.
 *
 * For example:
 * A scaling of 0.1 means 10% of the height is used for the visual height.
 * A scaling of 0.3 means 30% of the height is used for the visual height.
 *
 * @author islermic
 */
public enum VisualScaling {
    SPACE_SHIP(0.1),
    COIN(0.1),
    COIN_COUNT(0.05),
    UFO(0.15),
    ASTEROID(0.2),
    POWER_UP(0.15),
    POWER_UP_UI(0.05),
    ROCKET(0.1);


    private double scaling;

    VisualScaling(double scaling) {
        this.scaling = scaling;
    }

    public double getScaling() {
        return scaling;
    }
}