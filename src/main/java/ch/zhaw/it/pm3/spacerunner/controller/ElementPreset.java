package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.Coin;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;

import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class ElementPreset {
    Timer timer = new Timer();
    boolean canGenerate = true;

    private static SpaceElement[][] presets = new SpaceElement[][] {
            {new Coin(new Point(500, 100), 50, 50)},
            {new Coin(new Point(500, 250), 50, 50)}
    };

    public SpaceElement[] getRandomPreset() {
        if(canGenerate) {
            canGenerate = false;
            int index = (int)Math.floor(Math.random() * presets.length);
            timer.schedule(new setCanGenerateTrue(), 2500);
            return presets[index];
        }
        return null;
    }

    class setCanGenerateTrue extends TimerTask {
        public void run() {canGenerate = true;}
    }
}
