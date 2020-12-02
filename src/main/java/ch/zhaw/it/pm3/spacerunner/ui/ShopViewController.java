package ch.zhaw.it.pm3.spacerunner.ui;

import ch.zhaw.it.pm3.spacerunner.domain.ItemType;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.JsonPersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.domain.ShopContent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The ShopViewController is a controller-class, which is responsible for the shop-view (Shop.fxml).
 * @author kunnuman
 **/
public class ShopViewController extends ViewController implements ShopContentCellControllerListener {
    private final Persistence persistenceUtil = JsonPersistenceUtil.getUtil();

    @FXML
    private TabPane tabPane;
    @FXML
    private ListView<ShopContent> listViewForUpgrades;
    private final Set<ShopContentCellController> upgradeElements = new HashSet<>();
    @FXML
    private ListView<ShopContent> listViewForSkins;
    private final Set<ShopContentCellController> skinElements = new HashSet<>();
    @FXML
    private Label collectedCoinsLabel;

    /**
     * Shows the menu if the BACK-Button was pressed.
     */
    @FXML
    public void showMenu() {
        upgradeElements.forEach((shopContentCellController -> shopContentCellController.removeListener(this)));
        skinElements.forEach((shopContentCellController -> shopContentCellController.removeListener(this)));
        getMain().setFXMLView(FXMLFile.MENU);
    }

    // https://stackoverflow.com/questions/19588029/customize-listview-in-javafx-with-fxml
    /**
     * Displays, after loading the FXML-file, sets up the shop content cell.
     * */
    public void initialize() {
        collectedCoinsLabel.setText("Coins: " + persistenceUtil.loadProfile().getCoins());
        List<ShopContent> shopContents = persistenceUtil.loadShopContent();

        List<ShopContent> upgrades = new ArrayList<>();
        List<ShopContent> skins = new ArrayList<>();

        for (ShopContent content : shopContents) {
            if (content.getItemType() == ItemType.UPGRADE) {
                upgrades.add(content);
            } else if (content.getItemType() == ItemType.PLAYER_MODEL) {
                skins.add(content);
            }
        }

        ObservableList<ShopContent> observableListOfUpgrades = FXCollections.observableArrayList();
        ObservableList<ShopContent> observableListOfSkins = FXCollections.observableArrayList();

        observableListOfUpgrades.setAll(upgrades);
        listViewForUpgrades.setItems(observableListOfUpgrades);
        listViewForUpgrades.setCellFactory(shopContentListView -> {
            ShopContentCellController shopContentCellController = new ShopContentCellController();
            shopContentCellController.addListener(this);
            upgradeElements.add(shopContentCellController);
            return shopContentCellController;
        });

        observableListOfSkins.setAll(skins);
        listViewForSkins.setItems(observableListOfSkins);
        listViewForSkins.setCellFactory(shopContentListView -> {
            ShopContentCellController shopContentCellController = new ShopContentCellController();
            shopContentCellController.addListener(this);
            skinElements.add(shopContentCellController);
            return shopContentCellController;
        });
    }


    /**
     * Depicts the updated coin value after the Buy-Button was pressed.
     */
    @Override
    public void purchasedItem() {
        collectedCoinsLabel.setText("Coins: " + persistenceUtil.loadProfile().getCoins());
    }
}
