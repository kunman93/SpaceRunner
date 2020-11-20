package ch.zhaw.it.pm3.spacerunner.ui;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.application.GameController;
import ch.zhaw.it.pm3.spacerunner.domain.GameDataCache;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup.DoubleCoinsPowerUp;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup.PowerUp;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup.ShieldPowerUp;
import ch.zhaw.it.pm3.spacerunner.technicalservices.performance.FPSTracker;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.*;
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
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameViewController extends ViewController {

    private final Logger logger = Logger.getLogger(GameViewController.class.getName());
    private final VisualUtil visualUtil = VisualUtil.getUtil();

    @FXML
    private Canvas gameCanvas;
    private GraphicsContext graphicsContext;
    private GameViewPort gameViewPort = null;
    private GameProportionUtil gameProportionUtil = GameProportionUtil.getUtil();

    private GameController gameController = new GameController();
    private boolean downPressed;
    private boolean upPressed;
    private Stage primaryStage;
    private EventHandler<KeyEvent> pressedHandler;
    private EventHandler<KeyEvent> releasedHandler;
    private EventHandler<KeyEvent> startGameKeyHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.SPACE) {
                gameController.togglePause();
                if (primaryStage != null) {
                    primaryStage.removeEventHandler(KeyEvent.KEY_RELEASED, this);
                }
            }
        }
    };
    private EventHandler<KeyEvent> pauseGameKeyHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            if (event.getCode() == KeyCode.P) {
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
    private final double infoBarImageMargin = 10;
    private final double infoBarTextMargin = 30;
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

        new Thread(() -> {
            gameController.initialize();

            int fps_config = gameController.getFps();
            long timeForFrameNano = (1_000_000_000 / fps_config) - FRAME_TIME_DELTA;

            isLoaded = true;

            updateGameFrame();
            gameController.togglePause();

            primaryStage.addEventHandler(KeyEvent.KEY_RELEASED, startGameKeyHandler);

            lastUpdate = System.nanoTime();
            gameLoop = new AnimationTimer() {
                private FPSTracker fpsTracker = new FPSTracker();

                public void handle(long currentNanoTime) {
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


    private void updateGameFrame() {
        gameController.processFrame(upPressed, downPressed);
        clearCanvas();
        displayUpdatedSpaceElements(gameController.getGameElements());
        displayCoinsAndScore(gameController.getCollectedCoins(), gameController.getScore());
        displayActivatedPowerUps(gameController.getActivePowerUps());

        boolean gameOver = gameController.isGameOver();

        if (gameOver) {
            removeKeyHandlers();
            removeWindowSizeListeners();
            if (gameLoop != null) {
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

        if (!wasPausedBeforeResize && !isResizing) {
            isResizing = true;
            wasPausedBeforeResize = gameController.isPaused();
            if (!wasPausedBeforeResize) {
                gameController.togglePause();
            }
        }

        if (resizeTask != null) {
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
                Platform.runLater(() -> {
                    gameCanvas.setWidth(finalWidth);
                    gameCanvas.setHeight(finalHeight + gameViewPort.getInfoBarHeight());
                    gameController.setViewport((int) finalWidth, (int) finalHeight);
                    if (!wasPausedBeforeResize) {
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

    private EventHandler<WindowEvent> handleCloseWindowEvent() {
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
        graphicsContext.setFont(new Font(DEFAULT_FONT, gameProportionUtil.getFontSize(gameViewPort.getInfoBarHeight(), infoBarPaddingPercent)));
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

    private void clearCanvas() {
        graphicsContext.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
    }


    private void displayUpdatedSpaceElements(List<SpaceElement> spaceElements) {
        for (SpaceElement spaceElement : spaceElements) {
            Point2D.Double position = spaceElement.getRelativePosition();
            Image image = null;
            try {
                image = fxmlImageProxy.getFXMLImage(spaceElement.getClass());
            } catch (VisualNotSetException e) {
                logger.log(Level.SEVERE, "Visual for {0} wasn't set", spaceElement.getClass());
            }
            graphicsContext.drawImage(image, position.x * visualManager.getWidth(), position.y * visualManager.getHeight());
        }
    }


    private void displayCoinsAndScore(int coins, int score) { //todo aufteilen
        double xPositionReference = gameViewPort.getGameWidth();
        double infoBarYPosition = gameViewPort.getGameHeight();


        graphicsContext.setFill(Color.DARKGRAY);
        graphicsContext.fillRect(0, infoBarYPosition, gameViewPort.getGameWidth(), gameViewPort.getInfoBarHeight());

        Image image = null;

        try {
            image = fxmlImageProxy.getFXMLImage(UIVisualElement.COIN_COUNT);
            xPositionReference -= image.getWidth();
            graphicsContext.drawImage(image, (gameViewPort.getGameWidth() - image.getWidth() - infoBarImageMargin),
                    infoBarYPosition, image.getWidth(), image.getHeight());
        } catch (VisualNotSetException e) {
            logger.log(Level.SEVERE, "Visual for {0} wasn't set", UIVisualElement.COIN_COUNT.getClass());
        }

        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(DEFAULT_FONT, gameProportionUtil.getFontSize(gameViewPort.getInfoBarHeight(), infoBarPaddingPercent)));
        graphicsContext.setTextAlign(TextAlignment.RIGHT);
        graphicsContext.setTextBaseline(VPos.TOP);
        xPositionReference -= infoBarTextMargin;
        double textWidth = gameProportionUtil.getTextWidth(coins, gameViewPort.getInfoBarHeight(), infoBarPaddingPercent);
        graphicsContext.fillText(String.valueOf(coins), xPositionReference, infoBarYPosition, textWidth);
        xPositionReference -= (infoBarTextMargin + textWidth);
        graphicsContext.fillText(String.valueOf(score), xPositionReference, infoBarYPosition, textWidth);
    }

    private void displayActivatedPowerUps(Map<Class<? extends PowerUp>, PowerUp> activePowerUps) {
        double xPositionReference = 0;
        double infoBarYPosition = gameViewPort.getGameHeight();

        Image image = null;
        for (Map.Entry<Class<? extends PowerUp>, PowerUp> classPowerUpEntry : activePowerUps.entrySet()) {
            Class<? extends VisualElement> uiVisualElementClass = null;
            if (DoubleCoinsPowerUp.class.equals(classPowerUpEntry.getKey())) {
                uiVisualElementClass = UIVisualElement.DOUBLE_COIN_POWER_UP;
            } else if (ShieldPowerUp.class.equals(classPowerUpEntry.getKey())) {
                uiVisualElementClass = UIVisualElement.SHIELD_POWER_UP;
            }

            if (uiVisualElementClass == null) {
                throw new NullPointerException("Power Up was not converted properly to UIVisualElement (forgot to add?)");
            }
            try {
                image = fxmlImageProxy.getFXMLImage(uiVisualElementClass);
                xPositionReference += image.getWidth();
                graphicsContext.drawImage(image, infoBarImageMargin + xPositionReference,
                        infoBarYPosition, image.getWidth(), image.getHeight());
            } catch (VisualNotSetException e) {
                logger.log(Level.SEVERE, "Visual for {0} wasn't set", uiVisualElementClass.getSimpleName());
            }
        }


    }

    private void displayInformation(String info) {
        graphicsContext.setFill(Color.WHITE);
        graphicsContext.setFont(new Font(DEFAULT_FONT, gameProportionUtil.getFontSize(gameViewPort.getInfoBarHeight(), infoBarPaddingPercent)));
        graphicsContext.setTextAlign(TextAlignment.CENTER);
        graphicsContext.fillText(info, gameViewPort.getGameWidth() / 2, gameViewPort.getGameHeight());
    }

    private void removeKeyHandlers() {
        primaryStage.removeEventHandler(KeyEvent.KEY_PRESSED, pressedHandler);
        primaryStage.removeEventHandler(KeyEvent.KEY_RELEASED, releasedHandler);
        primaryStage.removeEventHandler(KeyEvent.KEY_PRESSED, pauseGameKeyHandler);
    }

    private void initializeUiElements() {
        AnimatedVisual coinAnimation = new AnimatedVisual(VisualSVGAnimationFiles.COIN_ANIMATION, VisualScaling.COIN_COUNT);
        visualManager.loadAndSetAnimatedVisual(UIVisualElement.COIN_COUNT, coinAnimation);
        visualManager.loadAndSetVisual(UIVisualElement.DOUBLE_COIN_POWER_UP, new Visual(VisualSVGFile.DOUBLE_COIN_POWER_UP, VisualScaling.POWER_UP_UI));
        visualManager.loadAndSetVisual(UIVisualElement.SHIELD_POWER_UP, new Visual(VisualSVGFile.SHIELD_POWER_UP, VisualScaling.POWER_UP_UI));
    }
}
