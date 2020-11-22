package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

import java.util.List;

public interface Persistence {

    //TODO: JavaDOC and name
    boolean hasDoubleDurationForCoinPowerUp();

    //TODO: JavaDOC and name
    boolean hasPowerUpChanceMultiplierUpgrade();

    //TODO: JavaDOC and name
    void deactivateContent(ContentId contentId);

    //TODO: JavaDOC and name
    void activateContent(ContentId contentId);

    //TODO: JavaDOC and name
    int getAmountOfCoinsNeededToBuyContent(int price);

    //TODO: JavaDOC and name
    boolean playerHasEnoughCoinsToBuy(int price);

    //TODO: JavaDOC and name
    void buyContent(ContentId contentId, int price);

    //TODO: JavaDOC and name
    boolean contentIsActive(ContentId contentId);

    //TODO: JavaDOC and name
    boolean contentIsPurchased(ContentId contentId);

    //TODO: JavaDOC and name
    int getSoundVolume();

    //TODO: JavaDOC and name
    boolean isAudioEnabled();

    PlayerProfile loadProfile();

    void saveProfile(PlayerProfile playerProfile);

    //TODO: JavaDOC
    List<ShopContent> loadShopContent();
}
