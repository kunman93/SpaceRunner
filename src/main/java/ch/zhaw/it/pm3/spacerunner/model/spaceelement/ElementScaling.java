package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

public enum ElementScaling {
    SPACE_SHIP(0.3),
    COIN(0.1),
    UFO(0.3),
    ASTEROID(0.2);



    private double scaling;

    ElementScaling(double scaling) {
        this.scaling = scaling;
    }

   public double getScaling() {
        return scaling;
    }
}