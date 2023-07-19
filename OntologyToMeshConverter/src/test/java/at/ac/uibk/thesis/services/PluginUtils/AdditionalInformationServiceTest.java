package at.ac.uibk.thesis.services.PluginUtils;

import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.FileDTO;
import at.ac.uibk.thesis.services.BuildingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

@SpringBootTest
public class AdditionalInformationServiceTest {

    @Autowired
    AdditionalInformationService additionalInformationService;

    @Autowired
    BuildingService buildingService;

    Building building;

    @BeforeEach
    public void testSetUpBuilding() throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(Files.readString(Path.of(TestConstants.IFCFILELOCATION)));
        fileDTO.setFileName("SensorBIM");
        building = buildingService.setUpBuilding(fileDTO);
        Assertions.assertNotNull(building);
        Assertions.assertEquals(building.getFileName(), fileDTO.getFileName());
        Assertions.assertTrue(building.getItems().isEmpty());
    }
    @Test
    public void testAdditionalInformation(){
        Map<String, Object> additionalInformation = additionalInformationService.addAdditionalInformationToName(building);
        Assertions.assertEquals(3, additionalInformation.size());
        Assertions.assertTrue(additionalInformation.containsKey("buildingName"));
        Assertions.assertTrue(additionalInformation.containsKey("lightObjects"));
        Assertions.assertNotNull(additionalInformation.get("lightObjects"));
    }
}
