package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.GameRatioUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.GameViewPort;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameRatioUtilTest {

    private GameRatioUtil gameRatioUtil = GameRatioUtil.getUtil();
    private final static int ITERATIONS = 100;
    private final static int VALUE = 500;

    @Test
    void testCalcRatioHeightBiggerOrEqualThanWidth() {
        for (int multiplier = 1; multiplier < ITERATIONS; multiplier++) {
            GameViewPort actual = gameRatioUtil.calcRatio(VALUE, VALUE * multiplier);
            GameViewPort expected = new GameViewPort(VALUE, VALUE / gameRatioUtil.getX_RATIO() * gameRatioUtil.getY_RATIO_GAME(),
                    VALUE / gameRatioUtil.getX_RATIO() * gameRatioUtil.getY_RATIO_GAME_BAR());
            assertEquals(Math.round(expected.getGameHeight()), Math.round(actual.getGameHeight()));
            assertEquals(Math.round(expected.getInfoBarHeight()), Math.round(actual.getInfoBarHeight()));
            assertEquals(Math.round(expected.getGameWidth()), Math.round(actual.getGameWidth()));
        }
    }

    @Test
    void testCalcRatioWidthBiggerThanHeight() {
        for (int multiplier = 2; multiplier < ITERATIONS; multiplier++) {
            GameViewPort actual = gameRatioUtil.calcRatio(VALUE * multiplier, VALUE);
            GameViewPort expected = new GameViewPort(VALUE / (gameRatioUtil.getY_RATIO_GAME_BAR() + gameRatioUtil.getY_RATIO_GAME()) * gameRatioUtil.getX_RATIO(),
                    VALUE / (gameRatioUtil.getY_RATIO_GAME() + gameRatioUtil.getY_RATIO_GAME_BAR()) * gameRatioUtil.getY_RATIO_GAME(),
                    VALUE / (gameRatioUtil.getY_RATIO_GAME() + gameRatioUtil.getY_RATIO_GAME_BAR()) * gameRatioUtil.getY_RATIO_GAME_BAR());
            assertEquals(Math.round(expected.getGameHeight()), Math.round(actual.getGameHeight()));
            assertEquals(Math.round(expected.getInfoBarHeight()), Math.round(actual.getInfoBarHeight()));
            assertEquals(Math.round(expected.getGameWidth()), Math.round(actual.getGameWidth()));

        }
    }

    @Test
    void testCalcRatioNegativeNumbers() {
        assertThrows(IllegalArgumentException.class, () -> gameRatioUtil.calcRatio(0, VALUE * -1));
        assertThrows(IllegalArgumentException.class, () -> gameRatioUtil.calcRatio(VALUE * -1, VALUE));
        assertThrows(IllegalArgumentException.class, () -> gameRatioUtil.calcRatio(VALUE * -1, VALUE * -1));
    }

    @Test
    void testGetTextWidth() {
        double textWidth = gameRatioUtil.getTextWidth(VALUE,1);
        assertEquals(5* VALUE, textWidth);
    }

    @Test
    void testGetFontSize() {
        double fontSize = gameRatioUtil.getFontSize(VALUE,0);
        assertEquals(0, fontSize);
        fontSize = gameRatioUtil.getFontSize(VALUE,1);
        assertEquals(VALUE, fontSize);
        fontSize = gameRatioUtil.getFontSize(VALUE,0.5);
        assertEquals(0.5 * VALUE, fontSize);
    }

    @Test
    void testGetFontSizeNegativeNumbers() {
        assertThrows(IllegalArgumentException.class, () -> assertEquals(VALUE, gameRatioUtil.getFontSize(VALUE,-1)));
    }

}
