package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager;

import java.awt.image.BufferedImage;

/**
 * Implemented this interface to register to Events from VisualManager.
 * @author islermic
 */
public interface VisualManagerListener {
    void clear();

    void bufferedImageChanged(BufferedImage bufferedImage);
}
