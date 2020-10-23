package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

import com.google.gson.Gson;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
     * Load the profile of the player from the disk
     *
     * @return the player's profile
     */
    public static PlayerProfile loadProfile() {
        Path path = Path.of(GameFile.PROFILE.getFileName());
        PlayerProfile playerProfile = null;


        if(path != null  && Files.exists(path)){
            try (FileReader reader = new FileReader(GameFile.PROFILE.getFileName())) {
                playerProfile = gson.fromJson(reader, PlayerProfile.class);
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
     * Save the player profile to the disk
     *
     * @param playerProfile player profile to save
     */
    public static void saveProfile(PlayerProfile playerProfile) {
        //gson.toJson(playerProfile, new FileWriter(GameFile.PROFILE.getFileName()));
        try (FileWriter writer = new FileWriter(GameFile.PROFILE.getFileName())) {
            gson.toJson(playerProfile, writer);
        } catch (IOException e) {
            // TODO handle
            e.printStackTrace();
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
        //TODO: Implement
        return new HashSet<>();
    }

    public static List<ShopContent> loadShopContent() {
        //TODO: Implement
        return null;
    }


}
