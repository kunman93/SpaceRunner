package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.model.ElementPreset;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.HorizontalSpeed;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.VerticalSpeed;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.*;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.*;

public class GameController {
    private PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
    private final long GAME_SPEED_INCREASE_PERIOD_TIME = 1000L;
    private final double HORIZONTAL_GAME_SPEED_INCREASE_PER_SECOND = 0.05;
    private final double RELATIVE_GAME_SPEED_INCREASE_PER_SECOND = 0.0001;

    private Timer gameSpeedTimer;


    private boolean isPaused = false;

    private int collectedCoins;
    private int distance;
    private int score;

    private int fps;

    private SpaceWorld background = null;

    private double horizontalGameSpeed;


    private SpaceShip spaceShip;
    private Set<SpaceElement> elements = new HashSet<>();
    private PlayerProfile playerProfile;
    private ElementPreset elementPreset;

    private boolean gameOver = false;

    private int width = 0;
    private int height = 0;

    private final VisualManager visualManager = VisualManager.getInstance();
    private final VelocityManager velocityManager = VelocityManager.getInstance();





    public void saveGame() {
        //TODO: Use and maybe improve
        updatePlayerProfile();
        persistenceUtil.saveProfile(playerProfile);
    }

    public void processFrame(boolean upPressed, boolean downPressed) {

        if (!isPaused) {
            checkMovementKeys(upPressed, downPressed);


            processCollision(detectCollision());

            generateObstacles();
            moveElements();

            removePastDrawables();

        }
    }

    private void updateElementsSpeed() {
        //TODO: SpaceElementSpeedManager and use velocity
        velocityManager.accelerateAll(new Point2D.Double(-RELATIVE_GAME_SPEED_INCREASE_PER_SECOND, RELATIVE_GAME_SPEED_INCREASE_PER_SECOND));

//        for (SpaceElement spaceElement : elements) {
//            if (spaceElement instanceof UFO) {
//                spaceElement.setVelocity(new Point((int) (-HorizontalSpeed.UFO.getSpeed() * horizontalGameSpeed), VerticalSpeed.UFO.getSpeed()));
//            } else if (spaceElement instanceof Asteroid) {
//                spaceElement.setVelocity(new Point((int) (-HorizontalSpeed.ASTEROID.getSpeed() * horizontalGameSpeed), VerticalSpeed.ASTEROID.getSpeed()));
//            } else if (spaceElement instanceof Coin) {
//                spaceElement.setVelocity(new Point((int) (-HorizontalSpeed.COIN.getSpeed() * horizontalGameSpeed), VerticalSpeed.ZERO.getSpeed()));
//            }
//        }
//
//        if(spaceShip != null){
//            spaceShip.setSpaceShipSpeed((int) (VerticalSpeed.SPACE_SHIP.getSpeed() * horizontalGameSpeed));
//        }
//        if(background != null){
//            background.setVelocity(new Point((int) (-HorizontalSpeed.BACKGROUND.getSpeed() * horizontalGameSpeed), VerticalSpeed.ZERO.getSpeed()));
//        }
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
                if (spaceShip.getCurrentPosition().y <= 0.0)
                    return;
                spaceShip.directMoveUp();
                break;
            case DOWN:
                try {
                    //TODO: fix spaceship out of view
                    if (spaceShip.getCurrentPosition().y + visualManager.getElementPixelHeight(SpaceShip.class) >= height) return;
                } catch (VisualNotSetException e) {
                    //TODO: handle
                    e.printStackTrace();
                }
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
        velocityManager.initialize();

        gameSpeedTimer = new Timer("GameSpeedTimer");
        gameSpeedTimer.scheduleAtFixedRate(getGameSpeedTimerTask(), 0, GAME_SPEED_INCREASE_PERIOD_TIME);

        gameOver = false;

        playerProfile = persistenceUtil.loadProfile();

        elementPreset = new ElementPreset();

        elements = new HashSet<>();

        setUpSpaceElementImages();
        //TODO: eventuall give horizontalGameSpeed as paramter, implement a setHorizontalGameSpeed-Method
        background = new SpaceWorld(new Point(0, 0));
        spaceShip = new SpaceShip(new Point(20, 100));

        fps = playerProfile.getFps();

        distance = 0;
        collectedCoins = 0;
        horizontalGameSpeed = 1;
//        gameSpeed = playerProfile.getStartingGameSpeed;
//        gameSpeedIncrease = playerProfile.getGameSpeedIncrease;
//        spaceShipMoveSpeed = playerProfile.getSpaceShipMoveSpeed;
    }

    private TimerTask getGameSpeedTimerTask() {
        return new TimerTask() {
            public void run() {
                if(!isPaused){
                    horizontalGameSpeed += HORIZONTAL_GAME_SPEED_INCREASE_PER_SECOND;
                    updateElementsSpeed();
                }
            }
        };
    }


    public void setViewport(int width, int height) {
        this.height = height;
        this.width = width;
        boolean wasPaused = isPaused;
        if(!wasPaused){
            isPaused = true;
        }
        this.visualManager.setHeight(height);
        this.visualManager.setWidth(width);
        if(!wasPaused){
            isPaused = false;
        }

        //TODO: Update Images and hitboxes
        //TODO: UFO, ElementPreset
    }

    /**
     * Initializes the SpaceElement classes with their corresponding images
     */
    private void setUpSpaceElementImages() {
        try {
            visualManager.setVisual(SpaceShip.class, new Visual(VisualSVGFile.SPACE_SHIP_1, VisualScaling.SPACE_SHIP, true, false));
            visualManager.setVisual(UFO.class, new Visual(VisualSVGFile.UFO_1, VisualScaling.UFO));
            visualManager.setVisual(Asteroid.class, new Visual(VisualSVGFile.ASTEROID, VisualScaling.ASTEROID));
            visualManager.setVisual(SpaceWorld.class, new Visual(VisualFile.BACKGROUND_STARS));

            setUpCoinWithAnimation();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpCoinWithAnimation() {
        //TODO: not needed but not bad^^
        visualManager.setVisual(Coin.class, new Visual(VisualSVGFile.SHINEY_COIN_1, VisualScaling.COIN));



        AnimatedVisual coinAnimation = new AnimatedVisual(VisualSVGAnimationFiles.COIN_ANIMATION, VisualScaling.COIN);
        visualManager.setAnimatedVisual(Coin.class, coinAnimation);

    }

    /**
     * Removes drawable SpaceElements that have moved past the left side of the screen, so that their no longer visible on the UI
     */
    private void removePastDrawables() {
        elements.removeIf((SpaceElement element) ->
        {
            try {
                return element.getCurrentPosition().x + visualManager.getElementPixelWidth(element.getClass()) < 0;
            } catch (VisualNotSetException e) {
                //TODO: hanle
                e.printStackTrace();
                return true;
            }
        });
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
            gameSpeedTimer.cancel();
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
