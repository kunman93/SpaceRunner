package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceShip;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.*;

public class GameControllerTest {

    GameController controller = new GameController();

    /**
     * Tests that the SpaceShip in the GameController gets initialized and assigned visuals.
     */
    @Test
    void initializeTest() {
        controller.initialize();
        assertNotEquals(controller.getSpaceShip(), null);
        assertNotEquals(controller.getSpaceShip().getVisual(), null);
    }

    @Test
    void moveSpaceShipTestUp() {
        SpaceShip spaceShip = new SpaceShip(new Point(20, 97), 50, 200);
        controller.initialize();
        controller.moveSpaceShip(SpaceShipDirection.UP);
        assertEquals(spaceShip.getCurrentPosition(), controller.getSpaceShip().getCurrentPosition());
    }

    @Test
    void moveSpaceShipTestDown() {
        SpaceShip spaceShip = new SpaceShip(new Point(20, 103), 50, 200);
        controller.initialize();
        controller.moveSpaceShip(SpaceShipDirection.DOWN);
        assertEquals(spaceShip.getCurrentPosition(), controller.getSpaceShip().getCurrentPosition());
    }
}
