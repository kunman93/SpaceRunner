package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ItemType;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ShopContent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

/**
 * controller for shop view
 * */
public class ShopViewController extends ViewController {
    private PersistenceUtil persistenceUtil = PersistenceUtil.getUtil();
    @FXML private TabPane tabPane;
    @FXML private ListView<ShopContent> listViewForUpgrades;
    @FXML private ListView<ShopContent> listViewForSkins;

    @FXML
    public void showMenu() {
        getMain().setFXMLView(FXMLFile.MENU);
    }

    // https://stackoverflow.com/questions/19588029/customize-listview-in-javafx-with-fxml
    @Override
    public void initialize() {
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
        listViewForUpgrades.setCellFactory(shopContentListView -> new ShopContentCell());

        observableListOfSkins.setAll(skins);
        listViewForSkins.setItems(observableListOfSkins);
        listViewForSkins.setCellFactory(shopContentListView -> new ShopContentCell());
    }

    public void updateList() {
        initialize();
    }

}
