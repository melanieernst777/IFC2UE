package at.ac.uibk.thesis.services.PluginUtils;

import at.ac.uibk.thesis.entities.FileDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {

    /***
     * Creates a temp file and returns the absolute path of the created file
     * @param fileDTO contains the file content of the uploaded file
     * @param name The name of the file to be created. It is advised to generate a random string to avoid conflicts
     * @return The absolute path of the created file
     */
    public static String creatTmpFile(FileDTO fileDTO, String name) {
        try {
            File file = File.createTempFile(name, ".ifc");
            Files.write(file.toPath(), fileDTO.getFileContent().getBytes());
            return file.getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves the content of a file
     * @param fileName The name of the file of which we want to retrieve the content
     * @return The content of the file, empty if the file does not exist
     */
    public static String getFileContentAsString(String fileName) {
        Path path = Path.of(fileName);
        if (Files.exists(path)) {
            try {
                return Files.readString(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return "";
    }
}
