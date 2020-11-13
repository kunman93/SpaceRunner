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
    private Timer timer = new Timer();
    private boolean canGenerate = true;
    private int delay = 5000;
    private VisualManager visualManager = VisualManager.getInstance();

    private static Preset[] presets;

    private void generatePresets() {
        presets = new Preset[]{
                new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1, .5))}),
                new Preset(new SpaceElement[]{new Asteroid(new Point2D.Double(1,0))}),
                new Preset(new SpaceElement[]{new UFO(new Point2D.Double(1,0))})
        };
    }

    public Preset getRandomPreset() {
        generatePresets();
        int index = (int)Math.floor(Math.random() * presets.length);
        return presets[index];
    }
}
