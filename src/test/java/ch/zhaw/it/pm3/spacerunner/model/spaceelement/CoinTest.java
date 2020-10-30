package ch.zhaw.it.pm3.spacerunner.model.spaceelement;
import org.junit.jupiter.api.*;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class CoinTest {
    Coin coin1;

    @BeforeEach
    void setUp() {
        coin1 = new Coin(new Point(200,100),50,50);
        coin1.setVelocity(new Point(-3,0));
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