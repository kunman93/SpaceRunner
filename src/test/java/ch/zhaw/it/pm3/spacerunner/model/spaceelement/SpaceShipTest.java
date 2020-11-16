package ch.zhaw.it.pm3.spacerunner.model.spaceelement;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.manager.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.HorizontalSpeed;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.VerticalSpeed;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpaceShipTest {
    private SpaceShip ship;
    private VelocityManager velocityManager = VelocityManager.getManager();

    @BeforeEach
    void setUp() {
        ship = new SpaceShip(new Point(0,0));
        velocityManager.setWidth(100);
        velocityManager.setHeight(100);
        velocityManager.setVelocity(SpaceShip.class, new Point2D.Double(HorizontalSpeed.ZERO.getSpeed(), VerticalSpeed.SPACE_SHIP.getSpeed()));
    }

    @Test
    void directMoveUpTest(){
        assertEquals(new Point(0,0), ship.getRelativePosition());
        ship.directMoveUp();
        assertEquals(new Point(0,-2), ship.getRelativePosition());
        ship.directMoveUp();
        assertEquals(new Point(0,-4), ship.getRelativePosition());
    }

    @Test
    void directMoveDownTest(){
        assertEquals(new Point(0,0), ship.getRelativePosition());
        ship.directMoveDown();
        assertEquals(new Point(0,2), ship.getRelativePosition());
        ship.directMoveDown();
        assertEquals(new Point(0,4), ship.getRelativePosition());
    }

}
