package ch.zhaw.it.pm3.spacerunner.model;


import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;
import org.checkerframework.checker.units.qual.C;

import java.awt.*;
import java.awt.geom.Point2D;
import java.lang.Math;

public class ElementPreset {
    private Preset[] presets = generatePresets();

    private Preset[] generatePresets() {
        return new Preset[]{
                new Preset(new SpaceElement[]{new Asteroid(new Point2D.Double(1,0))}),
                new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1, .5)), new Coin(new Point2D.Double(1.2, .5)), new Coin(new Point2D.Double(1.4, .5)), new Coin(new Point2D.Double(1.6, .5))}),
                new Preset(new SpaceElement[]{new UFO(new Point2D.Double(1,0)), new UFO(new Point2D.Double(1,0), 0.5)}),
                new Preset(new SpaceElement[]{new Rocket(new Point2D.Double(1,0.2))}),
                new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1,0.6)), new Coin(new Point2D.Double(1.05,0.7)), new Coin(new Point2D.Double(1.05,0.5)), new Coin(new Point2D.Double(1.1,0.8)),
                        new Coin(new Point2D.Double(1.1,0.4)), new Coin(new Point2D.Double(1.1,0.6)), new Coin(new Point2D.Double(1.2,0.6)), new Coin(new Point2D.Double(1.3,0.6)),
                        new Coin(new Point2D.Double(1.4,0.6))}) //arrow
        };
    }

    public synchronized void regeneratePresets(){
        presets = generatePresets();
    }

    public synchronized Preset getRandomPreset() {
        int index = (int)Math.floor(Math.random() * presets.length);
        return presets[index];
    }
}
