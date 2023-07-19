package at.ac.uibk.thesis.services.Dataset;

import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.services.Dataset.IFCItems.PolylineService;
import com.aspose.threed.Vector3;
import com.aspose.threed.Vector4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.nio.file.Path;

@SpringBootTest
public class PolylineServiceTest {

    @Autowired
    PolylineService polylineService;

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
    public void testExtractDataFromSmallPolyline(){
        String representation = "http://linkedbuildingdata.net/ifc/resources/IfcPolyline_2472";
        Item item = new Item(new Building("Some Building"), "WallItem");
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        polylineService.extractDataFromPolyline(dataset, itemGeometry, representation);
        Assertions.assertEquals(0, itemGeometry.getVertices().size());
    }

    @Test
    public void testExtractDataFromPolyline(){
        String representation = "http://linkedbuildingdata.net/ifc/resources/IfcPolyline_1665";
        Item item = new Item(new Building("Some Building"), "WallItem");
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        polylineService.extractDataFromPolyline(dataset, itemGeometry, representation);
        Assertions.assertEquals(0, itemGeometry.getVertices().size());
    }

    @Test
    public void testExtractDataFromInvalidPolyline(){
        String representation = "";
        Item item = new Item(new Building("Some Building"), "WallItem");
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        polylineService.extractDataFromPolyline(dataset, itemGeometry, representation);
        Assertions.assertEquals(0, itemGeometry.getVertices().size());
    }
}
