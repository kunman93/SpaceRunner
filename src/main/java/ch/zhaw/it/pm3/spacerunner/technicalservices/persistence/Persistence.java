package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

import java.util.List;

/**
 * Interface for Persistence (Database / File Storage)
 */
public interface Persistence {

    /**
     * Checks if the user has activated the double duration upgrade for the coin power up.
     * @return if it is activated
     */
    boolean hasDoubleDurationForCoinPowerUp();

    /**
     * Checks if the user has activated the power up chance multiplier upgrade.
     * @return if it is activated
     */
    boolean hasPowerUpChanceMultiplierUpgrade();

    /**
     * Deactivated the content with the specified contentId in the profile of the user.
     * @param contentId id to be deactivated
     */
    void deactivateContent(ContentId contentId);

    /**
     * Activated the content with the specified contentId in the profile of the user.
     * @param contentId id to be activated
     */
    void activateContent(ContentId contentId);

    /**
     * returns the amount of coins needed to buy the content with the given price. If the player has enough coins => 0 is returned
     * @param price price of the content
     * @return coins needed to be able to buy the content
     */
    int getAmountOfCoinsNeededToBuyContent(int price);

    /**
     * Checks if the player has enough coins to buy the content with the given price.
     * @param price price of the content
     * @return user has enough coins
     */
    boolean playerHasEnoughCoinsToBuy(int price);

    /**
     * Buys the content with the specified content if. => Subtracts coins from profile and adds the content to the profile.
     * @param contentId content to add to profile
     * @param price price of content
     */
    void buyContent(ContentId contentId, int price);

    /**
     * Checks if the specified content is active.
     * @param contentId content to check for
     * @return is active
     */
    boolean contentIsActive(ContentId contentId);

    /**
     * Checks if the specified content is already purchased.
     * @param contentId content to check
     * @return is purchased
     */
    boolean contentIsPurchased(ContentId contentId);

    /**
     * Get the specified sound volume from the profile.
     * @return sound volume
     */
    int getSoundVolume();

    /**
     * Returns if the audio is enabled.
     * @return is audio enabled
     */
    boolean isAudioEnabled();

    /**
     * Load the profile of the player.
     * @return the player's profile
     */
    PlayerProfile loadProfile();

    /**
     * Save the player profile
     * @param playerProfile player profile to save
     */
    void saveProfile(PlayerProfile playerProfile);

    /**
     * Loads the shop content list.
     * @return shop content list
     */
    List<ShopContent> loadShopContent();
}
