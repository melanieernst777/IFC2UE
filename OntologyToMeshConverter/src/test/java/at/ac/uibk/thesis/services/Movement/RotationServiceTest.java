package at.ac.uibk.thesis.services.Movement;

import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.services.Dataset.DatasetService;
import com.aspose.threed.Vector4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.vecmath.Matrix4d;
import java.nio.file.Path;

@SpringBootTest
public class RotationServiceTest {

    @Autowired
    RotationService rotationService;

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
    public void testAngleBetweenVectors(){
        Vector4 vector1 = new Vector4(0,0,1);
        Vector4 vector2 = new Vector4(1,0,0);
        Double expected = 1.5707963267948966;
        Double theta = rotationService.getAngleBetweenTwoVectors(vector1, vector2);
        Assertions.assertNotNull(theta);
        Assertions.assertEquals(expected, theta);
    }

    @Test
    public void testConstantSetupRotationMatrixOfNegativeValues(){
        Vector4 v1 = new Vector4(1,-2,0);
        Vector4 v2 = new Vector4(0,1,-1);
        Matrix4d result = rotationService.setUpRotationMatrix(v1, v2);
        Matrix4d expected = rotationService.setUpRotationMatrix(v1, v2);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testConstantSetupRotationMatrix(){
        Vector4 v1 = new Vector4(0,1,0);
        Vector4 v2 = new Vector4(0,0,1);
        Matrix4d result = rotationService.setUpRotationMatrix(v1, v2);
        Matrix4d expected = rotationService.setUpRotationMatrix(v1, v2);
        Assertions.assertEquals(expected, result);
    }

    @Test
    public void testSetupRotationMatrix(){
        Vector4 v1 = new Vector4(0,0,1);
        Vector4 v2 = new Vector4(1,0,0);
        Matrix4d result = rotationService.setUpRotationMatrix(v1, v2);
        Matrix4d expected = new Matrix4d();
        expected.setIdentity();
        Assertions.assertEquals(expected, result);
    }
}
