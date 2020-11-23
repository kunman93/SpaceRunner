package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util;

/**
 * Used to define Animations.
 * Contains an array of VisualSVGFile for an animation.
 *
 * @author islermic
 */
public enum VisualSVGAnimationFiles {

    COIN_ANIMATION(new VisualSVGFile[]{
            VisualSVGFile.SHINEY_COIN_1,
            VisualSVGFile.SHINEY_COIN_2,
            VisualSVGFile.SHINEY_COIN_3,
            VisualSVGFile.SHINEY_COIN_4,
            VisualSVGFile.SHINEY_COIN_5,
            VisualSVGFile.SHINEY_COIN_6}, 125);

    private VisualSVGFile[] animationVisuals;
    private int animationStepTime;

    VisualSVGAnimationFiles(VisualSVGFile[] animationVisuals, int animationStepTime) {
        this.animationVisuals = animationVisuals;
        this.animationStepTime = animationStepTime;
    }

    public VisualSVGFile[] getAnimationVisuals() {
        return animationVisuals;
    }

    public int getAnimationStepTime() {
        return animationStepTime;
    }
}
