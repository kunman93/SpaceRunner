package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualSVGFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.VisualUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * class for each table row which contains various FXML elements
 * */
public class ShopContentCell extends ListCell<ShopContent> {
    private PersistenceUtil persistenceUtil = PersistenceUtil.getUtil();
    private VisualUtil visualUtil = VisualUtil.getInstance();

    private static final String BUY_TEXT_FOR_BUY_BUTTON = "buy";
    private static final String BOUGHT_TEXT_FOR_BUY_BUTTON = "bought";
    private static final String ACTIVATE_TEXT_FOR_ACTIVATE_BUTTON = "activate";
    private static final String DEACTIVATE_TEXT_FOR_ACTIVATE_BUTTON = "deactivate";

    private GridPane pane = new GridPane();
    private HBox hBox = new HBox();
    private Label contentTitelLabel = new Label();
    private Label contentPriceLabel = new Label();
    private Button buyButton = new Button(BUY_TEXT_FOR_BUY_BUTTON);
    private Button activateButton = new Button(ACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
    private static PlayerProfile playerProfile;
    private static Set<ContentId> purchasedContentIds = new HashSet<>();
    private static Set<ContentId> activeContentIds = new HashSet<>();
    private static boolean spaceShipModelIsAlreadySelected;
    private ShopContent content;

    public ShopContentCell() {
        super();
        pane.add(contentTitelLabel, 1, 0);
        pane.add(contentPriceLabel, 2,0);
        pane.add(buyButton, 3,0);
        pane.add(activateButton, 4,0);
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
        this.content = content;
        setText(null);
        if(this.content != null) {
            setUpImageAndLabelsOfContent();

            loadPlayerProfileAndContentIds();
            setUpBuyButton();
            setUpActivateButton();
            processShopping();

            setGraphic(hBox);
            // setGraphic(new ImageView().setImage(new Image("...")))
        }
    }

    private void setUpImageAndLabelsOfContent() {
        //TODO always loading images, this might be bad
        VisualSVGFile visualSVGFileOfContent = this.content.getImageId();
        Image imageOfContent = SwingFXUtils.toFXImage(visualUtil.loadSVGImage(SpaceRunnerApp.class.getResource(visualSVGFileOfContent.getFileName()), 60f), null);
        pane.add(new ImageView(imageOfContent), 0, 0);
        contentTitelLabel.setText(this.content.getTitle());
        contentPriceLabel.setText("Price: " + this.content.getPrice());
    }

    private void loadPlayerProfileAndContentIds() {
        playerProfile = persistenceUtil.loadProfile();
        purchasedContentIds = playerProfile.getPurchasedContentIds();
        activeContentIds = playerProfile.getActiveContentIds();
    }

    private void setUpBuyButton() {
        if(contentIsPurchased()){
            buyButton.setText(BOUGHT_TEXT_FOR_BUY_BUTTON);
            buyButton.setDisable(true);
        }else{
            buyButton.setText(BUY_TEXT_FOR_BUY_BUTTON);
            buyButton.setDisable(false);
        }
    }

    private void setUpActivateButton() {
        if(contentIsActive()){
            activateButton.setText(DEACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
            if(contentIsAPlayerModel()) {
                spaceShipModelIsAlreadySelected = true;
            }
        }

        if(spaceShipModelIsAlreadySelected) {
            if (!contentIsActive() && contentIsAPlayerModel()) {
                activateButton.setDisable(true);
            } else if(contentIsAPlayerModel()){
                activateButton.setText(DEACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
                activateButton.setDisable(false);
            }
        }else if(contentIsAPlayerModel()){
            activateButton.setDisable(false);
        }
    }

    private void processShopping() {
        if(contentIsPurchased()) {
            if (contentIsActive()) {
                deactivatePurchasedContent();
            } else {
                activatePurchasedContent();
            }
        }else{
            buyContent();
        }
    }

    private boolean contentIsAPlayerModel() {
        return content.getItemType() == ItemType.PLAYER_MODEL;
    }

    private boolean contentIsAnUpgrade(){
        return content.getItemType() == ItemType.UPGRADE;
    }

    private void deactivatePurchasedContent() {
        activateButton.setOnAction(event -> {
            if(contentIsAnUpgrade()) {
                deactivateContentInPlayerProfile();
            }else if(contentIsAPlayerModel() && (spaceShipModelIsAlreadySelected)){
                deactivateContentInPlayerProfile();
                spaceShipModelIsAlreadySelected = false;
            }
        });
    }

    private void deactivateContentInPlayerProfile() {
        playerProfile.deactivateContent(content.getContentId());
        persistenceUtil.saveProfile(playerProfile);
        activateButton.setText(ACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
    }

    private void activatePurchasedContent() {
        activateButton.setOnAction(event -> {
            if(contentIsAnUpgrade()){
                activateContentInPlayerProfile();
            }else if(contentIsAPlayerModel() && (!spaceShipModelIsAlreadySelected)){
                activateContentInPlayerProfile();
                spaceShipModelIsAlreadySelected = true;
            }
        });
    }

    private void activateContentInPlayerProfile() {
        playerProfile.activateContent(content.getContentId());
        persistenceUtil.saveProfile(playerProfile);
        activateButton.setText(DEACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
    }

    private void buyContent() {
        activateButton.setDisable(true);
        buyButton.setOnAction(event -> {
            if(playerHasEnoughCoinsToBuy()){
                showPurchaseContentConfirmationDialogue();
            }else{
                showFailedToPurchaseContentAlertDialogue();
            }
        });
    }

    private void showPurchaseContentConfirmationDialogue(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Purchase");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to buy " + content.getTitle());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            buy();
        }
    }

    private void buy() {
        playerProfile.setCoins(getAmountDeducted());
        playerProfile.addContent(content.getContentId());
        persistenceUtil.saveProfile(playerProfile);
        buyButton.setText(BOUGHT_TEXT_FOR_BUY_BUTTON);
        buyButton.setDisable(true);
        activateButton.setDisable(false);
    }

    private void showFailedToPurchaseContentAlertDialogue() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Purchase failed!");
        alert.setHeaderText(null);
        alert.setContentText("Not enough coins! You need atleast " + getAmountOfCoinsNeedToBuyContent() + " coins");
        alert.showAndWait();
    }

    private int getAmountOfCoinsNeedToBuyContent(){
        return content.getPrice() - playerProfile.getCoins();
    }

    private int getAmountDeducted() {
        return playerProfile.getCoins() - content.getPrice();
    }

    private boolean playerHasEnoughCoinsToBuy() {
        return playerProfile.getCoins() >= content.getPrice();
    }

    private boolean contentIsActive() {
        return activeContentIds.contains(content.getContentId());
    }

    private boolean contentIsPurchased() {
        return purchasedContentIds.contains(content.getContentId());
    }
}

