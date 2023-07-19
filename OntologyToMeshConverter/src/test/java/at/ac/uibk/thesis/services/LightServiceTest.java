package at.ac.uibk.thesis.services;

import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.FileDTO;
import at.ac.uibk.thesis.entities.LightSource;
import at.ac.uibk.thesis.services.Dataset.DatasetService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@SpringBootTest
public class LightServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    LightService lightService;

    @Autowired
    DatasetService datasetService;

    @Autowired
    BuildingService buildingService;

    Building building;

    @BeforeEach
    public void setUpBuilding() throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(Files.readString(Path.of(TestConstants.IFCFILELOCATION)));
        fileDTO.setFileName("SensorBIM");
        building = buildingService.setUpBuilding(fileDTO);
        Assertions.assertNotNull(building);
    }

    @Test
    public void test(){
        List<LightSource> lights = lightService.getAllLightSourcesFromScene(building.getOwlDataset(),
                productService.getItemRepresentationRecursively(building).values().stream().toList());
        Assertions.assertNotNull(lights);
        Assertions.assertEquals(2, lights.size());
    }
}
