package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;
import java.util.Set;

/**
 * Interface which can be implemented by a view. So the view has the functionality that GameController expects
 */
public interface GameView {

    void setSpaceElements(Set<SpaceElement> spaceElements);

    void displayUpdatedSpaceElements();

    void displayCollectedCoins(int coins);

    void displayCurrentScore(int score);

    boolean isUpPressed();

    boolean isDownPressed();

    void gameEnded();
}
