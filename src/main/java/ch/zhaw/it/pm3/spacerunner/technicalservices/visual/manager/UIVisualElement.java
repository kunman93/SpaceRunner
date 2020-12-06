package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager;

/**
 * Self made enum with classes.
 * Used to set visuals for ui elements (controls/icons... etc.) with the visual manager
 *
 * @author islermic
 */
public abstract class UIVisualElement {
    public static final Class<? extends VisualElement> COIN_COUNT = CoinCount.class;
    public static final Class<? extends VisualElement> DOUBLE_COIN_POWER_UP = DoubleCoinPowerUp.class;
    public static final Class<? extends VisualElement> SHIELD_POWER_UP = ShieldPowerUp.class;


    private abstract static class CoinCount implements VisualElement {

    }

    private abstract static class DoubleCoinPowerUp implements VisualElement {

    }

    private abstract static class ShieldPowerUp implements VisualElement {

    }
}
