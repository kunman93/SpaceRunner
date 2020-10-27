package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PersistenceUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class SettingsViewController extends ViewController implements EventHandler<KeyEvent> {

    public Button homeButton;
    public TextField playerName;

    @FXML
    public void showMenu() {
        getMain().setFXMLView("view/menu.fxml");
    }

    @Override
    public void initialize() {
        playerName.setText(PersistenceUtil.loadProfile().getPlayerName());

    }

    @Override
    public void handle(KeyEvent keyEvent) {

    }

}
