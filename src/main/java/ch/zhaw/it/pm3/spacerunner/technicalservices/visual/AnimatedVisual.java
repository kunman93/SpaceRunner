package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import java.awt.image.BufferedImage;

public class AnimatedVisual {
    private long animationTimeStamp = 0;
    private int animationStepTime = 250;
    private int animationPointer = 0;
    private VisualSVGAnimationFiles visualSVGAnimationFiles;
    private BufferedImage[] visuals;


    //TODO: ThreadSafe animationPointer etc!!!

    public AnimatedVisual(int animationStepTime, VisualSVGAnimationFiles visualSVGAnimationFiles) {
        this.animationStepTime = animationStepTime;
        this.animationPointer = 0;
        this.visualSVGAnimationFiles = visualSVGAnimationFiles;
    }

    public VisualSVGAnimationFiles getVisualSVGFiles() {
        return visualSVGAnimationFiles;
    }

    public void setVisuals(BufferedImage[] visuals) {
        this.visuals = visuals;
    }

    public BufferedImage getCurrentVisual(){
        long currentTime = System.currentTimeMillis();

        if(currentTime - animationTimeStamp > animationStepTime){
            if(animationPointer < visuals.length - 1){
                animationPointer++;
            }else{
                animationPointer = 0;
            }
            animationTimeStamp = System.currentTimeMillis();
        }

        return this.visuals[animationPointer];
    }
}
