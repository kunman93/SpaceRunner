package ch.zhaw.it.pm3.spacerunner.model.spaceelement;
import org.junit.jupiter.api.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class SpaceElementTest {
    SpaceElement element;

    @BeforeEach
    void setUp() {
        element = new SpaceElement(new Point(0,0)) {
            @Override
            protected void setElementHitbox() {
                setHeight(10);
                setWidth(10);
            }
        };
        element.setElementHitbox();
    }

    @Test
    void testGetPosition(){
        //velocity == (0,0) -> nextPosition = currentPosition
        assertEquals(element.getNextPosition(),element.getCurrentPosition());
        element.setVelocity(new Point(1,0));
        element.move();
        assertEquals(new Point(1,0), element.getCurrentPosition());
        assertEquals(new Point(2,0), element.getNextPosition());
    }

    @Test
    void testAccelerate(){
        assertEquals(new Point(0,0), element.getVelocity());
        element.accelerate(null);
        assertEquals(new Point(0,0), element.getVelocity());
        element.accelerate(new Point(1,0));
        assertEquals(new Point(1,0), element.getVelocity());
        element.accelerate(new Point(1,1));
        assertEquals(new Point(2,1), element.getVelocity());
    }

    @Test
    void testMove(){
        element.accelerate(new Point(1,0));
        element.move();
        assertEquals(new Point(1,0), element.getCurrentPosition());
        element.accelerate(new Point(1,0));
        element.move();
        assertEquals(new Point(3,0), element.getCurrentPosition());
    }

    @Test
    void testDoesCollide(){
        SpaceElement elementInside = new SpaceElement(new Point(1,1)){
            @Override
            protected void setElementHitbox() {
                setHeight(10);
                setWidth(10);
            }
        };
        SpaceElement elementOutside = new SpaceElement(new Point(11,11)){
            @Override
            protected void setElementHitbox() {
                setHeight(10);
                setWidth(10);
            }
        };
        elementInside.setElementHitbox();
        elementOutside.setElementHitbox();
        assertTrue(element.doesCollide(elementInside));
        assertFalse(element.doesCollide(elementOutside));
    }
}
