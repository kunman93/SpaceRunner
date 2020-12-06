package ch.zhaw.it.pm3.spacerunner.ui;

import javafx.fxml.FXML;


/**
 * Visual entry point of program. Loads new FXML files on button-clicks.
 * <p>
 * ViewController of Menu.fxml
 *
 * @author freymar1
 */
public class MenuViewController extends ViewController {

    /**
     * Changes the view to Game.fxml when the GAME-Button is pressed.
     */
    @FXML
    public void showGame() {
        getMain().setFXMLView(FXMLFile.GAME);
    }

    /**
     * Changes the view to Shop.fxml when the SHOP-Button is pressed.
     */
    @FXML
    public void showShop() {
        getMain().setFXMLView(FXMLFile.SHOP);
    }

    /**
     * Changes the view to Settings.fxml when the SETTINGS-Button is pressed.
     */
    @FXML
    public void showSettings() {
        getMain().setFXMLView(FXMLFile.SETTINGS);
    }

    /**
     * Changes the view to Help.fxml when the HELP-Button (?) is pressed.
     */
    @FXML
    public void showHelp() {
        getMain().setFXMLView(FXMLFile.HELP);
    }
}
