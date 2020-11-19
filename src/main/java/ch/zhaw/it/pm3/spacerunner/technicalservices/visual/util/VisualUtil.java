package ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.UFO;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.SVGConstants;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VisualUtil {

    private Logger logger = Logger.getLogger(VisualUtil.class.getName());

    // Singleton pattern
    private static final VisualUtil instance = new VisualUtil();

    /**
     * private constructor for the singleton-pattern
     */
    private VisualUtil(){}

    public static VisualUtil getInstance(){
        return instance;
    }

    /**
     * Loads the image from the URL provided
     *
     * @param imageURL URL of the image to load
     * @return loaded image
     */
    /**
     * Loads the image from the URL provided
     *
     * @param imageURL URL of the image to load
     * @return loaded image
     */
    public BufferedImage loadImage(URL imageURL) {
        Image image = new ImageIcon(imageURL).getImage();
        return toBufferedImage(image);
    }


    public BufferedImage generateBackground(BufferedImage inputImage,  int scaledWidth, int scaledHeight){
        BufferedImage outputImage = new BufferedImage(scaledWidth * 3, scaledHeight, inputImage.getType());

        // creates output image
        BufferedImage resizedImage = resizeImage(inputImage, scaledWidth, scaledHeight);
        BufferedImage mirrorImage = flipImage(inputImage, true);

        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(resizedImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.drawImage(mirrorImage, scaledWidth, 0, scaledWidth, scaledHeight, null);
        g2d.drawImage(resizedImage, scaledWidth * 2, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return outputImage;
    }

    /**
     * This will resize the inputImage and return the resized image
     * @param inputImage image to resize
     * @param scaledWidth width for resized image
     * @param scaledHeight height for resized image
     * @return resized image
     */
    public BufferedImage resizeImage(BufferedImage inputImage, int scaledWidth, int scaledHeight){

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth, scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return outputImage;
    }

    //TODO: Declare as copied from internet. (Code is from stackoverflow https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage)
    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    private BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    /**
     * Loads the SVG image from the URL provided
     *
     * @param imageURL URL of the image to load
     * @return loaded image
     */
    public BufferedImage loadSVGImage(URL imageURL, float height) {
        BufferedImage loadedImage = null;
        try {
            loadedImage = rasterize(new File(imageURL.getFile().replace("%20", " ")), height);
        } catch (IOException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE, "Error Rasterizing File");
            return null;
        }
        return loadedImage;
    }


    //TODO: https://stackoverflow.com/questions/11435671/how-to-get-a-bufferedimage-from-a-svg
    private BufferedImage rasterize(File svgFile, float height) throws IOException {

        final BufferedImage[] imagePointer = new BufferedImage[1];

        // Rendering hints can't be set programatically, so
        // we override defaults with a temporary stylesheet.
        // These defaults emphasize quality and precision, and
        // are more similar to the defaults of other SVG viewers.
        // SVG documents can still override these defaults.
        String css = "svg {" +
                "shape-rendering: geometricPrecision;" +
                "text-rendering:  geometricPrecision;" +
                "color-rendering: optimizeQuality;" +
                "image-rendering: optimizeQuality;" +
                "}";
        File cssFile = File.createTempFile("batik-default-override-", ".css");
        FileUtils.writeStringToFile(cssFile, css);

        TranscodingHints transcoderHints = new TranscodingHints();
        transcoderHints.put(ImageTranscoder.KEY_XML_PARSER_VALIDATING, Boolean.FALSE);
        transcoderHints.put(ImageTranscoder.KEY_DOM_IMPLEMENTATION,
                SVGDOMImplementation.getDOMImplementation());
        transcoderHints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT_NAMESPACE_URI,
                SVGConstants.SVG_NAMESPACE_URI);
        transcoderHints.put(ImageTranscoder.KEY_DOCUMENT_ELEMENT, "svg");
        transcoderHints.put(ImageTranscoder.KEY_USER_STYLESHEET_URI, cssFile.toURI().toString());
        transcoderHints.put(ImageTranscoder.KEY_HEIGHT, height);

        try {

            TranscoderInput input = new TranscoderInput(new FileInputStream(svgFile));

            ImageTranscoder t = new ImageTranscoder() {

                @Override
                public BufferedImage createImage(int w, int h) {
                    return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                }

                @Override
                public void writeImage(BufferedImage image, TranscoderOutput out)
                        throws TranscoderException {
                    imagePointer[0] = image;
                }
            };
            t.setTranscodingHints(transcoderHints);
            t.transcode(input, null);
        }
        catch (TranscoderException ex) {
            logger.log(Level.SEVERE, "Couldn't convert {0}", svgFile);
            // Requires Java 6
            ex.printStackTrace();
            throw new IOException("Couldn't convert " + svgFile);
        }
        finally {
            cssFile.delete();
        }

        return imagePointer[0];
    }

    public BufferedImage flipImage(BufferedImage image, boolean horizontal){
        // Flip the image horizontally
        AffineTransform tx;
        if(horizontal){
            tx = AffineTransform.getScaleInstance(-1, 1);
            tx.translate(-image.getWidth(null), 0);
        }else{
            tx = AffineTransform.getScaleInstance(1, -1);
            tx.translate(0, -image.getHeight(null));

        }
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        image = op.filter(image, null);
        return image;
    }

    public BufferedImage rotateImage(BufferedImage bufferedImage, int deg) {
        BufferedImage image = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());
        AffineTransform trans = AffineTransform.getRotateInstance(deg, bufferedImage.getWidth() / 2, bufferedImage.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(trans, AffineTransformOp.TYPE_BILINEAR);
        op.filter(bufferedImage, image);
        return image;
    }
}
