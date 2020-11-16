package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Preset implements Cloneable {
    SpaceElement[] elementsInPreset;
    double presetSize;

    VisualManager visualManager = VisualManager.getInstance();
    VelocityManager velocityManager = VelocityManager.getInstance();

    public Preset(SpaceElement[] elements) {
        elementsInPreset = elements;
        presetSize = calculateSize();
    }

    private double calculateSize() {
        try {
            double maxDistance = 0;
            for (SpaceElement e : elementsInPreset) {
                maxDistance = Math.max(maxDistance, (1 - (e.getRelativePosition().x + visualManager.getElementRelativeWidth(e.getClass()))) / velocityManager.getRelativeVelocity(e.getClass()).x);
            }
            return maxDistance;
        } catch (VisualNotSetException | VelocityNotSetException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public SpaceElement[] getElementsInPreset() {
        return elementsInPreset;
    }

    public double getPresetSize() {
        return presetSize;
    }
}
