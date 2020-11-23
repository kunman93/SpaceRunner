package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup;

/**
 * A PowerUpListener which is implemented by ActivatedPowerUpManager to execute the logic when a power-ups effect is
 * run out after a certain time.
 * @author nachbric
 */
public interface PowerUpListener {
    void powerUpTimerChanged(double timeLeft);

    /**
     * Performs the action when a power-up is finished.
     */
    void powerUpFinished(PowerUp powerUp);
}
