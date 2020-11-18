package ch.zhaw.it.pm3.spacerunner.technicalservices.performance;

public class FPSTracker {
    private final long[] frameTimes = new long[100];
    private int frameTimeIndex = 0 ;
    private boolean arrayFilled = false ;

    public void track(long currentNanoTime){
        long oldFrameTime = frameTimes[frameTimeIndex];
        frameTimes[frameTimeIndex] = currentNanoTime;
        frameTimeIndex = (frameTimeIndex + 1) % frameTimes.length;
        if (frameTimeIndex == 0) {
            arrayFilled = true ;
        }
        if (arrayFilled) {
            long elapsedNanos = currentNanoTime - oldFrameTime;
            long elapsedNanosPerFrame = elapsedNanos / frameTimes.length;
            double frameRate = 1_000_000_000.0 / elapsedNanosPerFrame;
            System.out.println(String.format("Current frame rate: %.3f", frameRate));
        }
    }

}
