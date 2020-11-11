package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.model.ElementPreset;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;
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

    //TODO: ConcurrentHashSet??
    //Set<String> mySet = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());
    private Set<SpaceElement> elements = new HashSet<>();
    private PlayerProfile playerProfile;
    private ElementPreset elementPreset;

    private boolean gameOver = false;

    private int width = 0;
    private int height = 0;

    private final VisualManager visualManager = VisualManager.getInstance();
    private final VelocityManager velocityManager = VelocityManager.getInstance();

    private long lastUpdate = 0;

    //TODO: information expert verletzung bei laden der bilder im voraus (wegen laggs).
    //TODO: proxy pattern mit manager



    public void saveGame() {
        updatePlayerProfile();
        persistenceUtil.saveProfile(playerProfile);
    }

    public void processFrame(boolean upPressed, boolean downPressed) {
        // System.out.println("Processing took " + lastProcessingTime / 1000000);

        long timeSinceLastUpdate = millisSinceLastProcessing();

        if (!isPaused) {
            moveSpaceShip(upPressed, downPressed);
            processCollision(detectCollision());
            generateObstacles();
            moveElements(timeSinceLastUpdate);
        }

        lastUpdate = System.currentTimeMillis();
    }


    private long millisSinceLastProcessing(){
        if(lastUpdate == 0){
            return 0;
        }else{
            return System.currentTimeMillis() - lastUpdate;
        }
    }

    private void updateElementsSpeed() {
        velocityManager.accelerateAll(new Point2D.Double(-RELATIVE_GAME_SPEED_INCREASE_PER_SECOND, RELATIVE_GAME_SPEED_INCREASE_PER_SECOND));
    }

    /**
     * Checks if movement keys are pressed & moves the spaceship accordingly
     */
    public void moveSpaceShip(boolean upPressed, boolean downPressed) {
        if (upPressed && !downPressed) {
            moveSpaceShip(SpaceShipDirection.UP);
        } else if (downPressed && !upPressed) {
            moveSpaceShip(SpaceShipDirection.DOWN);
        }
    }


    //TODO: move into spaceship. Also move the enum into package spaceelement
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

        velocityManager.setupGameElementVelocity();
        visualManager.loadGameElementVisuals();

        gameSpeedTimer = new Timer("GameSpeedTimer");
        gameSpeedTimer.scheduleAtFixedRate(getGameSpeedTimerTask(), 0, GAME_SPEED_INCREASE_PERIOD_TIME);

        //TODO: PowerupTimer and Task (Every "3" seconds)
        //TODO: Roll -> 0-100
        // 0 - 10 => Double Coins
        // 10 - 20 => Shield

        gameOver = false;

        playerProfile = persistenceUtil.loadProfile();

        elementPreset = new ElementPreset();

        elements = new HashSet<>();
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

                //TODO: move into own task??
                removePastDrawables();
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
        this.visualManager.setViewport(width, height);

        this.velocityManager.setHeight(height);
        this.velocityManager.setWidth(width);
        if(!wasPaused){
            isPaused = false;
        }

        //TODO: Update Images and hitboxes
        //TODO: UFO, ElementPreset
    }

      /**
     * Removes drawable SpaceElements that have moved past the left side of the screen, so that their no longer visible on the UI
     */
    private void removePastDrawables() {
        System.out.println("Removing past drawables");
        elements.removeIf((SpaceElement element) ->
        {
            try {
                return element.getCurrentPosition().x + visualManager.getElementPixelWidth(element.getClass()) < 0;
            } catch (VisualNotSetException e) {
                //TODO: handle
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
    }

    private void generatePreset(SpaceElement[] preset) {
        Collections.addAll(elements, preset);
    }

    /**
     * Moves all SpaceElements
     */
    public void moveElements(long timeSinceLastUpdate) {
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
            saveGame();
        } else if (spaceElement instanceof Coin) {
            collectedCoins++;
            elements.remove(spaceElement);
        } else if (spaceElement instanceof PowerUp) {
            //TODO: double coins for 10 seconds
            //TODO: shield until crash

            // spaceElement.getEffect(); //ToDo one of the two
            // handlePowerUp(spaceElement)
        }
    }

    private Map<PowerUp, Boolean> getActivePowerUps(){
        //TODO: rico implement
        return null;
    }

    protected SpaceShip getSpaceShip() {
        return spaceShip;
    }
}
