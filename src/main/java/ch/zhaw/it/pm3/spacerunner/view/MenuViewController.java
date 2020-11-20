package ch.zhaw.it.pm3.spacerunner.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MenuViewController extends ViewController {

    @FXML
    public Button settingsButton;
    @FXML
    public Button startGameButton;

    @FXML
    public void showGame() {
        getMain().setFXMLView(FXMLFile.GAME);
    }

    @FXML
    public void showShop() {
        getMain().setFXMLView(FXMLFile.SHOP);
    }

    @FXML
    public void showSettings() {
        getMain().setFXMLView(FXMLFile.SETTINGS);
    }

    // todo remove from super
    @Override
    public void initialize() {

    }
}
