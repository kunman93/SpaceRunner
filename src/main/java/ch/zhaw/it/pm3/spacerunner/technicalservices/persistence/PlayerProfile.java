package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class PlayerProfile {

    private boolean audioEnabled;
    private int volume;
    //Id des gekauften / default player model
    private int playerImageId;
    private String playerName;
    private int coins;
    //gekaufte shop inhalte
    private Set<Integer> purchasedContentIds;
    //aktivierte shop inhalte
    private Set<Integer> activeContentIds;
    private int fps;

    //TODO: Do not persist
    private Set<ShopContent> activeShopContent;

    public static boolean TEST = false;


    public PlayerProfile(){
        audioEnabled = true;
        volume = 100;
        playerImageId = -1;
        playerName = "Player1";
        coins = 0;
        fps = 60;
        purchasedContentIds = new HashSet<>();
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

    public int getPlayerImageId() {
        return playerImageId;
    }

    public void setPlayerImageId(int playerImageId) {
        this.playerImageId = playerImageId;
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

    public Set<Integer> getPurchasedContentIds() {
        return purchasedContentIds;
    }

    public void addContent(int contentId){
        purchasedContentIds.add(contentId);
    }

    public Set<Integer> getActiveContentIds() {
        return activeContentIds;
    }

    public void deactivateContent(int contentId) {
        activeContentIds.remove(contentId);
    }

    public void activateContent(int contentId) {
        activeContentIds.add(contentId);
    }

    public Set<ShopContent> getActiveShopContent() {
        return activeShopContent;
    }

    public void setActiveShopContent(Set<ShopContent> activeShopContent) {
        this.activeShopContent = activeShopContent;
    }

    public void setPurchasedContentIds(Set<Integer> purchasedContentIds) {
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
        if(TEST){
            if(o instanceof PlayerProfile){
                PlayerProfile that = (PlayerProfile) o;
                return audioEnabled == that.audioEnabled &&
                        volume == that.volume &&
                        playerImageId == that.playerImageId &&
                        coins == that.coins &&
                        fps == that.fps &&
                        Objects.equals(playerName, that.playerName) &&
                        Objects.equals(purchasedContentIds, that.purchasedContentIds) &&
                        activeShopContent.size() == that.activeShopContent.size();
            }else{
                return super.equals(o);
            }
        }else{
            return super.equals(o);
        }


    }
}
