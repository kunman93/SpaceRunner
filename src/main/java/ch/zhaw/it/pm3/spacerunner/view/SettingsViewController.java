package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PlayerProfile;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class SettingsViewController extends ViewController implements EventHandler<KeyEvent> {
    private PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
    public Button homeButton;
    public TextField playerName;
    public Slider soundVolume;
    public Slider framerate;
    private PlayerProfile player;

    @FXML
    public void showMenu() {
        player.setPlayerName(playerName.getText());
        player.setVolume((int) soundVolume.getValue());
        player.setFps((int) framerate.getValue());
        persistenceUtil.saveProfile(player);
        getMain().setFXMLView(FXMLFile.MENU);
    }

    @Override
    public void initialize() {
        player = persistenceUtil.loadProfile();
        playerName.setText(player.getPlayerName());
        soundVolume.setValue(player.getVolume());
        framerate.setValue(player.getFps());

        playerName.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals("")) {
                playerName.setStyle("-fx-border-color: red");
                homeButton.setDisable(true);
            } else {
                playerName.setStyle("-fx-border-color: white");
                homeButton.setDisable(false);
            }
        });

    }

    @Override
    public void handle(KeyEvent keyEvent) {

    }

}
