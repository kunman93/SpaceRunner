package ch.zhaw.it.pm3.spacerunner.ui;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ItemType;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ShopContent;
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
 **/
public class ShopViewControllerController extends ViewController implements ShopContentCellControllerListener {
    private final Persistence persistenceUtil = PersistenceUtil.getUtil();

    @FXML
    private TabPane tabPane;
    @FXML
    private ListView<ShopContent> listViewForUpgrades;
    private Set<ShopContentCellController> upgradeElements = new HashSet<>();
    @FXML
    private ListView<ShopContent> listViewForSkins;
    private Set<ShopContentCellController> skinElements = new HashSet<>();
    @FXML
    private Label collectedCoinsLabel;

    @FXML
    public void showMenu() {
        upgradeElements.forEach((shopContentCellController -> shopContentCellController.removeListener(this)));
        skinElements.forEach((shopContentCellController -> shopContentCellController.removeListener(this)));
        getMain().setFXMLView(FXMLFile.MENU);
    }

    // https://stackoverflow.com/questions/19588029/customize-listview-in-javafx-with-fxml
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

    public void updateList() {
        initialize();
    }

    @Override
    public void purchasedItem() {
        collectedCoinsLabel.setText("Coins: " + persistenceUtil.loadProfile().getCoins());
    }
}
