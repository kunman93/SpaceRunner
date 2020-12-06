package ch.zhaw.it.pm3.spacerunner.ui;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.Rocket;
import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.powerup.ShieldPowerUp;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.Visual;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualNotSetException;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualScaling;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;
import javafx.embed.swing.SwingFXUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FXMLImageProxyTest {
    private VisualManager visualManager = VisualManager.getManager();
    private FXMLImageProxy fxmlImageProxy = FXMLImageProxy.getProxy();

    @BeforeEach
    void setUp() {
        visualManager.clear();
        fxmlImageProxy.clear();
    }

    @Test
    void getFXMLImage() throws VisualNotSetException {
        visualManager.loadGameElementVisuals();

        assertEquals(fxmlImageProxy.getFXMLImage(Rocket.class).getHeight(), SwingFXUtils.toFXImage(visualManager.getImage(Rocket.class), null).getHeight());
        assertEquals(fxmlImageProxy.getFXMLImage(Rocket.class).getWidth(), SwingFXUtils.toFXImage(visualManager.getImage(Rocket.class), null).getWidth());
    }

    @Test
    void getFXMLImageWithNull() {
        assertThrows(VisualNotSetException.class, () -> fxmlImageProxy.getFXMLImage(Rocket.class));
    }

    @Test
    void bufferedImageChanged() throws VisualNotSetException {
        visualManager.loadGameElementVisuals();

        assertEquals(fxmlImageProxy.getFXMLImage(Rocket.class).getHeight(), SwingFXUtils.toFXImage(visualManager.getImage(Rocket.class), null).getHeight());
        assertEquals(fxmlImageProxy.getFXMLImage(Rocket.class).getWidth(), SwingFXUtils.toFXImage(visualManager.getImage(Rocket.class), null).getWidth());

        visualManager.loadAndSetVisual(Rocket.class, new Visual(VisualSVGFile.SHIELD_POWER_UP, VisualScaling.POWER_UP));
        fxmlImageProxy.bufferedImageChanged(visualManager.getImage(Rocket.class));

        assertEquals(fxmlImageProxy.getFXMLImage(Rocket.class).getHeight(), SwingFXUtils.toFXImage(visualManager.getImage(ShieldPowerUp.class), null).getHeight());
        assertEquals(fxmlImageProxy.getFXMLImage(Rocket.class).getWidth(), SwingFXUtils.toFXImage(visualManager.getImage(ShieldPowerUp.class), null).getWidth());

    }
}