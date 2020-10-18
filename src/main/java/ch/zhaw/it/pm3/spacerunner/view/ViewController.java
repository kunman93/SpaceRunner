package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;

/**
 * contains game instance
 * used for methods which are similar in each Controller (e.g. responsive calculations)
 * */
public abstract class ViewController {
    public SpaceRunnerApp main;

    public abstract void setMain(SpaceRunnerApp main);

    /**
     * initializes values (e.g. setting default values in a text-box)
     * */
    public abstract void initialize();
}
