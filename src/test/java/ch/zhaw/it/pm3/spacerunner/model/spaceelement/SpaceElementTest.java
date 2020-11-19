package ch.zhaw.it.pm3.spacerunner.model.spaceelement;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.Visual;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualScaling;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceElementTest {
    SpaceElement[] elements;
    private final VisualManager visualManager = VisualManager.getManager();
    private final VelocityManager velocityManager = VelocityManager.getManager();


    /**
     * Sets Up the necessary velocities and visuals of classes that extend SpaceElements.
     */
    @BeforeEach
    void setUp() {
        velocityManager.setupGameElementVelocity();
        visualManager.loadAndSetVisual(Coin.class, new Visual(VisualSVGFile.SHINEY_COIN_1, VisualScaling.COIN));
        visualManager.loadAndSetVisual(Asteroid.class, new Visual(VisualSVGFile.ASTEROID, VisualScaling.ASTEROID));
    }

    /**
     * Initialises the array 'SpaceElements[] elements' that contains all of the non-abstract classes that extend SpaceElement.
     */
    private void createElementsArray() {
        Coin coin = new Coin(new Point2D.Double(1.0, 0.5));
        Asteroid asteroid = new Asteroid((new Point2D.Double(1.0, 0.5)));
        Rocket rocket = new Rocket(new Point2D.Double(1.0, 0.5));
        UFO ufo = new UFO(new Point2D.Double(1.0, 0.5));
        SpaceShip spaceShip = new SpaceShip(new Point2D.Double(1.0, 0.5));
        SpaceWorld spaceWorld = new SpaceWorld(new Point2D.Double(1.0, 0.5));
        elements = new SpaceElement[]{coin, asteroid, rocket, ufo, spaceShip, spaceWorld};
    }

    /**
     * Tests if the SpaceElement.getPosition method returns the correct position.
     */
    @Test
    void getPositionTest(){
        createElementsArray();
        for(SpaceElement e : elements) {
            assertEquals(e.getRelativePosition(), new Point2D.Double(1.0, 0.5));
        }
    }

    /**
     * Tests if the next position (after 1 sec of movement) is correctly returned.
     */
    @Test
    void getNextPositionTest(){
        createElementsArray();
        for(SpaceElement e : elements) {
            try {
                assertEquals(e.getNextPosition(), new Point2D.Double(1.0 + velocityManager.getRelativeVelocity(e.getClass()).x, 0.5 + velocityManager.getRelativeVelocity(e.getClass()).y));
            } catch (VelocityNotSetException exception) {
                fail("VelocityNotSetException thrown");
            }
        }
    }

    /**
     * Tests if there is a collision if 2 elements are in the same Position.
     */
    @Test
    void doesCollideSamePositionCollisionTest() {
        Coin coin1 = new Coin(new Point2D.Double(1.0, 0.5));
        Coin coin2 = new Coin(new Point2D.Double(1.0, 0.5));

        assertTrue(coin1.doesCollide(coin2));
        assertTrue(coin2.doesCollide(coin1));
    }

    /**
     * Tests if there is no collision if 2 elements have the same y value but different x values.
     */
    @Test
    void doesCollideSameYValueNoCollisionTest() {
        Coin coin1 = new Coin(new Point2D.Double(1.0, 0.5));
        Coin coin2 = new Coin(new Point2D.Double(0.0, 0.5));

        assertFalse(coin1.doesCollide(coin2));
        assertFalse(coin2.doesCollide(coin1));
    }

    /**
     * Tests if there is no collision if 2 elements have the same x value but different y values.
     */
    @Test
    void doesCollideSameXValueNoCollisionTest() {
        Coin coin1 = new Coin(new Point2D.Double(1.0, 0.0));
        Coin coin2 = new Coin(new Point2D.Double(1.0, 1.0));

        assertFalse(coin1.doesCollide(coin2));
        assertFalse(coin2.doesCollide(coin1));
    }

    /**
     * Tests if there is no collision if 2 elements have different x and y values.
     */
    @Test
    void doesCollideDifferentXYValuesNoCollisionTest() {
        Coin coin1 = new Coin(new Point2D.Double(0.0, 0.0));
        Coin coin2 = new Coin(new Point2D.Double(1.0, 1.0));

        assertFalse(coin1.doesCollide(coin2));
        assertFalse(coin2.doesCollide(coin1));
    }

    /**
     * Tests if there is a collision if one of the elements is inside another element.
     */
    @Test
    void doesCollideElementInsideElementCollisionTest() {
        Asteroid element2 = new Asteroid(new Point2D.Double(0.0, 0.0));
        Coin element1 = new Coin(new Point2D.Double(0.001, 0.001));

        assertTrue(element1.doesCollide(element2));
        assertTrue(element2.doesCollide(element1));
    }

    /**
     * Tests if there is a collision if an element overlaps with the top side of another element (not with any of the corners).
     */
    @Test
    void doesCollideOverlapTopCollisionTest() {
        Asteroid element2 = new Asteroid(new Point2D.Double(0.0, 0.0));
        Coin element1 = new Coin(new Point2D.Double(0.001, -0.001));

        assertTrue(element1.doesCollide(element2));
        assertTrue(element2.doesCollide(element1));
    }

    /**
     * Tests if there is a collision if an element overlaps with only the top-left corner of another element.
     */
    @Test
    void doesCollideOverlapTopLeftCollisionTest() {
        Asteroid element2 = new Asteroid(new Point2D.Double(0.0, 0.0));
        Coin element1 = new Coin(new Point2D.Double(-0.001, -0.001));

        assertTrue(element1.doesCollide(element2));
        assertTrue(element2.doesCollide(element1));
    }

    /**
     * Tests if there is a collision if an element overlaps with the left side of another element (not with any of the corners).
     */
    @Test
    void doesCollideOverlapLeftCollisionTest() {
        Asteroid element2 = new Asteroid(new Point2D.Double(0.0, 0.0));
        Coin element1 = new Coin(new Point2D.Double(-0.001, 0.001));

        assertTrue(element1.doesCollide(element2));
        assertTrue(element2.doesCollide(element1));
    }

    /**
     * Tests if there is a collision if an element overlaps with only the bottom-left corner of another element.
     */
    @Test
    void doesCollideOverlapBottomLeftCollisionTest() {
        Asteroid element2 = new Asteroid(new Point2D.Double(0.0, 0.0));
        Coin element1 = new Coin(new Point2D.Double(-0.001, 0.199));

        assertTrue(element1.doesCollide(element2));
        assertTrue(element2.doesCollide(element1));
    }

    /**
     * Tests if there is a collision if an element overlaps with the bottom side of another element (not with any of the corners).
     */
    @Test
    void doesCollideOverlapBottomCollisionTest() {
        Asteroid element2 = new Asteroid(new Point2D.Double(0.0, 0.0));
        Coin element1 = new Coin(new Point2D.Double(0.001, 0.199));

        assertTrue(element1.doesCollide(element2));
        assertTrue(element2.doesCollide(element1));
    }

    /**
     * Tests if there is a collision if an element overlaps with only the bottom-right corner of another element.
     */
    @Test
    void doesCollideOverlapBottomRightCollisionTest() {
        Asteroid element2 = new Asteroid(new Point2D.Double(0.0, 0.0));
        Coin element1 = new Coin(new Point2D.Double(0.199, 0.199));

        assertTrue(element1.doesCollide(element2));
        assertTrue(element2.doesCollide(element1));
    }

    /**
     * Tests if there is a collision if an element overlaps with the right side of another element (not with any of the corners).
     */
    @Test
    void doesCollideOverlapRightCollisionTest() {
        Asteroid element2 = new Asteroid(new Point2D.Double(0.0, 0.0));
        Coin element1 = new Coin(new Point2D.Double(0.199, 0.001));

        assertTrue(element1.doesCollide(element2));
        assertTrue(element2.doesCollide(element1));
    }

    /**
     * Tests if there is a collision if an element overlaps with only the top-right corner of another element.
     */
    @Test
    void doesCollideOverlapTopRightCollisionTest() {
        Asteroid element2 = new Asteroid(new Point2D.Double(0.0, 0.0));
        Coin element1 = new Coin(new Point2D.Double(0.199, -0.001));

        assertTrue(element1.doesCollide(element2));
        assertTrue(element2.doesCollide(element1));
    }

    /**
     * Test if elements move to the correct position after 1 second (1000 milliseconds).
     * The classes UFO and SpaceWorld are not tested, because they override the .move() method of SpaceElement.
     */
    @Test
    void move1SecTest() {
        createElementsArray();
        for(SpaceElement e : elements) {
            e.move(1000);
            if(e.getClass() != UFO.class && e.getClass() != SpaceWorld.class) {
                try {
                    assertEquals(e.getRelativePosition(), new Point2D.Double(1.0 + velocityManager.getRelativeVelocity(e.getClass()).x, 0.5 + velocityManager.getRelativeVelocity(e.getClass()).y));
                } catch (VelocityNotSetException exception) {
                    fail("VelocityNotSetException thrown");
                }
            }
        }
    }

    /**
     * Test if elements move to the correct position after 1 milliseconds.
     * The classes UFO and SpaceWorld are not tested, because they override the .move() method of SpaceElement.
     */
    @Test
    void move1MillisTest() {
        createElementsArray();
        for(SpaceElement e : elements) {
            e.move(1);
            if(e.getClass() != UFO.class && e.getClass() != SpaceWorld.class) {
                try {
                    assertEquals(e.getRelativePosition(), new Point2D.Double(1.0 + (1.0/1000.0)*velocityManager.getRelativeVelocity(e.getClass()).x, 0.5 + (1.0/1000.0)*velocityManager.getRelativeVelocity(e.getClass()).y));
                } catch (VelocityNotSetException exception) {
                    fail("VelocityNotSetException thrown");
                }
            }
        }
    }

    /**
     * Test if elements move to the correct position after -1 milliseconds (move backwards for 1 millisecond).
     * The classes UFO and SpaceWorld are not tested, because they override the .move() method of SpaceElement.
     */
    @Test
    void moveNegative1MillisTest() {
        createElementsArray();
        for(SpaceElement e : elements) {
            e.move(-1);
            if(e.getClass() != UFO.class && e.getClass() != SpaceWorld.class) {
                try {
                    assertEquals(e.getRelativePosition(), new Point2D.Double(1.0 + (-1.0/1000.0)*velocityManager.getRelativeVelocity(e.getClass()).x, 0.5 + (-1.0/1000.0)*velocityManager.getRelativeVelocity(e.getClass()).y));
                } catch (VelocityNotSetException exception) {
                    fail("VelocityNotSetException thrown");
                }
            }
        }
    }

    /**
     * Test if elements move to the correct position after 0 milliseconds (no movement).
     * The classes UFO and SpaceWorld are not tested, because they override the .move() method of SpaceElement.
     */
    @Test
    void move0MillisTest() {
        createElementsArray();
        for(SpaceElement e : elements) {
            e.move(0);
            if(e.getClass() != UFO.class && e.getClass() != SpaceWorld.class) {
                assertEquals(e.getRelativePosition(), new Point2D.Double(1.0, 0.5));
            }
        }
    }
}
