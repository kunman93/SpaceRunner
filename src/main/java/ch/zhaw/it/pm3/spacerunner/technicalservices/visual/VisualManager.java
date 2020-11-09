package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisualManager<T extends VisualElement>{
    private VisualUtil visualUtil = VisualUtil.getInstance();

    private int height = 500;
    private int width = 500;
    private static VisualManager instance = new VisualManager<SpaceElement>();
    private Map<Class<T>, Visual> visualList = new HashMap<>();
    private Map<Class<T>, AnimatedVisual> animatedVisualList = new HashMap<>();

    public static VisualManager getInstance(){
        return instance;
    }


    private VisualManager(){

    }

    public int getElementPixelHeight(Class<T> elementClass) throws VisualNotSetException {
        return getImage(elementClass).getHeight();
    }

    public int getElementPixelWidth(Class<T> elementClass) throws VisualNotSetException {
        return getImage(elementClass).getWidth();
    }

    public double getElementRelativeHeight(Class<T> elementClass) throws VisualNotSetException {
        return getVisual(elementClass).getVisualScaling().getScaling();
    }

    public double getElementRelativeWidth(Class<T> elementClass) throws VisualNotSetException {
        return getImage(elementClass).getWidth() / ((double)width);
    }

    public void loadAndSetVisual(Class<T> elementClass, Visual visual){
        BufferedImage image;
        if(visual.getVisualFile() == null){
            //load SVG
            image = getSVGBufferedImage(visual.getVisualSVGFile(), visual.getVisualScaling());
        }else{
            //load image file
            image = getBufferedImage(visual.getVisualFile());
        }

        image = flipVisual(visual.isFlipHorizontally(), visual.isFlipVertically(), image);
        visual.setImage(image);

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


    public void loadAndSetAnimatedVisual(Class<T> elementClass, AnimatedVisual animatedVisual){
        VisualSVGFile[] svgFiles = animatedVisual.getVisualSVGFiles().getAnimationVisuals();

        List<Visual> visuals = new ArrayList<>();
        for(VisualSVGFile svgFile : svgFiles){
            Visual currentVisual = new Visual(svgFile, animatedVisual.getVisualScaling());
            currentVisual.setImage(getSVGBufferedImage(svgFile, animatedVisual.getVisualScaling()));
            visuals.add(currentVisual);
        }
        animatedVisual.setVisuals(visuals.toArray(Visual[]::new));

        animatedVisualList.put(elementClass, animatedVisual);
    }

    public BufferedImage getImage(Class<T> elementClass) throws VisualNotSetException {
        return getVisual(elementClass).getImage();
    }

    private Visual getVisual(Class<T> elementClass) throws VisualNotSetException {
        Visual animatedVisual = getAnimatedVisual(elementClass);
        if(animatedVisual != null){
            return animatedVisual;
        }else{
            Visual visual = visualList.get(elementClass);
            if(visual == null){
                throw new VisualNotSetException("Visual for " + elementClass.toString() + " was not set!");
            }

            return visual;
        }
    }

    private Visual getAnimatedVisual(Class<T> elementClass) {
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
        for (Map.Entry<Class<T>, Visual> classVisualEntry : visualList.entrySet()){
            loadAndSetVisual(classVisualEntry.getKey(), classVisualEntry.getValue());
        }

        for (Map.Entry<Class<T>, AnimatedVisual> classVisualEntry : animatedVisualList.entrySet()){
            loadAndSetAnimatedVisual(classVisualEntry.getKey(), classVisualEntry.getValue());
        }
    }

    public void clear(){
        visualList = new HashMap<>();
        animatedVisualList = new HashMap<>();
    };


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

}
