package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.JsonPersistenceUtil;

import java.awt.geom.Point2D;

/**
 * DoubleCoinsPowerUp is a power-up which doubles the coin value for a certain time when collected.
 * @author nachbric
 */
public class DoubleCoinsPowerUp extends PowerUp {
    private final Persistence persistenceUtil = JsonPersistenceUtil.getUtil();

    private static int TIME_ACTIVE = 10000;

    /**
     * Sets up the startPosition and doubles the duration of this power-ups if the upgrade was selected in the shop.
     * @param startPosition The startPosition where the double coins power-up should appear.
     */
    public DoubleCoinsPowerUp(Point2D.Double startPosition) {
        super(startPosition);

        if(persistenceUtil.hasDoubleDurationForCoinPowerUp()){
            TIME_ACTIVE = TIME_ACTIVE*2;
        }
    }

    @Override
    public int getActiveTime() {
        return TIME_ACTIVE;
    }

    @Override
    public void activatePowerUp() {
        createPowerUpTimer();
    }
}
