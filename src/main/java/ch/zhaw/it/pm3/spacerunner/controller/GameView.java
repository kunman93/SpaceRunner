package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.model.element.SpaceElement;
import java.util.Set;

public interface GameView {

    void setSpaceElements(Set<SpaceElement> spaceElements);

    void displayUpdatedSpaceElements();

    void displayCollectedCoins(int coins);

    void displayCurrentScore(int score);

}
