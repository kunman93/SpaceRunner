package ch.zhaw.it.pm3.spacerunner.controller;

public class GameViewNotFoundException extends Exception {
    public GameViewNotFoundException(String errorMessage)
    {
        super(errorMessage);
    }
}
