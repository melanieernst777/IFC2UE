package at.ac.uibk.thesis.services.Dataset;

import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.services.Dataset.IFCItems.FacesService;
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
public class FacesServiceTest {

    @Autowired
    FacesService facesService;

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
    public void testGetDataFromComplexFacetedBoundaryRepresentation(){
        String representation = "http://linkedbuildingdata.net/ifc/resources/IfcFacetedBrep_8802";
        Building building = new Building("Some Building");
        building.setOwlDataset(dataset);
        Item item = new Item(building, "Door");
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        facesService.getDataFromFacetedBoundaryRepresentation(dataset, representation, itemGeometry, item);
        Assertions.assertNotNull(itemGeometry);
        Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
        Assertions.assertEquals(144, mesh.getControlPoints().size());
        Assertions.assertEquals(26, mesh.getPolygons().size());
        Assertions.assertEquals(new Vector4(0,0,0), itemGeometry.getRefDirection());
        Assertions.assertEquals(new Vector4(0,0,0), itemGeometry.getDirection());
        Assertions.assertEquals(new Vector3(0,0,0), itemGeometry.getTranslation());
    }

    @Test
    public void testGetDataFromFacetedBoundaryRepresentation(){
        String representation = "http://linkedbuildingdata.net/ifc/resources/IfcFacetedBrep_22896";
        Building building = new Building("Some Building");
        building.setOwlDataset(dataset);
        Item item = new Item(building, "Door");ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        facesService.getDataFromFacetedBoundaryRepresentation(dataset, representation, itemGeometry, item);
        Assertions.assertNotNull(itemGeometry);
        Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
        Assertions.assertEquals(24, mesh.getControlPoints().size());
        Assertions.assertEquals(6, mesh.getPolygons().size());
        Assertions.assertEquals(new Vector4(0,0,0), itemGeometry.getRefDirection());
        Assertions.assertEquals(new Vector4(0,0,0), itemGeometry.getDirection());
        Assertions.assertEquals(new Vector3(0,0,0), itemGeometry.getTranslation());
    }

    @Test
    public void testGetDataFromInvalidFacetedBoundaryRepresentation(){
        String representation = "";
        Item item = new Item(new Building("Some Building"), "WallItem");
        ItemGeometry itemGeometry = new ItemGeometry(representation, new Vector4(0,0,0), new Vector4(0,0,0), new Vector4(0,0,0), item);
        itemGeometry.setTranslation(new Vector3(0,0,0));
        facesService.getDataFromFacetedBoundaryRepresentation(dataset, representation, itemGeometry, item);
        Assertions.assertNotNull(itemGeometry);
        Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
        Assertions.assertEquals(mesh.getControlPoints().size(), 0);
        Assertions.assertEquals(mesh.getPolygons().size(), 0);
    }
}
