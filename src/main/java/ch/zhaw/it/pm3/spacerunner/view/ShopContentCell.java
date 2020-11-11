package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ShopContent;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualSVGFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.List;

/**
 * class for each table row which contains various FXML elements
 * */
public class ShopContentCell extends ListCell<ShopContent> {
        VisualUtil visualUtil = VisualUtil.getInstance();

        private GridPane pane = new GridPane();
        private HBox hBox = new HBox();
        private Label label = new Label("");
        private Button btn = new Button("buy");
        private static List<ShopContent> inProgress = new ArrayList<>();

        public ShopContentCell() {
            super();
            pane.add(label, 1, 0);
            pane.add(btn, 2,0);
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
                VisualSVGFile visualSVGFile = content.getImageId();
                Image image = SwingFXUtils.toFXImage(visualUtil.loadSVGImage(SpaceRunnerApp.class.getResource(visualSVGFile.getFileName()), 60f), null);
                pane.add(new ImageView(image), 0, 0);

                label.setText(content.getTitle());
                if (content.isEquipped()) {
                    btn.setText("equiped");
                    // background-image: Häckchen
                } else if (content.isPurchased()) {
                    btn.setText("equip");
                }
                btn.setOnAction(event -> {
                    if (!content.isPurchased()) {
                        inProgress.add(content);
                        btn.setText("purchase now");
                        // background-image: Einkauftasche
                        // remove all other purchase now
                    } else if (inProgress.contains(content)) {
                        inProgress.remove(content);
                        content.buyContent();
                        btn.setText("equip");
                        // background-image: Contains
                        // remove all other purchase now
                    } else if (!content.isEquipped()) {
                        content.equipContent(true);
                        btn.setText("equiped");
                        // background-image: Fragezeichen
                    }
                });
                setGraphic(hBox);
                // setGraphic(new ImageView().setImage(new Image("...")))
            }
        }
    }

