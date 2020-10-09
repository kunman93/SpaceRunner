package ch.zhaw.it.pm3.spacerunner.model.element;

import java.awt.*;

public abstract class Obstacle extends SpaceElement {

    public Obstacle(Point startPosition, int width, int length) throws Exception {
        super(startPosition, width, length);
    }
}
