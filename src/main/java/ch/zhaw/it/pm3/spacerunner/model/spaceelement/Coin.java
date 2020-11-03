package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Coin extends SpaceElement {

    private static BufferedImage[] coinVisuals = null;
    private static long animationTimeStamp;
    private static int animationStepTime = 250;
    private static int animationPointer;


    public Coin(Point startPosition, int width, int height) {
        super(startPosition, width, height);
    }

}
