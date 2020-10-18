package ch.zhaw.it.pm3.spacerunner;

import ch.zhaw.it.pm3.spacerunner.view.ViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class SpaceRunnerApp extends Application {

    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Space Runner");
        setFXMLView("view/menu.fxml");
    }

    public void setFXMLView(String source){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
            Pane rootPane = loader.load();
            ViewController windowViewController = loader.getController();
            windowViewController.setMain(this);
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
        } catch (Exception e) {
            //logger.log(Level.SEVERE, "!!!INCOMPATIBLE DATE FORMAT!!!");
        }
    }


    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
