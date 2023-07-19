package at.ac.uibk.thesis.services.Dataset;

import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.services.Dataset.IFCItems.MappingService;
import com.aspose.threed.Vector3;
import com.aspose.threed.Vector4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.nio.file.Path;
import java.util.List;

@SpringBootTest
public class MappingServiceTest {

    @Autowired
    MappingService mappingService;

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
    public void testGetDataFromMappedItem(){
        String representation = "http://linkedbuildingdata.net/ifc/resources/IfcMappedItem_32057";
        Item item = new Item(new Building("Some Building"), "WallItem");
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        List<ItemGeometry> itemGeometryList = mappingService.getDataFromMappedItem(dataset, representation, itemGeometry, item);
        Assertions.assertEquals(2, itemGeometryList.size());
        Assertions.assertNotEquals(itemGeometryList.get(0), itemGeometryList.get(1));
    }

    @Test
    public void testGetDataFromComplexMappedItem(){
        String representation = "http://linkedbuildingdata.net/ifc/resources/IfcMappedItem_21563";
        Item item = new Item(new Building("Some Building"), "WallItem");
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        List<ItemGeometry> itemGeometryList = mappingService.getDataFromMappedItem(dataset, representation, itemGeometry, item);
        Assertions.assertEquals(16, itemGeometryList.size());
    }


    @Test
    public void testGetDataFromInvalidMappedItem(){
        String representation = "";
        Item item = new Item(new Building("Some Building"), "WallItem");
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        List<ItemGeometry> itemGeometryList = mappingService.getDataFromMappedItem(dataset, representation, itemGeometry, item);
        Assertions.assertEquals(0, itemGeometryList.size());
    }
}
