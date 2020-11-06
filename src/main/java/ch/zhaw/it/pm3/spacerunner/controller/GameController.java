package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.model.ElementPreset;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.HorizontalSpeed;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.VerticalSpeed;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualSVGFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GameController {

    private boolean isPaused = false;

    private int collectedCoins;
    private int distance;
    private int score;

    private int fps;

    private SpaceWorld background = null;

    private double horizontalGameSpeed;
    private double horizontalGameSpeedIncreasePerSecond;


    private SpaceShip spaceShip;
    private Set<SpaceElement> elements;
    private PlayerProfile playerProfile;
    private ElementPreset elementPreset;

    private boolean gameOver = false;

    private int width = 0;
    private int height = 0;


    public void saveGame() {
        //TODO: Use and maybe improve
        updatePlayerProfile();
        PersistenceUtil.saveProfile(playerProfile);
    }

    public void processFrame(boolean upPressed, boolean downPressed) {

        if (!isPaused) {
            checkMovementKeys(upPressed, downPressed);


            processCollision(detectCollision());

            updateObstacleSpeed();
            generateObstacles();
            moveElements();
            removePastDrawables();


            //TODO: Dont make this FPS based!!! (imagine FrameDrops, etc!!) => Time based approach
            horizontalGameSpeed += horizontalGameSpeedIncreasePerSecond / fps;

            //TODO: this is to test game over (REMOVE AFTER implemented)
//            if(horizontalGameSpeed > 1.5){
//                System.out.println("GAME OVER");
//                gameOver = true;
//            }

            //displayToUI();
        }
    }

    private void updateObstacleSpeed() {
        for (SpaceElement spaceElement : elements) {
            if (spaceElement instanceof UFO) {
                spaceElement.setVelocity(new Point((int) (-HorizontalSpeed.UFO.getSpeed() * horizontalGameSpeed), VerticalSpeed.UFO.getSpeed()));
            } else if (spaceElement instanceof Asteroid) {
                spaceElement.setVelocity(new Point((int) (-HorizontalSpeed.ASTEROID.getSpeed() * horizontalGameSpeed), VerticalSpeed.ASTEROID.getSpeed()));
            } else if (spaceElement instanceof Coin) {
                spaceElement.setVelocity(new Point((int) (-HorizontalSpeed.COIN.getSpeed() * horizontalGameSpeed), VerticalSpeed.ZERO.getSpeed()));
            }
        }

        spaceShip.setSpaceShipSpeed((int) (VerticalSpeed.SPACE_SHIP.getSpeed() * horizontalGameSpeed));
        background.setVelocity(new Point((int) (-HorizontalSpeed.BACKGROUND.getSpeed() * horizontalGameSpeed), VerticalSpeed.ZERO.getSpeed()));
    }

    /**
     * Checks if movement keys are pressed & moves the spaceship accordingly
     */
    public void checkMovementKeys(boolean upPressed, boolean downPressed) {

        if (upPressed && !downPressed) {
            moveSpaceShip(SpaceShipDirection.UP);
        } else if (downPressed && !upPressed) {
            moveSpaceShip(SpaceShipDirection.DOWN);
        }
    }

    /**
     * Moves the spaceship
     *
     * @param direction The direction of movement (UP,DOWN or NONE)
     */
    protected void moveSpaceShip(SpaceShipDirection direction) {
        switch (direction) {
            case UP:
                if (spaceShip.getCurrentPosition().y + (spaceShip.getHeight() * ElementScaling.SPACE_SHIP.getScaling()) <= 0.0)
                    return;
                spaceShip.directMoveUp();
                break;
            case DOWN:
                if (spaceShip.getCurrentPosition().y - (spaceShip.getHeight() * ElementScaling.SPACE_SHIP.getScaling())
                        >= height) return; //TODO canvas = 500.0, height
                spaceShip.directMoveDown();
                break;
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

    public ArrayList<SpaceElement> getGameElements() {
        ArrayList<SpaceElement> dataToDisplay = new ArrayList<SpaceElement>(elements);
        dataToDisplay.add(0, background);
        dataToDisplay.add(1, spaceShip);
        return dataToDisplay;
    }

    public int getCollectedCoins() {
        return collectedCoins;
    }

    public int getScore() {
        return score;
    }

    public int getFps() {
        return fps;
    }

    /**
     * Continues or stops game logic according to clicking pause/resume button
     */
    public void togglePause() {
        isPaused = !isPaused;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * Initializes the class variables
     */
    public void initialize() {

        //TODO: check if 16:9 view

        gameOver = false;

        playerProfile = PersistenceUtil.loadProfile();

        elementPreset = new ElementPreset();

        elements = new HashSet<>();

        setUpSpaceElementImages();
        //TODO: eventuall give horizontalGameSpeed as paramter, implement a setHorizontalGameSpeed-Method
        background = new SpaceWorld(new Point(0, 0), 2880, 640);
        spaceShip = new SpaceShip(new Point(20, 100), 200, 50);

        fps = playerProfile.getFps();

        distance = 0;
        collectedCoins = 0;
        horizontalGameSpeed = 1;
        horizontalGameSpeedIncreasePerSecond = 0.05;
//        gameSpeed = playerProfile.getStartingGameSpeed;
//        gameSpeedIncrease = playerProfile.getGameSpeedIncrease;
//        spaceShipMoveSpeed = playerProfile.getSpaceShipMoveSpeed;
    }


    public void setViewport(int width, int height) {
        this.height = height;
        this.width = width;

        //TODO: Update Images and hitboxes
        //TODO: UFO, ElementPreset
    }

    /**
     * Initializes the SpaceElement classes with their corresponding images
     */
    private void setUpSpaceElementImages() {
        try {
            //TODO: SetVisuals for Coins, UFO, Powerups etc.
            //TODO: Maybe enum for resources strings
            URL spaceShipImageURL = SpaceRunnerApp.class.getResource(VisualSVGFile.SPACE_SHIP_1.getFileName());
            BufferedImage spaceShipImage = VisualUtil.loadSVGImage(spaceShipImageURL,
                    (float) (height * ElementScaling.SPACE_SHIP.getScaling()));
            spaceShipImage = VisualUtil.flipImage(spaceShipImage, true);
            SpaceShip.setVisual(spaceShipImage);

            URL unidentifiedSpaceObjectImageURL = SpaceRunnerApp.class.getResource(VisualSVGFile.UFO_1.getFileName());
            BufferedImage unidentifiedSpaceObjectImage = VisualUtil.loadSVGImage(unidentifiedSpaceObjectImageURL,
                    (float) (height * ElementScaling.UFO.getScaling()));
            UFO.setVisual(unidentifiedSpaceObjectImage);

            URL asteroidImageURL = SpaceRunnerApp.class.getResource(VisualSVGFile.ASTEROID.getFileName());
            BufferedImage asteroidImage = VisualUtil.loadSVGImage(asteroidImageURL,
                    (float) (height * ElementScaling.ASTEROID.getScaling()));
            Asteroid.setVisual(asteroidImage);

            URL backgroundImageURL = SpaceRunnerApp.class.getResource(VisualFile.BACKGROUND_STARS.getFileName());
            BufferedImage backgroundImage = VisualUtil.loadImage(backgroundImageURL);
            SpaceWorld.setVisual(backgroundImage);

            setUpCoinWithAnimation();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpCoinWithAnimation() {
        URL coin1ImageURL = SpaceRunnerApp.class.getResource(VisualSVGFile.SHINEY_COIN_1.getFileName());
        URL coin2ImageURL = SpaceRunnerApp.class.getResource(VisualSVGFile.SHINEY_COIN_2.getFileName());
        URL coin3ImageURL = SpaceRunnerApp.class.getResource(VisualSVGFile.SHINEY_COIN_3.getFileName());
        URL coin4ImageURL = SpaceRunnerApp.class.getResource(VisualSVGFile.SHINEY_COIN_4.getFileName());
        URL coin5ImageURL = SpaceRunnerApp.class.getResource(VisualSVGFile.SHINEY_COIN_5.getFileName());
        URL coin6ImageURL = SpaceRunnerApp.class.getResource(VisualSVGFile.SHINEY_COIN_6.getFileName());
        float coinHeight = (float) (height * ElementScaling.COIN.getScaling());
        BufferedImage coin1Image = VisualUtil.loadSVGImage(coin1ImageURL, coinHeight);
        BufferedImage coin2Image = VisualUtil.loadSVGImage(coin2ImageURL, coinHeight);
        BufferedImage coin3Image = VisualUtil.loadSVGImage(coin3ImageURL, coinHeight);
        BufferedImage coin4Image = VisualUtil.loadSVGImage(coin4ImageURL, coinHeight);
        BufferedImage coin5Image = VisualUtil.loadSVGImage(coin5ImageURL, coinHeight);
        BufferedImage coin6Image = VisualUtil.loadSVGImage(coin6ImageURL, coinHeight);
        Coin.setVisual(coin1Image);
        BufferedImage[] coinAnimation = new BufferedImage[]{coin1Image, coin2Image, coin3Image, coin4Image, coin5Image, coin6Image};
        Coin.setCoinAnimationVisuals(coinAnimation, 80);
    }

    /**
     * Removes drawable SpaceElements that have moved past the left side of the screen, so that their no longer visible on the UI
     */
    private void removePastDrawables() {
        elements.removeIf((SpaceElement element) ->
                element.getCurrentPosition().x + element.getWidth() < 0);
    }

    /**
     * Generates SpaceElements offscreen, which are meant to move left towards the spaceship
     */
    private void generateObstacles() {
        SpaceElement[] preset = elementPreset.getRandomPreset(horizontalGameSpeed);
        if (preset != null) {
            generatePreset(preset);
        }

        /*try {
            elements.add(new Coin(new Point(20, 100), 20, 20));
            elements.add(new UnidentifiedFlightObject(new Point(20, 100), 20, 20));
            elements.add(new PowerUp(new Point(20, 100), 20, 20));
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void generatePreset(SpaceElement[] preset) {
        Collections.addAll(elements, preset);
    }

    /**
     * Moves all SpaceElements
     */
    public void moveElements() {
        for (SpaceElement element : elements) {
            //TODO: islermic ask nachbric why not?
//            element.move(new Point(-(int) horizontalGameSpeed, 0)); //todo keine gute lösung vtl constructor anpassen

            element.move();
        }

        background.move();

    }

    /**
     * Checks if Spaceship has collided with any other SpaceElement and performs the corresponding actions
     */
    private SpaceElement detectCollision() {

        for (SpaceElement spaceElement : elements) {
            if (spaceShip.doesCollide(spaceElement)) {
                return spaceElement;
            }
        }
        return null;
    }

    /**
     * exevutes effects depending on type of spaceElement
     * @param spaceElement
     */
    private void processCollision(SpaceElement spaceElement) {
        if (spaceElement == null) return;

        if (spaceElement instanceof Obstacle) {
            spaceShip.crash();
            gameOver = true;
        } else if (spaceElement instanceof Coin) {
            collectedCoins++;
            elements.remove(spaceElement);
        } else if (spaceElement instanceof PowerUp) {
            // spaceElement.getEffect(); //ToDo one of the two
            // handlePowerUp(spaceElement)
        }
    }

    protected SpaceShip getSpaceShip() {
        return spaceShip;
    }
}
