package ch.zhaw.it.pm3.spacerunner.technicalservices.sound;

public enum GameSound {
    BACKGROUND("background.wav");


    private String fileName;

    GameSound(String fileName) {
        this.fileName = "sound/" + fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
