package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PlayerProfile;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameEndedViewController extends ViewController {

    @FXML
    public Button showMenuButton;
    @FXML
    public Button playAgainButton;
    @FXML
    public Label score;
    @FXML
    public Label name;
    @FXML
    public Label collectedCoins;

    private final Persistence persistenceUtil = PersistenceUtil.getUtil();

    @Override
    public void initialize() {
        PlayerProfile player = persistenceUtil.loadProfile();
        name.setText("Congratulation " + player.getPlayerName());
        score.setText("Score: " + getGameDataCache().getScore());
        collectedCoins.setText("Collected Coins: " + getGameDataCache().getCoins());
    }

    @FXML
    public void playAgain() {
        getMain().setFXMLView(FXMLFile.GAME);
    }

    @FXML
    public void showMenu() {
        getMain().setFXMLView(FXMLFile.MENU);
    }
}
