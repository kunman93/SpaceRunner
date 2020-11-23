package ch.zhaw.it.pm3.spacerunner.technicalservices.persistence.util;

/**
 * This enumeration shows the game files used by the persistence util.
 */
public enum GameFile {
    PROFILE("profile.json"),
    SHOP_CONTENT("shop_content.json");


    public static boolean TEST = false;
    private String fileName;

    private GameFile(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        if(TEST){
            return "src/test/resources/ch/zhaw/it/pm3/spacerunner/persistence/" + fileName;
        }
        return fileName;
    }
}
