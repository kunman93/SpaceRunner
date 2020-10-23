package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Utility tool to persist data (load / save)
 */
public class PersistenceUtil {


    /**
     * Load the profile of the player from the disk
     *
     * @return the player's profile
     */
    public static PlayerProfile loadProfile() {

        //TODO: Implement
        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setPurchasedContent(loadPurchasedContent(playerProfile.getPurchasedContentIds()));
        return playerProfile;
    }

    /**
     * Save the player profile to the disk
     *
     * @param playerProfile player profile to save
     */
    public static void saveProfile(PlayerProfile playerProfile) {

    }

    /**
     * Loads the image from the URL provided
     *
     * @param imageURL URL of the image to load
     * @return loaded image
     */
    public static Image loadImage(URL imageURL) {
        Image image = new ImageIcon(imageURL).getImage();
        return image;
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
