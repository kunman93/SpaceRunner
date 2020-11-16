package ch.zhaw.it.pm3.spacerunner.model.spaceelement;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.*;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceElementTest {
    SpaceElement element;
    private VisualManager visualManager = VisualManager.getManager();
    private VelocityManager velocityManager = VelocityManager.getManager();

    //TODO: Changed to coin cause SpaceElement does not really work with the manager... is this ok?

    @BeforeEach
    void setUp() {
        element = new Coin(new Point(0,0));
        visualManager.setWidth(100);
        visualManager.setHeight(100);
        velocityManager.setHeight(100);
        velocityManager.setWidth(100);
        Visual myVisual = new Visual(VisualSVGFile.SHINEY_COIN_1, VisualScaling.COIN);
        visualManager.loadAndSetVisual(Coin.class, myVisual);
        velocityManager.setVelocity(Coin.class, new Point2D.Double(0, 0));

    }

    @Test
    void testGetPosition(){
        //velocity == (0,0) -> nextPosition = currentPosition
        assertEquals(element.getNextPosition(),element.getRelativePosition());
        velocityManager.setVelocity(Coin.class, new Point2D.Double(0.1, 0));
        element.move();
        assertEquals(new Point(10,0), element.getRelativePosition());
        assertEquals(new Point(20,0), element.getNextPosition());
    }

    @Test
    void testAccelerate(){
        //assertEquals(new Point(0,0), element.getVelocity());
        //element.accelerate(null);
        //assertEquals(new Point(0,0), element.getVelocity());
        //element.accelerate(new Point(1,0));
        //assertEquals(new Point(1,0), element.getVelocity());
        //element.accelerate(new Point(1,1));
        //assertEquals(new Point(2,1), element.getVelocity());
    }

    @Test
    void testMove(){
        velocityManager.accelerateX(Coin.class, 0.01);
        element.move();
        assertEquals(new Point(1,0), element.getRelativePosition());
        velocityManager.accelerateX(Coin.class, 0.01);
        element.move();
        assertEquals(new Point(3,0), element.getRelativePosition());
    }

    @Test
    void testDoesCollide(){
        Coin elementInside = new Coin(new Point(1,1));
        Coin elementOutside = new Coin(new Point(11,11));
        assertTrue(element.doesCollide(elementInside));
        assertFalse(element.doesCollide(elementOutside));
    }
}
