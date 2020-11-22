package ch.zhaw.it.pm3.spacerunner.ui;

/**
 * The ShopContentCellControllerListener is used to depict the updated coin-value in ShopViewController,
 * when a shop content was purchased in ShopContentCellController by pressing the buy-Button.
 * @author kunnuman
 */
public interface ShopContentCellControllerListener {

    /**
     * Performs the action when a shop content was purchased.
     */
    void purchasedItem();
}
