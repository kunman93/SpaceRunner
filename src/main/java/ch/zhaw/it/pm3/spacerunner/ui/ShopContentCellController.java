package ch.zhaw.it.pm3.spacerunner.ui;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import ch.zhaw.it.pm3.spacerunner.domain.ItemType;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util.JsonPersistenceUtil;
import ch.zhaw.it.pm3.spacerunner.domain.ShopContent;
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
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Shop-view (Shop.fxml) has multiple ShopContentCells. Each ShopContentCell contains various FXML elements.
 * This controller class is responsible for shop-content-cell-view (ShopContentCell.fxml).
 * @author kunnuman
 */
public class ShopContentCellController extends ListCell<ShopContent> {

    private final Logger logger = Logger.getLogger(ShopContent.class.getName());

    private final Persistence persistenceUtil = JsonPersistenceUtil.getUtil();
    private final VisualUtil visualUtil = VisualUtil.getUtil();

    private static final String BUY_TEXT_FOR_BUY_BUTTON = "buy";
    private static final String BOUGHT_TEXT_FOR_BUY_BUTTON = "bought";
    private static final String ACTIVATE_TEXT_FOR_ACTIVATE_BUTTON = "activate";
    private static final String DEACTIVATE_TEXT_FOR_ACTIVATE_BUTTON = "deactivate";

    private final Set<ShopContentCellControllerListener> shopContentCellControllerListeners = new HashSet<>();

    @FXML
    private GridPane cellContent;
    @FXML
    private GridPane verticalSeparation;
    @FXML
    private ImageView contentImageView = new ImageView();
    @FXML
    private Label contentTitleLabel = new Label();
    @FXML
    private Label contentPriceLabel = new Label();
    @FXML
    private Button buyButton = new Button(BUY_TEXT_FOR_BUY_BUTTON);
    @FXML
    private Button activateButton = new Button(ACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
    private static boolean spaceShipModelIsAlreadySelected;
    private ShopContent content;

    /**
     * Sets up the UI of the shop content cell.
     */
    public ShopContentCellController() {
        FXMLLoader fxmlLoader = new FXMLLoader(SpaceRunnerApp.class.getResource(FXMLFile.SHOP_CONTENT_CELL.getFileName()));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error loading FXML");
            throw new RuntimeException(e);
        }
        cellContent = fxmlLoader.getRoot();

        setUpUI();
    }

    private void setUpUI() {
        contentImageView = (ImageView) cellContent.getChildren().stream().filter((child) -> child.getId().equals("contentImageView")).collect(Collectors.toList()).get(0);
        verticalSeparation = (GridPane) cellContent.getChildren().stream().filter((child) -> child.getId().equals("verticalSeparation")).collect(Collectors.toList()).get(0);
        contentTitleLabel = (Label) verticalSeparation.getChildren().stream().filter((child) -> child.getId().equals("contentTitelLabel")).collect(Collectors.toList()).get(0);
        contentPriceLabel = (Label) verticalSeparation.getChildren().stream().filter((child) -> child.getId().equals("contentPriceLabel")).collect(Collectors.toList()).get(0);
        buyButton = (Button) verticalSeparation.getChildren().stream().filter((child) -> child.getId().equals("buyButton")).collect(Collectors.toList()).get(0);
        GridPane.setFillWidth(buyButton, true);
        GridPane.setFillHeight(buyButton, true);
        activateButton = (Button) verticalSeparation.getChildren().stream().filter((child) -> child.getId().equals("activateButton")).collect(Collectors.toList()).get(0);
    }

    /**
     * This method is called automatically from listViewForUpgrades or listViewForSkins by clicking somewhere.
     * It changes buttons texts according to buying-stat and processes the shopping.
     * @param content The shop content that is available for the player to buy or activate.
     * @param empty true if empty, else false
     */
    @Override
    public void updateItem(ShopContent content, boolean empty) {
        super.updateItem(getItem(), empty);
        this.content = content;
        setText(null);
        if (this.content != null) {
            setUpImageAndLabelsOfContent();

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
        contentTitleLabel.setText(this.content.getTitle());
        contentPriceLabel.setText("Price: " + this.content.getPrice());
    }

    private void setUpBuyButton() {
        if (persistenceUtil.isContentPurchased(content.getContentId())) {
            buyButton.setText(BOUGHT_TEXT_FOR_BUY_BUTTON);
            buyButton.setDisable(true);
        } else {
            buyButton.setText(BUY_TEXT_FOR_BUY_BUTTON);
            buyButton.setDisable(false);
        }
    }

    private void setUpActivateButton() {
        if (persistenceUtil.isContentActive(content.getContentId())) {
            activateButton.setText(DEACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
            if (contentIsAPlayerModel()) {
                spaceShipModelIsAlreadySelected = true;
            }
        } else {
            activateButton.setText(ACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
        }

        if (spaceShipModelIsAlreadySelected) {
            if (!persistenceUtil.isContentActive(content.getContentId()) && contentIsAPlayerModel()) {
                activateButton.setDisable(true);
            } else if (contentIsAPlayerModel()) {
                activateButton.setText(DEACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
                activateButton.setDisable(false);
            }
        } else if (contentIsAPlayerModel()) {
            activateButton.setDisable(false);
        }
    }

    private void processShopping() {
        if (persistenceUtil.isContentPurchased(content.getContentId())) {
            if (persistenceUtil.isContentActive(content.getContentId())) {
                deactivatePurchasedContent();
            } else {
                activatePurchasedContent();
            }
        } else {
            buyContent();
        }
    }

    private boolean contentIsAPlayerModel() {
        return content.getItemType() == ItemType.PLAYER_MODEL;
    }

    private boolean contentIsAnUpgrade() {
        return content.getItemType() == ItemType.UPGRADE;
    }

    private void deactivatePurchasedContent() {
        activateButton.setOnAction(event -> {
            if (contentIsAnUpgrade()) {
                deactivateContentInPlayerProfile();
            } else if (contentIsAPlayerModel() && (spaceShipModelIsAlreadySelected)) {
                deactivateContentInPlayerProfile();
                spaceShipModelIsAlreadySelected = false;
            }
        });
    }

    private void deactivateContentInPlayerProfile() {
        persistenceUtil.deactivateContent(content.getContentId());
        activateButton.setText(ACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
    }

    private void activatePurchasedContent() {
        activateButton.setOnAction(event -> {
            if (contentIsAnUpgrade()) {
                activateContentInPlayerProfile();
            } else if (contentIsAPlayerModel() && (!spaceShipModelIsAlreadySelected)) {
                activateContentInPlayerProfile();
                spaceShipModelIsAlreadySelected = true;
            }
        });
    }

    private void activateContentInPlayerProfile() {
        persistenceUtil.activateContent(content.getContentId());
        activateButton.setText(DEACTIVATE_TEXT_FOR_ACTIVATE_BUTTON);
    }

    private void buyContent() {
        activateButton.setDisable(true);
        buyButton.setOnAction(event -> {
            if (persistenceUtil.playerHasEnoughCoinsToBuy(content.getPrice())) {
                showPurchaseContentConfirmationDialogue();
            } else {
                showFailedToPurchaseContentAlertDialogue();
            }
        });
    }

    private void showPurchaseContentConfirmationDialogue() {
        ButtonType purchaseButtonType = new ButtonType("Purchase", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", purchaseButtonType, cancelButtonType);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Confirm Purchase");
        alert.setHeaderText(null);
        alert.setContentText("Do you really want to buy " + content.getTitle() + "?");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("Style.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == purchaseButtonType) {
            buy();
        }
    }

    private void buy() {
        persistenceUtil.buyContent(content.getContentId(), content.getPrice());
        buyButton.setText(BOUGHT_TEXT_FOR_BUY_BUTTON);
        buyButton.setDisable(true);
        activateButton.setDisable(false);
        shopContentCellControllerListeners.forEach(ShopContentCellControllerListener::purchasedItem);
    }

    public void addListener(ShopContentCellControllerListener shopContentCellControllerListener) {
        shopContentCellControllerListeners.add(shopContentCellControllerListener);
    }

    public void removeListener(ShopContentCellControllerListener shopContentCellControllerListener) {
        shopContentCellControllerListeners.remove(shopContentCellControllerListener);
    }

    private void showFailedToPurchaseContentAlertDialogue() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Purchase failed!");
        alert.setHeaderText(null);
        alert.setContentText("Not enough coins! You need at least " + persistenceUtil.getAmountOfCoinsNeededToBuyContent(content.getPrice()) + " more coins.");

        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("Style.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog");

        alert.showAndWait();
    }
}

