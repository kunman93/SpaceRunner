package ch.zhaw.it.pm3.spacerunner.ui;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualElement;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManagerListener;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Image proxy for FXML-Images.
 * Used the VisualManager to get the images and converts them into fxml images.
 * Also creates a cache for fxml images.
 *
 * @author islermic
 */
public class FXMLImageProxy implements VisualManagerListener {

    private static final FXMLImageProxy proxyInstance = new FXMLImageProxy();
    private final VisualManager visualManager = VisualManager.getManager();
    private Map<BufferedImage, Image> fxmlImageClassMap = new HashMap<>();

    private FXMLImageProxy() {
        visualManager.addListener(this);
    }

    public static FXMLImageProxy getProxy() {
        return proxyInstance;
    }

    /**
     * Clears all the data managed by this manager. (reset)
     */
    @Override
    public void clear() {
        fxmlImageClassMap = new HashMap<>();
    }

    /**
     * Gets the fxml image from the managers list.
     * If the image has not been loaded yet, it is loaded and added to the managed map. And returned afterwards.
     * @param elementClass class to load image
     * @return fxml image
     * @throws VisualNotSetException if no image set for this class
     */
    public Image getFXMLImage(Class<? extends VisualElement> elementClass) throws VisualNotSetException {
        BufferedImage bufferedImage = visualManager.getImage(elementClass);
        Image image = fxmlImageClassMap.get(bufferedImage);

        if (image != null) {
            return image;
        } else {
            Image fxmlImage = SwingFXUtils.toFXImage(bufferedImage, null);
            fxmlImageClassMap.put(bufferedImage, fxmlImage);
            return fxmlImage;
        }
    }

    /**
     * Method implemented for VisualManagerListener.
     * Will be called when an image should be removed from cache and reloaded.
     * @param bufferedImage image that changed
     */
    @Override
    public void bufferedImageChanged(BufferedImage bufferedImage) {
        Image fxmlImage = SwingFXUtils.toFXImage(bufferedImage, null);
        fxmlImageClassMap.put(bufferedImage, fxmlImage);
    }
}
