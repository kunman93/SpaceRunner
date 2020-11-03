package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import java.awt.image.BufferedImage;

public class AnimatedVisual {
    private long animationTimeStamp = 0;
    private int animationStepTime = 250;
    private int animationPointer = 0;
    private VisualSVGFile[] visualSVGFiles;
    private BufferedImage[] visuals;

    public AnimatedVisual(int animationStepTime, VisualSVGFile[] visualSVGFiles) {
        this.animationStepTime = animationStepTime;
        this.animationPointer = 0;
        this.visualSVGFiles = visualSVGFiles;
    }

    public VisualSVGFile[] getVisualSVGFiles() {
        return visualSVGFiles;
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
