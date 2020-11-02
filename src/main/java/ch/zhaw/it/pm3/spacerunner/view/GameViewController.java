package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.controller.GameController;
import ch.zhaw.it.pm3.spacerunner.controller.GameView;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.VisualNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualUtil;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;


public class GameViewController extends ViewController implements GameView {


    //TODO: Canvas has to be a fixed height and width so we dont have to deal with scaling
    @FXML
    public Canvas gameCanvas;
    private GraphicsContext graphicsContext;
    private GameController gameController = new GameController();
    private boolean downPressed;
    private boolean upPressed;
    private Stage primaryStage;
    private EventHandler<KeyEvent> pressedHandler;
    private EventHandler<KeyEvent> releasedHandler;


    @Override
    public void initialize() {
        gameController.setView(this);

        SpaceRunnerApp main = getMain();
        primaryStage = main.getPrimaryStage();

        setCanvasScale();


        graphicsContext = gameCanvas.getGraphicsContext2D();




        pressedHandler = createPressReleaseKeyHandler(true);
        releasedHandler = createPressReleaseKeyHandler(false);

        System.out.println("Adding handlers");
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);

        showLoadingScreen();

        Thread gameThread = new Thread(() ->{
            try {
                gameController.startGame();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        gameThread.start();



    }

    private void setCanvasScale() {
        calc16to9Proportions();
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            calc16to9Proportions();
        });
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            calc16to9Proportions();
        });
    }

    private void calc16to9Proportions() {
        double height = primaryStage.getHeight() - 20;
        double width = primaryStage.getWidth();
        if (width / 16 < height / 9) {
            height = width * 9 / 16;
        } else if (width / 16 > height / 9) {
            width = height * 16 / 9;
        }
        gameCanvas.setWidth(width);
        gameCanvas.setHeight(height);
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

    private void showLoadingScreen() {
        graphicsContext.drawImage(new Image(String.valueOf(SpaceRunnerApp.class.getResource("images/icon.png"))),
                (gameCanvas.getWidth() - 80) / 2, (gameCanvas.getHeight() - 160) / 2, 80, 80);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(DEFAULT_FONT, 40));
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillText("Game is loading...",  gameCanvas.getWidth() / 2,
                (gameCanvas.getHeight() + 80) / 2, gameCanvas.getWidth());
    }







    /*@Override
    public void handle(KeyEvent keyEvent) {
        game.moveSpaceShip(keyEvent.getCode());
    }*/

    /*public double canvasHeight() {
        return gameCanvas.getHeight();
    }*/


    @Override
    public void displayUpdatedSpaceElements(List<SpaceElement> spaceElements) {
        //TODO: Should we clear it?

        Platform.runLater(()->{
            graphicsContext.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());



            for(SpaceElement spaceElement : spaceElements){
                Point position = spaceElement.getCurrentPosition();
                Image image = null;
                try {
                    image = SwingFXUtils.toFXImage(spaceElement.getVisual(), null);
                } catch (VisualNotSetException e) {
                    e.printStackTrace();
                    //TODO: handle
                }
                graphicsContext.drawImage(image, position.x, position.y);
                //TODO: possible memory leak
            }


        });

    }

    // todo private, wenn mit AnimationTimer
    @Override
    public void displayCollectedCoins(int coins) {
        //Image image = SwingFXUtils.toFXImage(VisualUtil.loadSVGImage(SpaceRunnerApp.class.getResource("images/shiny-coin1.svg"), 40f), null);
        //graphicsContext.drawImage(image, (gameCanvas.getWidth() - 40), 40, 30, 30);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(DEFAULT_FONT, 30));
        graphicsContext.setTextAlign(TextAlignment.RIGHT);
        graphicsContext.fillText(String.valueOf(coins),  (gameCanvas.getWidth() - 80),40, 100);
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

    @Override
    public void gameEnded() {
        primaryStage.removeEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);
        primaryStage.removeEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);
    }

    //TODO: implemented these two methods, ask Isler
    @Override
    public double getCanvasHeight() {
        return gameCanvas.getHeight();
    }

    @Override
    public double getCanvasWidth() {
        return gameCanvas.getWidth();
    }

}
