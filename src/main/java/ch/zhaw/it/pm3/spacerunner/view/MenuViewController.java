package ch.zhaw.it.pm3.spacerunner.view;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;

public class MenuViewController extends ViewController implements EventHandler<KeyEvent> {

    @FXML
    public Button settingsButton;
    @FXML
    public Button startGameButton;

    @FXML
    public void showGame() {
        getMain().setFXMLView("view/game.fxml");
    }

    @FXML
    public void showSettings() {
        getMain().setFXMLView("view/settings.fxml");
    }

    @Override
    public void initialize() {

    }

    @Override
    public void handle(KeyEvent keyEvent) {

    }
}
