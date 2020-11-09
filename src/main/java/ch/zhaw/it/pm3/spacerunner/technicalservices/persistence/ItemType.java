package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

public enum ItemType {
    UPGRADES(0),
    PLAYER_MODEL(1);



    private final int value;

    ItemType(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
