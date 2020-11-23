package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PersistenceUtil;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ActivatedPowerUpManager is responsible to generate, activate and deactivate power-ups.
 * @author islermic
 */
public class ActivatedPowerUpManager implements PowerUpListener {

    private final Logger logger = Logger.getLogger(ActivatedPowerUpManager.class.getName());
    private final Persistence persistenceUtil = PersistenceUtil.getUtil();

    private Random randomGen = new Random();
    private int GENERAL_POWER_UP_PROBABILITY = 33;
    private final Map<Class<? extends PowerUp>, PowerUp> activePowerUps = new HashMap<>();
    private final Map<Class<? extends PowerUp>, Integer> probabilities = new HashMap<>() {{
        put(DoubleCoinsPowerUp.class, 10);
        put(ShieldPowerUp.class, 15);
    }};

    /**
     * Sets up the ActivatedPowerUpManager and increases the probability of power-ups if the upgrade was selected in the shop.
     */
    public ActivatedPowerUpManager(){
        if(persistenceUtil.hasPowerUpChanceMultiplierUpgrade()){
            GENERAL_POWER_UP_PROBABILITY = GENERAL_POWER_UP_PROBABILITY * 2;
        }
    }

    /**
     * Generates a random power-up.
     * @return Returns the randomly generated PowerUp.
     */
    public PowerUp generatePowerUps() {

        int x = randomGen.nextInt(101);
        if (x < GENERAL_POWER_UP_PROBABILITY) {
            int sum = 0;

            for (Map.Entry<Class<? extends PowerUp>, Integer> probability : probabilities.entrySet()) {
                sum += probability.getValue();
            }


            x = randomGen.nextInt(sum);
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
                secondSum += probability.getValue();
            }
        }
        return null;
    }

    /**
     * Adds the powerUp to activePowerUps and activates it.
     * @param powerUp The power-up which should be activated.
     */
    public synchronized void addPowerUp(PowerUp powerUp) {
        if (activePowerUps.containsKey(powerUp.getClass())) {
            activePowerUps.get(powerUp.getClass()).incrementPowerUpMultiplier();
        } else {
            activePowerUps.put(powerUp.getClass(), powerUp);
            //TODO: is a listener necessary?
            powerUp.addListener(this);
            powerUp.activatePowerUp();
        }
    }

    /**
     * Checks if activePowerUps has ShieldPowerUp.
     * @return Return true if activePowerUps has ShieldPowerUp, else false.
     */
    public synchronized boolean hasShield() {
        return activePowerUps.containsKey(ShieldPowerUp.class);
    }

    /**
     * Remove the ShieldPowerUp from activePowerUps.
     */
    public synchronized void removeShield() {
        activePowerUps.remove(ShieldPowerUp.class);
    }

    public synchronized Map<Class<? extends PowerUp>, PowerUp> getActivePowerUps() {
        return activePowerUps;
    }

    public synchronized int getCoinMultiplicator() {
        if (activePowerUps.containsKey(DoubleCoinsPowerUp.class)) {
            return activePowerUps.get(DoubleCoinsPowerUp.class).getMultiplier();
        } else {
            return 0;
        }
    }

   //TODO: ask Team
    @Override
    public void powerUpTimerChanged(double timeLeft) {
        //Ignored
        logger.log(Level.INFO, "Not yet Implemented");
    }

    /**
     * Removes the powerUp if it's finished.
     * @param powerUp The powerUp which should be removed.
     */
    @Override
    public synchronized void powerUpFinished(PowerUp powerUp) {
        activePowerUps.remove(powerUp.getClass());
        powerUp.removeListener(this);
    }
}
