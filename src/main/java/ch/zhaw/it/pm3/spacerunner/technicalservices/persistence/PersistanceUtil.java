package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersistanceUtil {


    public static PlayerProfile loadProfile(){

        PlayerProfile playerProfile = new PlayerProfile();
        playerProfile.setPurchasedContent(loadPurchasedContent(playerProfile.getPurchasedContentIds()));
        return playerProfile;
    }

    public static void saveProfile(PlayerProfile playerProfile){

    }

    public static Image loadImage(String imageName) throws IOException {
        BufferedImage image = ImageIO.read(new File("resources/images", imageName));
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
