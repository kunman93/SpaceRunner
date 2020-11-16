package ch.zhaw.it.pm3.spacerunner.view;

/**
 * umbennen zu GameProportions
 * -> nach singleton pattern umsetzen, da genau nur einmal benötigt und auch im InitGame eigentlich:
 *
 * private static GameProportions instance = new GameProportions();
 * public static GameProportions getInstance(){
 *    return instance;
 * }
 * */

public class GameProportions {
    private double width;

    private double infoBarHeight;
    private double infoBarPaddingPercent = 0.1;
    private double infoBarMarginRightImage = 10;
    private double infoBarMarginRightText = 30;

    double proportionGame = 9;
    double proportionGameBar = 0.5; // todo aus 16:9 rausnehmen

    public void setInfoBarHeight(double infoBarHeight) {
        this.infoBarHeight = infoBarHeight;
    }

    public double getFontSize() {
        return infoBarHeight * (1 - 2 * infoBarPaddingPercent);
    }

    public double getTextWidth(int value) {
        int responsiveMinNumber = 1_000_000;
        if (value / responsiveMinNumber > 0) {
            return getFontSize() * 7;
        }
        return getFontSize() * 5;
    }

    public double getYPosition() {
        return infoBarHeight + infoBarHeight * proportionGame / proportionGameBar - getFontSize(); // todo schöner
    }

    public double getInfoBarHeight() {
        return infoBarHeight;
    }

    public double getInfoBarPaddingPercent() {
        return infoBarPaddingPercent;
    }

    public double getInfoBarMarginRightImage() {
        return infoBarMarginRightImage;
    }

    public double getInfoBarMarginRightText() {
        return infoBarMarginRightText;
    }


    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}
