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
    private double gameSpeed;
    private int fps;
    private long sleepTime;
    private double gameSpeedIncrease;
    private double spaceShipMoveSpeed;

    private SpaceShip spaceShip;
    private Set<SpaceElement> elements;
    private PlayerProfile playerProfile;
    private GameView gameView;

    /**
     * Initializes gameView
     * @param gameView
     */
    public void setView(GameView gameView) {
        this.gameView = gameView;
    }

    /**
     * Initialises, Runs (in a GameLoop) and Ends a Space-Runner game
     * @throws Exception when GameController doesnt have a gameView
     */
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

            gameSpeed += gameSpeedIncrease/fps;

            gameLoopTime = System.currentTimeMillis() - gameLoopTime;
            if (sleepTime - gameLoopTime > 0) {
                Thread.sleep(sleepTime-gameLoopTime);
            }
        }


        updatePlayerProfile();
        PersistenceUtil.saveProfile(playerProfile);
        gameView.gameEnded();
    }

    /**
     * Checks if movement keys are pressed & moves the spaceship accordingly
     */
    private void checkMovementKeys() {
        boolean upPressed = gameView.isUpPressed();
        boolean downPressed = gameView.isDownPressed();
        if (upPressed && downPressed) {
            moveSpaceShip(SpaceShipDirection.NONE);
        } else if (upPressed) {
            moveSpaceShip(SpaceShipDirection.UP);
        } else if (downPressed) {
            moveSpaceShip(SpaceShipDirection.DOWN);
        }
    }

    /**
     * Moves the spaceship
     * @param direction The direction of movement (UP,DOWN or NONE)
     */
    private void moveSpaceShip(SpaceShipDirection direction) {
        switch (direction) {
            case UP:
                spaceShip.move(new Point(0, (int)spaceShipMoveSpeed/fps));
            case DOWN:
                spaceShip.move(new Point(0, -(int)spaceShipMoveSpeed/fps));
        }
    }

    /**
     * Updates the playerProfile with collected coins and new highscore
     */
    private void updatePlayerProfile() {
        playerProfile.addCoins(collectedCoins);
//        if(score > playerProfile.getHighScore()) {
//            playerProfile.setHighScore();
//        }
    }

    /**
     * Displays the GameElements to UI
     */
    private void displayToUI() {
        gameView.displayUpdatedSpaceElements();
        gameView.displayCollectedCoins(collectedCoins);
        gameView.displayCurrentScore(score);
    }

    /**
     * Continues or stops game logic according to clicking pause/resume button
     */
    public void togglePause() {
        isPaused = !isPaused;
    }

    /**
     * Initializes the class variables
     */
    private void initialize() {
        playerProfile = PersistenceUtil.loadProfile();

        elements = new HashSet<>();
        gameView.setSpaceElements(elements); //TODO: Test with gameView.setSpaceElements(Collections.unmodifiableSet(elements));

        setUpSpaceElementImages();

        elements.add(spaceShip);

        fps = playerProfile.getFps();

        isRunning = true;
        sleepTime = 1000/fps;

        distance = 0;
        collectedCoins = 0;
//        gameSpeed = playerProfile.getStartingGameSpeed;
//        gameSpeedIncrease = playerProfile.getGameSpeedIncrease;
//        spaceShipMoveSpeed = playerProfile.getSpaceShipMoveSpeed;
    }

    /**
     * Initializes the SpaceElement classes with their corresponding images
     */
    private void setUpSpaceElementImages() {
        try {
            //TODO: SetVisuals for Coins, UFO, Powerups etc.
            URL imageURL = SpaceRunnerApp.class.getResource("images/" + playerProfile.getPlayerImageId() + ".png");
            SpaceShip.setVisual(PersistenceUtil.loadImage(imageURL));
            spaceShip = new SpaceShip(new Point(20, 100), 50, 200);
        } catch (IOException e) {
            //TODO: Handle: Should never happen unless theres a model which doesnt have an corresponding image in resources
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes drawable SpaceElements that have moved past the left side of the screen, so that their no longer visible on the UI
     */
    private void removePastDrawables() {
        for(SpaceElement element : elements) {
            if(element.getPosition().x + element.getLength() < 0) {
                elements.remove(element);
            }
        }
    }

    /**
     * Generates SpaceElements offscreen, which are meant to move left towards the spaceship
     */
    private void generateObstacles() {

        //TODO: This is not how it should be => Generate from presets and only randomly

        try {
            elements.add(new Coin(new Point(20, 100), 20, 20));
            elements.add(new UnidentifiedFlightObject(new Point(20, 100), 20, 20));
            elements.add(new PowerUp(new Point(20, 100), 20, 20));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Moves all SpaceElements
     */
    private void moveElements() {
        for(SpaceElement element : elements) {
            element.move(new Point(-(int)gameSpeed, 0));
        }
    }

    /**
     * Checks if Spaceship has collided with any other SpaceElement and performs the corresponding actions
     */
    private boolean detectCollision() {

        //TODO: Loop over all elements and check for collision

        return false;
    }


}
