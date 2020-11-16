package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Preset implements Cloneable {
    SpaceElement[] elementsInPreset;
    double timeUntilEntirePresetOnScreen;

    VisualManager visualManager = VisualManager.getInstance();
    VelocityManager velocityManager = VelocityManager.getInstance();

    public Preset(SpaceElement[] elements) {
        elementsInPreset = elements;
        timeUntilEntirePresetOnScreen = calculateSize();
    }

    private double calculateSize() {
        try {
            double maxTime = 0;
            for (SpaceElement e : elementsInPreset) {
                maxTime = Math.max(maxTime, (1 - (e.getRelativePosition().x + visualManager.getElementRelativeWidth(e.getClass()))) / velocityManager.getRelativeVelocity(e.getClass()).x);
            }
            return maxTime;
        } catch (VisualNotSetException | VelocityNotSetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public SpaceElement[] getElementsInPreset() {
        return elementsInPreset;
    }

    public double getPresetTimeUntilOnScreen() {
        return timeUntilEntirePresetOnScreen;
    }
}
