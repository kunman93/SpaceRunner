package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import java.awt.image.BufferedImage;

public class AnimatedVisual {
    private long animationTimeStamp = 0;
    private int animationPointer = 0;
    private VisualSVGAnimationFiles visualSVGAnimationFiles;
    private BufferedImage[] visuals;
    private VisualScaling visualScaling;


    //TODO: ThreadSafe animationPointer etc!!!

    public AnimatedVisual(VisualSVGAnimationFiles visualSVGAnimationFiles, VisualScaling visualScaling) {
        this.animationPointer = 0;
        this.visualSVGAnimationFiles = visualSVGAnimationFiles;
        this.visualScaling = visualScaling;
    }

    public VisualSVGAnimationFiles getVisualSVGFiles() {
        return visualSVGAnimationFiles;
    }

    public void setVisuals(BufferedImage[] visuals) {
        this.visuals = visuals;
    }

    public BufferedImage getCurrentVisual(){
        long currentTime = System.currentTimeMillis();

        if(currentTime - animationTimeStamp > visualSVGAnimationFiles.getAnimationStepTime()){
            if(animationPointer < visuals.length - 1){
                animationPointer++;
            }else{
                animationPointer = 0;
            }
            animationTimeStamp = System.currentTimeMillis();
        }

        return this.visuals[animationPointer];
    }

    public VisualScaling getVisualScaling() {
        return visualScaling;
    }

    public void setVisualScaling(VisualScaling visualScaling) {
        this.visualScaling = visualScaling;
    }
}
