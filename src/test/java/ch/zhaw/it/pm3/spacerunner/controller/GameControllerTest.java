package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceShip;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.*;

public class GameControllerTest {

    private GameController controller;

    @BeforeEach
    void setUp() {
        controller = new GameController();
    }
    /**
     * Tests that the SpaceShip in the GameController gets initialized and assigned visuals.
     */
    @Test
    void initializeTest() {
        controller.initialize();
        assertNotEquals(controller.getSpaceShip(), null);
    }

    @Test
    void moveSpaceShipTestUp() {
        controller.initialize();
        int y = controller.getSpaceShip().getCurrentPosition().y - SpaceShip.getSpaceShipSpeed();
        controller.moveSpaceShip(SpaceShipDirection.UP);
        assertEquals(y, controller.getSpaceShip().getCurrentPosition().y);
    }

    @Test
    void moveSpaceShipTestDown() {
        controller.initialize();
        int y = controller.getSpaceShip().getCurrentPosition().y + SpaceShip.getSpaceShipSpeed();
        controller.moveSpaceShip(SpaceShipDirection.DOWN);
        assertEquals(y, controller.getSpaceShip().getCurrentPosition().y);
    }
}
