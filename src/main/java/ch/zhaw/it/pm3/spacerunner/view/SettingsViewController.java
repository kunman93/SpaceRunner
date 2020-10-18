package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class SettingsViewController extends ViewController implements EventHandler<KeyEvent> {

    /**
     * sets main
     * */
    @Override
    public void setMain(SpaceRunnerApp main) {
        this.main = main;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void handle(KeyEvent keyEvent) {

    }
}
