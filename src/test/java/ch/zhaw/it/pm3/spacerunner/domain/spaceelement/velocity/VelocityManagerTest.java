package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.Asteroid;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.Rocket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.geom.Point2D;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VelocityManagerTest {

    private final VelocityManager velocityManager = VelocityManager.getManager();

    @BeforeEach
    void setUp() {
        velocityManager.clear();
    }

    @Test
    void getRelativeVelocityTestWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            velocityManager.getRelativeVelocity(null);
        });
    }

    @Test
    void getRelativeVelocityTestWhenNotSet() {
        assertThrows(VelocityNotSetException.class, () -> {
            velocityManager.getRelativeVelocity(Rocket.class);
        });
    }

    @Test
    void accelerateTest() throws VelocityNotSetException {
        Point2D.Double rocketVelocity = new Point2D.Double(1.0, 2.0);

        velocityManager.setRelativeVelocity(Rocket.class, rocketVelocity);

        Point2D.Double acceleration = new Point2D.Double(0.5, -0.5);
        velocityManager.accelerate(Rocket.class, acceleration);

        assertEquals(1.5, velocityManager.getRelativeVelocity(Rocket.class).x);
        assertEquals(1.5, velocityManager.getRelativeVelocity(Rocket.class).y);
    }

    @Test
    void accelerateTestWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            velocityManager.accelerate(Rocket.class, null);
        });

    }

    @Test
    void accelerateXTest() throws VelocityNotSetException {
        Point2D.Double rocketVelocity = new Point2D.Double(1.0, 2.0);

        velocityManager.setRelativeVelocity(Rocket.class, rocketVelocity);

        velocityManager.accelerateX(Rocket.class, 0.5);

        assertEquals(1.5, velocityManager.getRelativeVelocity(Rocket.class).x);
    }

    @Test
    void accelerateYTest() throws VelocityNotSetException {
        Point2D.Double rocketVelocity = new Point2D.Double(1.0, 2.0);

        velocityManager.setRelativeVelocity(Rocket.class, rocketVelocity);

        velocityManager.accelerateY(Rocket.class, 0.5);

        assertEquals(2.5, velocityManager.getRelativeVelocity(Rocket.class).y);
    }

    @Test
    void accelerateAllTest() throws VelocityNotSetException {
        Point2D.Double rocketVelocity = new Point2D.Double(1.0, 1.0);
        Point2D.Double asteroidVelocity = new Point2D.Double(-1, 10.0);

        velocityManager.setRelativeVelocity(Rocket.class, rocketVelocity);
        velocityManager.setRelativeVelocity(Asteroid.class, asteroidVelocity);

        Point2D.Double acceleration = new Point2D.Double(0.5, -0.5);
        velocityManager.accelerateAll(acceleration);

        assertEquals(1.5, velocityManager.getRelativeVelocity(Rocket.class).x);
        assertEquals(0.5, velocityManager.getRelativeVelocity(Rocket.class).y);
        assertEquals(-0.5, velocityManager.getRelativeVelocity(Asteroid.class).x);
        assertEquals(9.5, velocityManager.getRelativeVelocity(Asteroid.class).y);
    }

    @Test
    void accelerateAllTestWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            velocityManager.accelerateAll(null);
        });
    }

    @Test
    void setRelativeVelocityTestWithClassNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Point2D.Double velocity = new Point2D.Double(1.0, 1.0);
            velocityManager.setRelativeVelocity(null, velocity);
        });
    }

    @Test
    void setRelativeVelocityTestWithVelocityNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            velocityManager.setRelativeVelocity(Rocket.class, null);
        });
    }

    @Test
    void setRelativeVelocityTest() throws VelocityNotSetException {
        Point2D.Double velocity = new Point2D.Double(1.0, 1.0);

        velocityManager.setRelativeVelocity(Rocket.class, velocity);

        assertEquals(velocity, velocityManager.getRelativeVelocity(Rocket.class));
    }


    @Test
    void setupGameElementVelocityTest() throws VelocityNotSetException {
        velocityManager.setupGameElementVelocity();
        velocityManager.getRelativeVelocity(Rocket.class);
    }

}
