package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGAnimationFiles;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Container for multiple visuals which represent an animated visual.
 *
 * @author islermic
 */
public class AnimatedVisual {
    private long animationTimeStamp = 0;
    private final AtomicInteger animationPointer = new AtomicInteger(0);
    private final VisualSVGAnimationFiles visualSVGAnimationFiles;
    private Visual[] visuals;
    private VisualScaling visualScaling;


    public AnimatedVisual(VisualSVGAnimationFiles visualSVGAnimationFiles, VisualScaling visualScaling) {
        this.animationPointer.set(0);
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

    /**
     * Gets the current visual depending on the animation pointer.
     *
     * @return the current visual which represents this animation
     */
    public Visual getCurrentVisual() {
        long currentTime = System.currentTimeMillis();

        if (currentTime - animationTimeStamp > visualSVGAnimationFiles.getAnimationStepTime()) {
            if (animationPointer.get() < visuals.length - 1) {
                animationPointer.incrementAndGet();
            } else {
                animationPointer.set(0);
            }
            animationTimeStamp = System.currentTimeMillis();
        }
        return this.visuals[animationPointer.get()];
    }

    public VisualScaling getVisualScaling() {
        return visualScaling;
    }

    public void setVisualScaling(VisualScaling visualScaling) {
        this.visualScaling = visualScaling;
    }
}
