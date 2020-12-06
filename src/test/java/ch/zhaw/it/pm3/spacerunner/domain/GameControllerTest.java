package ch.zhaw.it.pm3.spacerunner.domain;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.SpaceShip;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.SpaceWorld;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.UFO;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityNotSetException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerTest {

    private GameController controller;
    private VelocityManager velocityManager = VelocityManager.getManager();

    @BeforeEach
    void setUp() {
        controller = new GameController();
        controller.setViewport(1000, 1000);
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
    void testProcessFrame() throws VelocityNotSetException, InterruptedException {
        controller.initialize();

        //Wait till a an object that moves "casually" appears
        // TODO: critical maybe improve the test so it stops if nothing comes for 15 seconds,
        //  because currently it would loop infinitely if we have en error in generating :c
        Optional<SpaceElement> spaceElement = findFirstTestableElement();

        while (spaceElement.isEmpty()) {
            controller.processFrame(false, false);
            spaceElement = findFirstTestableElement();
        }


        controller.processFrame(false, false);
        Point2D.Double velocity = velocityManager.getRelativeVelocity(spaceElement.get().getClass());
        Point2D.Double positionBefore = new Point2D.Double(spaceElement.get().getRelativePosition().x, spaceElement.get().getRelativePosition().y);
        Thread.sleep(1000);
        controller.processFrame(false, false);

        //This is to avoid floating point calculation errors
        double roundedOldPositionXPlusVelocity = Math.round((positionBefore.x + velocity.x) * 100.0) / 100.0;
        double roundedCurrentPositionX = Math.round(spaceElement.get().getRelativePosition().x * 100.0) / 100.0;
        double roundedOldPositionYPlusVelocity = Math.round((positionBefore.y + velocity.y) * 100.0) / 100.0;
        double roundedCurrentPositionY = Math.round(spaceElement.get().getRelativePosition().y * 100.0) / 100.0;

        //This is to avoid a very minimal error in the milliseconds caused by the lines between the (controller.processFrame and Thread.sleep) above
        double delta = 0.01;

        if (!(roundedOldPositionXPlusVelocity <= (roundedCurrentPositionX + delta) && roundedOldPositionXPlusVelocity >= (roundedCurrentPositionX - delta)) || !(roundedOldPositionYPlusVelocity <= (roundedCurrentPositionY + delta) && roundedOldPositionYPlusVelocity >= (roundedCurrentPositionY - delta))) {
            assertEquals(roundedOldPositionXPlusVelocity, roundedCurrentPositionX);
            assertEquals(roundedOldPositionYPlusVelocity, roundedCurrentPositionY);
        }

        controller.terminate();


    }

    /**
     * Find first Element which is not SpaceShip, SpaceWorld or UFO because they have special movement.
     *
     * @return Optional first element found if there is one
     */
    private Optional<SpaceElement> findFirstTestableElement() {
        return controller.getGameElements().stream().filter((spaceElement) -> {
            return !(spaceElement instanceof SpaceShip) && !(spaceElement instanceof SpaceWorld) && !(spaceElement instanceof UFO);
        }).findFirst();
    }

    @Test
    void processFrameWhenInitialized() {
        controller.initialize();
        controller.terminate();
    }

    @Test
    void processFrameWhenNotInitialized() {
        assertThrows(IllegalStateException.class, () -> {
            controller.processFrame(false, false);
        });
    }

    @Test
    void processFrameWhenTerminated() {
        controller.initialize();
        controller.terminate();

        assertThrows(IllegalStateException.class, () -> {
            controller.processFrame(false, false);
        });
    }
}
