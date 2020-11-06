package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import java.awt.image.BufferedImage;

public class Visual{
    private BufferedImage image;
    private VisualSVGFile visualSVGFile;
    private VisualFile visualFile;
    private VisualScaling visualScaling;
    private boolean flipVertically = false;
    private boolean flipHorizontally = false;


    public Visual(VisualFile visualFile, boolean flipHorizontally, boolean flipVertically) {
        this.visualFile = visualFile;
        this.flipVertically = flipVertically;
        this.flipHorizontally = flipHorizontally;
    }

    public Visual(VisualFile visualFile) {
        this.visualFile = visualFile;
    }

    public Visual(VisualSVGFile visualSVGFile, VisualScaling visualScaling, boolean flipHorizontally, boolean flipVertically) {
        this.visualSVGFile = visualSVGFile;
        this.visualScaling = visualScaling;
        this.flipVertically = flipVertically;
        this.flipHorizontally = flipHorizontally;
    }

    public Visual(VisualSVGFile visualSVGFile, VisualScaling visualScaling) {
        this.visualSVGFile = visualSVGFile;
        this.visualScaling = visualScaling;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public void setVisualFile(VisualSVGFile visualFile) {
        this.visualSVGFile = visualFile;
        this.visualFile = null;

    }

    public void setVisualFile(VisualFile visualFile) {
        this.visualFile = visualFile;
        this.visualSVGFile = null;
    }


    public VisualSVGFile getVisualSVGFile() {
        return visualSVGFile;
    }

    public VisualFile getVisualFile() {
        return visualFile;
    }

    public VisualScaling getVisualScaling() {
        return visualScaling;
    }

    public void setVisualScaling(VisualScaling visualScaling) {
        this.visualScaling = visualScaling;
    }

    public boolean isFlipVertically() {
        return flipVertically;
    }

    public void setFlipVertically(boolean flipVertically) {
        this.flipVertically = flipVertically;
    }

    public boolean isFlipHorizontally() {
        return flipHorizontally;
    }

    public void setFlipHorizontally(boolean flipHorizontally) {
        this.flipHorizontally = flipHorizontally;
    }
}
