package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.controller.GameController;
import ch.zhaw.it.pm3.spacerunner.model.GameDataCache;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.UFO;
import ch.zhaw.it.pm3.spacerunner.technicalservices.performance.FPSTracker;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.AnimatedVisual;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.UIVisualElement;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualScaling;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGAnimationFiles;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualUtil;
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
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameViewController extends ViewController {

    private Logger logger = Logger.getLogger(GameViewController.class.getName());

    private VisualUtil visualUtil = VisualUtil.getInstance();
    //TODO: Canvas has to be a fixed height and width so we dont have to deal with scaling
    @FXML private Canvas gameCanvas;
    private GraphicsContext graphicsContext;
    private GameViewPort gameViewPort = null;
    private GameProportionUtil gameProportionUtil = GameProportionUtil.getUtil();

    private GameController gameController = new GameController();
    private boolean downPressed;
    private boolean upPressed;
    private Stage primaryStage;
    private EventHandler<KeyEvent> pressedHandler;
    private EventHandler<KeyEvent> releasedHandler;
    private EventHandler<KeyEvent> startGameKeyHandler = new EventHandler<KeyEvent>(){
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
    private EventHandler<KeyEvent> pauseGameKeyHandler = new EventHandler<KeyEvent>(){
        @Override
        public void handle(KeyEvent event) {
            if(event.getCode() == KeyCode.P){
                gameController.togglePause();
            }
        }
    };

    private boolean wasPausedBeforeResize = false;
    private boolean isResizing = false;

    private FXMLImageProxy fxmlImageProxy = FXMLImageProxy.getProxy();

    private AnimationTimer gameLoop;
    private AnimationTimer loadingAnimation;

    private Timer resizeTimer = new Timer("ResizeTimer");
    private TimerTask resizeTask = null;


    private boolean isLoaded = false;

    private final double infoBarPaddingPercent = 0.1; // todo capital
    private final double infoBarMarginRightImage = 10;
    private final double infoBarMarginRightText = 30;
    private final VisualManager visualManager = VisualManager.getManager();

    private long lastUpdate = 0;
    //Used to overperform a little bit. if we dont have this we dont reach the required fps (has to do with some internal AnimationTimer stuff)
    private final long FRAME_TIME_DELTA = 2_000_000;

    @Override
    public void initialize() {
        initializeUiElements();


        SpaceRunnerApp main = getMain();
        primaryStage = main.getPrimaryStage();
        gameViewPort = gameProportionUtil.calcProportions(primaryStage.getWidth(), primaryStage.getHeight());


        resize();
        addWindowSizeListeners();


        graphicsContext = gameCanvas.getGraphicsContext2D();

        pressedHandler = createPressReleaseKeyHandler(true);
        releasedHandler = createPressReleaseKeyHandler(false);

        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);
        primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);
        primaryStage.addEventHandler(KeyEvent.KEY_PRESSED, pauseGameKeyHandler);
        primaryStage.setOnCloseRequest(handleCloseWindowEvent());

        showLoadingScreen();

        new Thread(()->{
            gameController.initialize();

            int fps_config = gameController.getFps();
            long timeForFrameNano = (1_000_000_000 / fps_config) - FRAME_TIME_DELTA;

            isLoaded = true;

            updateGameFrame();
            gameController.togglePause();

            primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, startGameKeyHandler);

            lastUpdate = System.nanoTime();
            gameLoop = new AnimationTimer()
            {
                private FPSTracker fpsTracker = new FPSTracker();

                public void handle(long currentNanoTime)
                {
                    if (currentNanoTime - lastUpdate >= timeForFrameNano) {
                        updateGameFrame();

                        if (gameController.isPaused()) {
                            if (gameController.getScore() == 0) {
                                displayInformation("Press SPACE to start");
                            } else {
                                displayInformation("Press P to continue");
                            }
                        }

                        fpsTracker.track(currentNanoTime);

                        lastUpdate = System.nanoTime();
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
            removeWindowSizeListeners();
            if(gameLoop != null){
                gameLoop.stop();
                setGameDataCache(new GameDataCache(gameController.getCollectedCoins(), gameController.getScore()));
                getMain().setFXMLView(FXMLFile.GAME_ENDED);
            }
        }
    }

    private void addWindowSizeListeners() {
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> {
            resize();
        });
        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> {
            resize();
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

    private void resize() {

        if(!wasPausedBeforeResize && !isResizing){
            isResizing = true;
            wasPausedBeforeResize = gameController.isPaused();
            if(!wasPausedBeforeResize){
                gameController.togglePause();
            }
        }

        if(resizeTask != null){
            resizeTask.cancel();
        }

        double appBarHeight = 40;

        gameViewPort = gameProportionUtil.calcProportions(primaryStage.getWidth(), primaryStage.getHeight() - appBarHeight);

        //needed for scheduler
        double finalWidth = gameViewPort.getGameWidth();
        double finalHeight = gameViewPort.getGameHeight();

        resizeTask = new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(()->{
                    gameCanvas.setWidth(finalWidth);
                    gameCanvas.setHeight(finalHeight + gameViewPort.getInfoBarHeight());
                    gameController.setViewport((int) finalWidth, (int) finalHeight);
                    if(!wasPausedBeforeResize){
                        gameController.togglePause();
                    }
                    isResizing = false;
                    wasPausedBeforeResize = false;
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
    */
    private void showLoadingScreen() {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(DEFAULT_FONT, gameProportionUtil.getFontSize(gameViewPort.getInfoBarHeight(),infoBarPaddingPercent)));
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


    private void displayUpdatedSpaceElements(List<SpaceElement> spaceElements) {
        for (SpaceElement spaceElement : spaceElements) {
            Point2D.Double position = spaceElement.getRelativePosition();
            Image image = null;
            try {
                image = fxmlImageProxy.getFXMLImage(spaceElement.getClass());
            } catch (VisualNotSetException e) {
                e.printStackTrace();
                logger.log(Level.SEVERE, "Visual for {0} wasn't set", spaceElement.getClass());
                //TODO: handle
            }
            graphicsContext.drawImage(image, position.x * visualManager.getWidth(), position.y * visualManager.getHeight());
        }
    }



    private void displayCoinsAndScore(int coins, int score) {
        double xPositionReference = gameViewPort.getGameWidth();
        double infoBarYPosition = gameViewPort.getGameHeight();


        graphicsContext.setFill(Color.DARKGRAY);
        graphicsContext.fillRect(0, infoBarYPosition, gameViewPort.getGameWidth(), gameViewPort.getInfoBarHeight());

        logger.log(Level.INFO, "Game Height is: {0}", gameViewPort.getGameHeight());
        logger.log(Level.INFO, "Infobar Height is: {0}", gameViewPort.getInfoBarHeight());
        logger.log(Level.INFO, "Total Height is: {0}", primaryStage.getHeight());

        BufferedImage image = null;
        try {
            image = visualManager.getImage(UIVisualElement.COIN_COUNT.getClass());
            xPositionReference -= image.getWidth();
            graphicsContext.drawImage(SwingFXUtils.toFXImage(image, null),
                    (gameViewPort.getGameWidth() - image.getWidth() - infoBarMarginRightImage),
                    infoBarYPosition, image.getWidth(), image.getHeight());
        } catch (VisualNotSetException e) {
            // todo handle
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", UIVisualElement.COIN_COUNT.getClass());
            e.printStackTrace();
        }
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(DEFAULT_FONT, gameProportionUtil.getFontSize(gameViewPort.getInfoBarHeight(),infoBarPaddingPercent)));
        graphicsContext.setTextAlign(TextAlignment.RIGHT);
        graphicsContext.setTextBaseline(VPos.TOP);
        xPositionReference -= infoBarMarginRightText;
        double textWidth = gameProportionUtil.getTextWidth(coins,gameViewPort.getInfoBarHeight(),infoBarPaddingPercent);
        graphicsContext.fillText(String.valueOf(coins), xPositionReference, infoBarYPosition, textWidth);
        xPositionReference -= (infoBarMarginRightText + textWidth);
        graphicsContext.fillText(String.valueOf(score), xPositionReference, infoBarYPosition, textWidth);
    }

    private void displayInformation(String info) {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(DEFAULT_FONT, gameProportionUtil.getFontSize(gameViewPort.getInfoBarHeight(),infoBarPaddingPercent)));
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillText(info, gameViewPort.getGameWidth() / 2, gameViewPort.getGameHeight());
    }

    private void removeKeyHandlers() {
        primaryStage.removeEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);
        primaryStage.removeEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);
        primaryStage.removeEventHandler(KeyEvent.KEY_PRESSED, pauseGameKeyHandler);
    }

    private void initializeUiElements(){
        AnimatedVisual coinAnimation = new AnimatedVisual(VisualSVGAnimationFiles.COIN_ANIMATION, VisualScaling.COIN_COUNT);
        visualManager.loadAndSetAnimatedVisual(UIVisualElement.COIN_COUNT.getClass(), coinAnimation);
    }
}
