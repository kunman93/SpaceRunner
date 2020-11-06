package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

public enum VisualHeightToWidthRatio {
    SPACE_SHIP(3.3),
    COIN(1.0),
    UFO(1.0),
    ASTEROID(1.0);

    private double heightToWidthRatio;

    VisualHeightToWidthRatio(double heightToWidthRatio) {
        this.heightToWidthRatio = heightToWidthRatio;
    }

    public double getRatio() {
        return heightToWidthRatio;
    }
}
