package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class ElementPreset {
    private Timer timer = new Timer();
    private boolean canGenerate = true;
    private int delay = 2500;

    private static SpaceElement[][] presets;

    private void generatePresets() {
        presets = new SpaceElement[][]{
                {new Coin(new Point(900,200),50,50), new Coin(new Point(900,280),50,50), new Coin(new Point(950,240),50,50), new Coin(new Point(1000,200),50,50), new Coin(new Point(1000,280),50,50)},
                {new Asteroid(new Point(900,-100), 100, 100), new Asteroid(new Point(950,50), 100, 100)},
                {new UFO(new Point(900,0), 100, 100), new UFO(new Point(900,100), 100, 100)}
        };
    }

    public SpaceElement[] getRandomPreset() {
        if(canGenerate) {
            generatePresets();
            canGenerate = false;
            int index = (int)Math.floor(Math.random() * presets.length);
            timer.schedule(new setCanGenerateTrue(), delay);
            return presets[index];
        }
        return null;
    }

    class setCanGenerateTrue extends TimerTask {
        public void run() {canGenerate = true;}
    }

    /*

           elements.add(new Coin(new Point(300,20), 50,50));
        elements.add(new Coin(new Point(370,20), 50,50));
        elements.add(new Coin(new Point(420,20), 50,50));


        elements.add(new UFO(new Point((int)gameView.getCanvasWidth()-30,0), 100, 100));
        elements.add(new Asteroid(new Point((int)gameView.getCanvasWidth(),0), 100, 100));
     */
}
