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
                y = (Math.random() * (1.0 - 2 * visualManager.getElementRelativeHeight(Asteroid.class))) + visualManager.getElementRelativeHeight(Asteroid.class);
                return new Preset(new SpaceElement[]{new Asteroid(new Point2D.Double(1,y))});
            //case COINS_LINE:
            //    return new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1, .5)), new Coin(new Point2D.Double(1.2, .5)), new Coin(new Point2D.Double(1.4, .5)), new Coin(new Point2D.Double(1.6, .5))});
            case UFOS_MIRRORED:
                return new Preset(new SpaceElement[]{new UFO(new Point2D.Double(1,0)), new UFO(new Point2D.Double(1,0), 0.5)});
            case ROCKET:
                y = (Math.random() * (1.0 - 2 * visualManager.getElementRelativeHeight(Rocket.class))) + visualManager.getElementRelativeHeight(Rocket.class);
                return new Preset(new SpaceElement[]{new Rocket(new Point2D.Double(1,y))});
            case COINS_ARROW:
                y = (Math.random() * (0.7 - 0.3)) + 0.3;
                return new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1,y)), new Coin(new Point2D.Double(1.05,y+0.1)), new Coin(new Point2D.Double(1.05,y-0.1)), new Coin(new Point2D.Double(1.1,y+0.2)),
                        new Coin(new Point2D.Double(1.1,y-0.2)), new Coin(new Point2D.Double(1.1,y)), new Coin(new Point2D.Double(1.2,y)), new Coin(new Point2D.Double(1.3,y)),
                        new Coin(new Point2D.Double(1.4,y))});
            case COINS_RANDOM_LINE:
                y = (Math.random() * (1.0 - 2 * visualManager.getElementRelativeHeight(Coin.class)) + visualManager.getElementRelativeHeight(Coin.class));
                return new Preset(randomCoinLine(y));
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

    private SpaceElement[] randomCoinLine(double y) {
        double x = 1.0;
        int count = (int) (Math.random() * 10);
        SpaceElement[] spaceElements = new SpaceElement[count];
        for (int i = 1; i <= count; i++) {
            spaceElements[i-1] = new Coin(new Point2D.Double(x,y));
            x = x + 0.1;
        }
        return spaceElements;
    }
}
