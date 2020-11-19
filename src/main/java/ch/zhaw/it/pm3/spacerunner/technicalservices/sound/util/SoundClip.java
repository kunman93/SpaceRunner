package ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util;

import ch.zhaw.it.pm3.spacerunner.model.spaceelement.UFO;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SoundClip Object that offers methods related to handling playback.
 */
public class SoundClip {

    private Logger logger = Logger.getLogger(SoundClip.class.getName());

    private final Clip clip;

    private Long pausedFramePosition = null;
    private boolean loopClip = false;

    private PlayStates state = PlayStates.READY_TO_PLAY;

    private final Set<SoundClipListener> soundClipEventListeners = new HashSet<>();


    public SoundClip(Clip clip) {
        this.clip = clip;
        clip.addLineListener(this::clipLineListener);
    }

    /**
     * Sets the volume of the SoundClip.
     * @param volume desired volume from 0 - 100
     */
    public void setVolume(int volume){
        if(volume < 0 || volume > 100){
            logger.log(Level.WARNING, "Invalid volume. Volume must me between 0 - 100");
            throw new IllegalArgumentException("Invalid volume. Volume must me between 0 - 100");
        }

        float f_volume = volume/100f;

        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainControl.getMaximum() - gainControl.getMinimum();
        float gain = (range * f_volume) + gainControl.getMinimum();

        gainControl.setValue(gain);
    }

    public void setLoop(boolean enableLoop) {
        loopClip = enableLoop;
    }

    public boolean isPlaying() {
        return state == PlayStates.PLAYING;
    }

    public Long getPausedFramePosition() {
        return pausedFramePosition;
    }

    /**
     * Pauses current Clip.
     *
     * @return true if paused, false if not possible or not currently playing
     */
    public boolean pause() {
        if (loopClip) {
            return false;
        }

        if (state == PlayStates.PLAYING) {
            pausedFramePosition = clip.getMicrosecondPosition();
            clip.stop();

            this.state = PlayStates.PAUSED;
            return true;
        }

        return false;
    }

    /**
     * Sets up all clip data for playing, setting up a paused clip sets playback position where it was paused.
     */
    public void play() {
        if (state == PlayStates.PAUSED) {
            playPausedClip();
        } else {
            playClipFromStart();
        }

        state = PlayStates.PLAYING;
    }

    private void playClipFromStart() {
        pausedFramePosition = null;
        clip.setMicrosecondPosition(0);

        playClip();
    }

    private void playPausedClip() {
        clip.setMicrosecondPosition(pausedFramePosition);

        pausedFramePosition = null;
        playClip();
    }

    /**
     * Actually starts playing the clip.
     */
    private void playClip() {
        if (loopClip) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.start();
        }
    }

    /**
     * Stops the currently playing clip.
     *
     * @return true if clip was stopped, false if clip was not stopped cause it was not currently playing
     */
    public boolean stop() {
        if (state == PlayStates.PLAYING) {
            clip.stop();

            state = PlayStates.READY_TO_PLAY;
            pausedFramePosition = null;
            return true;
        }
        pausedFramePosition = null;
        return false;
    }


    public void addListener(SoundClipListener soundClipListener) {
        soundClipEventListeners.add(soundClipListener);
    }

    public void removeListener(SoundClipListener soundClipListener) {
        soundClipEventListeners.remove(soundClipListener);
    }

    private void clipLineListener(LineEvent e) {
        if (state == PlayStates.PLAYING) {
            if (e.getType() == LineEvent.Type.STOP) {
                for (SoundClipListener listener : soundClipEventListeners) {
                    listener.stoppedPlayback();
                }
                state = PlayStates.READY_TO_PLAY;
            }
        }
    }
}