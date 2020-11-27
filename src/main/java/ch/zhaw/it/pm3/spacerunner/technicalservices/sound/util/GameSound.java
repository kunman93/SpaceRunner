package ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util;

import ch.zhaw.it.pm3.spacerunner.FileResource;

/**
 * Sound files used by the space-runner game
 */
public enum GameSound implements FileResource {
    BACKGROUND("background.wav"),
    EXPLOSION("explosion.wav"),
    GAME_OVER_VOICE("game-over-1.wav"),
    GAME_OVER_1("game-over-2.wav"),
    GAME_OVER_2("game-over-3.wav"),
    POWER_UP_PICKUP("powerup-pickup.wav"),
    COIN_PICKUP("coin-pickup.wav");


    private final String fileName;

    GameSound(String fileName) {
        this.fileName = "sound/" + fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
