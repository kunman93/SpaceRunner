package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import java.awt.image.BufferedImage;

public class AnimatedVisual {
    private long animationTimeStamp = 0;
    private int animationStepTime = 250;
    private int animationPointer = 0;
    private BufferedImage[] visuals;

    public AnimatedVisual(int animationStepTime, BufferedImage[] visuals) {
        this.animationStepTime = animationStepTime;
        this.animationPointer = 0;
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
