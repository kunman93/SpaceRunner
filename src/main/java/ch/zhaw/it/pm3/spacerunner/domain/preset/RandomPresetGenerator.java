package ch.zhaw.it.pm3.spacerunner.domain.preset;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * In this class the different presets are created, which are can shown randomly in the game.
 * @author blattpet
 */
public class RandomPresetGenerator {

    private final Logger logger = Logger.getLogger(RandomPresetGenerator.class.getName());
    private final VisualManager visualManager = VisualManager.getManager();

    /**
     * Creates all cases of presets.
     * @param p PresetType
     * @return A preset when the case exists
     */
    private Preset generatePreset(PresetType p) throws VisualNotSetException {
        double y;
        switch (p) {
            case ASTEROID:
                y = Math.random() * (1.0 - visualManager.getElementRelativeHeight(Asteroid.class));
                return new Preset(new SpaceElement[]{new Asteroid(new Point2D.Double(1, y))});
            case UFO_SINGLE:
                return new Preset(new SpaceElement[]{new UFO(new Point2D.Double(1, 0))});
            case UFOS_MIRRORED:
                return new Preset(new SpaceElement[]{new UFO(new Point2D.Double(1, 0)), new UFO(new Point2D.Double(1, 0), 0.5)});
            case ROCKET:
                y = Math.random() * (1.0 - visualManager.getElementRelativeHeight(Rocket.class));
                return new Preset(new SpaceElement[]{new Rocket(new Point2D.Double(1, y))});
            case ROCKET_RANDOM_THREE:
                return new Preset(randomRocket());
            case COINS_ARROW:
                return new Preset(coinArrow());
            case COINS_RANDOM_LINE:
                return new Preset(randomCoinLine());
            case COINS_RANDOM_SQUARE:
                return new Preset(randomCoinSquare());
        }
        logger.log(Level.WARNING, "No Case for this Preset Type: {0}", p.name());
        return null;
    }

    /**
     * Returns one of the random preset
     */
    public synchronized Preset getRandomPreset() {
        int index = (int) Math.floor(Math.random() * (PresetType.values().length));

        try {
            return generatePreset(PresetType.values()[index]);
        } catch (VisualNotSetException e) {
            logger.log(Level.SEVERE, "Error with Preset generation");
        }

        return null;
    }

    private SpaceElement[] randomCoinLine() throws VisualNotSetException {
        double y = Math.random() * (1.0 - visualManager.getElementRelativeHeight(Coin.class));
        double x = 1.0;
        int count = (int) (Math.random() * 10);
        SpaceElement[] spaceElements = new SpaceElement[count];
        for (int i = 0; i < count; i++) {
            spaceElements[i] = new Coin(new Point2D.Double(x, y));
            x = x + 2 * visualManager.getElementRelativeWidth(Coin.class);
        }
        return spaceElements;
    }

    private SpaceElement[] randomCoinSquare() throws VisualNotSetException {
        double coinHeight = visualManager.getElementRelativeHeight(Coin.class);
        double y = Math.random() * (1.0 - coinHeight);
        int squareSize = (int) ((Math.random() * (6 - 2)) + 2);

        if (Double.compare(squareSize * coinHeight + y, 1.0 - coinHeight) >= 0) {
            return new SpaceElement[]{new Coin(new Point2D.Double(1, y))};
        }

        return generateCoinSquare(coinHeight, y, squareSize);
    }

    private SpaceElement[] generateCoinSquare(double coinHeight, double y, int squareSize) throws VisualNotSetException {
        SpaceElement[] spaceElements = new SpaceElement[squareSize * squareSize];
        int index = 0;
        for (int i = 0; i < squareSize; i++) {
            double x = 1.0;
            for (int j = 0; j < squareSize; j++) {
                spaceElements[index] = new Coin(new Point2D.Double(x, y));
                x = x + 2 * visualManager.getElementRelativeWidth(Coin.class);
                index++;
            }
            y = y + coinHeight;
        }
        return spaceElements;
    }

    private SpaceElement[] randomRocket() throws VisualNotSetException {
        SpaceElement[] spaceElements = new SpaceElement[3];
        double x = 1.0;
        for (int i = 0; i < 3; i++) {
            spaceElements[i] = new Rocket(new Point2D.Double(x, Math.random() * (1.0 - visualManager.getElementRelativeHeight(Rocket.class))));
            x = x + 2 * visualManager.getElementRelativeWidth(Rocket.class);
        }
        return spaceElements;
    }

    private SpaceElement[] coinArrow() throws VisualNotSetException {
        double coinHeight = visualManager.getElementRelativeHeight(Coin.class);
        double coinWidth = visualManager.getElementRelativeWidth(Coin.class);
        double y = (Math.random() * (1.0 - 6 * coinHeight)) + 3 * coinHeight;
        return new SpaceElement[]{new Coin(new Point2D.Double(1, y)), new Coin(new Point2D.Double(1 + 2 * coinWidth, y + coinHeight)),
                new Coin(new Point2D.Double(1 + 2 * coinWidth, y - coinHeight)), new Coin(new Point2D.Double(1 + 4 * coinWidth, y + 2 * coinHeight)),
                new Coin(new Point2D.Double(1 + 4 * coinWidth, y - 2 * coinHeight)), new Coin(new Point2D.Double(1 + 2 * coinWidth, y)),
                new Coin(new Point2D.Double(1 + 4 * coinWidth, y)), new Coin(new Point2D.Double(1 + 6 * coinWidth, y)), new Coin(new Point2D.Double(1 + 8 * coinWidth, y))};
    }
}
