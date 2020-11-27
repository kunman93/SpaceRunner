package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util;

/**
 * Used to define Animations.
 * Contains an array of VisualSVGFile for an animation.
 *
 * @author islermic
 */
public enum VisualSVGAnimationFiles {

    COIN_ANIMATION(new VisualSVGFile[]{
            VisualSVGFile.SHINY_COIN_1,
            VisualSVGFile.SHINY_COIN_2,
            VisualSVGFile.SHINY_COIN_3,
            VisualSVGFile.SHINY_COIN_4,
            VisualSVGFile.SHINY_COIN_5,
            VisualSVGFile.SHINY_COIN_6}, 125);

    private final VisualSVGFile[] animationVisuals;
    private final int animationStepTime;

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
