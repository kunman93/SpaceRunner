package ch.zhaw.it.pm3.spacerunner.model.spaceelement;

import java.awt.geom.Point2D;
import java.util.*;

public class PowerUpManager implements PowerUpListener{

    private final int GENERAL_POWERUP_PROBABILITY = 33;
    private Map<PowerUp, Integer> activePowerUps = new HashMap<>();


    public PowerUp generatePowerUps() {
        int x = (int) Math.floor(Math.random()*100);
        if (x < GENERAL_POWERUP_PROBABILITY) {
            int sum = 0;
            for (PowerUpType p : PowerUpType.values()) {
                sum += p.getProbabilityPercent();
            }
            x = (int) Math.floor(Math.random()*sum);
            int secondSum = 0;
            for (PowerUpType p : PowerUpType.values()) {
                if (x < p.getProbabilityPercent() + secondSum){
                    PowerUp powerUp = new PowerUp(new Point2D.Double(1,Math.random()*0.8+0.1), p);
                    addPowerUp(powerUp);
                    return powerUp;
                }
            }
        }
        return null;
    }

    private void powerUpIncrement(PowerUp powerUp){
        if (activePowerUps.containsKey(powerUp)) {
            activePowerUps.put(powerUp, activePowerUps.get(powerUp) + 1);
            for (PowerUp power : activePowerUps.keySet()){
                if (power.equals(powerUp)){
                    power.resetPowerUpTimer();
                }
            }
        } else {
            activePowerUps.put(powerUp, 1);
        }
    }


    private void addPowerUp(PowerUp powerUp){
        if (powerUp.getType().equals(PowerUpType.DOUBLECOINS)) {
            powerUpIncrement(powerUp);
            powerUp.addListener(this);
        } else if (powerUp.getType().equals(PowerUpType.SHIELD)) {
            if (!activePowerUps.containsKey(powerUp)) {
                activePowerUps.put(powerUp, 1);
            }
        } else {
            //ToDo unknown PowerUp
        }
    }

    public Map<PowerUp, Integer> getActivePowerUps() {
        return activePowerUps;
    }

    public boolean hasShield(){
        PowerUp powerUp =  new PowerUp(new Point2D.Double(0,0), PowerUpType.SHIELD);
        return activePowerUps.containsKey(powerUp);
    }

    public void removeShield(){
        PowerUp powerUp =  new PowerUp(new Point2D.Double(0,0), PowerUpType.SHIELD);
        activePowerUps.remove(powerUp);
    }

    public int getCoinMultiplicator(){
        PowerUp powerUp =  new PowerUp(new Point2D.Double(0,0), PowerUpType.DOUBLECOINS);
        return activePowerUps.getOrDefault(powerUp, 0);
    }

    @Override
    public void powerUpTimerChanged(double timeLeft) {
        //Ignored
    }

    @Override
    public void powerUpFinished(PowerUp powerUp) {
        activePowerUps.remove(powerUp);
        powerUp.removeListener(this);
    }
}
