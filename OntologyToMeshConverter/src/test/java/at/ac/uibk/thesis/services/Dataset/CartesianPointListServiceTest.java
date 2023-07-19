package at.ac.uibk.thesis.services.Dataset;

import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.services.Dataset.IFCItems.CartesianPointListService;
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
public class CartesianPointListServiceTest {

    @Autowired
    CartesianPointListService cartesianPointListService;

    @Autowired
    DatasetService datasetService;

    IfcOWLDataset owlDataset;

    @BeforeEach
    public void setUpDataset() {
        owlDataset = new IfcOWLDataset(datasetService.getDataset(Path.of(TestConstants.TTLFILELOCATION).toAbsolutePath().toString()));
        Assert.notNull(owlDataset, "The dataset shall not be null");
        Assertions.assertFalse(owlDataset.getDataset().isEmpty());
    }

    @Test
    public void testGetAllVectorsOfVeryShortCartesianPointList(){
        List<Vector4> vectors = cartesianPointListService.getAllVectorOfCartesianPointList(owlDataset, "http://linkedbuildingdata.net/ifc/resources/IfcCartesianPoint_List_28010");
        Assertions.assertFalse(vectors.isEmpty());
        Assertions.assertEquals(2, vectors.size());
    }

    @Test
    public void testGetAllVectorsOfCartesianPointList(){
        List<Vector4> vectors = cartesianPointListService.getAllVectorOfCartesianPointList(owlDataset, "http://linkedbuildingdata.net/ifc/resources/IfcCartesianPoint_List_42167");
        Assertions.assertFalse(vectors.isEmpty());
        Assertions.assertEquals(4, vectors.size());
    }

    @Test
    public void testGetAllVectorsOfLongCartesianPointList(){
        List<Vector4> vectors = cartesianPointListService.getAllVectorOfCartesianPointList(owlDataset, "http://linkedbuildingdata.net/ifc/resources/IfcCartesianPoint_List_57807");
        Assertions.assertFalse(vectors.isEmpty());
        Assertions.assertEquals(30, vectors.size());
    }


    @Test
    public void testGetAllVectorsOfInvalidPointList(){
        List<Vector4> vectors = cartesianPointListService.getAllVectorOfCartesianPointList(owlDataset, "");
        Assertions.assertTrue(vectors.isEmpty());
    }
}
