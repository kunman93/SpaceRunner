package ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.UFO;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

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


    private int volume = 100;

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
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

        SoundClip soundClip = getSoundClip(audioInputStream);
        soundClip.setVolume(volume);
        return soundClip;
    }

    private SoundClip getSoundClip(AudioInputStream audioInputStream) throws LineUnavailableException, IOException {
        Clip loadedClip;

        loadedClip = AudioSystem.getClip();
        loadedClip.open(audioInputStream);

        return new SoundClip(loadedClip);
    }


}
