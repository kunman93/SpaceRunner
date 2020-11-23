package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Data model for the personal profile of the player
 * @author islermic
 */
public class PlayerProfile {

    private boolean audioEnabled;
    private int volume;
    private String playerName;
    private int coins;
    private int highScore;
    //gekaufte shop inhalte
    private Set<ContentId> purchasedContentIds;
    //aktivierte shop inhalte
    private Set<ContentId> activeContentIds;
    private int fps;

    private transient Set<ShopContent> activeShopContent;

    public static boolean TEST = false;


    public PlayerProfile() {
        audioEnabled = true;
        volume = 100;
        playerName = "Player1";
        coins = 0;
        highScore = 0;
        fps = 60;
        purchasedContentIds = new HashSet<>();
        activeContentIds = new HashSet<>();
        activeShopContent = new HashSet<>();
    }

    public boolean isAudioEnabled() {
        return audioEnabled;
    }

    public void setAudioEnabled(boolean audioEnabled) {
        this.audioEnabled = audioEnabled;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public void addCoins(int coins) {
        this.coins += coins;
    }

    public void subtractCoins(int coins) {
        this.coins -= coins;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public Set<ContentId> getPurchasedContentIds() {
        return purchasedContentIds;
    }

    public void addContent(ContentId contentId) {
        purchasedContentIds.add(contentId);
    }

    public Set<ContentId> getActiveContentIds() {
        return activeContentIds;
    }

    public void deactivateContent(ContentId contentId) {
        activeContentIds.remove(contentId);
    }

    public void activateContent(ContentId contentId) {
        activeContentIds.add(contentId);
    }

    public Set<ShopContent> getActiveShopContent() {
        return activeShopContent;
    }

    public void setActiveShopContent(Set<ShopContent> activeShopContent) {
        this.activeShopContent = activeShopContent;
    }

    public void setPurchasedContentIds(Set<ContentId> purchasedContentIds) {
        this.purchasedContentIds = purchasedContentIds;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    @Override
    public boolean equals(Object o) {
        if (TEST) {
            if (o instanceof PlayerProfile) {
                PlayerProfile that = (PlayerProfile) o;
                return audioEnabled == that.audioEnabled &&
                        volume == that.volume &&
                        coins == that.coins &&
                        fps == that.fps &&
                        Objects.equals(playerName, that.playerName) &&
                        Objects.equals(purchasedContentIds, that.purchasedContentIds) &&
                        activeShopContent.size() == that.activeShopContent.size();
            } else {
                return super.equals(o);
            }
        } else {
            return super.equals(o);
        }
    }
}
