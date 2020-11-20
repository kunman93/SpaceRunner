package ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util;

import ch.zhaw.it.pm3.spacerunner.FileResource;

public enum GameSound implements FileResource {
    BACKGROUND("background.wav"),
    EXPLOSION("explosion.wav"),
    GAME_OVER_VOICE("game-over-1.wav"),
    GAME_OVER_1("game-over-2.wav"),
    GAME_OVER_2("game-over-3.wav"),
    COIN_PICKUP("coin-pickup.wav");


    private String fileName;

    GameSound(String fileName) {
        this.fileName = "sound/" + fileName;
    }

    @Override
    public String getFileName() {
        return fileName;
    }
}
