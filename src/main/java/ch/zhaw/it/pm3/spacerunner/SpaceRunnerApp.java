package ch.zhaw.it.pm3.spacerunner;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.JsonPersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.GameSound;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.GameSoundUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.SoundClip;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualFile;
import ch.zhaw.it.pm3.spacerunner.ui.FXMLFile;
import ch.zhaw.it.pm3.spacerunner.ui.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Responsible to start application, display of user-interfaces and to initialize background sound.
 *
 * @author freymar1
 */
public class SpaceRunnerApp extends Application {

    private final Logger logger = Logger.getLogger(SpaceRunnerApp.class.getName());

    private Stage primaryStage;
    private final GameSoundUtil gameSoundUtil = GameSoundUtil.getUtil();
    private final Persistence persistenceUtil = JsonPersistenceUtil.getUtil();
    private SoundClip backgroundMusic;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Initializes primaryStage's UI and music.
     *
     * @param primaryStage which will be assigned to the ui
     */
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Space Runner");
        ViewController.setMain(this);
        setFXMLView(FXMLFile.MENU);

        gameSoundUtil.setVolume(persistenceUtil.getSoundVolume());
        setupBackgroundMusic();
    }

    /**
     * Loads the new view to the primaryStage. Sizes and app-icon are set/loaded only once to avoid redundant
     * operations and to maintain same sizes while loading another view.
     *
     * @param source of FXMLFile which should be loaded
     */
    public void setFXMLView(FXMLFile source) {
        double height = 490;
        double width = 800;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(source.getFileName()));
            Pane rootPane = loader.load();
            Scene scene = new Scene(rootPane);
            primaryStage.setScene(scene);

            URL a = getClass().getResource("font/video_games.ttf");
            Font.loadFont(a.toString().replace("%20", " "), 10);

            if (primaryStage.getIcons().size() == 0) {
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(VisualFile.ROCKET_ICON.getFileName())));
                primaryStage.show();
                primaryStage.setHeight(height);
                primaryStage.setWidth(width);
                primaryStage.setMinHeight(height);
                primaryStage.setMinWidth(width);
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "File not Found");
            e.printStackTrace();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Incompatible Date Format");
            e.printStackTrace();
        }
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Initializes background music and sets it to an endless loop.
     */
    public void setupBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic = null;
        }
        if (!persistenceUtil.isAudioEnabled()) {
            return;
        }

        try {
            backgroundMusic = gameSoundUtil.loadClip(GameSound.BACKGROUND);
            backgroundMusic.setLoop(true);
            backgroundMusic.play();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error Loading Sound BACKGROUND");
        } catch (UnsupportedAudioFileException e) {
            logger.log(Level.SEVERE, "Sound BACKGROUND_2 has an Unsupported Type");
        } catch (LineUnavailableException e) {
            logger.log(Level.SEVERE, "Line wasn't available");
        }
    }
}
