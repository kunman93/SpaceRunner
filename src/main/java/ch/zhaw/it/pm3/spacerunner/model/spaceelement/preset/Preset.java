package ch.zhaw.it.pm3.spacerunner.model.spaceelement.preset;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Preset implements Cloneable {

    private Logger logger = Logger.getLogger(Preset.class.getName());

    private SpaceElement[] elementsInPreset;
    private double timeUntilEntirePresetOnScreen;

    private VisualManager visualManager = VisualManager.getManager();
    private VelocityManager velocityManager = VelocityManager.getManager();

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
        } catch (VisualNotSetException e) {
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", e.getClass());
            e.printStackTrace();
        } catch (VelocityNotSetException e) {
            logger.log(Level.SEVERE, "Velocity for {0} wasn't set", e.getClass());
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
