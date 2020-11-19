package ch.zhaw.it.pm3.spacerunner.model;


import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ElementPreset {

    private Logger logger = Logger.getLogger(ElementPreset.class.getName());

    VisualManager visualManager = VisualManager.getManager();

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
                double coinHeight = visualManager.getElementRelativeHeight(Coin.class);
                double coinWidth = visualManager.getElementRelativeWidth(Coin.class);
                y = (Math.random() * (1.0 - 6 * coinHeight)) + 3 * coinHeight;
                return new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1,y)), new Coin(new Point2D.Double(1+2*coinWidth,y+coinHeight)),
                        new Coin(new Point2D.Double(1+2*coinWidth,y-coinHeight)), new Coin(new Point2D.Double(1+4*coinWidth,y+2*coinHeight)),
                        new Coin(new Point2D.Double(1+4*coinWidth,y-2*coinHeight)), new Coin(new Point2D.Double(1+2*coinWidth,y)),
                        new Coin(new Point2D.Double(1+4*coinWidth,y)), new Coin(new Point2D.Double(1+6*coinWidth,y)), new Coin(new Point2D.Double(1+8*coinWidth,y))});
            case COINS_RANDOM_LINE:
                return new Preset(randomCoinLine());
            case COINS_SQUARE:
                return new Preset(randomCoinSquare());
        }
        logger.log(Level.WARNING, "No Case for this Preset Type: {0}", p.name());
        return null;
    }

    public synchronized Preset getRandomPreset() {
        int index = (int)Math.floor(Math.random() * (PresetType.values().length));

        try {
            return generatePreset(PresetType.values()[index]);
        } catch (VisualNotSetException e) {
            logger.log(Level.SEVERE, "Error with Preset generation");
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
        double coinHeight = visualManager.getElementRelativeHeight(Coin.class);
        double coinWidth = visualManager.getElementRelativeWidth(Coin.class);

        double y = Math.random() * (1.0 - coinHeight);
        int count = (int) ((Math.random() * (6-2)) + 2);

        if (Double.compare(count * coinHeight + y, 1.0 - coinHeight) >= 0) {
           return new SpaceElement[]{new Coin(new Point2D.Double(1,y))};
        }

        SpaceElement[] spaceElements = new SpaceElement[count * count];
        int index = 0;
        for (int i = 0; i < count; i++) {
            double x = 1.0;
            for (int j = 0; j < count; j++) {
                spaceElements[index] = new Coin(new Point2D.Double(x,y));
                x = x + 2 * coinWidth;
                index++;
            }
            y = y + coinHeight;
        }
        return spaceElements;
    }
}
