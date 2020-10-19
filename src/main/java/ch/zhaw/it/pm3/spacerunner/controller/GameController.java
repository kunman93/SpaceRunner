package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.model.element.*;
import ch.zhaw.it.pm3.spacerunner.model.gamedata.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.util.FileUtil;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GameController {

    private boolean isRunning = false;
    private boolean isPaused = false;

    private int collectedCoins;
    private int distance;
    private int points;

    private SpaceShip spaceShip;
    private Set<SpaceElement> elementMap;
    private PlayerProfile playerProfile;

    private GameView gameView;

    public void setView(GameView gameView){
        this.gameView = gameView;
    }


    public void startGame() throws Exception {
        if(gameView == null){
            //TODO: Make own exception types and handle
            throw new Exception();
        }

        isRunning = true;
        while(isRunning){

            if(isPaused){

            }

            //TODO: Get KeyEvent and
            boolean keypressed = false;
            if(keypressed){
                moveSpaceShip(null);
            }
            //TODO: Draw Elements to canvas
            gameView.drawSpaceElements(getDrawables());
            collisionDetector();

            generateObstacles();
            moveElements();
        }

        //TODO: Add collected coins to playerProfile and save it!

    }


    /**
     * continues or stops game logic according to clicking pause/resume button
     * */
    public void togglePause() {
        isPaused = !isPaused;
    }

    /**
     * adds obstacles, powerups and coins to map
     * */
    private void initializeObstacles() {
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
        elementMap = new HashSet<>();
    }

    public Set<SpaceElement> getDrawables(){
        Set<SpaceElement> spaceElements = new HashSet<>(elementMap);
        spaceElements.add(spaceShip);

        return spaceElements;
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
     * */
    public void moveSpaceShip(KeyCode input) {
        switch (input) {
            case UP:
                //spaceShip.move(1);
                break;
            case DOWN:
                //spaceShip.move(-1);
        }
        System.out.println(input.toString());
    }


    public void generateObstacles(){

        //TODO: This is not how it should be => Generate from presets and only randomly
        try {
            elementMap.add(new Coin(new Point(20, 100), 20, 20));
            elementMap.add(new UnidentifiedFlightObject(new Point(20, 100), 20, 20));
            elementMap.add(new PowerUp(new Point(20, 100), 20, 20));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void moveElements(){
        //TODO: move all movable elements
        //TODO: delete alls with x < 0
    }

    /**
     * checks continuously for a crash with an SpaceElement
     * */
    public boolean collisionDetector() {


        //TODO: Loop over all elements and check for collision
            /*SpaceElement spaceElement = elementMap.getOrDefault(spaceShip.getPosition(), null);

            if (spaceElement instanceof Coins) coinCollected();
            if (spaceElement instanceof PowerUp) break; //spaceShip.setPowerUp()
            if (spaceElement instanceof Obstacle) ended = true;*/
        return false;
    }

    private void coinCollected () {
        collectedCoins++;
    }


}
