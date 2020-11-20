package ch.zhaw.it.pm3.spacerunner.ui;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PlayerProfile;
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

    public void initialize() {
        PlayerProfile player = persistenceUtil.loadProfile();
        name.setText("Congratulation " + player.getPlayerName());
        if (player.getHighScore() == getGameDataCache().getScore()) {
            score.setText("new Highscore: " + getGameDataCache().getScore());
        } else {
            score.setText("score: " + getGameDataCache().getScore());
        }
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
