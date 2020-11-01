package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.controller.GameController;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.VisualNotSetException;
import javafx.animation.AnimationTimer;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.awt.*;
import java.util.List;


public class GameViewController extends ViewController {


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

    private AnimationTimer gameLoop;

    private long lastUpdate;
    private int framesCount = 0;
    private long framesTimestamp = 0;
    private long lastProcessingTime = 0;


    @Override
    public void initialize() {

        SpaceRunnerApp main = getMain();
        primaryStage = main.getPrimaryStage();

        initializeResponsive(main);


        graphicsContext = gameCanvas.getGraphicsContext2D();




        pressedHandler = createPressReleaseKeyHandler(true);
        releasedHandler = createPressReleaseKeyHandler(false);

        System.out.println("Adding handlers");
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);

        showLoadingScreen();

        //TODO: Thread is required for loading screen but its ugly
        new Thread(()->{
            gameController.initialize((int)gameCanvas.getWidth(), (int)gameCanvas.getHeight());
            int fps = gameController.getFps();
            long timeForFrameNano = 1_000_000_000 / fps;

            framesTimestamp = 0;


            gameLoop = new AnimationTimer()
            {
                public void handle(long currentNanoTime)
                {
                    if (currentNanoTime - lastUpdate >= (timeForFrameNano - lastProcessingTime)) {
                        lastUpdate = currentNanoTime;
                        try{
                            framesCount++;
                            gameController.processFrame(upPressed, downPressed);
                            displayUpdatedSpaceElements(gameController.getGameElements());
                            displayCollectedCoins(gameController.getCollectedCoins());
                            displayCurrentScore(gameController.getScore());

                            boolean gameOver = gameController.isGameOver();

                            if(gameOver){
                                removeKeyHandlers();
                                if(gameLoop != null){
                                    gameLoop.stop();
                                    //TODO: GameOver => Show GameOver Screen
                                }
                            }

                            lastProcessingTime = (System.nanoTime() - currentNanoTime);
                            // System.out.println("Processing took " + lastProcessingTime / 1000000);

                            if(currentNanoTime - framesTimestamp >= 1000_000_000){
                                System.out.println("Current FPS " + framesCount);
                                framesTimestamp = currentNanoTime;
                                framesCount = 0;
                            }
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            };

            gameLoop.start();
        }).start();



    }


    //TODO immer 16:9 verhält
    private void initializeResponsive(SpaceRunnerApp main) {
        gameCanvas.setHeight(main.getPrimaryStage().getHeight());
        gameCanvas.setWidth(main.getPrimaryStage().getWidth());
        main.getPrimaryStage().heightProperty().addListener((obs, oldVal, newVal) -> {
            gameCanvas.setHeight((Double) newVal);
            //todo notify about new scale
        });
        main.getPrimaryStage().widthProperty().addListener((obs, oldVal, newVal) -> {
            gameCanvas.setWidth((Double) newVal);
        });
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
        graphicsContext.setFont(new Font("Bauhaus 93", 40));
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


    public void displayUpdatedSpaceElements(List<SpaceElement> spaceElements) {
        //TODO: Should we clear it?

        //Platform.runLater(()->{
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


        //});

    }


    public void displayCollectedCoins(int coins) {

    }

    public void displayCurrentScore(int score) {

    }

    public void removeKeyHandlers() {
        primaryStage.removeEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);
        primaryStage.removeEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);
    }


}
