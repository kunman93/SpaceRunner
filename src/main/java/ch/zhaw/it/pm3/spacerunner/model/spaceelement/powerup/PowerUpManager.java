package ch.zhaw.it.pm3.spacerunner.model.spaceelement.powerup;

import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class PowerUpManager implements PowerUpListener{

    private final int GENERAL_POWERUP_PROBABILITY = 33;
    private Map<Class<? extends PowerUp>,PowerUp> activePowerUps = new HashMap();
    private Map<Class<? extends PowerUp>, Integer> probabilities = new HashMap<>() {{
        put(DoubleCoinsPowerUp.class, 10);
        put(ShieldPowerUp.class, 15);
    }};

    public PowerUp generatePowerUps() {
        int x = (int) Math.floor(Math.random()*100);
        if (x < GENERAL_POWERUP_PROBABILITY) {
            int sum = 0;
            for (Map.Entry<Class<? extends PowerUp>, Integer> probability : probabilities.entrySet()) {
                sum += probability.getValue();
            }
            x = (int) Math.floor(Math.random()*sum);
            int secondSum = 0;

            for (Map.Entry<Class<? extends PowerUp>, Integer> probability : probabilities.entrySet()) {
                if (x < probability.getValue() + secondSum){
                    PowerUp powerUp;
                    try {
                        powerUp = probability.getKey().getConstructor(Point2D.Double.class).newInstance(new Point2D.Double(1,Math.random()*0.8+0.1));
                        addPowerUp(powerUp);
                        return powerUp;      
                     } catch (Exception e) {
                        // TODO: handle and logger
                        return null;
                    }

                }
            }
        }
        return null;
    }

    private void addPowerUp(PowerUp powerUp){
        if(activePowerUps.containsKey(powerUp.getClass())){
            activePowerUps.get(powerUp.getClass()).incrementPowerUpMultiplier();
        }
    }

    public Map<Class<? extends PowerUp>, PowerUp> getActivePowerUps() {
        return activePowerUps;
    }

    public boolean hasShield(){
        return activePowerUps.containsKey(ShieldPowerUp.class);
    }

    public void removeShield(){
        activePowerUps.remove(ShieldPowerUp.class);
    }

    public int getCoinMultiplicator(){
        if(activePowerUps.containsKey(DoubleCoinsPowerUp.class)){
            return activePowerUps.get(DoubleCoinsPowerUp.class).getMultiplier();
        }else{
            return 0;
        }
    }

    @Override
    public void powerUpTimerChanged(double timeLeft) {
        //Ignored
    }

    @Override
    public void powerUpFinished(PowerUp powerUp) {
        activePowerUps.remove(powerUp.getClass());
        powerUp.removeListener(this);
    }
}
