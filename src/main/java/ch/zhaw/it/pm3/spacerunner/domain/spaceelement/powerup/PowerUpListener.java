package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup;

public interface PowerUpListener {
    void powerUpTimerChanged(double timeLeft);

    void powerUpFinished(PowerUp powerUp);
}
