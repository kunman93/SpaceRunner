package ch.zhaw.it.pm3.spacerunner.view;

public enum FXMLFile {
    MENU("view/menu.fxml"),
    GAME("view/game.fxml"),
    SETTINGS("view/settings.fxml");

    private String filename;

    FXMLFile(String filename){
        this.filename = filename;
    }

    public String getFilename() {
        return filename;
    }
}
