package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.ElementScaling;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.VisualNotSetException;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceElementVisualManager <T extends SpaceElement>{

    private int height = 500;
    private static SpaceElementVisualManager instance = new SpaceElementVisualManager();
    private Map<Class<T>, BufferedImage> visualList = new HashMap<>();
    private Map<Class<T>, AnimatedVisual> animatedVisualList = new HashMap<>();

    private SpaceElementVisualManager(){

    }

    public void flipAndSetVisual(Class<T> elementClass, VisualSVGFile imagePath, ElementScaling elementScaling, boolean flipHorizontally, boolean flipVertically){
        BufferedImage image = getSVGBufferedImage(imagePath, elementScaling);
        image = flipVisual(flipHorizontally, flipVertically, image);
        visualList.put(elementClass, image);
    }

    public void flipAndSetVisual(Class<T> elementClass, VisualFile imagePath, boolean flipHorizontally, boolean flipVertically){
        BufferedImage image = getBufferedImage(imagePath);
        image = flipVisual(flipHorizontally, flipVertically, image);
        visualList.put(elementClass, image);
    }

    public void setVisual(Class<T> elementClass, VisualFile imagePath){
        BufferedImage image = getBufferedImage(imagePath);
        visualList.put(elementClass, image);
    }

    public void setVisual(Class<T> elementClass, VisualSVGFile imagePath, ElementScaling elementScaling){
        BufferedImage image = getSVGBufferedImage(imagePath, elementScaling);
        visualList.put(elementClass, image);
    }

    private BufferedImage flipVisual(boolean flipHorizontally, boolean flipVertically, BufferedImage image) {
        if (flipHorizontally) {
            image = VisualUtil.flipImage(image, true);
        }
        if (flipVertically) {
            image = VisualUtil.flipImage(image, false);
        }
        return image;
    }

    private BufferedImage getSVGBufferedImage(VisualSVGFile imagePath, ElementScaling elementScaling) {
        URL imageURL = SpaceRunnerApp.class.getResource(imagePath.getFileName());
        return VisualUtil.loadSVGImage(imageURL, (float) (height * elementScaling.getScaling()));
    }

    private BufferedImage getBufferedImage(VisualFile imagePath) {
        URL imageURL = SpaceRunnerApp.class.getResource(imagePath.getFileName());
        return VisualUtil.loadImage(imageURL);
    }


    public void setAnimatedVisual(Class<T> elementClass, AnimatedVisual animatedVisual, ElementScaling elementScaling){
        VisualSVGFile[] svgFiles = animatedVisual.getVisualSVGFiles();

        List<BufferedImage> visuals = new ArrayList<>();
        for(VisualSVGFile svgFile : svgFiles){
            visuals.add(getSVGBufferedImage(svgFile, elementScaling));
        }
        animatedVisual.setVisuals(visuals.toArray(BufferedImage[]::new));

        animatedVisualList.put(elementClass, animatedVisual);
    }

    public BufferedImage getVisual(Class<T> elementClass) throws VisualNotSetException {
        BufferedImage animatedVisual = getAnimatedVisual(elementClass);
        if(animatedVisual != null){
            return animatedVisual;
        }else{
            BufferedImage image = visualList.get(elementClass);
            if(image == null){
                throw new VisualNotSetException("Visual for " + elementClass.toString() + " was not set!");
            }

            return image;
        }
    }

    private BufferedImage getAnimatedVisual(Class<T> elementClass) {
        AnimatedVisual visualsForAnimation = animatedVisualList.get(elementClass);

        if(visualsForAnimation == null){
            return null;
        }else{
            return visualsForAnimation.getCurrentVisual();
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public static SpaceElementVisualManager getInstance(){
        return instance;
    }

}
