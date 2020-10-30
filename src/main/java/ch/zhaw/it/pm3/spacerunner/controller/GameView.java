package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Interface which can be implemented by a view. So the view has the functionality that GameController expects
 */
public interface GameView {

    void displayUpdatedSpaceElements(List<SpaceElement> spaceElements);

    void displayCollectedCoins(int coins);

    void displayCurrentScore(int score);

    boolean isUpPressed();

    boolean isDownPressed();

    void gameEnded();

    double getCanvasHeight();

    double getCanvasWidth();
}
