package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerGame;

/**
 * contains game instance
 * used for methods which are similar in each Controller (e.g. responsive calculations)
 * */
public abstract class Controller {
    public SpaceRunnerGame main;

    public abstract void setMain(SpaceRunnerGame main);

    /**
     * initializes values (e.g. setting default values in a text-box)
     * */
    public abstract void initialize();
}
