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

        elements = new HashSet<>();
        initialize();
        elements.add(spaceShip);

        gameView.setSpaceElements(Collections.unmodifiableSet(elements));

        isRunning = true;

        while (isRunning) {

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

            gameView.displayUpdatedSpaceElements();
            gameView.displayCollectedCoins(collectedCoins);
            gameView.displayCurrentScore(score);

            gameSpeed += gameSpeedIncrease/fps;
        }

        playerProfile.addCoins(collectedCoins);
        playerProfile.addScore(score);

        FileUtil.saveProfile(playerProfile);
        //TODO: Add collected coins to playerProfile and save it!

    }


    /**
     * continues or stops game logic according to clicking pause/resume button
     */
    public void togglePause() {
        isPaused = !isPaused;
    }


    /**
     * TODO: Move or delete
     * saves collected coins as well as the distance (if it is a new record) to a local file
     *
     private void saveState() {}
     */

    /**
     * - starts game, if this is the first movement
     * --> transmit new position to spaceship
     */
    public void moveSpaceShip(SpaceShipDirection direction) {
        switch (direction) {
            case UP:
                spaceShip.move(SpaceShipDirection.UP);
            case DOWN:
                spaceShip.move(SpaceShipDirection.DOWN);
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
            element.move(new Point(-gameSpeed, 0));
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
