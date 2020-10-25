package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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


    public static <T> void serializeAndSaveData(String path, T data) throws IOException {
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            throw e;
        }
    }

    /**
     * Loads the image from the URL provided
     *
     * @param imageURL URL of the image to load
     * @return loaded image
     */
    public static BufferedImage loadImage(URL imageURL) {
        Image image = new ImageIcon(imageURL).getImage();
        return toBufferedImage(image);
    }


    //TODO: Declare as copied from internet. (Code is from stackoverflow https://stackoverflow.com/questions/13605248/java-converting-image-to-bufferedimage)
    /**
     * Converts a given Image into a BufferedImage
     *
     * @param img The Image to be converted
     * @return The converted BufferedImage
     */
    private static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
            return (BufferedImage) img;
        }

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
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
