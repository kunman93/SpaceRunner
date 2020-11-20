package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util;

public class GameProportionUtil {

    private final double proportionGame = 8.5; // todo
    private final double proportionGameBar = 0.5;
    private final static GameProportionUtil GAME_PROPORTION_UTIL = new GameProportionUtil();

    private GameProportionUtil() {
    }

    public static GameProportionUtil getUtil() {
        return GAME_PROPORTION_UTIL;
    }

    public GameViewPort calcProportions(double width, double height) {
        double proportionY = proportionGame + proportionGameBar;
        double proportionX = 16;

        if (width / proportionX < height / proportionY) {
            height = width * proportionY / proportionX;
        } else if (width / proportionX > height / proportionY) {
            width = height * proportionX / proportionY;
        }

        double infoBarHeight = calcInfoBarHeight(height, proportionY);
        double gameHeight = height - infoBarHeight;

        return new GameViewPort(width, gameHeight, infoBarHeight);
    }

    public double getTextWidth(int value, double height, double percentage) { //todo count digits
        int responsiveMinNumber = 1_000_000;
        if (value / responsiveMinNumber > 0) {
            return getFontSize(height, percentage) * 7;
        }
        return getFontSize(height, percentage) * 5;
    }

    public double getFontSize(double height, double percentage) {
        return height * (1 - 2 * percentage);
    }

    private double calcInfoBarHeight(double height, double proportionY) {
        return height * (proportionGameBar / proportionY);
    }
}
