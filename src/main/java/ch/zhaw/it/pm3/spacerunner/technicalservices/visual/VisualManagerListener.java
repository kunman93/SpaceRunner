package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import java.awt.image.BufferedImage;

/**
 * Implemented this interface to register to Events from VisualManager.
 */
public interface VisualManagerListener {
    void clear();
    void bufferedImageChanged(BufferedImage bufferedImage);
}
