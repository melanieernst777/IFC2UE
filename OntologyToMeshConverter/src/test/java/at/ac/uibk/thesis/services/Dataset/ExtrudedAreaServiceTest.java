package at.ac.uibk.thesis.services.Dataset;

import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.services.BuildingService;
import at.ac.uibk.thesis.services.Dataset.IFCItems.ExtrudedAreaSolidService;
import at.ac.uibk.thesis.services.ProductService;
import com.aspose.threed.Mesh;
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
public class ExtrudedAreaServiceTest {

    @Autowired
    BuildingService buildingService;

    @Autowired
    ProductService productService;

    @Autowired
    ExtrudedAreaSolidService extrudedAreaService;

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
    public void testValidExtrudedAreaSolidWithoutChangingDirs () {
        Item item = new Item(new Building("Some Building"), "WallItem");
        String representation = "http://linkedbuildingdata.net/ifc/resources/IfcExtrudedAreaSolid_1155";
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        extrudedAreaService.getVector4sOfExtrudedAreaSolid(dataset, item, itemGeometry, representation);
        Assertions.assertNotNull(itemGeometry);
        Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
        Assertions.assertEquals(mesh.getControlPoints().size(), 8);
        Assertions.assertEquals(mesh.getPolygons().size(), 6);
        Assertions.assertEquals(new Vector4(0,0,0), itemGeometry.getRefDirection());
        Assertions.assertEquals(new Vector4(0,0,0), itemGeometry.getDirection());
        Assertions.assertEquals(new Vector3(0,0,0), itemGeometry.getTranslation());
    }

    @Test
    public void testValidExtrudedAreaSolidWithChangingDirs () {
        Item item = new Item(new Building("Some Building"), "WallItem");
        String representation = "http://linkedbuildingdata.net/ifc/resources/IfcExtrudedAreaSolid_42418";
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        extrudedAreaService.getVector4sOfExtrudedAreaSolid(dataset, item, itemGeometry, representation);
        Assertions.assertNotNull(itemGeometry);
        Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
        Assertions.assertEquals(mesh.getControlPoints().size(), 8);
        Assertions.assertEquals(mesh.getPolygons().size(), 6);
        Assertions.assertEquals(new Vector4(0,0,1), itemGeometry.getRefDirection());
        Assertions.assertEquals(new Vector4(0,1,0), itemGeometry.getDirection());
        Assertions.assertEquals(new Vector3(0,0,0), itemGeometry.getTranslation());
    }

    @Test
    public void testEmptyExtrudedAreaSolid() {
        Item item = new Item(new Building("Some Building"), "WallItem");
        String representation = "";
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        extrudedAreaService.getVector4sOfExtrudedAreaSolid(dataset, item, itemGeometry, representation);
        Assertions.assertNotNull(itemGeometry);
        Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
        Assertions.assertEquals(mesh.getControlPoints().size(), 0);
        Assertions.assertEquals(mesh.getPolygons().size(), 0);
    }
}
