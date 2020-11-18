package ch.zhaw.it.pm3.spacerunner.model.spaceelement.velocity;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.powerup.DoubleCoinsPowerUp;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.powerup.PowerUp;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.powerup.ShieldPowerUp;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.HorizontalSpeed;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.VerticalSpeed;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class VelocityManager {

    private Map<Class<? extends SpaceElement>, Point2D.Double> velocityMap = new HashMap<>();
    private static VelocityManager velocityManager = new VelocityManager();

    public static VelocityManager getManager(){
        return velocityManager;
    }

    private VelocityManager(){

    }

    public void setupGameElementVelocity(){
        velocityManager.setVelocity(Coin.class, new Point2D.Double(-HorizontalSpeed.COIN.getSpeed(), VerticalSpeed.ZERO.getSpeed()));
        velocityManager.setVelocity(UFO.class, new Point2D.Double(-HorizontalSpeed.UFO.getSpeed(), VerticalSpeed.UFO.getSpeed()));
        velocityManager.setVelocity(SpaceShip.class, new Point2D.Double(HorizontalSpeed.ZERO.getSpeed(), VerticalSpeed.SPACE_SHIP.getSpeed()));
        velocityManager.setVelocity(Asteroid.class, new Point2D.Double(-HorizontalSpeed.ASTEROID.getSpeed(), VerticalSpeed.ASTEROID.getSpeed()));
        velocityManager.setVelocity(ShieldPowerUp.class, new Point2D.Double(-HorizontalSpeed.POWERUP.getSpeed(), VerticalSpeed.ZERO.getSpeed()));
        velocityManager.setVelocity(DoubleCoinsPowerUp.class, new Point2D.Double(-HorizontalSpeed.POWERUP.getSpeed(), VerticalSpeed.ZERO.getSpeed()));
        velocityManager.setVelocity(SpaceWorld.class, new Point2D.Double(-HorizontalSpeed.BACKGROUND.getSpeed(), VerticalSpeed.ZERO.getSpeed()));
    }

    public synchronized void setVelocity(Class<? extends SpaceElement> elementClass, Point2D.Double velocity){
        velocityMap.put(elementClass, velocity);
    }

    public synchronized void accelerateAll(Point2D.Double acceleration){
        for(Map.Entry<Class<? extends SpaceElement>, Point2D.Double> currentVelocity : velocityMap.entrySet()){
            Point2D.Double velocity = currentVelocity.getValue();
            if(velocity.x != 0){
                velocity.x = velocity.x + acceleration.x;
            }
            if(velocity.y != 0){
                velocity.y = velocity.y + acceleration.y;
            }
            velocityMap.put(currentVelocity.getKey(), velocity);
        }
    }

    public synchronized void accelerateX(Class<? extends SpaceElement> elementClass, double xAcceleration){
        Point2D.Double velocity = velocityMap.get(elementClass);

        if(velocity != null){
            velocityMap.put(elementClass, new Point2D.Double(velocity.x + xAcceleration, velocity.y));
        }
    }

    public synchronized void accelerateY(Class<? extends SpaceElement> elementClass, double yAcceleration){
        Point2D.Double velocity = velocityMap.get(elementClass);

        if(velocity != null){
            velocityMap.put(elementClass, new Point2D.Double(velocity.x, velocity.y + yAcceleration));
        }
    }

    public synchronized void accelerate(Class<? extends SpaceElement> elementClass, Point2D.Double acceleration){
        Point2D.Double velocity = velocityMap.get(elementClass);

        if(velocity != null){
            velocityMap.put(elementClass, new Point2D.Double(velocity.x + acceleration.x, velocity.y + acceleration.y));
        }
    }


    public synchronized Point2D.Double getRelativeVelocity(Class<? extends SpaceElement> elementClass) throws VelocityNotSetException {
        Point2D.Double velocity = velocityMap.get(elementClass);
        if(velocity == null){
            throw new VelocityNotSetException("Velocity for " + elementClass.getSimpleName() + " was not set!");
        }

        return velocity;
    }
}
