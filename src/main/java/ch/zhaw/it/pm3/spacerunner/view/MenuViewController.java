package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;

public class MenuViewController extends ViewController implements EventHandler<KeyEvent> {

    @FXML public Button settingsButton;
    @FXML public Button startGameButton;

    /**
     * sets main
     * */
    @Override
    public void setMain(SpaceRunnerApp main) {
        this.main = main;
    }

    @FXML
    public void showGame() {
        main.setFXMLView("view/game.fxml");
    }

    @FXML
    public void showSettings() {
        main.setFXMLView("view/settings.fxml");
    }

    @Override
    public void initialize() {

    }

    @Override
    public void handle(KeyEvent keyEvent) {

    }
}
