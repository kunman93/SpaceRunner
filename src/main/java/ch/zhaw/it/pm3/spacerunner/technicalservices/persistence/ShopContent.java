package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

public class ShopContent {

    //Content der im shop zum Kaufen erhältlich ist
    private ContentId contentId;

    private String title;
    private int price = 0;
    private int imageId;
    private ItemType itemType;
    private boolean purchased;

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    private boolean equipped;


    public ShopContent(String title, ItemType itemType, ContentId contentId, boolean purchased) {
        this.title = title;
        this.itemType = itemType;
        this.contentId = contentId;
        this.purchased = purchased;
    }

    public boolean buyContent() {
        purchased = true;
        return purchased;
    }

    public ContentId getContentId() {
        return contentId;
    }

    public String getTitle() {
        return title;
    }

    public int getPrice() {
        return price;
    }

    public int getImageId() {
        return imageId;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public boolean isEquipped() {
        return equipped;
    }
}
