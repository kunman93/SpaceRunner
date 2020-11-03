package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

public enum VisualSVGAnimationFiles {

    COIN_ANIMATION(new VisualSVGFile[]{
            VisualSVGFile.SHINEY_COIN_1,
            VisualSVGFile.SHINEY_COIN_2,
            VisualSVGFile.SHINEY_COIN_3,
            VisualSVGFile.SHINEY_COIN_4,
            VisualSVGFile.SHINEY_COIN_5,
            VisualSVGFile.SHINEY_COIN_6});


    private VisualSVGFile[] animationVisuals;

    VisualSVGAnimationFiles(VisualSVGFile[] animationVisuals) {
        this.animationVisuals = animationVisuals;
    }

    public VisualSVGFile[] getAnimationVisuals() {
        return animationVisuals;
    }
}
