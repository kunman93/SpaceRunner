package ch.zhaw.it.pm3.spacerunner.technicalservices.performance;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class will track the Frames per Second. Call the track method every time you process a frame.
 *
 * @author islermic
 */
public class FPSTracker {

    private Logger logger = Logger.getLogger(FPSTracker.class.getName());

    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0;
    private boolean arrayFilled = false;
    private long lastTimeFPSLogged = System.currentTimeMillis();

    /**
     * Tracks the process of a frame. Once the internal buffer (100) is filled, it will start to log the current FPS once per second.
     * @param currentNanoTime timestamp in nanoseconds, when this function has been called.
     */
    public void track(long currentNanoTime) {
        long oldFrameTime = frameTimes[frameTimeIndex];
        frameTimes[frameTimeIndex] = currentNanoTime;
        frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
        if (frameTimeIndex == 0) {
            arrayFilled = true;
        }
        if (arrayFilled && (System.currentTimeMillis() - lastTimeFPSLogged > 1000)) {
            long elapsedNanos = currentNanoTime - oldFrameTime;
            long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
            double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
            logger.log(Level.INFO, String.format("Current frame rate: %.3f", frameRate));
            lastTimeFPSLogged = System.currentTimeMillis();
        }
    }
}
