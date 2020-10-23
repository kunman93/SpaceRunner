package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

import ch.zhaw.it.pm3.spacerunner.SpaceRunnerApp;
import com.google.common.io.Resources;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

public class PersistanceUtil {


    public static PlayerProfile loadProfile(){

        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setPurchasedContent(loadPurchasedContent(playerProfile.getPurchasedContentIds()));
        return playerProfile;
    }

    public static void saveProfile(PlayerProfile playerProfile){

    }

    public static Image loadImage(URL imageURL) throws IOException {
        Image image = new ImageIcon(imageURL).getImage();
        return image;
    }


    //TODO: Maybe move this to FileUtil
    private static Set<ShopContent> loadPurchasedContent(Set<Integer> purchasedContentIds){
        return new HashSet<>();
    }

    public static List<ShopContent> loadShopContent(){
        return null;
    }




}
