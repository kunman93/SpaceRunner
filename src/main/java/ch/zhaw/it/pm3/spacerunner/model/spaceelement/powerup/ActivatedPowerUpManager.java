package ch.zhaw.it.pm3.spacerunner.model.spaceelement.powerup;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActivatedPowerUpManager implements PowerUpListener {

    private final Logger logger = Logger.getLogger(ActivatedPowerUpManager.class.getName());

    private final int GENERAL_POWER_UP_PROBABILITY = 33;
    private final Map<Class<? extends PowerUp>, PowerUp> activePowerUps = new HashMap<>();
    private final Map<Class<? extends PowerUp>, Integer> probabilities = new HashMap<>() {{
        put(DoubleCoinsPowerUp.class, 10);
        put(ShieldPowerUp.class, 15);
    }};

    public PowerUp generatePowerUps() {
        int x = (int) Math.floor(Math.random() * 100);
        if (x < GENERAL_POWER_UP_PROBABILITY) {
            int sum = 0;

            for (Map.Entry<Class<? extends PowerUp>, Integer> probability : probabilities.entrySet()) {
                sum += probability.getValue();
            }


            x = (int) Math.floor(Math.random() * sum);
            int secondSum = 0;

            for (Map.Entry<Class<? extends PowerUp>, Integer> probability : probabilities.entrySet()) {
                if (x < probability.getValue() + secondSum) {
                    PowerUp powerUp;
                    try {
                        powerUp = probability.getKey().getConstructor(Point2D.Double.class).newInstance(new Point2D.Double(1, Math.random() * 0.8 + 0.1));
                        return powerUp;
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error in PowerUp generation");
                        return null;
                    }

                }
            }
        }
        return null;
    }

    public synchronized void addPowerUp(PowerUp powerUp) {
        if (activePowerUps.containsKey(powerUp.getClass())) {
            activePowerUps.get(powerUp.getClass()).incrementPowerUpMultiplier();
        } else {
            activePowerUps.put(powerUp.getClass(), powerUp);
            powerUp.addListener(this);
            powerUp.activatePowerUp();
        }
    }

    public synchronized Map<Class<? extends PowerUp>, PowerUp> getActivePowerUps() {
        return activePowerUps;
    }

    public synchronized boolean hasShield() {
        return activePowerUps.containsKey(ShieldPowerUp.class);
    }

    public synchronized void removeShield() {
        activePowerUps.remove(ShieldPowerUp.class);
    }

    public synchronized int getCoinMultiplicator() {
        if (activePowerUps.containsKey(DoubleCoinsPowerUp.class)) {
            return activePowerUps.get(DoubleCoinsPowerUp.class).getMultiplier();
        } else {
            return 0;
        }
    }

    @Override
    public void powerUpTimerChanged(double timeLeft) {
        //Ignored
        logger.log(Level.INFO, "Not yet Implemented");
    }

    @Override
    public synchronized void powerUpFinished(PowerUp powerUp) {
        activePowerUps.remove(powerUp.getClass());
        powerUp.removeListener(this);
    }
}
