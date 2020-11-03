package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.controller.GameController;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.SpaceElementVisualManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.VisualNotSetException;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualFile;
import javafx.application.Platform;

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
import javafx.stage.WindowEvent;

import javafx.scene.paint.Color;

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

    private final SpaceElementVisualManager spaceElementVisualManager = SpaceElementVisualManager.getInstance();


    @Override
    public void initialize() {

        SpaceRunnerApp main = getMain();
        primaryStage = main.getPrimaryStage();

        calc16to9Proportions();
        addWindowSizeListeners();


        graphicsContext = gameCanvas.getGraphicsContext2D();

        pressedHandler = createPressReleaseKeyHandler(true);
        releasedHandler = createPressReleaseKeyHandler(false);

        System.out.println("Adding handlers");
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);
        primaryStage.setOnCloseRequest(handleCloseWindowEvent());

        showLoadingScreen();

        //TODO: Thread is required for loading screen but its ugly
        new Thread(()->{
            gameController.initialize();

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

    private void addWindowSizeListeners() {
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            calc16to9Proportions();
        });
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            calc16to9Proportions();
        });
    }

    //TODO: remove window size Listeners




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
        gameController.setViewport((int)width, (int)height);
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

    private EventHandler<WindowEvent> handleCloseWindowEvent(){
        return event -> {
            Platform.exit();
            System.exit(0);
        };
    }

    private void showLoadingScreen() {
        graphicsContext.drawImage(new Image(String.valueOf(SpaceRunnerApp.class.getResource(VisualFile.ROCKET_ICON.getFileName()))),
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


    public void displayUpdatedSpaceElements(List<SpaceElement> spaceElements) {
        //TODO: Should we clear it?

        //Platform.runLater(()->{
            graphicsContext.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());



            for(SpaceElement spaceElement : spaceElements){
                Point position = spaceElement.getCurrentPosition();
                Image image = null;
                try {
                    image = SwingFXUtils.toFXImage(spaceElementVisualManager.getVisual(spaceElement.getClass()), null);
                } catch (VisualNotSetException e) {
                    e.printStackTrace();
                    //TODO: handle
                }
                graphicsContext.drawImage(image, position.x, position.y);
                //TODO: possible memory leak
            }


        //});

    }

    // todo private, wenn mit AnimationTimer
    public void displayCollectedCoins(int coins) {
        //Image image = SwingFXUtils.toFXImage(VisualUtil.loadSVGImage(SpaceRunnerApp.class.getResource("images/shiny-coin1.svg"), 40f), null);
        //graphicsContext.drawImage(image, (gameCanvas.getWidth() - 40), 40, 30, 30);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(DEFAULT_FONT, 30));
        graphicsContext.setTextAlign(TextAlignment.RIGHT);
        graphicsContext.fillText(String.valueOf(coins),  (gameCanvas.getWidth() - 80),40, 100);
    }

    public void displayCurrentScore(int score) {

    }

    public void removeKeyHandlers() {
        primaryStage.removeEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);
        primaryStage.removeEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);
    }

}
