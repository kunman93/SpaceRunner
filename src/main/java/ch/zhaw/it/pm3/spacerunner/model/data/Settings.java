package ch.zhaw.it.pm3.spacerunner.model.data;

public class Settings {



    private boolean audioEnabled;
    private int volume;
    //Id des gekauften / default player model
    private int playerImageId;
    private String playerName;



    public Settings(){
        audioEnabled = true;
        volume = 100;
        playerImageId = -1;
        playerName = "Player1";
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
}
