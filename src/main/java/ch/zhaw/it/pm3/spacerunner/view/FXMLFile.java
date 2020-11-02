package ch.zhaw.it.pm3.spacerunner.view;

public enum FXMLFile {
    MENU("menu.fxml"),
    GAME("game.fxml"),
    SETTINGS("settings.fxml");

    private String filename;

    FXMLFile(String filename){
        this.filename = "view/" + filename;
    }

    public String getFilename() {
        return filename;
    }
}
