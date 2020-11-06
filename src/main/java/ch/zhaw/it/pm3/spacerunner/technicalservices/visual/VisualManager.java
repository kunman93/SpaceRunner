package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualManager<T extends VisualElement>{
    private VisualUtil visualUtil = VisualUtil.getInstance();

    private int height = 500;
    private static VisualManager instance = new VisualManager();
    private Map<Class<T>, Visual> visualList = new HashMap<>();
    private Map<Class<T>, AnimatedVisual> animatedVisualList = new HashMap<>();

    private VisualManager(){

    }

    public int getElementHeight(Class<T> elementClass) throws VisualNotSetException {
        return getVisual(elementClass).getHeight();
    }

    public int getElementWidth(Class<T> elementClass) throws VisualNotSetException {
        return getVisual(elementClass).getWidth();
    }

    public void flipAndSetVisual(Class<T> elementClass, VisualSVGFile imagePath, VisualScaling visualScaling, boolean flipHorizontally, boolean flipVertically){
        BufferedImage image = getSVGBufferedImage(imagePath, visualScaling);
        image = flipVisual(flipHorizontally, flipVertically, image);

        Visual visual = new Visual(image, imagePath);
        visualList.put(elementClass, visual);
    }

    public void flipAndSetVisual(Class<T> elementClass, VisualFile imagePath, boolean flipHorizontally, boolean flipVertically){
        BufferedImage image = getBufferedImage(imagePath);
        image = flipVisual(flipHorizontally, flipVertically, image);


        Visual visual = new Visual(image, imagePath);
        visualList.put(elementClass, visual);
    }

    public void setVisual(Class<T> elementClass, VisualFile imagePath){
        BufferedImage image = getBufferedImage(imagePath);
        Visual visual = new Visual(image, imagePath);
        visualList.put(elementClass, visual);
    }

    public void setVisual(Class<T> elementClass, VisualSVGFile imagePath, VisualScaling visualScaling){
        BufferedImage image = getSVGBufferedImage(imagePath, visualScaling);
        Visual visual = new Visual(image, imagePath);
        visualList.put(elementClass, visual);
    }

    private BufferedImage flipVisual(boolean flipHorizontally, boolean flipVertically, BufferedImage image) {
        if (flipHorizontally) {
            image = visualUtil.flipImage(image, true);
        }
        if (flipVertically) {
            image = visualUtil.flipImage(image, false);
        }
        return image;
    }

    private BufferedImage getSVGBufferedImage(VisualSVGFile imagePath, VisualScaling visualScaling) {
        URL imageURL = SpaceRunnerApp.class.getResource(imagePath.getFileName());
        return visualUtil.loadSVGImage(imageURL, (float) (height * visualScaling.getScaling()));
    }

    private BufferedImage getBufferedImage(VisualFile imagePath) {
        URL imageURL = SpaceRunnerApp.class.getResource(imagePath.getFileName());
        return visualUtil.loadImage(imageURL);
    }


    public void setAnimatedVisual(Class<T> elementClass, AnimatedVisual animatedVisual, VisualScaling visualScaling){
        VisualSVGFile[] svgFiles = animatedVisual.getVisualSVGFiles().getAnimationVisuals();

        List<BufferedImage> visuals = new ArrayList<>();
        for(VisualSVGFile svgFile : svgFiles){
            visuals.add(getSVGBufferedImage(svgFile, visualScaling));
        }
        animatedVisual.setVisuals(visuals.toArray(BufferedImage[]::new));

        animatedVisualList.put(elementClass, animatedVisual);
    }

    public BufferedImage getVisual(Class<T> elementClass) throws VisualNotSetException {
        BufferedImage animatedVisual = getAnimatedVisual(elementClass);
        if(animatedVisual != null){
            return animatedVisual;
        }else{
            Visual visual = visualList.get(elementClass);
            if(visual == null){
                throw new VisualNotSetException("Visual for " + elementClass.toString() + " was not set!");
            }

            return visual.getImage();
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

    public static VisualManager getInstance(){
        return instance;
    }

}
