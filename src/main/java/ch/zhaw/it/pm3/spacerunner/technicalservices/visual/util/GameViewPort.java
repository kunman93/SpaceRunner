package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util;

/**
 * Holds the calculated sizes, according to ratio (from GameRatioUtil.java), for the game.
 *
 * @author freymar1
 */
public class GameViewPort {
    private final double gameWidth;
    private final double gameHeight;
    private final double infoBarHeight;

    public GameViewPort(double gameWidth, double gameHeight, double infoBarHeight) {
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.infoBarHeight = infoBarHeight;
    }

    public double getGameWidth() {
        return gameWidth;
    }

    public double getGameHeight() {
        return gameHeight;
    }

    public double getInfoBarHeight() {
        return infoBarHeight;
    }

}
