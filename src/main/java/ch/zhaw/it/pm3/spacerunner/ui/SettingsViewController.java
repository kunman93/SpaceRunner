package ch.zhaw.it.pm3.spacerunner.ui;

import ch.zhaw.it.pm3.spacerunner.domain.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.JsonPersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.GameSoundUtil;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

/**
 * Processes user input and persists the adjustments of the profile.
 * <p>
 * ViewController of Settings.fxml
 *
 * @author freymar1
 */
public class SettingsViewController extends ViewController {
    private final Persistence persistenceUtil = JsonPersistenceUtil.getUtil();
    private final GameSoundUtil gameSoundUtil = GameSoundUtil.getUtil();
    public Button homeButton;
    public TextField playerName;
    public Slider soundVolume;
    public Slider framerate;
    private PlayerProfile playerProfile;

    /**
     * Displays, after loading the FXML-file, the stored values and adds listener to verify the input.
     */
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

        soundVolume.valueProperty().addListener((observable, oldValue, newValue) -> {
            gameSoundUtil.setVolume(newValue.intValue());
            persistenceUtil.setSoundVolume(newValue.intValue());
            getMain().setupBackgroundMusic();
        });
    }

    /**
     * When the Menu-Button is pressed, the setting of the player profile are updated and the view changes to Menu.fxml.
     */
    @FXML
    public void showMenu() {
        playerProfile.setPlayerName(playerName.getText());
        playerProfile.setVolume((int) soundVolume.getValue());
        playerProfile.setFps((int) framerate.getValue());
        playerProfile.setVolume((int) soundVolume.getValue());
        persistenceUtil.saveProfile(playerProfile);
        removePropertyListener();
        getMain().setFXMLView(FXMLFile.MENU);
    }

    private void removePropertyListener() {
        new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String string, String t1) {
                playerName.textProperty().removeListener(this);
            }
        };
        new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                soundVolume.valueProperty().removeListener(this);
            }
        };
    }
}
