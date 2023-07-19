package at.ac.uibk.thesis.services.Movement;

import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.services.Dataset.DatasetService;
import com.aspose.threed.Vector3;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class GeometryServiceTest {

    @Autowired
    GeometryService geometryService;

    @Autowired
    DatasetService datasetService;

    IfcOWLDataset dataset;

    @BeforeEach
    public void setUpDataset() {
        dataset = new IfcOWLDataset(datasetService.getDataset(Path.of(TestConstants.TTLFILELOCATION).toAbsolutePath().toString()));
        Assert.notNull(dataset, "The dataset shall not be null");
        Assertions.assertFalse(dataset.getDataset().isEmpty());
    }


    @Test
    public void testGetTranslationForList(){
        List<Vector3> translations = new ArrayList<>();
        translations.add(new Vector3(1,2,3));
        translations.add(new Vector3(1,2,3));
        Vector3 result = geometryService.getTranslation(translations);
        Vector3 expected = new Vector3(2,4,6);
        Assertions.assertEquals(result, expected);
    }

    @Test
    public void testGetTranslationForItem(){
        Item item = new Item(new Building("Some Building"), "WallItem");
        List<Vector3> translations = new ArrayList<>();
        translations.add(new Vector3(1,2,3));
        translations.add(new Vector3(1,2,3));
        item.setTranslations(translations);
        Vector3 result = geometryService.getTranslation(item);
        Vector3 expected = new Vector3(2,4,6);
        Assertions.assertEquals(result, expected);
    }

}
