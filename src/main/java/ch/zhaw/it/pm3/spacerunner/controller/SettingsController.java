package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerGame;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class SettingsController extends Controller implements EventHandler<KeyEvent> {

    /**
     * sets main
     * */
    @Override
    public void setMain(SpaceRunnerGame main) {
        this.main = main;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void handle(KeyEvent keyEvent) {

    }
}
