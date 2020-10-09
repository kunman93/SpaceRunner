package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerGame;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;

public class MenuController extends Controller implements EventHandler<KeyEvent> {

    @FXML public Button settingsButton;
    @FXML public Button startGameButton;

    /**
     * sets main
     * */
    @Override
    public void setMain(SpaceRunnerGame main) {
        this.main = main;
    }

    @FXML
    public void showGame() {
        main.setView("game.fxml");
    }

    @FXML
    public void showSettings() {
        main.setView("settings.fxml");
    }

    @Override
    public void initialize() {

    }

    @Override
    public void handle(KeyEvent keyEvent) {

    }
}
