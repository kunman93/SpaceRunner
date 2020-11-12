package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ContentId;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ShopContent;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualSVGFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * class for each table row which contains various FXML elements
 * */
public class ShopContentCell extends ListCell<ShopContent> {
    private PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
    private VisualUtil visualUtil = VisualUtil.getInstance();

    private GridPane pane = new GridPane();
    private HBox hBox = new HBox();
    private Label label = new Label();
    private Button buyButton = new Button("buy");
    private Button activateButton = new Button("activate");
    private static List<ShopContent> inProgress = new ArrayList<>();
    private static PlayerProfile playerProfile;
    private static Set<ContentId> purchasedContentIds = new HashSet<>();
    private static Set<ContentId> activeContentIds = new HashSet<>();

    public ShopContentCell() {
        super();
        pane.add(label, 1, 0);
        pane.add(buyButton, 2,0);
        pane.add(activateButton, 3,0);
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
            VisualSVGFile visualSVGFileOfContent = content.getImageId();
            Image imageOfContent = SwingFXUtils.toFXImage(visualUtil.loadSVGImage(SpaceRunnerApp.class.getResource(visualSVGFileOfContent.getFileName()), 60f), null);
            pane.add(new ImageView(imageOfContent), 0, 0);

            playerProfile = persistenceUtil.loadProfile();
            purchasedContentIds = playerProfile.getPurchasedContentIds();
            activeContentIds = playerProfile.getActiveContentIds();

            label.setText(content.getTitle());

            if(contentIsPurchased(content)) {
                if (contentIsActive(content)) {
                    deactivatePurchasedContent(content);
                } else {
                    activatePurchasedContent(content);
                }
            }else{
                activateButton.setDisable(true);
                buyContent(content);
            }


            /*if (content.isEquipped()) {
                btn.setText("equipped");
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
            });*/
            setGraphic(hBox);
            // setGraphic(new ImageView().setImage(new Image("...")))
        }
    }

    private void deactivatePurchasedContent(ShopContent content) {
        activateButton.setOnAction(event -> {
           playerProfile.deactivateContent(content.getContentId());
           persistenceUtil.saveProfile(playerProfile);
           activateButton.setText("activate");
        });
    }

    private void activatePurchasedContent(ShopContent content) {
        activateButton.setOnAction(event -> {
            playerProfile.activateContent(content.getContentId());
            persistenceUtil.saveProfile(playerProfile);
            activateButton.setText("deactivate");
        });
    }

    private void buyContent(ShopContent content) {
        buyButton.setOnAction(event -> {
            if(playerHasEnoughCoinsToBuy(content)){
                playerProfile.setCoins(getAmountDeducted(content));
                playerProfile.addContent(content.getContentId());
                persistenceUtil.saveProfile(playerProfile);
                buyButton.setText("bought");
                buyButton.setDisable(true);
                activateButton.setDisable(false);
            }else{
                alertFailedToPurchaseContent(content);
            }
        });
    }

    private void alertFailedToPurchaseContent(ShopContent content) {
        int amountOfCoinsNeed = content.getPrice() - playerProfile.getCoins();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Purchase failed!");
        alert.setHeaderText(null);
        alert.setContentText("Not enough coins!\n You need atleast " + amountOfCoinsNeed + " coins");
        alert.showAndWait();
    }

    private int getAmountDeducted(ShopContent content) {
        return playerProfile.getCoins() - content.getPrice();
    }

    private boolean playerHasEnoughCoinsToBuy(ShopContent content) {
        return playerProfile.getCoins() >= content.getPrice();
    }

    private boolean contentIsActive(ShopContent content) {
        return activeContentIds.contains(content.getContentId());
    }

    private boolean contentIsPurchased(ShopContent content) {
        return purchasedContentIds.contains(content.getContentId());
    }
}

