package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;

/**
 * contains game instance
 * used for methods which are similar in each Controller (e.g. responsive calculations)
 */
public abstract class ViewController {
    private static SpaceRunnerApp main;

    public static void  setMain(SpaceRunnerApp main) {
        ViewController.main = main;
    }

    public static SpaceRunnerApp getMain() {
        return main;
    }

    /**
     * initializes values (e.g. setting default values in a text-box)
     */
    public abstract void initialize();
}
