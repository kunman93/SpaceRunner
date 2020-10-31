package ch.zhaw.it.pm3.spacerunner.technicalservices.visual;

/**
 * Enum only for SVG files
 */
public enum ImageSVGFile {
    SHINEY_COIN_1("coin/shiny-coin1.svg"),
    SHINEY_COIN_2("coin/shiny-coin2.svg"),
    SHINEY_COIN_3("coin/shiny-coin3.svg"),
    SHINEY_COIN_4("coin/shiny-coin4.svg"),
    SHINEY_COIN_5("coin/shiny-coin5.svg"),
    SHINEY_COIN_6("coin/shiny-coin6.svg");





    private String fileName;

    private ImageSVGFile(String fileName) {
        this.fileName = "images/" + fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
