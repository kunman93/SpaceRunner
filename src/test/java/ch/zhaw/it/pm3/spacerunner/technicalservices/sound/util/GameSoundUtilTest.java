package ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util;

import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameSoundUtilTest {
    private final GameSoundUtil gameSoundUtil = GameSoundUtil.getUtil();

    /**
     * Tests if a valid file can be loaded.
     */
    @Test
    void loadValidClipTest() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        SoundClip soundClip = gameSoundUtil.loadClip(GameSound.POWER_UP_PICKUP);
        assertNotNull(soundClip);
    }

}
