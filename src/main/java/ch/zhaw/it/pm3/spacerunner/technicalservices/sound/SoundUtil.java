package ch.zhaw.it.pm3.spacerunner.technicalservices.sound;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PersistenceUtil;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundUtil {
    // Singleton pattern
    private static final SoundUtil instance = new SoundUtil();

    /**
     * private constructor for the singleton-pattern
     */
    private SoundUtil(){}

    public static SoundUtil getInstance(){
        return instance;
    }

    //TODO: Copied from old project...

    /**
     * Loads a SoundClip from a File
     *
     * @param audioFile File to be loaded from
     * @return SoundClip that was loaded
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     */
    public SoundClip loadClip(File audioFile) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        if (audioFile == null) {
            throw new IllegalArgumentException();
        }

        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile.getAbsoluteFile());
        return getSoundClip(audioInputStream);
    }

    private SoundClip getSoundClip(AudioInputStream audioInputStream) throws LineUnavailableException, IOException {
        Clip loadedClip;

        loadedClip = AudioSystem.getClip();
        loadedClip.open(audioInputStream);

        return new SoundClip(loadedClip);
    }


}
