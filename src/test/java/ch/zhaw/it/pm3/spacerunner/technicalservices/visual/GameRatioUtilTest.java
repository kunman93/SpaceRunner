package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.GameRatioUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.GameViewPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameRatioUtilTest {

    private GameRatioUtil gameRatioUtil = GameRatioUtil.getUtil();
    private Random random = new Random();
    private final int ITERATIONS = 10000;
    private final int UPPER_BOUND = ITERATIONS * 100;
    private int value;
    private int multiplier;
    private double containerHeight;

    @BeforeEach
    void setUp() {
        value = random.nextInt(10000);
        multiplier = random.nextInt(100);
        containerHeight = random.nextDouble();
    }

    @Test
    void testCalcRatioHeightBiggerThanWidth() {
        for (int i = 0; i < ITERATIONS; i++) {
            GameViewPort actual = gameRatioUtil.calcRatio(value, value * multiplier);
            GameViewPort expected = new GameViewPort(value, value / gameRatioUtil.getX_RATIO() * gameRatioUtil.getY_RATIO_GAME(),
                    value / gameRatioUtil.getX_RATIO() * gameRatioUtil.getY_RATIO_GAME_BAR());
            assertEquals(Math.round(expected.getGameHeight()), Math.round(actual.getGameHeight()));
            assertEquals(Math.round(expected.getInfoBarHeight()), Math.round(actual.getInfoBarHeight()));
            assertEquals(Math.round(expected.getGameWidth()), Math.round(actual.getGameWidth()));
            value = random.nextInt(UPPER_BOUND);
        }
    }

    @Test
    void testCalcRatioWidthBiggerThanHeight() {
        for (int i = 0; i < ITERATIONS; i++) {
            GameViewPort actual = gameRatioUtil.calcRatio(value * multiplier, value);
            GameViewPort expected = new GameViewPort(value / (gameRatioUtil.getY_RATIO_GAME_BAR() + gameRatioUtil.getY_RATIO_GAME()) * gameRatioUtil.getX_RATIO(),
                    value / (gameRatioUtil.getY_RATIO_GAME() + gameRatioUtil.getY_RATIO_GAME_BAR()) * gameRatioUtil.getY_RATIO_GAME(), value / (gameRatioUtil.getY_RATIO_GAME() + gameRatioUtil.getY_RATIO_GAME_BAR()) * gameRatioUtil.getY_RATIO_GAME_BAR());
            assertEquals(Math.round(expected.getGameHeight()), Math.round(actual.getGameHeight()));
            assertEquals(Math.round(expected.getInfoBarHeight()), Math.round(actual.getInfoBarHeight()));
            assertEquals(Math.round(expected.getGameWidth()), Math.round(actual.getGameWidth()));
            value = random.nextInt(UPPER_BOUND);
        }
    }

    @Test
    void testCalcRatioNegativeNumbers() {
        assertThrows(IllegalArgumentException.class, () -> gameRatioUtil.calcRatio(multiplier, value * -1));
        assertThrows(IllegalArgumentException.class, () -> gameRatioUtil.calcRatio(multiplier * -1, value));
        assertThrows(IllegalArgumentException.class, () -> gameRatioUtil.calcRatio(multiplier * -1, value * -1));
    }

    @Test
    void testGetTextWidth() {
        double textWidth = gameRatioUtil.getTextWidth(containerHeight,1);
        assertEquals(5*containerHeight, textWidth);
    }

    @Test
    void testGetFontSize() {
        double fontSize = gameRatioUtil.getFontSize(containerHeight,0);
        assertEquals(0, fontSize);
        fontSize = gameRatioUtil.getFontSize(containerHeight,1);
        assertEquals(containerHeight, fontSize);
        fontSize = gameRatioUtil.getFontSize(containerHeight,0.5);
        assertEquals(0.5 * containerHeight, fontSize);
    }

    @Test
    void testGetFontSizeNegativeNumbers() {
        assertThrows(IllegalArgumentException.class, () -> assertEquals(containerHeight, gameRatioUtil.getFontSize(containerHeight,-1)));
    }

}
