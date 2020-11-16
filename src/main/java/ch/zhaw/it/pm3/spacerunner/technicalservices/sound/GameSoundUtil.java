package ch.zhaw.it.pm3.spacerunner.technicalservices.sound;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.net.URL;

public class GameSoundUtil{

    private SoundUtil soundUtil = SoundUtil.getInstance();
    private static GameSoundUtil instance = new GameSoundUtil();

    private GameSoundUtil(){

    }

    public static GameSoundUtil getUtil() {
        return instance;
    }

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
