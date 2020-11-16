package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

public interface PowerUpListener {
    void powerUpTimerChanged(double timeLeft);
    void powerUpFinished(PowerUp powerUp);
}
