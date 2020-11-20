package ch.zhaw.it.pm3.spacerunner.domain.spaceelement;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.SpaceShip;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.SpaceShipDirection;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.Visual;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualScaling;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;
import org.junit.jupiter.api.*;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceShipTest {
    private SpaceShip spaceShip;
    private final VelocityManager velocityManager = VelocityManager.getManager();
    private final VisualManager visualManager = VisualManager.getManager();

    /**
     * Sets Up the velocity and visual of SpaceShip.
     */
    @BeforeEach
    void setUp() {
        velocityManager.setupGameElementVelocity();
        visualManager.loadAndSetVisual(SpaceShip.class, new Visual(VisualSVGFile.SPACE_SHIP_1, VisualScaling.SPACE_SHIP));
    }

    /**
     * Tests if the SpaceShip moves UP to the correct position after 1 second (1000 millisecond) of movement.
     */
    @Test
    void directMoveUp1SecTest() {
        spaceShip = new SpaceShip(new Point2D.Double(0,1));
        spaceShip.moveSpaceShip(SpaceShipDirection.UP, 1000);
        try {
            assertEquals(spaceShip.getRelativePosition(), new Point2D.Double(0, 1 - velocityManager.getRelativeVelocity(SpaceShip.class).y));
        } catch (VelocityNotSetException exception) {
            fail("VelocityNotSetException thrown");
        }
    }

    /**
     * Tests if the SpaceShip moves UP to the correct position after 1 millisecond of movement.
     */
    @Test
    void directMoveUp1MillisTest() {
        spaceShip = new SpaceShip(new Point2D.Double(0,1));
        spaceShip.moveSpaceShip(SpaceShipDirection.UP, 1);
        try {
            assertEquals(spaceShip.getRelativePosition(), new Point2D.Double(0, 1 - (1.0/1000.0)*velocityManager.getRelativeVelocity(SpaceShip.class).y));
        } catch (VelocityNotSetException exception) {
            fail("VelocityNotSetException thrown");
        }
    }

    /**
     * Tests if the SpaceShip moves DOWN to the correct position after 1 second (1000 millisecond) of movement.
     */
    @Test
    void directMoveDown1SecTest() {
        spaceShip = new SpaceShip(new Point2D.Double(0,0));
        spaceShip.moveSpaceShip(SpaceShipDirection.DOWN, 1000);
        try {
            assertEquals(spaceShip.getRelativePosition(), new Point2D.Double(0, 0 + velocityManager.getRelativeVelocity(SpaceShip.class).y));
        } catch (VelocityNotSetException exception) {
            fail("VelocityNotSetException thrown");
        }
    }

    /**
     * Tests if the SpaceShip moves DOWN to the correct position after 1 millisecond of movement.
     */
    @Test
    void directMoveDown1MillisTest() {
        spaceShip = new SpaceShip(new Point2D.Double(0,0));
        spaceShip.moveSpaceShip(SpaceShipDirection.DOWN, 1);
        try {
            assertEquals(spaceShip.getRelativePosition(), new Point2D.Double(0, 0 + (1.0/1000.0)*velocityManager.getRelativeVelocity(SpaceShip.class).y));
        } catch (VelocityNotSetException exception) {
            fail("VelocityNotSetException thrown");
        }
    }

    /**
     * Tests that the SpaceShip doesnt exceed the bottom screen limit.
     */
    @Test
    void directMoveDownPastScreenLimitTest() {
        spaceShip = new SpaceShip(new Point2D.Double(0,0));
        spaceShip.moveSpaceShip(SpaceShipDirection.DOWN, 1000);
        spaceShip.moveSpaceShip(SpaceShipDirection.DOWN, 1000);
        try {
            assertEquals(spaceShip.getRelativePosition(), new Point2D.Double(0, 1 - visualManager.getElementRelativeHeight(SpaceShip.class)));
        } catch (VisualNotSetException e) {
            fail("VisualNotSetException thrown");
        }
    }

    /**
     * Tests that the SpaceShip doesnt exceed the top screen limit.
     */
    @Test
    void directMoveUpPastScreenLimitTest() {
        spaceShip = new SpaceShip(new Point2D.Double(0,0.5));
        spaceShip.moveSpaceShip(SpaceShipDirection.UP, 1000);
        spaceShip.moveSpaceShip(SpaceShipDirection.UP, 1000);
        assertEquals(spaceShip.getRelativePosition(), new Point2D.Double(0, 0));
    }
}
