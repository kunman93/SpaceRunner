package ch.zhaw.it.pm3.spacerunner.model;

import java.awt.*;

public abstract class Obstacle extends SpaceElement {
    public Obstacle(Point startPosition, int width, int length) {
        super(startPosition, width, length);
    }
}
