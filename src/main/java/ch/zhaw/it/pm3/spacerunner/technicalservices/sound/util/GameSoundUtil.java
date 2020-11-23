package ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * Project specific wrapper for the SoundUtil
 * @author islermic
 */
public class GameSoundUtil {

    private final SoundUtil soundUtil = SoundUtil.getUtil();
    private final static GameSoundUtil GAME_SOUND_UTIL = new GameSoundUtil();

    private GameSoundUtil() {
    }

    public static GameSoundUtil getUtil() {
        return GAME_SOUND_UTIL;
    }

    /**
     * Loads a SoundClip from the specified enum value
     *
     * @param soundFile Sound to load
     * @return SoundClip that was loaded
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     */
    public SoundClip loadClip(GameSound soundFile) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        URL backgroundMusicURL = SpaceRunnerApp.class.getResource(soundFile.getFileName());
        File audioFile = new File(backgroundMusicURL.getPath().replace("%20", " "));

        return soundUtil.loadClip(audioFile);
    }

    public int getVolume() {
        return soundUtil.getVolume();
    }

    public void setVolume(int volume) {
        soundUtil.setVolume(volume);
    }
}
