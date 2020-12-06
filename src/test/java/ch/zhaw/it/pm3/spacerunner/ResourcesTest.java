package ch.zhaw.it.pm3.spacerunner;

import ch.zhaw.it.pm3.spacerunner.technicalservices.sound.util.GameSound;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualFile;
import ch.zhaw.it.pm3.spacerunner.technicalservices.visual.util.VisualSVGFile;
import ch.zhaw.it.pm3.spacerunner.ui.FXMLFile;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.fail;

/**
 * These tests will find unused resources and FileResource enum entries which don't have an existing file in the resources!
 * <p>
 * It's purpose is to clean up resources and enums!
 */
public class ResourcesTest {
    private final String ResourcePath = "src/main/resources/ch/zhaw/it/pm3/spacerunner/";


    @Test
    void testIfSoundEnumValuesExistInFileSystem() {
        for (GameSound gameSound : GameSound.values()) {
            if (!Files.exists(Path.of(ResourcePath + gameSound.getFileName()))) {
                fail("The SoundFile for " + gameSound.name() + " was not found! " + gameSound.getFileName() + " does not exist!");
            }

        }
    }

    @Test
    void testIfSoundResourceFilesHaveAnEnumEntry() {
        checkIfPathFilesWithExtensionExistInEnum(GameSound.class, new String[]{"wav"}, "sound", "Sound");
    }

    @Test
    void testIfImageEnumValuesExistInFileSystem() {
        for (VisualSVGFile image : VisualSVGFile.values()) {
            if (!Files.exists(Path.of(ResourcePath + image.getFileName()))) {
                fail("The VisualSVGFile for " + image.name() + " was not found! " + image.getFileName() + " does not exist!");
            }
        }

        for (VisualFile image : VisualFile.values()) {
            if (!Files.exists(Path.of(ResourcePath + image.getFileName()))) {
                fail("The VisualFile for " + image.name() + " was not found! " + image.getFileName() + " does not exist!");
            }

        }
    }

    @Test
    void testIfImageResourceFilesHaveAnEnumEntry() {
        checkIfPathFilesWithExtensionExistInEnum(VisualSVGFile.class, new String[]{"svg"}, "images", "SVG Image");
        checkIfPathFilesWithExtensionExistInEnum(VisualFile.class, new String[]{"jpg", "jpeg", "gif", "png"}, "images", "Image");
    }

    @Test
    void testIfFXMLEnumValuesExistInFileSystem() {
        for (FXMLFile image : FXMLFile.values()) {
            if (!Files.exists(Path.of(ResourcePath + image.getFileName()))) {
                fail("The VisualSVGFile for " + image.name() + " was not found! " + image.getFileName() + " does not exist!");
            }
        }

    }

    @Test
    void testIfFXMLResourceFilesHaveAnEnumEntry() {
        checkIfPathFilesWithExtensionExistInEnum(FXMLFile.class, new String[]{"fxml"}, "ui", "FXML");
    }

    private <T extends Enum & FileResource> void checkIfPathFilesWithExtensionExistInEnum(Class<T> enumToCheck, String[] extensions, String subfolder, String typeForError) {
        try (Stream<Path> filePathStream = Files.walk(Path.of(ResourcePath + subfolder))) {
            filePathStream.forEach(filePath -> {
                if (Files.isRegularFile(filePath) && Arrays.asList(extensions).stream().anyMatch((extension) -> filePath.toString().endsWith(extension))) {
                    boolean FileExistsInEnum = false;
                    for (T enumConstant : enumToCheck.getEnumConstants()) {
                        if (enumConstant.getFileName().equals(getFilePathRelativeToFolder(filePath, subfolder))) {
                            FileExistsInEnum = true;
                            break;
                        }
                    }

                    if (!FileExistsInEnum) {
                        fail("The " + typeForError + " file " + filePath.toString() + " has no entry in the " + enumToCheck.getSimpleName() + " enum => unused resource!");
                    }
                }
            });
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private String getFilePathRelativeToFolder(Path filePath, String subfolder) {
        String pathString = filePath.toString();
        int subfolderIndex = pathString.indexOf(subfolder);

        return pathString.substring(subfolderIndex).replace("\\", "/");
    }

}
