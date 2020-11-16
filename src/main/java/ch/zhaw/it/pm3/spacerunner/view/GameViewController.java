package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.controller.GameController;
import ch.zhaw.it.pm3.spacerunner.model.GameDataCache;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.*;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
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

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class GameViewController extends ViewController {

    private VisualUtil visualUtil = VisualUtil.getInstance();
    //TODO: Canvas has to be a fixed height and width so we dont have to deal with scaling
    @FXML private Canvas gameCanvas;
    private GraphicsContext graphicsContext;
    private GameProportions gameBar = new GameProportions();

    private GameController gameController = new GameController();
    private boolean downPressed;
    private boolean upPressed;
    private Stage primaryStage;
    private EventHandler<KeyEvent> pressedHandler;
    private EventHandler<KeyEvent> releasedHandler;
    private EventHandler startGameKeyHandler = new EventHandler<KeyEvent>(){
        @Override
        public void handle(KeyEvent event) {
            if(event.getCode() == KeyCode.SPACE){
                gameController.togglePause();
                if(primaryStage != null){
                    primaryStage.removeEventHandler(KeyEvent.KEY_RELEASED, this);
                }
            }
        }
    };

    private AnimationTimer gameLoop;
    private AnimationTimer loadingAnimation;

    private Timer resizeTimer = new Timer("ResizeTimer");
    private TimerTask resizeTask = null;

    private long lastUpdate;
    private int framesCount = 0;
    private long framesTimestamp = 0;
    private long lastProcessingTime = 0;

    private boolean isLoaded = false;
    private final VisualManager visualManager = VisualManager.getInstance();



    @Override
    public void initialize() {
        initializeUiElements();


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

            updateGameFrame();
            gameController.togglePause();

            //TODO: not displayed yet




            primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, startGameKeyHandler);

            gameLoop = new AnimationTimer()
            {
                public void handle(long currentNanoTime)
                {
                    if (currentNanoTime - lastUpdate >= (timeForFrameNano - lastProcessingTime)) {
                        lastUpdate = currentNanoTime;
                        try{
                            framesCount++;
                            updateGameFrame();

                            lastProcessingTime = (System.nanoTime() - currentNanoTime);
                            //System.out.println("Processing took " + lastProcessingTime / 1000000);

                            if (gameController.isPaused()) {
                                if (gameController.getScore() == 0) {
                                    displayInformation("press SPACE to start");
                                } else {
                                    displayInformation("press SPACE to continue");
                                }
                            }

                            //Platform.runLater(() -> );
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


    private void updateGameFrame(){
        gameController.processFrame(upPressed, downPressed);
        clearCanvas();
        displayUpdatedSpaceElements(gameController.getGameElements());
        displayCoinsAndScore(gameController.getCollectedCoins(), gameController.getScore());

        boolean gameOver = gameController.isGameOver();

        if(gameOver){
            removeKeyHandlers();
            if(gameLoop != null){
                gameLoop.stop();
                setGameDataCache(new GameDataCache(gameController.getCollectedCoins(), gameController.getScore()));
                getMain().setFXMLView(FXMLFile.GAME_ENDED);
            }
        }
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
        //todo auslagern
        double proportionGame = 9;
        double proportionGameBar = 0.5;
        double proportionY = proportionGame + proportionGameBar;
        double proportionX = 16;

        double appBarHeight = 40;

        double height = primaryStage.getHeight() - appBarHeight;
        double width = primaryStage.getWidth();
        if (width / proportionX < height / proportionY) {
            height = width * proportionY / proportionX;
        } else if (width / proportionX > height / proportionY) {
            width = height * proportionX / proportionY;
        }

        gameBar.setInfoBarHeight(height * (proportionGameBar / proportionY));
        gameBar.setWidth(width);

        if(resizeTask != null){
            resizeTask.cancel();
        }

        //needed for scheduler
        double finalWidth = width;
        double finalHeight = height;

        resizeTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    gameCanvas.setWidth(finalWidth);
                    gameCanvas.setHeight(finalHeight + gameBar.getInfoBarHeight());

                    gameController.setViewport((int) finalWidth, (int) (finalHeight * proportionGame / proportionY));
                });
            }


        };

        resizeTimer.schedule(resizeTask, 300);

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
        graphicsContext.setFont(new Font(DEFAULT_FONT, gameBar.getFontSize()));
        graphicsContext.setTextAlign(TextAlignment.CENTER);

        loadingAnimation = new AnimationTimer() {
            BufferedImage img = visualUtil.loadSVGImage(SpaceRunnerApp.class.getResource(VisualSVGFile.LOADING_SPINNER.getFileName()), 80f);
            long lastLoadingAnimation = 0;
            long framerate = 100_000_000L;

            @Override
            public void handle(long l) {
                if (isLoaded && loadingAnimation != null) {
                    loadingAnimation.stop();
                    loadingAnimation = null;
                } else if (l - lastLoadingAnimation >= framerate) {
                    lastLoadingAnimation = l;
                    clearCanvas();
                    graphicsContext.fillText("Game is loading...", gameCanvas.getWidth() / 2,
                            (gameCanvas.getHeight() + 80) / 2, gameCanvas.getWidth());
                    img = visualUtil.rotateImage(img, -1);
                    graphicsContext.drawImage(SwingFXUtils.toFXImage(img, null), (gameCanvas.getWidth() - 80) / 2, (gameCanvas.getHeight() - 160) / 2, 80, 80);

                }

            }
        };
        loadingAnimation.start();
    }

    private void clearCanvas(){
        graphicsContext.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
    }


    public void displayUpdatedSpaceElements(List<SpaceElement> spaceElements) {
        for (SpaceElement spaceElement : spaceElements) {
            Point2D.Double position = spaceElement.getRelativePosition();
            Image image = null;
            try {
                image = SwingFXUtils.toFXImage(visualManager.getImage(spaceElement.getClass()), null);
            } catch (VisualNotSetException e) {
                e.printStackTrace();
                //TODO: handle
            }
            graphicsContext.drawImage(image, position.x * visualManager.getWidth(), position.y * visualManager.getHeight());
            //TODO: possible memory leak
        }
    }

    private void displayCoinsAndScore(int coins, int score) {
        double xPositionReference = gameBar.getWidth();
        double yPosition = gameBar.getYPosition();

        BufferedImage image = null;
        try {
            image = visualManager.getImage(UIElement.COIN_COUNT.getClass());
            xPositionReference -= image.getWidth();
            graphicsContext.drawImage(SwingFXUtils.toFXImage(image, null),
                    (gameBar.getWidth() - image.getWidth() - gameBar.getInfoBarMarginRightImage()),
                    yPosition, image.getWidth(), image.getHeight());
        } catch (VisualNotSetException e) {
            // todo handle
            e.printStackTrace();
        }
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(DEFAULT_FONT, gameBar.getFontSize()));
        graphicsContext.setTextAlign(TextAlignment.RIGHT);
        graphicsContext.setTextBaseline(VPos.TOP);
        xPositionReference -= gameBar.getInfoBarMarginRightText();
        double textWidth = gameBar.getTextWidth(coins);
        graphicsContext.fillText(String.valueOf(coins), xPositionReference, yPosition, textWidth);
        xPositionReference -= (gameBar.getInfoBarMarginRightText() + textWidth);
        graphicsContext.fillText(String.valueOf(score), xPositionReference, yPosition, textWidth);
    }

    private void displayInformation(String info) {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(DEFAULT_FONT, gameBar.getFontSize()));
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillText(info, gameBar.getWidth() / 2, gameBar.getYPosition());
    }

    private void removeKeyHandlers() {
        primaryStage.removeEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);
        primaryStage.removeEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);
    }

    private void initializeUiElements(){
        AnimatedVisual coinAnimation = new AnimatedVisual(VisualSVGAnimationFiles.COIN_ANIMATION, VisualScaling.COIN_COUNT);
        visualManager.loadAndSetAnimatedVisual(UIElement.COIN_COUNT.getClass(), coinAnimation);
    }

}
