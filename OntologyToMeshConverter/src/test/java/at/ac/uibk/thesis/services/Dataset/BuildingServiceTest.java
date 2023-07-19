package at.ac.uibk.thesis.services.Dataset;

import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.FileDTO;
import at.ac.uibk.thesis.services.BuildingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
public class BuildingServiceTest {

    @Autowired
    BuildingService buildingService;

    @Test
    public void testSetUpBuilding() throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(Files.readString(Path.of(TestConstants.IFCFILELOCATION)));
        fileDTO.setFileName("SensorBIM");
        Building building = buildingService.setUpBuilding(fileDTO);
        Assertions.assertNotNull(building);
        Assertions.assertEquals(building.getFileName(), fileDTO.getFileName());
        Assertions.assertTrue(building.getItems().isEmpty());
    }

    @Test
    public void testSetUpInvalidBuilding() {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent("");
        fileDTO.setFileName("SensorBIM");
        Building building = buildingService.setUpBuilding(fileDTO);
        Assertions.assertNull(building);
    }
}
