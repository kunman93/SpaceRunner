package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.model.element.*;
import ch.zhaw.it.pm3.spacerunner.model.gamedata.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.util.FileUtil;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameController {

    private boolean isRunning = false;
    private boolean isPaused = false;

    private int collectedCoins;
    private int distance;
    private int score;
    private double gameSpeed;
    private double gameSpeedIncrease;
    private int fps;
    private double spaceShipMoveSpeed;

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

        elements = new HashSet<>();
        elements.add(spaceShip);

        isRunning = true;

        long gameLoopTime = 0;
        long sleepTime = 1000/fps;

        while (isRunning) {

            gameLoopTime = System.currentTimeMillis();

            if (isPaused) {

            }

            //TODO: Get KeyEvent and
            boolean upPressed = false;
            boolean downPressed = false;
            if (upPressed && downPressed) {
                moveSpaceShip(SpaceShipDirection.NONE);
            } else if (upPressed) {
                moveSpaceShip(SpaceShipDirection.UP);
            } else if (downPressed) {
                moveSpaceShip(SpaceShipDirection.DOWN);
            }

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

        FileUtil.saveProfile(playerProfile);
    }

    private void updatePlayerProfile() {
        playerProfile.addCoins(collectedCoins);
        if(score > playerProfile.getHighScore) {
            playerProfile.setHighScore();
        }
    }

    private void displayToUI() {
        gameView.setSpaceElements(Collections.unmodifiableSet(elements));
        gameView.displayUpdatedSpaceElements();
        gameView.displayCollectedCoins(collectedCoins);
        gameView.displayCurrentScore(score);
    }

    /**
     * continues or stops game logic according to clicking pause/resume button
     */
    public void togglePause() {
        isPaused = !isPaused;
    }

    /**
     * - starts game, if this is the first movement
     * --> transmit new position to spaceship
     */
    public void moveSpaceShip(SpaceShipDirection direction) {
        switch (direction) {
            case UP:
                spaceShip.move(new Point(0, (int)spaceShipMoveSpeed/fps));
            case DOWN:
                spaceShip.move(new Point(0, -(int)spaceShipMoveSpeed/fps));
        }
    }



    private void initialize() {
        playerProfile = FileUtil.loadProfile();
        try {
            //TODO: SetVisuals for Coins, UFO, Powerups etc.
            SpaceShip.setVisual(FileUtil.loadImage(playerProfile.getPlayerImageId() + ".svg"));
            spaceShip = new SpaceShip(new Point(20, 100), 50, 200);
        } catch (IOException e) {
            //TODO: Handle: Should never happen unless theres a model which doesnt have an corresponding image in resources
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        distance = 0;
        collectedCoins = 0;
        gameSpeed = playerProfile.getStartingGameSpeed;
        gameSpeedIncrease = playerProfile.getGameSpeedIncrease;
        fps = playerProfile.getFPS;
        spaceShipMoveSpeed = playerProfile.getSpaceShipMoveSpeed;
    }

    private void removePastDrawables() {
        for(SpaceElement element : elements) {
            if(element.getCurrentPosition().x + element.getLength() < 0) {
                elements.remove(element);
            }
        }
    }


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


    private void moveElements() {
        for(SpaceElement element : elements) {
            element.move(new Point(-(int)gameSpeed, 0));
        }
    }

    /**
     * checks continuously for a crash with an SpaceElement
     */
    private boolean detectCollision() {

        //TODO: Loop over all elements and check for collision

        return false;
    }


}
