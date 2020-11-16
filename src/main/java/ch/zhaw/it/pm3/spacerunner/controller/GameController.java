package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.model.ElementPreset;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.*;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.manager.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.speed.HorizontalSpeed;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.GameSound;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.GameSoundUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.SoundClip;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameController {
    //TODO: make all final Manager and Util
    private final PersistenceUtil persistenceUtil = PersistenceUtil.getUtil();
    private final GameSoundUtil gameSoundUtil = GameSoundUtil.getUtil();
    private final VisualManager visualManager = VisualManager.getManager();
    private final VelocityManager velocityManager = VelocityManager.getManager();
    private final PowerUpManager powerUpManager = new PowerUpManager();


    private final long GAME_SPEED_INCREASE_PERIOD_TIME = 1000L;
    private final double RELATIVE_GAME_SPEED_INCREASE_PER_SECOND = 0.0001;

    private final double BUFFER_DISTANCE_BETWEEN_PRESETS = 0.2;
    private double remainingDistanceUntilNextPreset = 0.1;


    private boolean isPaused = false;
    private int collectedCoins = 0;
    private int score = 0;
    private int fps = 60;
    private boolean gameOver = false;

    private Timer gameTimer;
    private SpaceWorld background = null;
    private SpaceShip spaceShip;

    //TODO: ConcurrentHashSet -> TEST
    private Set<SpaceElement> elements = ConcurrentHashMap.newKeySet();
    private PlayerProfile playerProfile;
    private ElementPreset elementPreset;

    private final int GENERAL_POWERUP_COOLDOWN = 5000;

    private long lastUpdate = 0;

    //TODO: information expert verletzung bei laden der bilder im voraus (wegen laggs).
    //TODO: proxy pattern mit manager



    public void saveGame() {
        updatePlayerProfile();
        persistenceUtil.saveProfile(playerProfile);
    }

    public void processFrame(boolean upPressed, boolean downPressed) {
        long timeSinceLastUpdate = millisSinceLastProcessing();

        if (!isPaused) {
            moveSpaceShip(upPressed, downPressed, timeSinceLastUpdate);
            updateHighScore(timeSinceLastUpdate);
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
    public void moveSpaceShip(boolean upPressed, boolean downPressed, long timeInMillis) {
        if (upPressed && !downPressed) {
            spaceShip.moveSpaceShip(SpaceShipDirection.UP, timeInMillis);
        } else if (downPressed && !upPressed) {
            spaceShip.moveSpaceShip(SpaceShipDirection.DOWN, timeInMillis);
        }
    }




    /**
     * Updates the playerProfile with collected coins and new highscore
     */
    private void updatePlayerProfile() {
        playerProfile.addCoins(collectedCoins);
        if(score > playerProfile.getHighScore()) {
            playerProfile.setHighScore(score);
        }
    }

    public ArrayList<SpaceElement> getGameElements() {
        ArrayList<SpaceElement> dataToDisplay = new ArrayList<SpaceElement>(elements);
        dataToDisplay.add(0, background);
        dataToDisplay.add(spaceShip);
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

    public boolean isPaused() {return isPaused;}

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
        velocityManager.setupGameElementVelocity();
        visualManager.loadGameElementVisuals();

        gameTimer = new Timer("GameSpeedTimer");
        gameTimer.scheduleAtFixedRate(getGameSpeedTimerTask(), 0, GAME_SPEED_INCREASE_PERIOD_TIME);
        gameTimer.schedule(getPowerUpGeneratorTask(), 0, GENERAL_POWERUP_COOLDOWN);

        playerProfile = persistenceUtil.loadProfile();

        elementPreset = new ElementPreset();

        background = new SpaceWorld(new Point2D.Double(0, 0));
        spaceShip = new SpaceShip(new Point2D.Double(.05, 0.45));

        fps = playerProfile.getFps();

    }

    private TimerTask getPowerUpGeneratorTask(){
        return new TimerTask() {
            @Override
            public void run() {
                if (!isPaused){
                    elements.add(powerUpManager.generatePowerUps());
                }
            }
        };
    }

    private TimerTask getGameSpeedTimerTask() {
        return new TimerTask() {
            public void run() {
                if(!isPaused){
                    updateElementsSpeed();
                }

                //TODO: move into own task??
                removePastDrawables();
            }
        };
    }


    public void setViewport(int width, int height) {
        boolean wasPaused = isPaused;
        if(!wasPaused){
            isPaused = true;
        }
        this.visualManager.setViewport(width, height);

        if(!wasPaused){
            isPaused = false;
        }
    }

      /**
     * Removes drawable SpaceElements that have moved past the left side of the screen, so that their no longer visible on the UI
     */
    private void removePastDrawables() {
        System.out.println("Removing past drawables");
        elements.removeIf((SpaceElement element) ->
        {
            try {
                return element.getRelativePosition().x + visualManager.getElementRelativeWidth(element.getClass()) < 0;
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
        if(remainingDistanceUntilNextPreset < -BUFFER_DISTANCE_BETWEEN_PRESETS) {
            generatePreset(elementPreset.getRandomPreset());
        }
    }

    private void generatePreset(Preset preset) {
        Collections.addAll(elements, preset.getElementsInPreset());
        remainingDistanceUntilNextPreset = preset.getPresetSize();
        new Thread(()->{
            elementPreset.regeneratePresets();
        }).start();
    }

    /**
     * Moves all SpaceElements
     */
    public void moveElements(long timeSinceLastUpdate) {
        for (SpaceElement element : elements) {
            element.move(timeSinceLastUpdate);
        }
        background.move(timeSinceLastUpdate);
        remainingDistanceUntilNextPreset -= timeSinceLastUpdate/1000.0 * HorizontalSpeed.BACKGROUND.getSpeed();
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

    private void endRun(){
        spaceShip.crash();
        gameTimer.cancel();
        gameOver = true;
        if(playerProfile.isAudioEnabled()){
            //ToDo why not one try-catch
            new Thread(()->{
                try {
                    SoundClip explosion = gameSoundUtil.loadClip(GameSound.EXPLOSION);
                    explosion.addListener(() -> {
                        try {
                            SoundClip gameOverVoice = gameSoundUtil.loadClip(GameSound.GAME_OVER_VOICE);
                            gameOverVoice.addListener(()->{
                                try {
                                    gameSoundUtil.loadClip(GameSound.GAME_OVER_2).play();
                                }  catch (Exception e){
                                    //IGNORE ON PURPOSE
                                }
                            });
                            gameOverVoice.play();
                            gameSoundUtil.loadClip(GameSound.GAME_OVER_1).play();
                        } catch (Exception e){
                            //IGNORE ON PURPOSE
                        }
                    });
                    explosion.play();

                } catch (Exception e){
                    //IGNORE ON PURPOSE
                }
            }).start();
        }
        try {
            Thread.sleep(500);
        } catch (Exception e){
            //IGNORE ON PURPOSE
        }
        saveGame();
    }

    /**
     * executes effects depending on type of spaceElement
     *
     * @param spaceElement
     */
    private void processCollision(SpaceElement spaceElement) {
        if (spaceElement == null) return;

        if (spaceElement instanceof Obstacle) {
            collisionWithObstacle((Obstacle) spaceElement);
        } else if (spaceElement instanceof Coin) {
            collisionWithCoin((Coin) spaceElement);
        } else if (spaceElement instanceof PowerUp) {
            collisionWithPowerUp((PowerUp) spaceElement);
        }
    }

    private void collisionWithObstacle(Obstacle o){
        if (powerUpManager.hasShield()){
            elements.remove(o);
            powerUpManager.removeShield();
        } else {
            endRun();
        }
    }

    private void collisionWithCoin(Coin c){
        collectedCoins += 1 * Math.pow(2, powerUpManager.getCoinMultiplicator());
        score += 25;
        elements.remove(c);
        new Thread(()->{
            try {
                gameSoundUtil.loadClip(GameSound.COIN_PICKUP).play();
            } catch (Exception e){
                //IGNORE ON PURPOSE
            }
        }).start();
    }

    private void collisionWithPowerUp(PowerUp p){
        p.activatePowerUp();
        elements.remove(p);
        score += 10;
    }


    public Map<PowerUp, Integer> getActivePowerUps(){
        return Collections.unmodifiableMap(powerUpManager.getActivePowerUps());
    }

    protected SpaceShip getSpaceShip() {
        return spaceShip;
    }

    private void updateHighScore(long timeSinceLastUpdate) {
        score = score + (int)(timeSinceLastUpdate/10);
    }
}
