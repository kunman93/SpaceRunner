package ch.zhaw.it.pm3.spacerunner;

import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.SoundClip;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.SoundUtil;
import ch.zhaw.it.pm3.spacerunner.view.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class SpaceRunnerApp extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Space Runner");
        ViewController.setMain(this);
        setFXMLView("view/menu.fxml");

        setupBackgroundMusic();

    }


    public void setFXMLView(String source){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(source));

            URL a = getClass().getResource("font/video_games.ttf");
            Font.loadFont(a.toString().replace("%20", " "), 10);



            Pane rootPane = loader.load();
            ViewController windowViewController = loader.getController();
            Scene scene = new Scene(rootPane);
            primaryStage.setScene(scene);

            //if(primaryStage.getIcons().size() == 0) {
                //primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/ch.zhaw.pm2.gui/icons/favicon.png")));
                primaryStage.show();
                primaryStage.setHeight(500);
                primaryStage.setWidth(850);
                primaryStage.setMinHeight(420);
                primaryStage.setMinWidth(650);
            //}
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

    private void setupBackgroundMusic() {
        try {
            URL backgroundMusicURL = getClass().getResource("sound/background.wav");
            SoundClip backgroundMusic = SoundUtil.loadClip(new File(backgroundMusicURL.getPath().replace("%20", " ")));
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
