package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.*;
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

import java.util.HashSet;
import java.util.Set;

/**
 * class for each table row which contains various FXML elements
 * */
public class ShopContentCell extends ListCell<ShopContent> {
    private PersistenceUtil persistenceUtil = PersistenceUtil.getInstance();
    private VisualUtil visualUtil = VisualUtil.getInstance();

    private static final String BUY_BUTTON_BUY_TEXT = "buy";
    private static final String BUY_BUTTON_BOUGHT_TEXT = "bought";
    private static final String ACTIVATE_BUTTON_ACTIVATE_TEXT = "activate";
    private static final String ACTIVATE_BUTTON_DEACTIVATE_TEXT = "deactivate";

    private GridPane pane = new GridPane();
    private HBox hBox = new HBox();
    private Label label = new Label();
    private Button buyButton = new Button(BUY_BUTTON_BUY_TEXT);
    private Button activateButton = new Button(ACTIVATE_BUTTON_ACTIVATE_TEXT);
    private static PlayerProfile playerProfile;
    private static Set<ContentId> purchasedContentIds = new HashSet<>();
    private static Set<ContentId> activeContentIds = new HashSet<>();
    private static boolean spaceShipModelIsAlreadySelected;

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
            //TODO always loading images
            VisualSVGFile visualSVGFileOfContent = content.getImageId();
            Image imageOfContent = SwingFXUtils.toFXImage(visualUtil.loadSVGImage(SpaceRunnerApp.class.getResource(visualSVGFileOfContent.getFileName()), 60f), null);
            pane.add(new ImageView(imageOfContent), 0, 0);

            playerProfile = persistenceUtil.loadProfile();
            purchasedContentIds = playerProfile.getPurchasedContentIds();
            activeContentIds = playerProfile.getActiveContentIds();

            label.setText(content.getTitle());

            if(contentIsActive(content)){
                activateButton.setText(ACTIVATE_BUTTON_DEACTIVATE_TEXT);
                if(contentIsAPlayerModel(content)) {
                    spaceShipModelIsAlreadySelected = true;
                }
            }

            if(spaceShipModelIsAlreadySelected) {
                if (!contentIsActive(content) && contentIsAPlayerModel(content)) {
                    activateButton.setDisable(true);
                } else if(contentIsAPlayerModel(content)){
                    activateButton.setText(ACTIVATE_BUTTON_DEACTIVATE_TEXT);
                    activateButton.setDisable(false);
                }
            }else if(contentIsAPlayerModel(content)){
                activateButton.setDisable(false);
            }

            if(contentIsPurchased(content)) {
                if (contentIsActive(content)) {
                    deactivatePurchasedContent(content);
                } else {
                    activatePurchasedContent(content);
                }
            }else{
                buyContent(content);
            }

            setGraphic(hBox);
            // setGraphic(new ImageView().setImage(new Image("...")))
        }
    }

    private boolean contentIsAPlayerModel(ShopContent content) {
        return content.getItemType() == ItemType.PLAYER_MODEL;
    }

    private boolean contentIsAnUpgrade(ShopContent content){
        return content.getItemType() == ItemType.UPGRADE;
    }

    private void deactivatePurchasedContent(ShopContent content) {
        buyButton.setText(BUY_BUTTON_BOUGHT_TEXT);
        buyButton.setDisable(true);
        activateButton.setOnAction(event -> {
            if(contentIsAnUpgrade(content)) {
                playerProfile.deactivateContent(content.getContentId());
                persistenceUtil.saveProfile(playerProfile);
                activateButton.setText(ACTIVATE_BUTTON_ACTIVATE_TEXT);
            }else if(contentIsAPlayerModel(content) && (spaceShipModelIsAlreadySelected)){
                //TODO
                playerProfile.deactivateContent(content.getContentId());
                persistenceUtil.saveProfile(playerProfile);
                activateButton.setText(ACTIVATE_BUTTON_ACTIVATE_TEXT);
                spaceShipModelIsAlreadySelected = false;
            }
        });
    }

    private void activatePurchasedContent(ShopContent content) {
        buyButton.setText(BUY_BUTTON_BOUGHT_TEXT);
        buyButton.setDisable(true);
        activateButton.setOnAction(event -> {
            if(contentIsAnUpgrade(content)){
                playerProfile.activateContent(content.getContentId());
                persistenceUtil.saveProfile(playerProfile);
                activateButton.setText(ACTIVATE_BUTTON_DEACTIVATE_TEXT);
            }else if(contentIsAPlayerModel(content) && (!spaceShipModelIsAlreadySelected)){
                //TODO
                playerProfile.activateContent(content.getContentId());
                persistenceUtil.saveProfile(playerProfile);
                activateButton.setText(ACTIVATE_BUTTON_DEACTIVATE_TEXT);
                spaceShipModelIsAlreadySelected = true;
            }
        });
    }

    private void buyContent(ShopContent content) {
        activateButton.setDisable(true);
        buyButton.setOnAction(event -> {
            if(playerHasEnoughCoinsToBuy(content)){
                playerProfile.setCoins(getAmountDeducted(content));
                playerProfile.addContent(content.getContentId());
                persistenceUtil.saveProfile(playerProfile);
                activateButton.setDisable(false);
            }else{
                alertFailedToPurchaseContent(content);
            }
        });
    }

    private void alertFailedToPurchaseContent(ShopContent content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Purchase failed!");
        alert.setHeaderText(null);
        alert.setContentText("Not enough coins!\n You need atleast " + getAmountOfCoinsNeedToBuyContent(content) + " coins");
        alert.showAndWait();
    }

    private int getAmountOfCoinsNeedToBuyContent(ShopContent content){
        return content.getPrice() - playerProfile.getCoins();
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

