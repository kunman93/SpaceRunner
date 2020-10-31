package ch.zhaw.it.pm3.spacerunner.controller;

public enum ElementScaling {
    SPACE_SHIP_1(0.3),
    COIN_1(0.1),
    UFO_1(0.3),
    ASTEROID_1(0.2);



    private double scaling;

    ElementScaling(double scaling) {
        this.scaling = scaling;
    }

   public double getScaling() {
        return scaling;
    }
}