package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup.DoubleCoinsPowerUp;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup.ShieldPowerUp;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.speed.HorizontalSpeed;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.speed.VerticalSpeed;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a Manager for the Velocity of the different SpaceElements.
 * It is implemented with the singleton-pattern.
 * The Manager was implemented because for example all Asteroids share the same velocity. -> So it would make no sense to have the velocity in every element itself.
 * The Velocity is set per Class of SpaceElement (? extends SpaceElement)
 */
public class VelocityManager {

    private Map<Class<? extends SpaceElement>, Point2D.Double> velocityMap = new HashMap<>();
    private static VelocityManager velocityManager = new VelocityManager();

    public static VelocityManager getManager() {
        return velocityManager;
    }

    private VelocityManager() {

    }

    /**
     * Clear the velocity map in the manager. (Reset)
     */
    public void clear(){
        velocityMap = new HashMap<>();
    }

    /**
     * Setup velocities for the game elements.
     * If a new element is implemented, add code for the element in this function so it has a velocity at the start.
     */
    public void setupGameElementVelocity() {
        velocityManager.setRelativeVelocity(Coin.class, new Point2D.Double(-HorizontalSpeed.COIN.getSpeed(), VerticalSpeed.ZERO.getSpeed()));
        velocityManager.setRelativeVelocity(UFO.class, new Point2D.Double(-HorizontalSpeed.UFO.getSpeed(), VerticalSpeed.ZERO.getSpeed()));
        velocityManager.setRelativeVelocity(SpaceShip.class, new Point2D.Double(HorizontalSpeed.ZERO.getSpeed(), VerticalSpeed.SPACE_SHIP.getSpeed()));
        velocityManager.setRelativeVelocity(Asteroid.class, new Point2D.Double(-HorizontalSpeed.ASTEROID.getSpeed(), VerticalSpeed.ASTEROID.getSpeed()));
        velocityManager.setRelativeVelocity(ShieldPowerUp.class, new Point2D.Double(-HorizontalSpeed.POWERUP.getSpeed(), VerticalSpeed.ZERO.getSpeed()));
        velocityManager.setRelativeVelocity(DoubleCoinsPowerUp.class, new Point2D.Double(-HorizontalSpeed.POWERUP.getSpeed(), VerticalSpeed.ZERO.getSpeed()));
        velocityManager.setRelativeVelocity(SpaceWorld.class, new Point2D.Double(-HorizontalSpeed.BACKGROUND.getSpeed(), VerticalSpeed.ZERO.getSpeed()));
        velocityManager.setRelativeVelocity(Rocket.class, new Point2D.Double(-HorizontalSpeed.ROCKET.getSpeed(), VerticalSpeed.ZERO.getSpeed()));

    }

    /**
     * Set the velocity for a Class<? extends SpaceElement>. The velocity is given as a point. x is the x velocity and y is the y velocity.
     * @param elementClass Class<? extends SpaceElement> to set the velocity
     * @param velocity Point2D.Double velocity to be set
     */
    public synchronized void setRelativeVelocity(Class<? extends SpaceElement> elementClass, Point2D.Double velocity) {
        if(elementClass == null){
            throw new IllegalArgumentException("Element class can not be null");
        }else if(velocity == null){
            throw new IllegalArgumentException("velocity can not be null");
        }

        velocityMap.put(elementClass, velocity);
    }

    /**
     * Accelerates all the element classes which are currently managed.
     * @param acceleration Point2D.Double acceleration. Accelerate all x-velocities with acceleration.x and all y-velocities with acceleration.y
     */
    public synchronized void accelerateAll(Point2D.Double acceleration) {
        if(acceleration == null){
            throw new IllegalArgumentException("acceleration can not be null");
        }

        for (Map.Entry<Class<? extends SpaceElement>, Point2D.Double> currentVelocity : velocityMap.entrySet()) {
            Point2D.Double velocity = currentVelocity.getValue();
            if (velocity.x != 0) {
                velocity.x = velocity.x + acceleration.x;
            }
            if (velocity.y != 0) {
                velocity.y = velocity.y + acceleration.y;
            }
            velocityMap.put(currentVelocity.getKey(), velocity);
        }
    }

    /**
     * Accelerates the x velocity of a specific class. If the class is not currently managed, nothing happens.
     * @param elementClass Class<? extends SpaceElement> to accelerate
     * @param xAcceleration acceleration for x-velocity
     */
    public synchronized void accelerateX(Class<? extends SpaceElement> elementClass, double xAcceleration) {
        Point2D.Double velocity = velocityMap.get(elementClass);

        if (velocity != null) {
            velocityMap.put(elementClass, new Point2D.Double(velocity.x + xAcceleration, velocity.y));
        }
    }

    /**
     * Accelerates the y velocity of a specific class. If the class is not currently managed, nothing happens.
     * @param elementClass Class<? extends SpaceElement> to accelerate
     * @param yAcceleration acceleration for y-velocity
     */
    public synchronized void accelerateY(Class<? extends SpaceElement> elementClass, double yAcceleration) {
        Point2D.Double velocity = velocityMap.get(elementClass);

        if (velocity != null) {
            velocityMap.put(elementClass, new Point2D.Double(velocity.x, velocity.y + yAcceleration));
        }
    }

    /**
     * Accelerates velocity of a specific class.
     * @param elementClass Class<? extends SpaceElement> to accelerate
     * @param acceleration Point2D.Double acceleration. Accelerate x-velocity with acceleration.x and y-velocity with acceleration.y
     */
    public synchronized void accelerate(Class<? extends SpaceElement> elementClass, Point2D.Double acceleration) {
        if(acceleration == null){
            throw new IllegalArgumentException("acceleration can not be null");
        }

        Point2D.Double velocity = velocityMap.get(elementClass);

        if (velocity != null) {
            velocityMap.put(elementClass, new Point2D.Double(velocity.x + acceleration.x, velocity.y + acceleration.y));
        }
    }


    /**
     * Get the velocity of a specific class.
     * @param elementClass Class<? extends SpaceElement> to get the velocity of
     * @return velocity of the class
     * @throws VelocityNotSetException if the velocity was not set
     */
    public synchronized Point2D.Double getRelativeVelocity(Class<? extends SpaceElement> elementClass) throws VelocityNotSetException {
        if(elementClass == null){
            throw new IllegalArgumentException("Element class can not be null");
        }

        Point2D.Double velocity = velocityMap.get(elementClass);
        if (velocity == null) {
            throw new VelocityNotSetException("Velocity for " + elementClass.getSimpleName() + " was not set!");
        }

        return velocity;
    }
}
