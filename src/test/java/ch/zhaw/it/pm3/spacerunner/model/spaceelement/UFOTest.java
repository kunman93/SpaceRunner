package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.Visual;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualScaling;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.geom.Point2D;

public class UFOTest {
    VelocityManager velocityManager = VelocityManager.getManager();
    VisualManager visualManager = VisualManager.getManager();
    UFO ufo;

    /**
     * Sets Up the velocity and visual of SpaceShip.
     */
    @BeforeEach
    void setUp() {
        velocityManager.setupGameElementVelocity();
        visualManager.loadAndSetVisual(UFO.class, new Visual(VisualSVGFile.UFO_1, VisualScaling.UFO));
    }

    /**
     * Tests if the UFO moves the correct distance in the x position after 1 second (1000 milliseconds) of movement.
     */
    @Test
    void move1SecTest() {
        ufo = new UFO(new Point2D.Double(0, 0));
        ufo.move(1000);
        try {
            assertEquals(ufo.getRelativePosition().x, 0 + velocityManager.getRelativeVelocity(UFO.class).x);
        } catch (VelocityNotSetException e) {
            fail("VelocityNotSetException or VisualNotSetException thrown");
        }
    }

    /**
     * Tests if the UFO moves the correct distance in the x position after 1 millisecond of movement.
     */
    @Test
    void move1MillisTest() {
        ufo = new UFO(new Point2D.Double(0, 0));
        ufo.move(1);
        try {
            assertEquals(ufo.getRelativePosition().x, 0 + (1.0/1000.0)*velocityManager.getRelativeVelocity(UFO.class).x);
        } catch (VelocityNotSetException e) {
            fail("VelocityNotSetException or VisualNotSetException thrown");
        }
    }
}
