package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager;

/**
 * Self made enum with classes.
 * Used to set visuals for ui elements (controls/icons... etc.) with the visual manager
 */
public class UIVisualElement {
    public final static Class<? extends VisualElement> COIN_COUNT = CoinCount.class;
    public final static Class<? extends VisualElement> DOUBLE_COIN_POWER_UP = DoubleCoinPowerUp.class;
    public final static Class<? extends VisualElement> SHIELD_POWER_UP = ShieldPowerUp.class;


    private class CoinCount implements VisualElement {

    }

    private class DoubleCoinPowerUp implements VisualElement {

    }

    private class ShieldPowerUp implements VisualElement {

    }
}
