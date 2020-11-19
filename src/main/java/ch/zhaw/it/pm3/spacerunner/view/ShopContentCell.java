package ch.zhaw.it.pm3.spacerunner.view;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.model.spaceelement.UFO;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.*;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualUtil;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * class for each table row which contains various FXML elements
 * */
public class ShopContentCell extends ListCell<ShopContent> {

    private Logger logger = Logger.getLogger(ShopContent.class.getName());

    private PersistenceUtil persistenceUtil = PersistenceUtil.getUtil();
    private VisualUtil visualUtil = VisualUtil.getInstance();

    private static final String BUY_TEXT_FOR_BUY_BUTTON = "buy";
    private static final String BOUGHT_TEXT_FOR_BUY_BUTTON = "bought";
    private static final String ACTIVATE_TEXT_FOR_ACTIVATE_BUTTON = "activate";
    private static final String DEACTIVATE_TEXT_FOR_ACTIVATE_BUTTON = "deactivate";

    private final Set<ShopContentCellListener> shopContentCellListeners = new HashSet<>();

    @FXML private GridPane cellContent;
    @FXML private GridPane verticalSeparation;
    @FXML private ImageView contentImageView = new ImageView();
    @FXML private Label contentTitelLabel = new Label();
    @FXML private Label contentPriceLabel = new Label();
    @FXML private Button buyButton = new Button(BUY_TEXT_FOR_BUY_BUTTON);
    @FXML private Button activateButton = new Button(ACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
    private static PlayerProfile playerProfile;
    private static Set<ContentId> purchasedContentIds = new HashSet<>();
    private static Set<ContentId> activeContentIds = new HashSet<>();
    private static boolean spaceShipModelIsAlreadySelected;
    private ShopContent content;

    public ShopContentCell() {
        FXMLLoader fxmlLoader = new FXMLLoader(SpaceRunnerApp.class.getResource(FXMLFile.SHOP_CONTENT_CELL.getFileName()));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            //TODO
            logger.log(Level.SEVERE, "Error loading FXML");
            throw new RuntimeException(e);
        }
        cellContent = fxmlLoader.getRoot();

        setUpUI();
    }

    private void setUpUI(){
        contentImageView = (ImageView) cellContent.getChildren().stream().filter((child) -> child.getId().equals("contentImageView")).collect(Collectors.toList()).get(0);
        verticalSeparation = (GridPane) cellContent.getChildren().stream().filter((child) -> child.getId().equals("verticalSeparation")).collect(Collectors.toList()).get(0);
        contentTitelLabel = (Label) verticalSeparation.getChildren().stream().filter((child) -> child.getId().equals("contentTitelLabel")).collect(Collectors.toList()).get(0);
        contentPriceLabel = (Label) verticalSeparation.getChildren().stream().filter((child) -> child.getId().equals("contentPriceLabel")).collect(Collectors.toList()).get(0);
        buyButton = (Button) verticalSeparation.getChildren().stream().filter((child) -> child.getId().equals("buyButton")).collect(Collectors.toList()).get(0);
        GridPane.setFillWidth(buyButton, true);
        GridPane.setFillHeight(buyButton, true);
        activateButton = (Button) verticalSeparation.getChildren().stream().filter((child) -> child.getId().equals("activateButton")).collect(Collectors.toList()).get(0);
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

            setGraphic(cellContent);
        }
    }

    private void setUpImageAndLabelsOfContent() {
        VisualSVGFile visualSVGFileOfContent = this.content.getImageId();
        Image imageOfContent = SwingFXUtils.toFXImage(visualUtil.loadSVGImage(SpaceRunnerApp.class.getResource(visualSVGFileOfContent.getFileName()), 60f), null);
        contentImageView.setImage(imageOfContent);
        contentImageView.setEffect(new DropShadow(20, Color.RED));
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
        }else{
            activateButton.setText(ACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
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
        ButtonType purchaseButtonType = new ButtonType("Purchase", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"",purchaseButtonType,cancelButtonType);
        alert.setTitle("Confirm Purchase");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to buy " + content.getTitle() + "?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("style.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == purchaseButtonType){
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
        shopContentCellListeners.forEach(ShopContentCellListener::purchasedItem);
    }

    public void addListener(ShopContentCellListener shopContentCellListener) {
        shopContentCellListeners.add(shopContentCellListener);
    }

    public void removeListener(ShopContentCellListener shopContentCellListener) {
        shopContentCellListeners.remove(shopContentCellListener);
    }

    private void showFailedToPurchaseContentAlertDialogue() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Purchase failed!");
        alert.setHeaderText(null);
        alert.setContentText("Not enough coins! You need at least " + getAmountOfCoinsNeedToBuyContent() + " coins.");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("style.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog");

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

