package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util;

import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ContentId;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.Persistence;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.PlayerProfile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.ShopContent;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Utility tool to persist data (load / save)
 */
public class PersistenceUtil implements Persistence {

    private Logger logger = Logger.getLogger(PersistenceUtil.class.getName());

    // Singleton pattern
    private static final PersistenceUtil persistenceUtil = new PersistenceUtil();

    private static final Gson gson = new Gson();

    /**
     * private constructor for the singleton-pattern
     */
    private PersistenceUtil() {
    }

    public static PersistenceUtil getUtil() {
        return persistenceUtil;
    }

    @Override
    public boolean hasDoubleDurationForCoinPowerUp() {
        PlayerProfile profile = loadProfile();
        return profile.getActiveContentIds().stream().anyMatch((activeContent) -> activeContent.equals(ContentId.DOUBLE_DURATION_COIN_UPGRADE));
    }

    @Override
    public boolean hasPowerUpChanceMultiplierUpgrade() {
        PlayerProfile profile = loadProfile();
        return profile.getActiveContentIds().stream().anyMatch((activeContent) -> activeContent.equals(ContentId.POWER_UP_CHANCE_MULTIPLIER));
    }

    //TODO: JavaDOC and name
    @Override
    public void deactivateContent(ContentId contentId) {
        PlayerProfile profile = loadProfile();
        profile.deactivateContent(contentId);
        saveProfile(profile);
    }

    //TODO: JavaDOC and name
    @Override
    public void activateContent(ContentId contentId) {
        PlayerProfile profile = loadProfile();
        profile.activateContent(contentId);
        saveProfile(profile);
    }

    //TODO: JavaDOC and name
    @Override
    public int getAmountOfCoinsNeededToBuyContent(int price) {
        PlayerProfile profile = loadProfile();
        if (profile.getCoins() >= price) {
            return 0;
        }
        return price - profile.getCoins();
    }

    //TODO: JavaDOC and name
    @Override
    public boolean playerHasEnoughCoinsToBuy(int price) {
        PlayerProfile profile = loadProfile();
        return profile.getCoins() >= price;
    }

    //TODO: JavaDOC and name
    @Override
    public void buyContent(ContentId contentId, int price) {
        PlayerProfile profile = loadProfile();
        profile.subtractCoins(price);
        profile.addContent(contentId);
        saveProfile(profile);
    }

    //TODO: JavaDOC and name
    @Override
    public boolean contentIsActive(ContentId contentId) {
        PlayerProfile profile = loadProfile();
        return profile.getActiveContentIds().contains(contentId);
    }

    //TODO: JavaDOC and name
    @Override
    public boolean contentIsPurchased(ContentId contentId) {
        PlayerProfile profile = loadProfile();
        return profile.getPurchasedContentIds().contains(contentId);
    }

    //TODO: JavaDOC and name
    @Override
    public int getSoundVolume() {
        return loadProfile().getVolume();
    }

    //TODO: JavaDOC and name
    @Override
    public boolean isAudioEnabled() {
        return loadProfile().isAudioEnabled();
    }

    /**
     * Load the profile of the player from the disk where it is saved in json.
     * If there is no profile.json found it will return a new default profile!
     *
     * @return the player's profile (or a default profile if it doesn't exist)
     */
    @Override
    public PlayerProfile loadProfile() {
        Path path = Path.of(GameFile.PROFILE.getFileName());
        PlayerProfile playerProfile = null;


        if (path != null && Files.exists(path)) {
            try {
                playerProfile = loadAndDeserializeData(GameFile.PROFILE.getFileName(), PlayerProfile.class);
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Unable to Load and / or Deserialize Data");
                playerProfile = new PlayerProfile();
            }
        } else {
            playerProfile = new PlayerProfile();
        }

        playerProfile.setActiveShopContent(loadActiveContent(playerProfile.getActiveContentIds()));

        return playerProfile;
    }

    /**
     * load and deserialize data from path to a data object of class T
     *
     * @param path      path to load data from
     * @param dataClass Class of data object
     * @param <T>       Type of the data object
     * @return loaded data as object of class T
     * @throws IOException
     */
    public <T> T loadAndDeserializeData(String path, Type dataClass) throws IOException {
        T data = null;
        try (FileReader reader = new FileReader(path)) {
            data = gson.fromJson(reader, dataClass);
        } catch (IOException e) {
            throw e;
        }
        return data;
    }

    /**
     * Save the player profile in json-format to the disk
     *
     * @param playerProfile player profile to save
     */
    @Override
    public void saveProfile(PlayerProfile playerProfile) {
        if (playerProfile == null) {
            throw new IllegalArgumentException("null is not a legal argument for a player profile!");
        }
        //gson.toJson(playerProfile, new FileWriter(GameFile.PROFILE.getFileName()));
        try {
            serializeAndSaveData(GameFile.PROFILE.getFileName(), playerProfile);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Unable to Serialize and / or load Data");
        }
    }


    /**
     * Serialize the data object of type T and save it as JSON to the path
     *
     * @param path path for the file
     * @param data data to serialize and save
     * @param <T>  type of the data to be saved
     * @throws IOException
     */
    public <T> void serializeAndSaveData(String path, T data) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw e;
        }
    }

    //TODO: JavaDOC
    private Set<ShopContent> loadActiveContent(Set<ContentId> activeContentIds) {
        List<ShopContent> shopContentList = loadShopContent();
        return shopContentList.stream().filter((shopContent) -> {
            return activeContentIds.stream().anyMatch((purchasedContentId) -> {
                return purchasedContentId == shopContent.getContentId();
            });
        }).collect(Collectors.toSet());
    }

    //TODO: JavaDOC
    private Set<ShopContent> loadPurchasedContent(Set<ContentId> purchasedContentIds) {
        List<ShopContent> shopContentList = loadShopContent();
        return shopContentList.stream().filter((shopContent) -> {
            return purchasedContentIds.stream().anyMatch((purchasedContentId) -> {
                return purchasedContentId == shopContent.getContentId();
            });
        }).collect(Collectors.toSet());
    }

    //TODO: JavaDOC
    @Override
    public List<ShopContent> loadShopContent() {
        Type listOfShopContentType = new TypeToken<ArrayList<ShopContent>>() {
        }.getType();

        List<ShopContent> shopContentList = null;
        try {
            shopContentList = loadAndDeserializeData(GameFile.SHOP_CONTENT.getFileName(), listOfShopContentType);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error with Loading and / or Deserializing Data");
        }
        return shopContentList;
    }
}
