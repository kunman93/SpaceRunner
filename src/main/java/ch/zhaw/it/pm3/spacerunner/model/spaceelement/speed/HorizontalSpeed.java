package ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed;

public enum HorizontalSpeed {
    ZERO(0),
    ASTEROID(3),
    COIN(2),
    BACKGROUND(3),
    UFO(3);


    private int speed;

    HorizontalSpeed(int speed){
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }
}
