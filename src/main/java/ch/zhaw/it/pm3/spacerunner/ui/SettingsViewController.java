package ch.zhaw.it.pm3.spacerunner.ui;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.GameSoundUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.SoundUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

/**
 * SettingsViewController is a controller class responsible for the Settings-View (Settings.fxml).
 * @author freymar1
 */
public class SettingsViewController extends ViewController {
    private final Persistence persistenceUtil = PersistenceUtil.getUtil();
    private final GameSoundUtil gameSoundUtil = GameSoundUtil.getUtil();
    public Button homeButton;
    public TextField playerName;
    public Slider soundVolume;
    public Slider framerate;
    private PlayerProfile playerProfile;

    /**
     * Changes the view to Menu.fxml when the Menu-Button is pressed and updates the setting of the player profile.
     */
    @FXML
    public void showMenu() {
        playerProfile.setPlayerName(playerName.getText());
        playerProfile.setVolume((int) soundVolume.getValue());
        playerProfile.setAudioEnabled(soundVolume.getValue() > 0);
        playerProfile.setFps((int) framerate.getValue());
        persistenceUtil.saveProfile(playerProfile);
        gameSoundUtil.setVolume(playerProfile.getVolume());
        getMain().setupBackgroundMusic();
        getMain().setFXMLView(FXMLFile.MENU);
    }

    public void initialize() {
        playerProfile = persistenceUtil.loadProfile();
        playerName.setText(playerProfile.getPlayerName());
        soundVolume.setValue(playerProfile.getVolume());
        framerate.setValue(playerProfile.getFps());

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
}
