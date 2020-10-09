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

    /**
     * sets main
     * */
    @Override
    public void setMain(SpaceRunnerGame main) {
        this.main = main;
    }

    @Override
    public void initialize() {
        game = new Game();
        GraphicsContext gc = gameCanvas.getGraphicsContext2D();

        gc.setFill(Color.BLUE);
        gc.fillRect(0,0,10000,10000);
    }

    @Override
    public void handle(KeyEvent keyEvent) {
        game.moveSpaceShip(keyEvent.getCode());
    }
}
