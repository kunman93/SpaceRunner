package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

public enum VisualFile {

    BACKGROUND_STARS("background.jpg"),
    ROCKET_ICON("icon.png");
    





    private String fileName;

    VisualFile(String fileName) {
        this.fileName = "images/" + fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
