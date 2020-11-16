package ch.zhaw.it.pm3.spacerunner.model;


import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;

import java.awt.geom.Point2D;

public class ElementPreset {
    private Preset[] presets = generatePresets();

    private Preset[] generatePresets() {
        return new Preset[]{
                new Preset(new SpaceElement[]{new Asteroid(new Point2D.Double(1,0))}),
                new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1, .5)), new Coin(new Point2D.Double(1.2, .5)), new Coin(new Point2D.Double(1.4, .5)), new Coin(new Point2D.Double(1.6, .5))}),
                new Preset(new SpaceElement[]{new UFO(new Point2D.Double(1,0))})
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
