package ch.zhaw.it.pm3.spacerunner.ui;


import javafx.fxml.FXML;

/**
 * Displays description of game functionality.
 *
 * ViewController of Help.fxml
 *
 * @author freymar1
 * */
public class HelpViewController extends ViewController {

    /**
     * Changes the view to Menu.fxml when the MENU-Button is pressed.
     */
    @FXML
    public void showMenu() {
        getMain().setFXMLView(FXMLFile.MENU);
    }

}
