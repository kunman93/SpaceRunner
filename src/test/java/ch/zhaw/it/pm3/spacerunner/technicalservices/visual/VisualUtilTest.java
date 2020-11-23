package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class VisualUtilTest {
    private final VisualUtil visualUtil = VisualUtil.getUtil();

    @BeforeEach
    void setUp() {

    }

    @Test
    void rotateImageTestWithNullAsImage() {
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.rotateImage(null, 12);
        });
    }

    @Test
    void rotateImageTest() throws MalformedURLException {
        BufferedImage backgroundImage = visualUtil.loadImage(Paths.get("src/test/resources/ch/zhaw/it/pm3/spacerunner/image/background.jpg").toUri().toURL());
        assertNotNull(visualUtil.rotateImage(backgroundImage, 12));
        assertNotNull(visualUtil.rotateImage(backgroundImage, -12));
    }

    @Test
    void flipImageTestWithNullAsImage() {
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.flipImage(null, true);
        });
    }

    @Test
    void flipImageTest() throws MalformedURLException {
        BufferedImage backgroundImage = visualUtil.loadImage(Paths.get("src/test/resources/ch/zhaw/it/pm3/spacerunner/image/background.jpg").toUri().toURL());
        assertNotNull(visualUtil.flipImage(backgroundImage, true));
        assertNotNull(visualUtil.flipImage(backgroundImage, false));
    }

    @Test
    void loadSVGImageTestWithNegativeOrZeroHeight() {
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.loadSVGImage(Paths.get("src/test/resources/ch/zhaw/it/pm3/spacerunner/image/rocket.svg").toUri().toURL(), -500);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.loadSVGImage(Paths.get("src/test/resources/ch/zhaw/it/pm3/spacerunner/image/rocket.svg").toUri().toURL(), 0);
        });
    }

    @Test
    void loadSVGImageTestWithNullAsURL() throws MalformedURLException {
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.loadSVGImage(null, 500);
        });
    }

    @Test
    void loadSVGImageTest() throws MalformedURLException {
        assertNotNull(visualUtil.loadSVGImage(Paths.get("src/test/resources/ch/zhaw/it/pm3/spacerunner/image/rocket.svg").toUri().toURL(), 500));
    }

    @Test
    void resizeImageTestWithNegativeOrZeroScaledSizes() throws MalformedURLException {
        BufferedImage backgroundImage = visualUtil.loadImage(Paths.get("src/test/resources/ch/zhaw/it/pm3/spacerunner/image/background.jpg").toUri().toURL());
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.resizeImage(backgroundImage, -12,12);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.resizeImage(backgroundImage, 0,12);
        });

        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.resizeImage(backgroundImage, 12,-12);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.resizeImage(backgroundImage, 12,0);
        });
    }

    @Test
    void resizeImageTestWithNullAsImage(){
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.resizeImage(null, 12,12);
        });
    }

    @Test
    void resizeImageTest() throws MalformedURLException {
        BufferedImage backgroundImage = visualUtil.loadImage(Paths.get("src/test/resources/ch/zhaw/it/pm3/spacerunner/image/background.jpg").toUri().toURL());
        assertNotNull(visualUtil.resizeImage(backgroundImage, 12,12));
    }

    @Test
    void generateBackgroundTestWithNegativeScaledSizes() throws MalformedURLException {
        BufferedImage backgroundImage = visualUtil.loadImage(Paths.get("src/test/resources/ch/zhaw/it/pm3/spacerunner/image/background.jpg").toUri().toURL());
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.generateBackground(backgroundImage, -12,12);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.generateBackground(backgroundImage, 0,12);
        });

        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.generateBackground(backgroundImage, 12,-12);
        });
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.generateBackground(backgroundImage, 12,0);
        });
    }

    @Test
    void generateBackgroundTestWithNullAsImage(){
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.generateBackground(null, 12,12);
        });
    }

    @Test
    void generateBackgroundTest() throws MalformedURLException {
        BufferedImage backgroundImage = visualUtil.loadImage(Paths.get("src/test/resources/ch/zhaw/it/pm3/spacerunner/image/background.jpg").toUri().toURL());
        assertNotNull(visualUtil.generateBackground(backgroundImage, 12,12));
    }

    @Test
    void testLoadImage() throws MalformedURLException {
        assertNotNull(visualUtil.loadImage(Paths.get("src/test/resources/ch/zhaw/it/pm3/spacerunner/image/background.jpg").toUri().toURL()));
    }

    @Test
    void testLoadImageWithNull() {
        assertThrows(IllegalArgumentException.class, ()->{
            visualUtil.loadImage(null);
        });
    }

}
