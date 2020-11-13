package ch.zhaw.it.pm3.spacerunner.model;


import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;

public class ElementPreset {
    private Timer timer = new Timer();
    private boolean canGenerate = true;
    private int delay = 5000;
    private VisualManager visualManager = VisualManager.getInstance();

    private static SpaceElement[][] presets;

    private void generatePresets() {
        presets = new SpaceElement[][]{
                /*{       new Coin(new Point(coinPreset1X1,coinPreset1Y1)),
                        new Coin(new Point(coinPreset1X3,coinPreset1Y1)),

                        new Coin(new Point(coinPreset1X2, coinPreset1Y2)),

                        new Coin(new Point(coinPreset1X1, coinPreset1Y3)),
                        new Coin(new Point(coinPreset1X3, coinPreset1Y3))
                },

                {new Asteroid(new Point(visualManager.getWidth(),-100)), new Asteroid(new Point(visualManager.getWidth() + visualManager.getElementPixelWidth(Asteroid.class) + 10,50))},
                {new UFO(new Point(new Point(visualManager.getWidth(),0))), new UFO(new Point(new Point(visualManager.getWidth(),100)))},
                {new Asteroid(new Point(visualManager.getWidth(),200))},
                {new Asteroid(new Point(visualManager.getWidth(),125))}*/
                {new Coin(new Point2D.Double(1,.5))},
                {new Asteroid(new Point2D.Double(1,0))},
                {new UFO(new Point2D.Double(1,0))}
        };
    }

    public SpaceElement[] getRandomPreset(double gameSpeed) {
        if(canGenerate) {
            generatePresets();
            canGenerate = false;
            int index = (int)Math.floor(Math.random() * presets.length);
            timer.schedule(new setCanGenerateTrue(), (long) (delay/gameSpeed));
            return presets[index];
        }
        return null;
    }

    class setCanGenerateTrue extends TimerTask {
        public void run() {canGenerate = true;}
    }
}
