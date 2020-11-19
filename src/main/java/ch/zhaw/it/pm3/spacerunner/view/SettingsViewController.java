package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.SoundUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

public class SettingsViewController extends ViewController {
    private PersistenceUtil persistenceUtil = PersistenceUtil.getUtil();
    private SoundUtil soundUtil = SoundUtil.getInstance();
    public Button homeButton;
    public TextField playerName;
    public Slider soundVolume;
    public Slider framerate;
    private PlayerProfile playerProfile;

    @FXML
    public void showMenu() {
        playerProfile.setPlayerName(playerName.getText());
        playerProfile.setVolume((int) soundVolume.getValue());
        playerProfile.setAudioEnabled(soundVolume.getValue() > 0);
        playerProfile.setFps((int) framerate.getValue());
        persistenceUtil.saveProfile(playerProfile);
        soundUtil.setVolume(playerProfile.getVolume());
        getMain().setupBackgroundMusic();
        getMain().setFXMLView(FXMLFile.MENU);
    }

    @Override
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
