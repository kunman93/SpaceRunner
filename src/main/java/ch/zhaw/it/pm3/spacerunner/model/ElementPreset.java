package ch.zhaw.it.pm3.spacerunner.model;


import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ElementPreset {
    VisualManager visualManager = VisualManager.getInstance();

    private Preset generatePreset(PresetType p) throws VisualNotSetException {
        double y;
        switch (p) {
            case ASTERIOD:
                y = Math.random() * 1.1 - 0.3;
                return new Preset(new SpaceElement[]{new Asteroid(new Point2D.Double(1,y))});
            case COINS_LINE:
                return new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1, .5)), new Coin(new Point2D.Double(1.2, .5)), new Coin(new Point2D.Double(1.4, .5)), new Coin(new Point2D.Double(1.6, .5))});
            case UFOS_MIRRORED:
                return new Preset(new SpaceElement[]{new UFO(new Point2D.Double(1,0)), new UFO(new Point2D.Double(1,0), 0.5)});
            case ROCKET:
                y = Math.random() * (1 - visualManager.getElementRelativeWidth(Rocket.class));
                return new Preset(new SpaceElement[]{new Rocket(new Point2D.Double(1,y))});
            case COINS_ARROW:
                return new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1,0.6)), new Coin(new Point2D.Double(1.05,0.7)), new Coin(new Point2D.Double(1.05,0.5)), new Coin(new Point2D.Double(1.1,0.8)),
                        new Coin(new Point2D.Double(1.1,0.4)), new Coin(new Point2D.Double(1.1,0.6)), new Coin(new Point2D.Double(1.2,0.6)), new Coin(new Point2D.Double(1.3,0.6)),
                        new Coin(new Point2D.Double(1.4,0.6))});
            case COINS_RANDOM:
                return new Preset(randomCoin());
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

    private SpaceElement[] randomCoin() {
        double position_y = (Math.random() * (0.9 - 0.1)) + 0.1;
        double position_x = 1.0;
        SpaceElement[] spaceElements = new SpaceElement[5];
        for (int i = 1; i <= 5; i++) {
            spaceElements[i-1] = new Coin(new Point2D.Double(position_x,position_y));
            position_x = position_x + 0.1;
        }
        return spaceElements;
    }
}
