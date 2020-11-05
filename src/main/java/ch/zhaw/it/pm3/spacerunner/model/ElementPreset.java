package ch.zhaw.it.pm3.spacerunner.model;


import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class ElementPreset {
    private Timer timer = new Timer();
    private boolean canGenerate = true;
    private int delay = 5000;

    private static SpaceElement[][] presets;

    private void generatePresets() {
        presets = new SpaceElement[][]{
                {new Coin(new Point(900,200)), new Coin(new Point(900,280)), new Coin(new Point(940,240)), new Coin(new Point(980,200)), new Coin(new Point(980,280))},
                {new Asteroid(new Point(900,-100)), new Asteroid(new Point(950,50))},
                {new UFO(new Point(900,0)), new UFO(new Point(900,100))},
                {new Asteroid(new Point(900,200))},
                {new Asteroid(new Point(900,125))}
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
