package ch.zhaw.it.pm3.spacerunner.view;

import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;

/**
 * Visual entry point of program. Loads new FXML files on button-clicks.
 *
 * @author freymar1
 * */
public class MenuViewController extends ViewController {

    @FXML
    public void showGame() {
        getMain().setFXMLView(FXMLFile.GAME);
    }

    @FXML
    public void showShop() {
        getMain().setFXMLView(FXMLFile.SHOP);
    }

    @FXML
    public void showSettings() { getMain().setFXMLView(FXMLFile.SETTINGS); }

    @FXML
    public void showHelp() { getMain().setFXMLView(FXMLFile.HELP); }
}
