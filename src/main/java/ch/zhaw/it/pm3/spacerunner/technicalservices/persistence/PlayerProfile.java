package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence;

import java.util.HashSet;
import java.util.Set;

public class PlayerProfile {

    private boolean audioEnabled;
    private int volume;
    //Id des gekauften / default player model
    private int playerImageId;
    private String playerName;
    private int coins;
    private Set<Integer> purchasedContentIds;
    private int fps;

    //TODO: Do not persist
    private Set<ShopContent> purchasedContent;


    public PlayerProfile(){
        audioEnabled = true;
        volume = 100;
        playerImageId = -1;
        playerName = "Player1";
        coins = 0;
        fps = 60;
        purchasedContentIds = new HashSet<>();
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

    public Set<ShopContent> getPurchasedContent() {
        return purchasedContent;
    }

    public void setPurchasedContent(Set<ShopContent> purchasedContent) {
        this.purchasedContent = purchasedContent;
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
}
