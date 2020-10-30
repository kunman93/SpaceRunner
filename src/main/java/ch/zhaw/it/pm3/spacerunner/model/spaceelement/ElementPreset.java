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
        {new Coin(new Point(500,100),50,50)},
        {new Coin(new Point(500,250),50,50),new Coin(new Point(500,175),50,50)},
        {new Coin(new Point(500,350),50,50)}
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
}
