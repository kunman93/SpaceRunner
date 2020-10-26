package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PowerUp extends SpaceElement {
    private static BufferedImage visual;

    public PowerUp(Point startPosition, int width, int length) {
        super(startPosition, width, length);
    }


    public static void setVisual(BufferedImage visual){
        PowerUp.visual = visual;
    }

    @Override
    public BufferedImage getVisual() throws VisualNotSetException{
        if (visual == null) {
            throw new VisualNotSetException("Visual for PowerUp was not set!");
        }
        return visual;
    }
}
