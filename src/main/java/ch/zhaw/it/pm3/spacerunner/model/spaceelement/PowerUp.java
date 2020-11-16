package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.geom.Point2D;
import java.util.*;

public class PowerUp extends SpaceElement {
    private Set<PowerUpListener> powerUpListeners = new HashSet<>();
    private Timer powerUpTimer = new Timer("PowerUp Timer");
    private PowerUpType type;
    private TimerTask currentPowerUpTimerTask;
    public PowerUp(Point2D.Double startPosition, PowerUpType type) {
        super(startPosition);
        this.type = type;
    }

    public PowerUpType getType() {
        return type;
    }

    public void setType(PowerUpType type) {
        this.type = type;
    }


    public void addListener(PowerUpListener powerUpManagerListener) {
        powerUpListeners.add(powerUpManagerListener);
    }

    public void removeListener(PowerUpListener powerUpManagerListener) {
        powerUpListeners.remove(powerUpManagerListener);
    }

    private void createPowerUpTimer(){
        currentPowerUpTimerTask = createPowerUpTimerTask();
        powerUpTimer.schedule(currentPowerUpTimerTask, type.getDuration());
    }

    public void resetPowerUpTimer(){
        if (currentPowerUpTimerTask != null){
            currentPowerUpTimerTask.cancel();
            currentPowerUpTimerTask = createPowerUpTimerTask();
            powerUpTimer.schedule(currentPowerUpTimerTask, type.getDuration());
        }
    }

    private TimerTask createPowerUpTimerTask(){
        PowerUp powerUp = this;
        return new TimerTask() {
            @Override
            public void run() {
                for (PowerUpListener powerUpListener : powerUpListeners) {
                    powerUpListener.powerUpFinished(powerUp);
                }
            }
        };
    }
    public void activatePowerUp(){
        if (type.equals(PowerUpType.DOUBLECOINS)) {
            createPowerUpTimer();
        } else {
            //ToDo unknown PowerUp
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PowerUp powerUp = (PowerUp) o;
        return type == powerUp.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type);
    }
}
