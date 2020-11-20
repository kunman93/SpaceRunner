package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGAnimationFiles;

public class AnimatedVisual {
    private long animationTimeStamp = 0;
    private int animationPointer = 0;
    private VisualSVGAnimationFiles visualSVGAnimationFiles;
    private Visual[] visuals;
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

    public Visual[] getVisuals() {
        return visuals;
    }

    public void setVisuals(Visual[] visuals) {
        this.visuals = visuals;
    }

    public Visual getCurrentVisual() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - animationTimeStamp > visualSVGAnimationFiles.getAnimationStepTime()) {
            if (animationPointer < visuals.length - 1) {
                animationPointer++;
            } else {
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
