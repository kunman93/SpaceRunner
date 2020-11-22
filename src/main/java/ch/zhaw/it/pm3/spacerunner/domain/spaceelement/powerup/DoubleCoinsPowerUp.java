package ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PersistenceUtil;

import java.awt.geom.Point2D;

public class DoubleCoinsPowerUp extends PowerUp {
    private final Persistence persistenceUtil = PersistenceUtil.getUtil();

    private int TIME_ACTIVE = 10000;

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
