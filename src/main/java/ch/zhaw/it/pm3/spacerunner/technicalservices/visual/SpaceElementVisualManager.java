package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.SpaceElement;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.VisualNotSetException;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class SpaceElementVisualManager <T extends SpaceElement>{

    private static SpaceElementVisualManager instance = new SpaceElementVisualManager();
    private Map<Class<T>, BufferedImage> visualList = new HashMap<>();
    private Map<Class<T>, AnimatedVisual> animatedVisualList = new HashMap<>();

    private SpaceElementVisualManager(){

    }

    public void setVisual(Class<T> elementClass, BufferedImage image){
        visualList.put(elementClass, image);
    }

    public void setAnimatedVisual(Class<T> elementClass, AnimatedVisual animatedVisual){
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



    public static SpaceElementVisualManager getInstance(){
        return instance;
    }

}
