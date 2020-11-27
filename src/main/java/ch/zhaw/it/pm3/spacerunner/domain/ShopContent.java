package ch.zhaw.it.pm3.spacerunner.domain;

import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;

/**
 * Data model for the content in the shop
 * @author islermic
 */
public class ShopContent {

    private ContentId contentId;

    private String title;

    private String description;
    private int price;

    private VisualSVGFile imageId;
    private ItemType itemType;
    private transient boolean purchased;

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

    public boolean isPurchased() {
        return purchased;
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
