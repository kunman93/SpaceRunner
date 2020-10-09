package ch.zhaw.it.pm3.spacerunner.enumerable;

public enum GameFile {
    SETTINGS("settings.json"),
    SHOP_CONTENT("shop_content.json");

    


    private String file;
    private GameFile(String file){
        this.file = file;
    }


}
