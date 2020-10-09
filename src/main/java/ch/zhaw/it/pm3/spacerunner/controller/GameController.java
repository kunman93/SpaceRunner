package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerGame;
import ch.zhaw.it.pm3.spacerunner.model.Game;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

public class GameController extends Controller implements EventHandler<KeyEvent> {


    @FXML public Canvas gameCanvas;
    private Game game;
    private GraphicsContext graphicsContext;

    /**
     * sets main
     * */
    @Override
    public void setMain(SpaceRunnerGame main) {
        this.main = main;
        gameCanvas.setHeight(main.getPrimaryStage().getHeight());
        gameCanvas.setWidth(main.getPrimaryStage().getWidth());
        main.getPrimaryStage().heightProperty().addListener((obs, oldVal, newVal) -> {

            graphicsContext.fillRect(0,0,10000,10000);
            gameCanvas.setHeight((Double) newVal);
        });
        main.getPrimaryStage().widthProperty().addListener((obs, oldVal, newVal) -> {
            gameCanvas.setWidth((Double) newVal);
        });
    }

    @Override
    public void initialize() {
        game = new Game();
        graphicsContext = gameCanvas.getGraphicsContext2D();

        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillRect(0,0,10000,10000);


    }

    @Override
    public void handle(KeyEvent keyEvent) {
        game.moveSpaceShip(keyEvent.getCode());
    }

    public double canvasHeight() {
        return gameCanvas.getHeight();
    }
}
