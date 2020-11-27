package ch.zhaw.it.pm3.spacerunner.ui;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.domain.GameDataCache;

/**
 * Defines required methods of a ViewController and stores the achievements of the last played game.
 *
 * @author freymar1
 */
public abstract class ViewController {
    private static SpaceRunnerApp main;
    protected static final String DEFAULT_FONT = "Bauhaus 93";
    private static GameDataCache gameDataCache = null;

    public static void setMain(SpaceRunnerApp main) { ViewController.main = main; }

    public static SpaceRunnerApp getMain() {
        return main;
    }

    public static GameDataCache getGameDataCache() {
        return gameDataCache;
    }

    public static void setGameDataCache(GameDataCache gameDataCache) {
        ViewController.gameDataCache = gameDataCache;
    }
}
