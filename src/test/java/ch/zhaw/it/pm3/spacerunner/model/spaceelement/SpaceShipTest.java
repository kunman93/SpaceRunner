package ch.zhaw.it.pm3.spacerunner.model.spaceelement;
import org.junit.jupiter.api.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpaceShipTest {
    SpaceShip ship;

    @BeforeEach
    void setUp() {
        ship = new SpaceShip(new Point(0,0));
        ship.setSpaceShipSpeed(3);
    }

    @Test
    void directMoveUpTest(){
        assertEquals(new Point(0,0), ship.getCurrentPosition());
        ship.directMoveUp();
        assertEquals(new Point(0,-3), ship.getCurrentPosition());
        ship.setSpaceShipSpeed(1);
        ship.directMoveUp();
        assertEquals(new Point(0,-4), ship.getCurrentPosition());
    }

    @Test
    void directMoveDownTest(){
        assertEquals(new Point(0,0), ship.getCurrentPosition());
        ship.directMoveDown();
        assertEquals(new Point(0,3), ship.getCurrentPosition());
        ship.setSpaceShipSpeed(1);
        ship.directMoveDown();
        assertEquals(new Point(0,4), ship.getCurrentPosition());
    }

}
