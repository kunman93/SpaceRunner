package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerGame;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class GameController extends Controller implements EventHandler<KeyEvent> {


    @FXML
    public Canvas gameCanvas;

    /**
     * sets main
     * */
    @Override
    public void setMain(SpaceRunnerGame main) {
        this.main = main;
    }

    @FXML
    public void showSettings() {
        main.setView("settings.fxml");
    }

    @Override
    public void initialize() {

        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        gc.setFill(Color.BLUE);
        gc.fillRect(0,0,10000,10000);
    }

    @Override
    public void handle(KeyEvent keyEvent) {

    }
}
