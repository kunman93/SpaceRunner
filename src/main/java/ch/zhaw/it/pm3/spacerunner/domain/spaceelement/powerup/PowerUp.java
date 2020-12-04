package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.SpaceElement;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * PowerUp is a space element that gives the spaceship a special ability when it's collected during a game.
 * @author nachbric
 */
public abstract class PowerUp extends SpaceElement {
    private final Set<PowerUpListener> powerUpListeners = new HashSet<>();
    private static final Timer powerUpTimer = new Timer("PowerUp Timer");
    private TimerTask currentPowerUpTimerTask;
    private int multiplier = 1;

    /**
     * Sets up the startPosition where a PowerUp should appear in the game.
     * @param startPosition The startPosition where the power-up should appear.
     */
    public PowerUp(Point2D.Double startPosition) {
        super(startPosition);
    }

    /**
     * Adds a powerUpManagerListener to powerUpListeners which is set of PowerUpListeners.
     * @param powerUpManagerListener Is a reference to ActivatedPowerUpManger which implements a PowerUpListener.
     */
    public void addListener(PowerUpListener powerUpManagerListener) {
        powerUpListeners.add(powerUpManagerListener);
    }

    /**
     * Removes a powerUpManagerListener from the set of PowerUpListeners.
     * @param powerUpManagerListener Is a reference to ActivatedPowerUpManger which implements a PowerUpListener.
     */
    public void removeListener(PowerUpListener powerUpManagerListener) {
        powerUpListeners.remove(powerUpManagerListener);
    }

    /**
     * Increment power-up multiplier and resets power-up timer.
     */
    public synchronized void incrementPowerUpMultiplier() {
        multiplier++;
        resetPowerUpTimer();
    }

    public int getMultiplier() {
        return multiplier;
    }

    /**
     * Creates a power-up timer.
     */
    protected synchronized void createPowerUpTimer() {
        currentPowerUpTimerTask = createPowerUpTimerTask();
        powerUpTimer.schedule(currentPowerUpTimerTask, getActiveTime());
    }

    private synchronized void resetPowerUpTimer() {
        if (currentPowerUpTimerTask != null) {
            currentPowerUpTimerTask.cancel();
            currentPowerUpTimerTask = createPowerUpTimerTask();
            powerUpTimer.schedule(currentPowerUpTimerTask, getActiveTime());
        }
    }

    private TimerTask createPowerUpTimerTask() {
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

    /**
     * Is an abstract-method which is implemented by the sub-classes to set the power-active-time.
     * @param activeTime active time to be set.
     */
    protected abstract void setActiveTime(int activeTime);

    /**
     * Is an abstract-method which is implemented by the sub-classes to get the power-active-time.
     * @return Returns the time how long a power-up should be active.
     */
    public abstract int getActiveTime();

    /**
     * Is an abstract-method which is implemented by the sub-classes to activate a power up.
     */
    public abstract void activatePowerUp();

    Set<PowerUpListener> getPowerUpListeners(){
        return Collections.unmodifiableSet(powerUpListeners);
    }
}
