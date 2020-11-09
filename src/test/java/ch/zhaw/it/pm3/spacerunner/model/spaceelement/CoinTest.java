package ch.zhaw.it.pm3.spacerunner.model.spaceelement;
import org.junit.jupiter.api.*;

import java.awt.*;
import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.*;

class CoinTest {
    Coin coin1;

    //TODO: Do we need this test?
    //TODO: move is implemented in SpaceElement (Coin has no special movement)
    private VelocityManager velocityManager = VelocityManager.getInstance();

    @BeforeEach
    void setUp() {
        coin1 = new Coin(new Point(200,100));
        //coin1.setVelocity(new Point(-3,0));
        velocityManager.setHeight(100);
        velocityManager.setWidth(100);
        velocityManager.setVelocity(Coin.class, new Point2D.Double(-0.03, 0));

    }

    @Test
    void moveTest(){
        assertEquals(new Point(200,100), coin1.getCurrentPosition());
        coin1.move();
        assertEquals(new Point(197,100), coin1.getCurrentPosition());
        coin1.move();
        assertEquals(new Point(194,100), coin1.getCurrentPosition());
    }
}