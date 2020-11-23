package ch.zhaw.it.pm3.spacerunner.domain.preset;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * In preset several SpaceElements can be stored.
 * The class calculates the relative width of this sequence of SpaceElements.
 * @author hirsceva
 */
public class Preset implements Cloneable {

    private Logger logger = Logger.getLogger(Preset.class.getName());

    private SpaceElement[] elementsInPreset;
    private double timeUntilEntirePresetOnScreen;

    private VisualManager visualManager = VisualManager.getManager();
    private VelocityManager velocityManager = VelocityManager.getManager();

    /**
     * Constructor for the class preset.
     * @param elements array with SpaceElements
     */
    public Preset(SpaceElement[] elements) {
        elementsInPreset = elements;
        timeUntilEntirePresetOnScreen = calculatePresetTimeUntilOnScreen();
    }

    private double calculatePresetTimeUntilOnScreen() {
        try {
            double maxTime = 0;
            for (SpaceElement e : elementsInPreset) {
                maxTime = Math.max(maxTime, (1.0 - (e.getRelativePosition().x + visualManager.getElementRelativeWidth(e.getClass()))) / velocityManager.getRelativeVelocity(e.getClass()).x);
            }
            return maxTime;
        } catch (VisualNotSetException e) {
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", e.getClass());
        } catch (VelocityNotSetException e) {
            logger.log(Level.SEVERE, "Velocity for {0} wasn't set", e.getClass());
        }
        return 0;
    }

    /**
     * Return all stored SpaceElements from the preset
     * @return array of SpaceElements
     */
    public SpaceElement[] getElementsInPreset() {
        return elementsInPreset;
    }

    /**
     * Return the time of the sequence of all SpaceElements from the preset.
     * @return time on the screen
     */
    public double getPresetTimeUntilOnScreen() {
        return timeUntilEntirePresetOnScreen;
    }
}
