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

    private Preset generatePreset(int index) throws VisualNotSetException {
        switch (index) {
            case 0:
                return new Preset(new SpaceElement[]{new Asteroid(new Point2D.Double(1,0))});
            case 1:
                return new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1, .5)), new Coin(new Point2D.Double(1.2, .5)), new Coin(new Point2D.Double(1.4, .5)), new Coin(new Point2D.Double(1.6, .5))});
            case 2:
                return new Preset(new SpaceElement[]{new UFO(new Point2D.Double(1,0)), new UFO(new Point2D.Double(1,0), 0.5)});
            case 3:
                double y = Math.random() * (1 - visualManager.getElementRelativeWidth(Rocket.class));
                return new Preset(new SpaceElement[]{new Rocket(new Point2D.Double(1,y))});
        }
        return null;
    }

    public synchronized Preset getRandomPreset() {
        int index = (int)Math.floor(Math.random() * 4);

        try {
            return generatePreset(index);
        } catch (VisualNotSetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
