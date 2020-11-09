package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ContentId;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ItemType;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ShopContent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.List;

/**
 * controller for shop view
 * */
public class ShopViewController extends ViewController {
    public TabPane tabPane;
    public ListView listView;

    private List<ShopContent> dummyData() {
        List<ShopContent> dummyData = new ArrayList<>();
        //TODO: load from persistenceUtil
        //dummyData.add(new ShopContent("NASA Shuttle", ItemType.PLAYER_MODEL, ContentId.NASA_SPACE_SHIP, false));
        //dummyData.add(new ShopContent("Millenium Falcon", ItemType.PLAYER_MODEL, ContentId.MILLENNIUM_FALCON, false));
        //dummyData.add(new ShopContent("NASA Shuttle 2", ItemType.PLAYER_MODEL, ContentId.NASA_SPACE_SHIP, true));
        //dummyData.add(new ShopContent("Millenium Falcon 2", ItemType.PLAYER_MODEL, ContentId.MILLENNIUM_FALCON, true));
        return dummyData;
    }

    @FXML
    public void showMenu() {
        getMain().setFXMLView(FXMLFile.MENU);
    }

    // https://stackoverflow.com/questions/19588029/customize-listview-in-javafx-with-fxml
    @Override
    public void initialize() {
        ObservableList<ShopContent> observableList = FXCollections.observableArrayList();
        observableList.setAll(dummyData());
        listView.setItems(observableList);
        listView.setCellFactory(param -> new ShopContentCell());
    }

    public void updateList() {
        initialize();
    }

}
