package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ContentId;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ItemType;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ShopContent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * controller for shop view
 * */
public class ShopViewController extends ViewController {
    private PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
    @FXML private TabPane tabPane;
    @FXML private ListView listViewForUpgrades;
    @FXML private ListView listViewForSkins;

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
        listViewForUpgrades.setCellFactory(param -> new ShopContentCell());

        observableListOfSkins.setAll(skins);
        listViewForSkins.setItems(observableListOfSkins);
        listViewForSkins.setCellFactory(param -> new ShopContentCell());
    }

    public void updateList() {
        initialize();
    }

}
