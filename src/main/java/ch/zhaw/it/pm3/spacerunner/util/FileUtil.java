package ch.zhaw.it.pm3.spacerunner.util;

import ch.zhaw.it.pm3.spacerunner.model.data.PurchasedShopContent;
import ch.zhaw.it.pm3.spacerunner.model.data.Settings;
import ch.zhaw.it.pm3.spacerunner.model.data.ShopContent;

import java.util.List;

public class FileUtil {


    public static Settings loadSettings(){
        return new Settings();
    }

    public static List<ShopContent> loadShopContent(){
        return null;
    }

    //Maybe load shop content model directly....
    public static List<PurchasedShopContent> loadPurchasedShopContent(){
        return null;
    }

    public static void saveSettings(Settings settings){

    }

    public static void savePurchaseShopContent(PurchasedShopContent purchasedShopContent){

    }




}
