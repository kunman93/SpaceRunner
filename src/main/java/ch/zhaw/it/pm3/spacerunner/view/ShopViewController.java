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
        dummyData.add(new ShopContent("NASA Shuttle", ItemType.PLAYER_MODEL, ContentId.NASA_SPACE_SHIP, false));
        dummyData.add(new ShopContent("Millenium Falcon", ItemType.PLAYER_MODEL, ContentId.MILLENNIUM_FALCON, false));
        dummyData.add(new ShopContent("NASA Shuttle 2", ItemType.PLAYER_MODEL, ContentId.NASA_SPACE_SHIP, true));
        dummyData.add(new ShopContent("Millenium Falcon 2", ItemType.PLAYER_MODEL, ContentId.MILLENNIUM_FALCON, true));
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
        listView.setCellFactory(param -> new Cell());
    }

    public void updateList() {
        initialize();
    }

    /**
     * static class for each table row which contains various FXML elements
     * */
    static class Cell extends ListCell<ShopContent> {

        private HBox hBox = new HBox();
        private Label label = new Label("");
        private Button btn = new Button("buy");
        private static List<ShopContent> inProgress = new ArrayList<>();

        public Cell() {
            super();
            GridPane pane = new GridPane();
            pane.add(label, 0, 0);
            pane.add(btn, 1,0);
            hBox.getChildren().addAll(pane);
            hBox.setHgrow(pane, Priority.ALWAYS);
        }

        /**
         * called automatic from ListView by clicking somewhere
         * - changes button texts according to buying-state
         * */
        @Override
        public void updateItem(ShopContent content, boolean empty) {
            super.updateItem(getItem(),empty);
            setText(null);
            if(content != null) {
                label.setText(content.getTitle());
                if (content.isEquipped()) {
                    btn.setText("equiped");
                } else if (content.isPurchased()) {
                    btn.setText("equip");
                }
                btn.setOnAction(event -> {
                    if (!content.isPurchased()) {
                        inProgress.add(content);
                        btn.setText("purchase now");
                        // remove all other purchase now
                    } else if (inProgress.contains(content)) {
                        inProgress.remove(content);
                        content.buyContent();
                        btn.setText("equip");
                        // remove all other purchase now
                    } else if (!content.isEquipped()) {
                        content.setEquipped(true);
                        btn.setText("equiped");
                    }
                });
                setGraphic(hBox);
                // setGraphic(new ImageView().setImage(new Image("...")))
            }
        }
    }
}
