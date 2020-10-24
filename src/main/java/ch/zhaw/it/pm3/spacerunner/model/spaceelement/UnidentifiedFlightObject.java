package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class UnidentifiedFlightObject extends Obstacle {

    public UnidentifiedFlightObject(Point startPosition, int width, int length, BufferedImage visual) throws Exception {
        super(startPosition, width, length, visual);
    }
}
