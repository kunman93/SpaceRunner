package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import java.awt.image.BufferedImage;

public class Visual{
    private BufferedImage image;
    private VisualSVGFile visualSVGFile;
    private VisualFile visualFile;

    public Visual(BufferedImage image, VisualFile visualFile) {
        this.image = image;
        this.visualFile = visualFile;
    }

    public Visual(BufferedImage image, VisualSVGFile visualSVGFile) {
        this.image = image;
        this.visualSVGFile = visualSVGFile;
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

}
