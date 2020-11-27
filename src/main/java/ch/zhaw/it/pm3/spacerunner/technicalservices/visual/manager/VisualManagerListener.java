package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager;

import java.awt.image.BufferedImage;

/**
 * Implemented this interface to register to Events from VisualManager.
 * @author islermic
 */
public interface VisualManagerListener {
    /**
     * Notifies when the VisualManager clears its data
     */
    void clear();

    /**
     * Notifies if an image in the visual manager has changed
     * @param bufferedImage
     */
    void bufferedImageChanged(BufferedImage bufferedImage);
}
