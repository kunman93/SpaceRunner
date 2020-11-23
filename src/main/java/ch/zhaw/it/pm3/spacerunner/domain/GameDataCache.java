package ch.zhaw.it.pm3.spacerunner.domain;

/**
 * Stores the achievements of a game to be used in different views (e.g. for game-ended-view).
 *
 * @author freymar1
 * */
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
