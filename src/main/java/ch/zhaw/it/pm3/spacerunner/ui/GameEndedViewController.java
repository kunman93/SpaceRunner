package ch.zhaw.it.pm3.spacerunner.ui;

import ch.zhaw.it.pm3.spacerunner.domain.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.JsonPersistenceUtil;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Displays achievements of a game and redirects the user to his/her next desired view.
 * <p>
 * ViewController of GameEnded.fxml
 *
 * @author freymar1
 */
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

    private final Persistence persistenceUtil = JsonPersistenceUtil.getUtil();

    /**
     * Displays a congratulation to the user, his achieved score and the collected coins.
     */
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

    /**
     * Changes the view to Game.fxml when the RESTART-Button is pressed.
     */
    @FXML
    public void playAgain() {
        getMain().setFXMLView(FXMLFile.GAME);
    }

    /**
     * Changes the view to Menu.fxml when the MENU-Button is pressed.
     */
    @FXML
    public void showMenu() {
        getMain().setFXMLView(FXMLFile.MENU);
    }
}
