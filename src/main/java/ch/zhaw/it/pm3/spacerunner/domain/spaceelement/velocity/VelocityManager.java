package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup.DoubleCoinsPowerUp;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup.ShieldPowerUp;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.speed.HorizontalSpeed;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.speed.VerticalSpeed;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class VelocityManager {

    private Map<Class<? extends SpaceElement>, Point2D.Double> velocityMap = new HashMap<>();
    private static VelocityManager velocityManager = new VelocityManager();

    public static VelocityManager getManager() {
        return velocityManager;
    }

    private VelocityManager() {

    }

    public void clear(){
        velocityMap = new HashMap<>();
    }

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

    public synchronized void setRelativeVelocity(Class<? extends SpaceElement> elementClass, Point2D.Double velocity) {
        if(elementClass == null){
            throw new IllegalArgumentException("Element class can not be null");
        }else if(velocity == null){
            throw new IllegalArgumentException("velocity can not be null");
        }

        velocityMap.put(elementClass, velocity);
    }

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

    public synchronized void accelerateX(Class<? extends SpaceElement> elementClass, double xAcceleration) {
        Point2D.Double velocity = velocityMap.get(elementClass);

        if (velocity != null) {
            velocityMap.put(elementClass, new Point2D.Double(velocity.x + xAcceleration, velocity.y));
        }
    }

    public synchronized void accelerateY(Class<? extends SpaceElement> elementClass, double yAcceleration) {
        Point2D.Double velocity = velocityMap.get(elementClass);

        if (velocity != null) {
            velocityMap.put(elementClass, new Point2D.Double(velocity.x, velocity.y + yAcceleration));
        }
    }

    public synchronized void accelerate(Class<? extends SpaceElement> elementClass, Point2D.Double acceleration) {
        if(acceleration == null){
            throw new IllegalArgumentException("acceleration can not be null");
        }

        Point2D.Double velocity = velocityMap.get(elementClass);

        if (velocity != null) {
            velocityMap.put(elementClass, new Point2D.Double(velocity.x + acceleration.x, velocity.y + acceleration.y));
        }
    }


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
