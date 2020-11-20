package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

public enum ItemType {
    UPGRADE(0),
    PLAYER_MODEL(1);


    private final int value;

    ItemType(final int newValue) {
        value = newValue;
    }

    public int getValue() {
        return value;
    }
}
