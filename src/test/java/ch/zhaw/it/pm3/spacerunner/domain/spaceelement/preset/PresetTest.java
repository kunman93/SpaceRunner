package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.preset;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.Coin;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.preset.Preset;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.Visual;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualScaling;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

public class PresetTest {
    Preset preset;
    private final VisualManager visualManager = VisualManager.getManager();
    private final VelocityManager velocityManager = VelocityManager.getManager();

    /**
     * Sets Up the necessary velocity and visual of Coin, which is used to test Preset.
     */
    @BeforeEach
    void setUp() {
        velocityManager.setupGameElementVelocity();
        visualManager.loadAndSetVisual(Coin.class, new Visual(VisualSVGFile.SHINEY_COIN_1, VisualScaling.COIN));
    }

    /**
     * Tests if the time it takes the Preset to be entirely onscreen is 0 if the SpaceElements are already onscreen.
     */
    @Test
    void calculateTimeUntilOnScreenZeroTest() {
        preset = new Preset(new SpaceElement[]{new Coin(new Point2D.Double(0,0))});
        assertEquals(0, preset.getPresetTimeUntilOnScreen());
    }

    /**
     * Tests if the correct time it takes for the Preset to be entirely onscreen is calculated when the Preset contains one SpaceElement.
     */
    @Test
    void calculateTimeUntilOnScreenOneElementTest() {
        preset = new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1,0))});
        try {
            assertEquals(preset.getPresetTimeUntilOnScreen(), (1.0 - (preset.getElementsInPreset()[0].getRelativePosition().x + visualManager.getElementRelativeWidth(preset.getElementsInPreset()[0].getClass()))) / velocityManager.getRelativeVelocity(preset.getElementsInPreset()[0].getClass()).x);
        } catch (VisualNotSetException | VelocityNotSetException e) {
            fail("VisualNotSetException or VelocityNotSetException thrown");
        }
    }

    /**
     * Tests if the correct time it takes for the Preset to be entirely onscreen is calculated when the Preset contains multiple SpaceElements.
     */
    @Test
    void calculateTimeUntilOnScreenMultipleElementsTest() {
        preset = new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1,0)), new Coin(new Point2D.Double(2,0))});
        try {
            assertEquals(preset.getPresetTimeUntilOnScreen(), (1.0 - (preset.getElementsInPreset()[1].getRelativePosition().x + visualManager.getElementRelativeWidth(preset.getElementsInPreset()[1].getClass()))) / velocityManager.getRelativeVelocity(preset.getElementsInPreset()[1].getClass()).x);
        } catch (VisualNotSetException | VelocityNotSetException e) {
            fail("VisualNotSetException or VelocityNotSetException thrown");
        }
    }
}
