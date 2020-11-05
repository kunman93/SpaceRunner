package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Coin extends SpaceElement {
    private static int coinHeight;
    private static int coinWidth;

    public Coin(Point startPosition) {
        super(startPosition);
        setElementHitbox();
    }

    @Override
    protected void setElementHitbox() {
        setHeight(coinHeight);
        setWidth(coinWidth);
    }

    public static void setClassHitbox(int height, int width) {
        coinHeight = height;
        coinWidth = width;
    }

}
