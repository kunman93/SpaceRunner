package ch.zhaw.it.pm3.spacerunner.domain.spaceelement;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.Visual;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualFile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class SpaceWorldTest {
    SpaceWorld spaceWorld;
    private final VisualManager visualManager = VisualManager.getManager();
    private final VelocityManager velocityManager = VelocityManager.getManager();


    /**
     * Sets Up the necessary velocity and visual of SpaceWorld.
     */
    @BeforeEach
    void setUp() {
        velocityManager.setupGameElementVelocity();
        visualManager.loadAndSetVisual(SpaceWorld.class, new Visual(VisualFile.BACKGROUND_STARS));
    }

    /**
     * Test if SpaceWorld moves to the correct position after 1 second (1000 milliseconds).
     */
    @Test
    void move1SecTest() {
        spaceWorld = new SpaceWorld(new Point2D.Double(0, 0));
        spaceWorld.move(1000);
        try {
            assertEquals(spaceWorld.getRelativePosition(), new Point2D.Double(0 + velocityManager.getRelativeVelocity(SpaceWorld.class).x, 0));
        } catch (VelocityNotSetException exception) {
            fail("VelocityNotSetException thrown");
        }
    }

    /**
     * Test if SpaceWorld moves to the correct position after 1 millisecond.
     */
    @Test
    void move1MillisTest() {
        spaceWorld = new SpaceWorld(new Point2D.Double(0, 0));
        spaceWorld.move(1);
        try {
            assertEquals(spaceWorld.getRelativePosition(), new Point2D.Double(0 + (1.0 / 1000.0) * velocityManager.getRelativeVelocity(SpaceWorld.class).x, 0));
        } catch (VelocityNotSetException exception) {
            fail("VelocityNotSetException thrown");
        }
    }

    /**
     * Test if the image moves back to (0,0) after moving far enough off the left side of the screen, making it seem as if the background is looping.
     */
    @Test
    void moveLoopSpaceWorldImage() {
        try {
            spaceWorld = new SpaceWorld(new Point2D.Double(-visualManager.getElementRelativeWidth(SpaceWorld.class), 0));
        } catch (VisualNotSetException e) {
            fail("VisualNotSetException thrown");
        }
        spaceWorld.move(1000);
        assertEquals(spaceWorld.getRelativePosition(), new Point2D.Double(0, 0));
    }
}
