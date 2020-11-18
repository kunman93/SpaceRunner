package ch.zhaw.it.pm3.spacerunner;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.GameSound;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.GameSoundUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.SoundClip;
import ch.zhaw.it.pm3.spacerunner.view.FXMLFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualFile;

import ch.zhaw.it.pm3.spacerunner.view.ViewController;
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

public class SpaceRunnerApp extends Application {

    private Stage primaryStage;
    private GameSoundUtil gameSoundUtil = GameSoundUtil.getUtil();
    private PersistenceUtil persistenceUtil = PersistenceUtil.getUtil();
    private SoundClip backgroundMusic;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Space Runner");
        ViewController.setMain(this);
        setFXMLView(FXMLFile.MENU);


        gameSoundUtil.setVolume(persistenceUtil.getSoundVolume());
        setupBackgroundMusic();

    }

    public void setFXMLView(FXMLFile source){
        double height = 515; // (800 * 9 / 16) + (800 * 0.5 / 16) - 40
        double width = 800;

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(source.getFileName()));

            URL a = getClass().getResource("font/video_games.ttf");
            Font.loadFont(a.toString().replace("%20", " "), 10);

            Pane rootPane = loader.load();
            Scene scene = new Scene(rootPane);
            primaryStage.setScene(scene);

            if(primaryStage.getIcons().size() == 0) { // damit breite gleich bleibt beim laden neuer view
                primaryStage.getIcons().add(new Image(getClass().getResourceAsStream(VisualFile.ROCKET_ICON.getFileName())));
                primaryStage.show();
                primaryStage.setHeight(height);
                primaryStage.setWidth(width);
                primaryStage.setMinHeight(height);
                primaryStage.setMinWidth(width);
            }
        } catch (IOException e) {
            //logger.log(Level.SEVERE, "!!!FILE NOT FOUND, CHECK FILEPATH!!!");
            e.printStackTrace();
        } catch (Exception e) {
            //logger.log(Level.SEVERE, "!!!INCOMPATIBLE DATE FORMAT!!!");
            e.printStackTrace();
        }
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void setupBackgroundMusic() {
        if(backgroundMusic != null){
            backgroundMusic.stop();
            backgroundMusic = null;
        }
        if(!persistenceUtil.isAudioEnabled()){
            return;
        }

        try {
            backgroundMusic = gameSoundUtil.loadClip(GameSound.BACKGROUND_2);
            backgroundMusic.setLoop(true);
            backgroundMusic.play();
        } catch (IOException e) {
            //TODO
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            //TODO
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            //TODO
            e.printStackTrace();
        }
    }


}
