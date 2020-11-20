package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualElement;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManagerListener;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class FXMLImageProxy implements VisualManagerListener {

    private static FXMLImageProxy proxyInstance = new FXMLImageProxy();
    private VisualManager visualManager = VisualManager.getManager();
    private Map<BufferedImage, Image> fxmlImageClassMap = new HashMap<>();

    private FXMLImageProxy() {
        visualManager.addListener(this);
    }

    public static FXMLImageProxy getProxy() {
        return proxyInstance;
    }

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

    @Override
    public void clear() {
        fxmlImageClassMap = new HashMap<>();
    }

    @Override
    public void bufferedImageChanged(BufferedImage bufferedImage) {
        Image fxmlImage = SwingFXUtils.toFXImage(bufferedImage, null);
        fxmlImageClassMap.put(bufferedImage, fxmlImage);
    }
}
