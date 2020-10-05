package ch.zhaw.it.pm3.spacerunner;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SpaceRunnerGame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        mainWindow(primaryStage);
    }


    public void mainWindow(Stage primaryStage){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("SpaceRunnerMain.fxml"));
            Scene scene = new Scene(root);
            //scene.getStylesheets().add("ch/zhaw/it/pm2/Soundboard/stylesheet.css");

            primaryStage.setTitle("Space-Runner");
            primaryStage.setScene(scene);
            primaryStage.sizeToScene();


            primaryStage.show();

            primaryStage.setMinHeight(765);
            primaryStage.setMinWidth(690);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
