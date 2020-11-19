package ch.zhaw.it.pm3.spacerunner.model.spaceelement.powerup;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;

import java.awt.geom.Point2D;
import java.util.*;

public abstract class PowerUp extends SpaceElement {
    private Set<PowerUpListener> powerUpListeners = new HashSet<>();
    private static Timer powerUpTimer = new Timer("PowerUp Timer");
    private TimerTask currentPowerUpTimerTask;
    private int multiplier = 1;

    public PowerUp(Point2D.Double startPosition) {
        super(startPosition);
    }

    public void addListener(PowerUpListener powerUpManagerListener) {
        powerUpListeners.add(powerUpManagerListener);
    }

    public void removeListener(PowerUpListener powerUpManagerListener) {
        powerUpListeners.remove(powerUpManagerListener);
    }

    public void incrementPowerUpMultiplier(){
        multiplier++;
        resetPowerUpTimer();
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void createPowerUpTimer(){
        currentPowerUpTimerTask = createPowerUpTimerTask();
        powerUpTimer.schedule(currentPowerUpTimerTask, getActiveTime());
    }

    private void resetPowerUpTimer(){
        if (currentPowerUpTimerTask != null){
            currentPowerUpTimerTask.cancel();
            currentPowerUpTimerTask = createPowerUpTimerTask();
            powerUpTimer.schedule(currentPowerUpTimerTask, getActiveTime());
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

    public abstract int getActiveTime();

    public abstract void activatePowerUp();
}
