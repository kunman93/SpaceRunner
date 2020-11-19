package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util;

public enum ContentId {
    POWER_UP_CHANCE_MULTIPLIER(0),
    DOUBLE_DURATION_COIN_UPGRADE(1),
    SHIP_SKIN_1(2),
    SHIP_SKIN_2(3);


    private final int value;

    ContentId(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
