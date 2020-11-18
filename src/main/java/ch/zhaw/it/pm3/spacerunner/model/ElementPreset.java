package ch.zhaw.it.pm3.spacerunner.model;


import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

import java.awt.geom.Point2D;

public class ElementPreset {
    VisualManager visualManager = VisualManager.getInstance();

    private Preset generatePreset(PresetType p) throws VisualNotSetException {
        double y;
        switch (p) {
            case ASTERIOD:
                y = Math.random() * (1.0 - visualManager.getElementRelativeHeight(Asteroid.class));
                return new Preset(new SpaceElement[]{new Asteroid(new Point2D.Double(1,y))});
            case UFO_SINGLE:
                return new Preset(new SpaceElement[]{new UFO(new Point2D.Double(1,0.5))});
            case UFOS_MIRRORED:
                return new Preset(new SpaceElement[]{new UFO(new Point2D.Double(1,0)), new UFO(new Point2D.Double(1,0), 0.5)});
            case ROCKET:
                y = Math.random() * (1.0 - visualManager.getElementRelativeHeight(Rocket.class));
                return new Preset(new SpaceElement[]{new Rocket(new Point2D.Double(1,y))});
            case COINS_ARROW:
                y = (Math.random() * (0.7 - 0.3)) + 0.3;
                double coinHeight = visualManager.getElementRelativeHeight(Coin.class);
                double coinWidth = visualManager.getElementRelativeWidth(Coin.class);
                return new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1,y)), new Coin(new Point2D.Double(1+2*coinWidth,y+coinHeight)), new Coin(new Point2D.Double(1+2*coinWidth,y-coinHeight)),
                        new Coin(new Point2D.Double(1+4*coinWidth,y+2*coinHeight)), new Coin(new Point2D.Double(1+4*coinWidth,y-2*coinHeight)), new Coin(new Point2D.Double(1+2*coinWidth,y)),
                        new Coin(new Point2D.Double(1+4*coinWidth,y)), new Coin(new Point2D.Double(1+6*coinWidth,y)), new Coin(new Point2D.Double(1+8*coinWidth,y))});
            case COINS_RANDOM_LINE:
                return new Preset(randomCoinLine());
            case COINS_SQUARE:
                return new Preset(randomCoinSquare());
        }
        return null;
    }

    public synchronized Preset getRandomPreset() {
        int index = (int)Math.floor(Math.random() * (PresetType.values().length));

        try {
            return generatePreset(PresetType.values()[index]);
        } catch (VisualNotSetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private SpaceElement[] randomCoinLine() throws VisualNotSetException {
        double y = Math.random() * (1.0 - visualManager.getElementRelativeHeight(Coin.class));
        double x = 1.0;
        int count = (int) (Math.random() * 10);
        SpaceElement[] spaceElements = new SpaceElement[count];
        for (int i = 0; i < count; i++) {
            spaceElements[i] = new Coin(new Point2D.Double(x,y));
            x = x + 2 * visualManager.getElementRelativeWidth(Coin.class);
        }
        return spaceElements;
    }

    private SpaceElement[] randomCoinSquare() throws VisualNotSetException {
        double y = Math.random() * (1.0 - visualManager.getElementRelativeHeight(Coin.class));
        int count = (int) ((Math.random() * (6-2)) + 2);
        SpaceElement[] spaceElements = new SpaceElement[count * count];
        int index = 0;
        for (int i = 0; i < count; i++) {
            double x = 1.0;
            for (int j = 0; j < count; j++) {
                spaceElements[index] = new Coin(new Point2D.Double(x,y));
                x = x + 0.1;
                index++;
            }
            y = y + 0.1;
        }
        return spaceElements;
    }
}
