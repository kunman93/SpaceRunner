package ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed;

public enum VerticalSpeed {
    ZERO(0),
    ASTEROID(1),
    SPACE_SHIP(6),
    UFO(3);


    private int speed;

    VerticalSpeed(int speed){
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
}
