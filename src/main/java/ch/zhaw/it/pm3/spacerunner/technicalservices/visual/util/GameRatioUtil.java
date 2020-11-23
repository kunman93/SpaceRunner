package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util;

/**
 * Calculates sizes and ratio required in the game ui. Implemented to use according to strategy pattern (only one
 * instance exists).
 *
 * @author freymar1
 * */
public class GameRatioUtil {

    private final double Y_RATIO_GAME = 8.5;
    private final double Y_RATIO_GAME_BAR = 0.5;
    private final double X_RATIO = 16;

    private final static GameRatioUtil GAME_PROPORTION_UTIL = new GameRatioUtil();

    private GameRatioUtil() { }

    public static GameRatioUtil getUtil() {
        return GAME_PROPORTION_UTIL;
    }

    /**
     * Calculates heights (game and infoBar) for GameViewPort.
     * @param width             width of primaryStage (view port)
     * @param height            height of primaryStage (view port)
     * @return GameViewPort     object holding the viewports ratios
     * */
    public GameViewPort calcRatio(double width, double height) {
        if (width < 0) {
            throw new IllegalArgumentException("Parameter 'width' requires a positive number.");
        } else if (height < 0) {
            throw new IllegalArgumentException("Parameter 'height' requires a positive number.");
        }

        double proportionY = Y_RATIO_GAME + Y_RATIO_GAME_BAR;

        if (width / X_RATIO < height / proportionY) {
            height = width * proportionY / X_RATIO;
        } else if (width / X_RATIO > height / proportionY) {
            width = height * X_RATIO / proportionY;
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
     * Negative numbers are ignored.
     * @param containerHeight   height of container where font is displayed
     * @param percentage        fontsize in percentage to container height
     * @return double           fontsize
     * */
    public double getFontSize(double containerHeight, double percentage) {
        if (containerHeight < 0) {
            throw new IllegalArgumentException("Parameter 'containerHeight' requires a positive number.");
        } else if (percentage < 0) {
            throw new IllegalArgumentException("Parameter 'percentage' requires a positive number.");
        }
        return Math.abs(containerHeight * percentage);
    }

    private double calcInfoBarHeight(double viewPortHeight, double proportionY) {
        if (viewPortHeight < 0) {
            throw new IllegalArgumentException("Parameter 'viewPortHeight' requires a positive number.");
        } else if (proportionY < 0) {
            throw new IllegalArgumentException("Parameter 'proportionY' requires a positive number.");
        }
        return viewPortHeight * (Y_RATIO_GAME_BAR / proportionY);
    }

    public double getY_RATIO_GAME() {
        return Y_RATIO_GAME;
    }

    public double getY_RATIO_GAME_BAR() {
        return Y_RATIO_GAME_BAR;
    }

    public double getX_RATIO() {
        return X_RATIO;
    }

}
