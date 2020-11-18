package ch.zhaw.it.pm3.spacerunner.view;

public class GameViewPort {
    private double gameWidth;
    private double gameHeight;
    private double infoBarHeight;

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
