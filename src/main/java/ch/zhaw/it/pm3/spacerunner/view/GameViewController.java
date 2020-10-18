package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.controller.GameController;
import ch.zhaw.it.pm3.spacerunner.controller.GameView;
import ch.zhaw.it.pm3.spacerunner.model.element.SpaceElement;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Set;

public class GameViewController extends ViewController implements GameView {


    //TODO: Canvas has to be a fixed height and width so we dont have to deal with scaling
    @FXML public Canvas gameCanvas;
    private GraphicsContext graphicsContext;
    private GameController gameController = new GameController();

    /**
     * sets main
     * */
    @Override
    public void setMain(SpaceRunnerApp main) {
        gameController.setView(this);


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
        graphicsContext = gameCanvas.getGraphicsContext2D();

        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillRect(0,0,10000,10000);


    }







    /*@Override
    public void handle(KeyEvent keyEvent) {
        game.moveSpaceShip(keyEvent.getCode());
    }*/

    public double canvasHeight() {
        return gameCanvas.getHeight();
    }

    @Override
    public void drawSpaceElements(Set<SpaceElement> spaceElements) {

    }
}
