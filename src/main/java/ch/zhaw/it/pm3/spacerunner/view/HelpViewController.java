package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.SoundUtil;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;

public class HelpViewController extends ViewController {

    private String helpText = "task combinations \n" +
            "p \tpause \n" +
            "esc \tend game";


    //@FXML public Text helpContent;

    @FXML
    public void showMenu() {
        getMain().setupBackgroundMusic();
        getMain().setFXMLView(FXMLFile.MENU);
    }

    @Override
    public void initialize() {
        //helpContent.setText(helpText);
    }
}
