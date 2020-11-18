package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;

public class ShopContent {

    //1.5 x Powerup Chance
    //Doppelte Coin Powerup hält 15 anstatt 10 sekunden

    //Content der im shop zum Kaufen erhältlich ist
    private ContentId contentId;

    private String title;

    private String description;
    private int price;

    private VisualSVGFile imageId;
    private ItemType itemType;
    private transient boolean purchased;
    private transient boolean equipped;

    public ShopContent(ContentId contentId, String title, String description, int price, VisualSVGFile imageId, ItemType itemType) {
        this.contentId = contentId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageId = imageId;
        this.itemType = itemType;
    }

    public void buyContent() {
        purchased = true;
    }

    public void equipContent(boolean equipped) {
        this.equipped = equipped;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public boolean isEquipped() {
        return equipped;
    }


    public ContentId getContentId() {
        return contentId;
    }

    public void setContentId(ContentId contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public VisualSVGFile getImageId() {
        return imageId;
    }

    public void setImageId(VisualSVGFile imageId) {
        this.imageId = imageId;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }
}
