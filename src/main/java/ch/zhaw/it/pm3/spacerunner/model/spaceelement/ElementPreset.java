package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

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
                {new Coin(new Point(900,200),50,50), new Coin(new Point(900,280),50,50), new Coin(new Point(940,240),50,50), new Coin(new Point(980,200),50,50), new Coin(new Point(980,280),50,50)},
                {new Asteroid(new Point(900,-100), 100, 100), new Asteroid(new Point(950,50), 100, 100)},
                {new UFO(new Point(900,0), 100, 100), new UFO(new Point(900,100), 100, 100)},
                {new Asteroid(new Point(900,200), 100, 100)},
                {new Asteroid(new Point(900,125), 100, 100)}
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
