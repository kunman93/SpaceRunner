package ch.zhaw.it.pm3.spacerunner.technicalservices.sound;

import ch.zhaw.it.pm3.spacerunner.FileResource;

public enum GameSound  implements FileResource {
    BACKGROUND("background.wav");


    private String fileName;

    GameSound(String fileName) {
        this.fileName = "sound/" + fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
