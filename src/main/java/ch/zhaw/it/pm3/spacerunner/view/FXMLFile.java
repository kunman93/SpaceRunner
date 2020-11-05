package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.FileResource;

public enum FXMLFile implements FileResource {
    MENU("menu.fxml"),
    GAME("game.fxml"),
    SETTINGS("settings.fxml");

    private String filename;

    FXMLFile(String filename){
        this.filename = "view/" + filename;
    }

    public String getFileName() {
        return filename;
    }
}
