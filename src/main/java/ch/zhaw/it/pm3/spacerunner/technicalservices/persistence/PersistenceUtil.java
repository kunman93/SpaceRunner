package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

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
import java.util.stream.Collectors;

/**
 * Utility tool to persist data (load / save)
 */
public class PersistenceUtil {
    // Singleton pattern
    private static final PersistenceUtil instance = new PersistenceUtil();

    private static final Gson gson = new Gson();

    /**
     * private constructor for the singleton-pattern
     */
    private PersistenceUtil(){}

    public static PersistenceUtil getInstance(){
        return instance;
    }



    public int getSoundVolume(){
        return loadProfile().getVolume();
    }

    public boolean isAudioEnabled(){
        return loadProfile().isAudioEnabled();
    }

    /**
     * Load the profile of the player from the disk where it is saved in json.
     * If there is no profile.json found it will return a new default profile!
     *
     * @return the player's profile (or a default profile if it doesn't exist)
     */
    public PlayerProfile loadProfile() {
        Path path = Path.of(GameFile.PROFILE.getFileName());
        PlayerProfile playerProfile = null;


        if (path != null && Files.exists(path)) {
            try {
                playerProfile = loadAndDeserializeData(GameFile.PROFILE.getFileName(), PlayerProfile.class);
            } catch (IOException e) {
                // TODO handle
                e.printStackTrace();
                playerProfile = new PlayerProfile();
            }
        } else {
            playerProfile = new PlayerProfile();
        }

        //TODO: implement
        playerProfile.setActiveShopContent(loadPurchasedContent(playerProfile.getPurchasedContentIds()));

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
    public void saveProfile(PlayerProfile playerProfile) {
        if (playerProfile == null) {
            throw new IllegalArgumentException("null is not a legal argument for a player profile!");
        }
        //gson.toJson(playerProfile, new FileWriter(GameFile.PROFILE.getFileName()));
        try {
            serializeAndSaveData(GameFile.PROFILE.getFileName(), playerProfile);
        } catch (IOException e) {
            // TODO handle
            e.printStackTrace();
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
    private Set<ShopContent> loadPurchasedContent(Set<ContentId> purchasedContentIds) {
        List<ShopContent> shopContentList = loadShopContent();
        return shopContentList.stream().filter((shopContent)->{
            return purchasedContentIds.stream().anyMatch((purchasedContentId) ->{
                return purchasedContentId == shopContent.getContentId();
            });
        }).collect(Collectors.toSet());
    }

    //TODO: JavaDOC
    public List<ShopContent> loadShopContent() {
        Type listOfShopContentType = new TypeToken<ArrayList<ShopContent>>() {}.getType();

        List<ShopContent> shopContentList = null;
        try {
            shopContentList = loadAndDeserializeData(GameFile.SHOP_CONTENT.getFileName(), listOfShopContentType);
        } catch (IOException e) {
            // TODO handle
            e.printStackTrace();
        }
        return shopContentList;
    }


}
