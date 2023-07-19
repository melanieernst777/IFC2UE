package at.ac.uibk.thesis.services.PluginUtils;

import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.FileDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertThrows;

@SpringBootTest
public class FileServiceTest {

    @Test
    public void testUploadingFile() throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(Files.readString(Path.of(TestConstants.INVALID_IFC)));
        String prefix = "SensorBIM";
        String suffix = "ifc";
        fileDTO.setFileName(prefix);
        String result = FileService.creatTmpFile(fileDTO, fileDTO.getFileName());
        Assertions.assertTrue(result.contains(prefix));
        Assertions.assertTrue(result.contains(suffix));
        Assertions.assertTrue(result.endsWith(suffix));
    }

    @Test
    public void testUploadingInvalidFile() throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(Files.readString(Path.of(TestConstants.INVALID_IFC)));
        String prefix = "?";
        fileDTO.setFileName(prefix);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            FileService.creatTmpFile(fileDTO, fileDTO.getFileName());
        });
        String expectedMessage = "Prefix string \"?\" too short: length must be at least 3";
        String actualMessage = exception.getMessage();

        Assertions.assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    public void testReadingFile() {
        String expected = "This is a invalid file";
        String result = FileService.getFileContentAsString(String.valueOf(Path.of(TestConstants.INVALID_TTL)));
        Assertions.assertEquals(expected, result);
    }
}
