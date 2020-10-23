package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.controller.GameController;
import ch.zhaw.it.pm3.spacerunner.controller.GameView;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.util.Set;

public class GameViewController extends ViewController implements GameView {


    //TODO: Canvas has to be a fixed height and width so we dont have to deal with scaling
    @FXML
    public Canvas gameCanvas;
    private GraphicsContext graphicsContext;
    private GameController gameController = new GameController();
    private boolean downPressed;
    private boolean upPressed;


    @Override
    public void initialize() {
        graphicsContext = gameCanvas.getGraphicsContext2D();

        graphicsContext.setFill(Color.BLUE);
        graphicsContext.fillRect(0, 0, 10000, 10000);

        gameController.setView(this);

        Scene scene = this.getMain().getPrimaryStage().getScene();

        EventHandler<KeyEvent> pressedHandler = createPressReleaseKeyHandler(true);
        EventHandler<KeyEvent> releasedHandler = createPressReleaseKeyHandler(false);

        scene.addEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);

        scene.addEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);


        try {
            gameController.startGame();
        } catch (Exception e) {
            //TODO: Handle
            e.printStackTrace();
        }


        //TODO: Uncomment when Game running
        //scene.removeEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);
        //scene.removeEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);

//        gameCanvas.setHeight(main.getPrimaryStage().getHeight());
//        gameCanvas.setWidth(main.getPrimaryStage().getWidth());
//        main.getPrimaryStage().heightProperty().addListener((obs, oldVal, newVal) -> {
//
//            graphicsContext.fillRect(0,0,10000,10000);
//            gameCanvas.setHeight((Double) newVal);
//        });
//        main.getPrimaryStage().widthProperty().addListener((obs, oldVal, newVal) -> {
//            gameCanvas.setWidth((Double) newVal);
//        });


    }

    private EventHandler<KeyEvent> createPressReleaseKeyHandler(boolean isPressedHandler) {
        return event -> {
            if (event.getCode() == KeyCode.UP) {
                upPressed = isPressedHandler;
            }
            if (event.getCode() == KeyCode.DOWN) {
                downPressed = isPressedHandler;
            }
        };
    }







    /*@Override
    public void handle(KeyEvent keyEvent) {
        game.moveSpaceShip(keyEvent.getCode());
    }*/

    public double canvasHeight() {
        return gameCanvas.getHeight();
    }


    @Override
    public void setSpaceElements(Set<SpaceElement> spaceElements) {

    }

    @Override
    public void displayUpdatedSpaceElements() {

    }

    @Override
    public void displayCollectedCoins(int coins) {

    }

    @Override
    public void displayCurrentScore(int score) {

    }

    @Override
    public boolean isUpPressed() {
        return upPressed;
    }

    @Override
    public boolean isDownPressed() {
        return downPressed;
    }
}
