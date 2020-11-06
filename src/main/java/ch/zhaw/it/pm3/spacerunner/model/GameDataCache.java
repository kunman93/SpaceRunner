package ch.zhaw.it.pm3.spacerunner.model;

public class GameDataCache {
    private int coins;
    private int score;

    public GameDataCache(int coins, int score) {
        this.coins = coins;
        this.score = score;
    }

    public int getCoins() {
        return coins;
    }

    public int getScore() {
        return score;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
