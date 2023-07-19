package at.ac.uibk.thesis.services.Dataset;

import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.services.Dataset.IFCItems.ItemService;
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
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

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
    public void testComputeGeometricVertices(){
        Item item = new Item(new Building("Some Building"), "WallItem");
        String representation = "http://linkedbuildingdata.net/ifc/resources/IfcExtrudedAreaSolid_1155";
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        List<Vector4> vertices = new ArrayList<>();
        vertices.add(new Vector4(0,0,0));
        vertices.add(new Vector4(0,0,0));
        vertices.add(new Vector4(0,0,0));
        Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
        itemService.computeGeometricVertices(itemGeometry, vertices);
        Assertions.assertEquals(vertices.size(), mesh.getControlPoints().size());
        Assertions.assertEquals(1, mesh.getPolygonCount());
    }

    @Test
    public void testComputeGeometricVerticesForEmptyList(){
        Item item = new Item(new Building("Some Building"), "WallItem");
        String representation = "http://linkedbuildingdata.net/ifc/resources/IfcExtrudedAreaSolid_1155";
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        List<Vector4> vertices = new ArrayList<>();
        Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
        itemService.computeGeometricVertices(itemGeometry, vertices);
        Assertions.assertEquals(0, mesh.getControlPoints().size());
        Assertions.assertEquals(0, mesh.getPolygonCount());
    }
}
