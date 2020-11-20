package ch.zhaw.it.pm3.spacerunner.model.spaceelement.preset;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.Coin;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityNotSetException;
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

    @BeforeEach
    void setUp() {
        velocityManager.setupGameElementVelocity();
        visualManager.loadAndSetVisual(Coin.class, new Visual(VisualSVGFile.SHINEY_COIN_1, VisualScaling.COIN));
    }

    @Test
    void calculateTimeUntilOnScreenZeroTest() {
        preset = new Preset(new SpaceElement[]{new Coin(new Point2D.Double(0,0))});
        assertEquals(0, preset.getPresetTimeUntilOnScreen());
    }

    @Test
    void calculateTimeUntilOnScreenOneElementTest() {
        preset = new Preset(new SpaceElement[]{new Coin(new Point2D.Double(1,0))});
        try {
            assertEquals(preset.getPresetTimeUntilOnScreen(), (1.0 - (preset.getElementsInPreset()[0].getRelativePosition().x + visualManager.getElementRelativeWidth(preset.getElementsInPreset()[0].getClass()))) / velocityManager.getRelativeVelocity(preset.getElementsInPreset()[0].getClass()).x);
        } catch (VisualNotSetException | VelocityNotSetException e) {
            fail("VisualNotSetException or VelocityNotSetException thrown");
        }
    }

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
