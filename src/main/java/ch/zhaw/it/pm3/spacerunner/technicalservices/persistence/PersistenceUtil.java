package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

import com.google.gson.Gson;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.TranscodingHints;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.util.SVGConstants;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility tool to persist data (load / save)
 */
public class PersistenceUtil {
    private static final Gson gson = new Gson();

    /**
     * Load the profile of the player from the disk where it is saved in json.
     * If there is no profile.json found it will return a new default profile!
     *
     * @return the player's profile (or a default profile if it doesn't exist)
     */
    public static PlayerProfile loadProfile() {
        Path path = Path.of(GameFile.PROFILE.getFileName());
        PlayerProfile playerProfile = null;


        if(path != null  && Files.exists(path)){
            try {
                playerProfile = loadAndDeserializeData(GameFile.PROFILE.getFileName(), PlayerProfile.class);
            } catch (IOException e) {
                // TODO handle
                e.printStackTrace();
                playerProfile = new PlayerProfile();
            }
        }else{
            playerProfile = new PlayerProfile();
        }

        //TODO: implement
        playerProfile.setPurchasedContent(loadPurchasedContent(playerProfile.getPurchasedContentIds()));

        return playerProfile;
    }

    /**
     * load and deserialize data from path to a data object of class T
     * @param path path to load data from
     * @param dataClass Class of data object
     * @param <T> Type of the data object
     * @return loaded data as object of class T
     * @throws IOException
     */
    public static <T> T loadAndDeserializeData(String path, Class<T> dataClass) throws IOException {
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
    public static void saveProfile(PlayerProfile playerProfile) {
        if(playerProfile == null){
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
     * @param path path for the file
     * @param data data to serialize and save
     * @param <T> type of the data to be saved
     * @throws IOException
     */
    public static <T> void serializeAndSaveData(String path, T data) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw e;
        }
    }



    private static Set<ShopContent> loadPurchasedContent(Set<Integer> purchasedContentIds) {
        //TODO: Implement and JavaDOC
        return new HashSet<>();
    }

    public static List<ShopContent> loadShopContent() {
        //TODO: Implement and JavaDOC
        return null;
    }


}
