package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;

import java.awt.image.BufferedImage;

/**
 * This class is used to specify a visual and load it with the visual manager.
 * A Visual which contains the image file, scaling and properties like resize, isBackground... etc.
 */
public class Visual {
    private BufferedImage bufferedImage;
    private VisualSVGFile visualSVGFile;
    private VisualFile visualFile;
    private VisualScaling visualScaling;
    private boolean flipVertically = false;
    private boolean flipHorizontally = false;
    private boolean resize = false;
    private boolean isBackground = false;
    private int resizeWidth;
    private int resizeHeight;


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

    public void setResize(int resizeHeight, int resizeWidth) {
        this.resize = true;
        this.resizeHeight = resizeHeight;
        this.resizeWidth = resizeWidth;
    }

    public void setIsBackground(boolean isBackground) {
        this.resize = isBackground;
        this.isBackground = isBackground;
    }

    public boolean shouldResize() {
        return resize;
    }

    public boolean isBackground() {
        return isBackground;
    }

    public int getResizeWidth() {
        return resizeWidth;
    }

    public int getResizeHeight() {
        return resizeHeight;
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public void setBufferedImage(BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
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
