package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PersistenceUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class GameController {

    private boolean isRunning = false;
    private boolean isPaused = false;

    private int collectedCoins;
    private int distance;
    private int score;

    private int fps;
    private long sleepTime;

    private double horizontalGameSpeed;
    private double horizontalGameSpeedIncreasePerSecond;
    private int spaceShipVerticalMoveSpeed;


    private SpaceShip spaceShip;
    private Set<SpaceElement> elements;
    private PlayerProfile playerProfile;
    private GameView gameView;



    public void setView(GameView gameView) {
        this.gameView = gameView;
    }


    public void startGame() throws Exception {
        if (gameView == null) {
            throw new GameViewNotFoundException("GameController has no GameView");
        }

        initialize();

        while (isRunning) {
            long gameLoopTime = System.currentTimeMillis();

            if (isPaused) {
                //TODO: Implement pause
            }

            checkMovementKeys();

            if(detectCollision()) {
                isRunning = false;
            }

            generateObstacles();
            moveElements();
            removePastDrawables();
            displayToUI();

            horizontalGameSpeed += horizontalGameSpeedIncreasePerSecond /fps;

            gameLoopTime = System.currentTimeMillis() - gameLoopTime;
            if (sleepTime - gameLoopTime > 0) {
                Thread.sleep(sleepTime-gameLoopTime);
            }
        }


        updatePlayerProfile();
        PersistenceUtil.saveProfile(playerProfile);
        gameView.gameEnded();
    }

    private void checkMovementKeys() {
        boolean upPressed = gameView.isUpPressed();
        boolean downPressed = gameView.isDownPressed();

        if (upPressed && !downPressed) {
            moveSpaceShip(SpaceShipDirection.UP);
        } else if (downPressed && !upPressed) {
            moveSpaceShip(SpaceShipDirection.DOWN);
        }
    }

    /**
     * - starts game, if this is the first movement
     * --> transmit new position to spaceship
     */
    protected void moveSpaceShip(SpaceShipDirection direction) {
        switch (direction) {
            case UP:
                spaceShip.directMoveUp();
//                spaceShip.directMove(new Point(0, -spaceShipVerticalMoveSpeed));
                break;
            case DOWN:
                spaceShip.directMoveDown();
//                spaceShip.directMove(new Point(0, spaceShipVerticalMoveSpeed));
                break;
        }
    }

    private void updatePlayerProfile() {
        playerProfile.addCoins(collectedCoins);
//        if(score > playerProfile.getHighScore()) {
//            playerProfile.setHighScore();
//        }
    }

    private void displayToUI() {
        Set<SpaceElement> dataToDisplay = new HashSet<SpaceElement>(elements);
        dataToDisplay.add(spaceShip);


        gameView.displayUpdatedSpaceElements(dataToDisplay);
        gameView.displayCollectedCoins(collectedCoins);
        gameView.displayCurrentScore(score);
    }

    /**
     * continues or stops game logic according to clicking pause/resume button
     */
    public void togglePause() {
        isPaused = !isPaused;
    }





    protected void initialize() {
        playerProfile = PersistenceUtil.loadProfile();

        elements = new HashSet<>();
        setUpSpaceElementImages();

        spaceShip = new SpaceShip(new Point(20, 100), 50, 200);

        fps = playerProfile.getFps();

        isRunning = true;
        sleepTime = 1000/fps;

        distance = 0;
        collectedCoins = 0;
        spaceShipVerticalMoveSpeed = 3;
        horizontalGameSpeed = 1;
        horizontalGameSpeedIncreasePerSecond = 0.1;
//        gameSpeed = playerProfile.getStartingGameSpeed;
//        gameSpeedIncrease = playerProfile.getGameSpeedIncrease;
//        spaceShipMoveSpeed = playerProfile.getSpaceShipMoveSpeed;
    }

    protected void setUpSpaceElementImages() {
        try {
            //TODO: SetVisuals for Coins, UFO, Powerups etc.
            URL imageURL = SpaceRunnerApp.class.getResource("images/" + playerProfile.getPlayerImageId() + ".png");

            //TODO: islermic ask nachbric we should set the image only once on a space element cause the space element will look the same for the whole game --> no visual in constructor
            //TODO: I already changed it back
            SpaceShip.setVisual(PersistenceUtil.loadImage(imageURL));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void removePastDrawables() {
        for(SpaceElement element : elements) {
            if(element.getCurrentPosition().x + element.getWidth() < 0) {
//                if(element.getPosition().x + element.getWidth() < 0) {
                elements.remove(element);
            }
        }
    }


    private void generateObstacles() {

        return;
        //TODO: This is not how it should be => Generate from presets and only randomly
        /*try {
            elements.add(new Coin(new Point(20, 100), 20, 20));
            elements.add(new UnidentifiedFlightObject(new Point(20, 100), 20, 20));
            elements.add(new PowerUp(new Point(20, 100), 20, 20));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    private void moveElements() {
        for(SpaceElement element : elements) {
            //TODO: islermic ask nachbric why not?
//            element.move(new Point(-(int) horizontalGameSpeed, 0)); //todo keine gute lösung vtl constructor anpassen
        }
    }

    /**
     * checks continuously for a crash with an SpaceElement
     */
    private boolean detectCollision() {

        //TODO: Loop over all elements and check for collision

        return false;
    }

    protected SpaceShip getSpaceShip() {
        return spaceShip;
    }
}
