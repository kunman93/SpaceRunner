package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util;

public enum GameFile {
    PROFILE("profile.json"),
    SHOP_CONTENT("shop_content.json");


    private String fileName;
    private GameFile(String fileName){
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
