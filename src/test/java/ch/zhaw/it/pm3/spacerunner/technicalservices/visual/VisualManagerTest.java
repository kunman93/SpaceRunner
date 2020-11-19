package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.Coin;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGAnimationFiles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;

public class VisualManagerTest {
    private VisualManager visualManager = VisualManager.getManager();

    @BeforeEach
    void setUp() {
        visualManager.clear();
    }


    @Test
    void setAndGetElementVisualTest() throws VisualNotSetException {
        Visual myVisual = new Visual(VisualFile.BACKGROUND_STARS);
        visualManager.loadAndSetVisual(Coin.class, myVisual);

        BufferedImage image = visualManager.getImage(Coin.class);

        assertNotNull(image);

    }

    @Test
    void setAndGetElementAnimationVisualTest() throws VisualNotSetException, InterruptedException {
        visualManager.loadAndSetAnimatedVisual(Coin.class, new AnimatedVisual(VisualSVGAnimationFiles.COIN_ANIMATION, VisualScaling.COIN));
        BufferedImage image = visualManager.getImage(Coin.class);
        assertNotNull(image);

        long animationStepTime = VisualSVGAnimationFiles.COIN_ANIMATION.getAnimationStepTime();

        Thread.sleep(animationStepTime + 10);
        BufferedImage image2 = visualManager.getImage(Coin.class);
        assertNotNull(image2);
        assertNotEquals(image, image2);
    }

    @Test
    void getVisualWithSingleVisualAndAnimationTest() throws VisualNotSetException {
        Visual myVisual = new Visual(VisualFile.BACKGROUND_STARS);
        visualManager.loadAndSetVisual(Coin.class, myVisual);
        BufferedImage image = visualManager.getImage(Coin.class);
        assertNotNull(image);

        visualManager.loadAndSetAnimatedVisual(Coin.class, new AnimatedVisual(VisualSVGAnimationFiles.COIN_ANIMATION, VisualScaling.COIN));
        BufferedImage image2 = visualManager.getImage(Coin.class);
        assertNotEquals(image, image2);
    }

    @Test
    void getVisualWhenNotSetTest(){
        assertThrows(VisualNotSetException.class, () ->{
            visualManager.getImage(Coin.class);
        });
    }

}
