package ch.zhaw.it.pm3.spacerunner.domain.preset;

import ch.zhaw.it.pm3.spacerunner.domain.spaceelement.velocity.VelocityManager;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.manager.VisualManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RandomPresetGeneratorTest {
    RandomPresetGenerator presetGenerator = new RandomPresetGenerator();
    private final VisualManager visualManager = VisualManager.getManager();
    private final VelocityManager velocityManager = VelocityManager.getManager();

    /**
     * Sets Up the necessary visuals of the SpaceElements.
     */
    @BeforeEach
    void setUp() {
        visualManager.loadGameElementVisuals();
        velocityManager.setupGameElementVelocity();
    }

    /**
     * Tests that getRandomPreset returns a non-null Preset.
     */
    @Test
    void getRandomPresetNotNull() {
        for(int i = 0; i < 100; i++) {
            assertNotEquals(presetGenerator.getRandomPreset(), null);
            assertEquals(presetGenerator.getRandomPreset().getClass(), Preset.class);
        }
    }

    /**
     * Tests that getRandomPreset always returns a new Preset that isn't equal to any previous Presets.
     */
    @Test
    void getRandomPresetNotEqual() {
        List<Preset> presetList = new ArrayList();
        for(int i = 0; i < 100; i++) {
            Preset newPreset = presetGenerator.getRandomPreset();
            boolean isNotEqual = true;
            for(Preset p : presetList) {
                if (p.equals(newPreset)) {
                    isNotEqual = false;
                    break;
                }
            }
            if(isNotEqual) {
                presetList.add(newPreset);
            }
        }
        assertEquals(100, presetList.size());
    }
}
