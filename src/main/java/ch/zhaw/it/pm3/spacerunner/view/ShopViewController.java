package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.ItemType;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.ShopContent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TabPane;

import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * The ShopViewController is a controller-class, which is responsible for the shop-view (shop.fxml).
 **/
public class ShopViewController extends ViewController implements ShopContentCellListener {
    private final Persistence persistenceUtil = PersistenceUtil.getUtil();

    @FXML private TabPane tabPane;
    @FXML private ListView<ShopContent> listViewForUpgrades;
    @FXML private ListView<ShopContent> listViewForSkins;
    @FXML private Label collectedCoinsLabel;

    @FXML public void showMenu() {
        getMain().setFXMLView(FXMLFile.MENU);
    }

    // https://stackoverflow.com/questions/19588029/customize-listview-in-javafx-with-fxml
    @Override
    public void initialize() {
        collectedCoinsLabel.setText("Coins: " + persistenceUtil.loadProfile().getCoins());
        List<ShopContent> shopContents = persistenceUtil.loadShopContent();
        //TODO: eventually use HashSet
        List<ShopContent> upgrades = new ArrayList<>();
        List<ShopContent> skins = new ArrayList<>();

        for(ShopContent content : shopContents){
            if(content.getItemType() == ItemType.UPGRADE){
                upgrades.add(content);
            }else if(content.getItemType() == ItemType.PLAYER_MODEL){
                skins.add(content);
            }
        }

        ObservableList<ShopContent> observableListOfUpgrades = FXCollections.observableArrayList();
        ObservableList<ShopContent> observableListOfSkins = FXCollections.observableArrayList();

        observableListOfUpgrades.setAll(upgrades);
        listViewForUpgrades.setItems(observableListOfUpgrades);
        listViewForUpgrades.setCellFactory(shopContentListView -> {
            ShopContentCell shopContentCell = new ShopContentCell();
            shopContentCell.addListener(this);
            return shopContentCell;
        });

        observableListOfSkins.setAll(skins);
        listViewForSkins.setItems(observableListOfSkins);
        listViewForSkins.setCellFactory(shopContentListView -> {
            ShopContentCell shopContentCell = new ShopContentCell();
            shopContentCell.addListener(this);
            return shopContentCell;
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
