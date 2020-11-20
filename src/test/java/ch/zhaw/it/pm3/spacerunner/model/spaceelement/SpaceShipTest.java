package ch.zhaw.it.pm3.spacerunner.model.spaceelement;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.Visual;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualScaling;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;
import org.junit.jupiter.api.*;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

public class SpaceShipTest {
    private SpaceShip spaceShip;
    private VelocityManager velocityManager = VelocityManager.getManager();
    private VisualManager visualManager = VisualManager.getManager();

    @BeforeEach
    void setUp() {
        velocityManager.setupGameElementVelocity();
        visualManager.loadAndSetVisual(SpaceShip.class, new Visual(VisualSVGFile.SPACE_SHIP_1, VisualScaling.SPACE_SHIP));
    }

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

    @Test
    void directMoveUpPastScreenLimitTest() {
        spaceShip = new SpaceShip(new Point2D.Double(0,0.5));
        spaceShip.moveSpaceShip(SpaceShipDirection.UP, 1000);
        spaceShip.moveSpaceShip(SpaceShipDirection.UP, 1000);
        assertEquals(spaceShip.getRelativePosition(), new Point2D.Double(0, 0));
    }
}
