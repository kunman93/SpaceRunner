package ch.zhaw.it.pm3.spacerunner.ui;


import javafx.fxml.FXML;

public class HelpViewController extends ViewController {

    private String helpText = "task combinations \n" +
            "p \tpause \n" +
            "esc \tend game";


    //@FXML public Text helpContent;

    @FXML
    public void showMenu() {
        getMain().setFXMLView(FXMLFile.MENU);
    }

    public void initialize() {
        //helpContent.setText(helpText);
    }
}
