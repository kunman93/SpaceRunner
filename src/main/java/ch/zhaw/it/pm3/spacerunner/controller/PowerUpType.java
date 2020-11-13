package ch.zhaw.it.pm3.spacerunner.controller;

public enum PowerUpType {
    DOUBLECOINS(10,10000),
    SHIELD(15, Integer.MAX_VALUE);

    private int probabilityPercent;
    private int duration;

    PowerUpType(int p, int d){
        probabilityPercent = p;
        duration = d;
    }

    public int getProbabilityPercent() {
        return probabilityPercent;
    }

    public void setProbabilityPercent(int probabilityPercent) {
        this.probabilityPercent = probabilityPercent;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
