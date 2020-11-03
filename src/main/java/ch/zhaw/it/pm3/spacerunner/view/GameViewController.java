package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.controller.GameController;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.Coin;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.SpaceElementVisualManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.VisualNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualSVGFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualUtil;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.WindowEvent;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    private AnimationTimer loadingAnimation;

    private long lastUpdate;
    private int framesCount = 0;
    private long framesTimestamp = 0;
    private long lastProcessingTime = 0;

    private boolean isLoaded = false;
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

            isLoaded = true;


            gameLoop = new AnimationTimer()
            {
                public void handle(long currentNanoTime)
                {
                    if (currentNanoTime - lastUpdate >= (timeForFrameNano - lastProcessingTime)) {
                        lastUpdate = currentNanoTime;
                        try{
                            framesCount++;
                            gameController.processFrame(upPressed, downPressed);
                            clearCanvas();
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

    private void removeWindowSizeListeners() {
        new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                primaryStage.widthProperty().removeListener(this);
                primaryStage.heightProperty().removeListener(this);
            }
        };
    }


    private void calc16to9Proportions() {
        double height = primaryStage.getHeight() - 20; // subtract 20 because app-bar overflows game screen
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


    /*
    * https://stackoverflow.com/questions/45326525/how-to-show-a-loading-animation-in-javafx-application
    * */
    private void showLoadingScreen() {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(DEFAULT_FONT, 40));
        graphicsContext.setTextAlign(TextAlignment.CENTER);

        loadingAnimation = new AnimationTimer() {
            BufferedImage img = VisualUtil.loadSVGImage(SpaceRunnerApp.class.getResource(VisualSVGFile.LOADING_SPINNER.getFileName()), 80f);
            long lastLoadingAnimation = 0;
            int i = 0;
            long framerate = 50_000_000L;

            @Override
            public void handle(long l) {
                if (isLoaded && loadingAnimation != null) {
                    loadingAnimation.stop();
                    loadingAnimation = null;
                } else if (l - lastLoadingAnimation >= framerate) {
                    lastLoadingAnimation = l;

                    clearCanvas();
                    i = i % 3 + 1;
                    graphicsContext.fillText("Game is loading" + ".".repeat(Math.max(0, i)), gameCanvas.getWidth() / 2,
                            (gameCanvas.getHeight() + 80) / 2, gameCanvas.getWidth());
                    img = VisualUtil.rotateImage(img, -1);
                    graphicsContext.drawImage(SwingFXUtils.toFXImage(img, null), (gameCanvas.getWidth() - 80) / 2, (gameCanvas.getHeight() - 160) / 2, 80, 80);

                }

            }
        };
        loadingAnimation.start();


    }







    /*@Override
    public void handle(KeyEvent keyEvent) {
        game.moveSpaceShip(keyEvent.getCode());
    }*/

    /*public double canvasHeight() {
        return gameCanvas.getHeight();
    }*/

    private void clearCanvas(){
        graphicsContext.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
    }


    public void displayUpdatedSpaceElements(List<SpaceElement> spaceElements) {
        //TODO: Should we clear it?

        //Platform.runLater(()->{



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
        BufferedImage image = null;
        try {
            image = spaceElementVisualManager.getVisual(Coin.class);
        } catch (VisualNotSetException e) {
            // todo handle
            e.printStackTrace();
        }
        //Image image = SwingFXUtils.toFXImage(VisualUtil.loadSVGImage(SpaceRunnerApp.class.getResource("images/shiny-coin1.svg"), 40f), null);
        graphicsContext.drawImage(SwingFXUtils.toFXImage(image, null), (gameCanvas.getWidth() - 40), 5, 50, 50);
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(DEFAULT_FONT, 50));
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
