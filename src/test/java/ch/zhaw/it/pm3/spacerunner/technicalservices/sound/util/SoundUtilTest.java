package ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests The SoundUtil-Class.
 *
 * @author islermic (copied from old project and extended)
 */
class SoundUtilTest {
    private SoundUtil soundUtil = SoundUtil.getUtil();
    private File file;
    private SoundClip soundClip;

    @BeforeEach
    void setUp() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        file = new File("src/test/resources/ch/zhaw/it/pm3/spacerunner/sound/background.wav");
        soundClip = soundUtil.loadClip(file);
    }


    /**
     * Tests if a valid file can be loaded.
     *
     * @throws UnsupportedAudioFileException
     * @throws IOException
     * @throws LineUnavailableException
     */
    @Test
    void loadValidClipTest() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        file = new File("src/test/resources/ch/zhaw/it/pm3/spacerunner/sound/background.wav");
        soundClip = soundUtil.loadClip(file);
        assertNotNull(soundClip);
    }

    /**
     * Tests if an invalid file can be loaded. Expects an UnsupportedAudioFileException.
     */
    @Test
    void loadInvalidClipTest() {
        file = new File("src/test/resources/ch/zhaw/it/pm3/spacerunner/sound/unsupportedAudioFile.txt");
        assertThrows(UnsupportedAudioFileException.class, () -> soundUtil.loadClip(file));
    }

    /**
     * Tests if SoundUtil.loadClip can handle fictional paths.
     */
    @Test
    void loadClipFromInvalidPathTest() {
        assertThrows(IOException.class, () -> soundUtil.loadClip(new File("src/fictionalPath/fiction.wav")));
    }


}