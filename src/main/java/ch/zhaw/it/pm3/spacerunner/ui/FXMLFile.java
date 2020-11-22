package ch.zhaw.it.pm3.spacerunner.ui;

import ch.zhaw.it.pm3.spacerunner.FileResource;

/**
 * Enumeration of the FXML-files that are being used.
 */
public enum FXMLFile implements FileResource {
    MENU("Menu.fxml"),
    GAME("Game.fxml"),
    GAME_ENDED("GameEnded.fxml"),
    HELP("Help.fxml"),
    SHOP("Shop.fxml"),
    SHOP_CONTENT_CELL("ShopContentCell.fxml"),
    SETTINGS("Settings.fxml");

    private String filename;

    FXMLFile(String filename) {
        this.filename = "ui/" + filename;
    }

    @Override
    public String getFileName() {
        return filename;
    }
}
