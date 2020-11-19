package ch.zhaw.it.pm3.spacerunner.model.spaceelement;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity.VelocityManager;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

class CoinTest {
    Coin coin1;

    //TODO: Do we need this test?
    //TODO: move is implemented in SpaceElement (Coin has no special movement)
    private VelocityManager velocityManager = VelocityManager.getManager();

    @BeforeEach
    void setUp() {
        coin1 = new Coin(new Point2D.Double(1,0.5));

    }

    @Test
    void moveTest(){

    }
}