package ch.zhaw.it.pm3.spacerunner.model;

import ch.zhaw.it.pm3.spacerunner.model.element.*;
import javafx.scene.input.KeyCode;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private boolean started = false;
    private boolean paused = false;
    private boolean ended = false;

    private int collectedCoins;
    private int distance;

    private SpaceShip spaceShip;
    private Map<Point, SpaceElement> elementMap;

    /**
     * !!! reads from file which space ship is sellected and takes the height and width
     * sets StartPostion according to window Height
     * */
    public Game() {
        spaceShip = new SpaceShip(new Point(20, 100),50,200);
        distance = 0;
        collectedCoins = 0;
        elementMap = new HashMap<>();
    }

    /**
     * adds obstacles, powerups and coins to map
     * */
    private void initializeObstacles() {

    }

    /**
     * continues or stops game logic according to clicking pause/resume button
     * */
    public void togglePause() {
        if (!paused) paused = true;
        else {
            paused = false;
            collisonDetector();
        }
    }

    /**
     * saves collected coins as well as the distance (if it is a new record) to a local file
     * */
    private void saveState() {}

    /**
     * - starts game, if this is the first movement
     * --> transmit new position to spaceship
     * */
    public void moveSpaceShip(KeyCode input) {
        if (!started) {
            started = true;
            collisonDetector();
        }
        switch (input) {
            case UP:
                //spaceShip.move(1);
                break;
            case DOWN:
                //spaceShip.move(-1);
        }
        System.out.println(input.toString());
    }

    /**
     * checks continuously for a crash with an SpaceElement
     * */
    public void collisonDetector() {
        while (!paused || !ended) {
            SpaceElement spaceElement = elementMap.getOrDefault(spaceShip.getPosition(), null);

            if (spaceElement instanceof Coins) coinCollected();
            if (spaceElement instanceof PowerUp) break; //spaceShip.setPowerUp()
            if (spaceElement instanceof Obstacle) ended = true;
        }

        if (ended) saveState();

    }

    private void coinCollected () {
        collectedCoins++;
    }
}
