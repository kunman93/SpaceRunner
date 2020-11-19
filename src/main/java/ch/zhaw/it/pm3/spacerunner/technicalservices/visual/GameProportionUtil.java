package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import ch.zhaw.it.pm3.spacerunner.view.GameViewPort;

/**
 *
 * */
public class GameProportionUtil {

    private final double proportionGame = 8.5; // todo
    private final double proportionGameBar = 0.5;
    private final static GameProportionUtil GAME_PROPORTION_UTIL = new GameProportionUtil();

    private GameProportionUtil() {}

    public static GameProportionUtil getUtil() {
        return GAME_PROPORTION_UTIL;
    }

    /**
     * Calculates heights (game and infoBar) for GameViewPort
     * @param width             width of primaryStage (view port)
     * @param height            width of primaryStage (view port)
     * @return double           fontsize
     * */
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

    /**
     * Calculates the text width proportional to the font size. Factor 5 is capable to display MAX_INTEGER.
     * @param containerHeight   height of container where font is displayed
     * @param percentage        font padding to container in percentage (only heights)
     * @return double           width that can display all integer values
     * */
    public double getTextWidth(double containerHeight, double percentage) {
        return getFontSize(containerHeight, percentage) * 5;
    }

    /**
     * Calculates the font size proportional to the parent container (subtracts the vertical paddings from the container).
     * @param containerHeight   height of container where font is displayed
     * @param percentage        font padding to container in percentage (only heights)
     * @return double           fontsize
     * */
    public double getFontSize(double containerHeight, double percentage) {
        return containerHeight * (1 - 2 * percentage);
    }

    private double calcInfoBarHeight(double viewPortHeight, double proportionY) {
        return viewPortHeight * (proportionGameBar / proportionY);
    }


}
