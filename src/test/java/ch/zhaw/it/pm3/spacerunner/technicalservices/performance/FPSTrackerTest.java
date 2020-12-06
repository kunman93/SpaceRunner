package ch.zhaw.it.pm3.spacerunner.technicalservices.performance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FPSTrackerTest {

    @Test
    void trackTest() {
        FPSTracker fpsTracker = new FPSTracker();

        long currentTimeFakeMs = 0;
        for (int i = 0; i < 1000; i++) {
            currentTimeFakeMs += 10_000_000;
            fpsTracker.track(currentTimeFakeMs);
        }


        currentTimeFakeMs += 10_000_000;
        assertEquals(100, fpsTracker.track(currentTimeFakeMs));


        currentTimeFakeMs = 0;
        for (int i = 0; i < 1000; i++) {
            currentTimeFakeMs += 1_000_000_000;
            fpsTracker.track(currentTimeFakeMs);
        }


        currentTimeFakeMs += 1_000_000_000;
        assertEquals(1, fpsTracker.track(currentTimeFakeMs));


    }
}