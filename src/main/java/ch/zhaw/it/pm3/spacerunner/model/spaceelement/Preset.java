package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualNotSetException;

public class Preset {
    SpaceElement[] elementsInPreset;
    double presetSize;

    VisualManager visualManager = VisualManager.getManager();

    public Preset(SpaceElement[] elements) {
        elementsInPreset = elements;
        presetSize = calculateSize();
    }

    private double calculateSize() {
        try {
            double minPosition = Double.MAX_VALUE;
            double maxPosition = Double.MIN_VALUE;
            for (SpaceElement element : elementsInPreset) {
                minPosition = Math.min(element.getRelativePosition().x, minPosition);
                maxPosition = Math.max(element.getRelativePosition().x + visualManager.getElementRelativeWidth(element.getClass()), maxPosition);
            }
            return maxPosition - minPosition;
        } catch (VisualNotSetException e) {
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
