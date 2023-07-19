package at.ac.uibk.thesis.services.Dataset;

import at.ac.uibk.thesis.services.Dataset.IFCItems.VectorService;
import com.aspose.threed.Vector3;
import com.aspose.threed.Vector4;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class VectorServiceTest {

    @Autowired
    VectorService vectorService;

    @Test
    public void testIsEqual() {
        Vector4 vector1 = new Vector4(1, 1, 1);
        Vector4 vector2 = new Vector4(1, 1, 1);
        Assertions.assertFalse(vectorService.isUnequal(vector1, vector2));
    }


    @Test
    public void testIsUnequal() {
        Vector4 vector1 = new Vector4(1, 1, 1);
        Vector4 vector2 = new Vector4(1, 1, 2);
        Assertions.assertTrue(vectorService.isUnequal(vector1, vector2));
    }

    @Test
    public void testAddVector4() {
        Vector4 vector1 = new Vector4(1, 1, 1);
        Vector4 vector2 = new Vector4(1, 1, 1);
        Vector4 result = vectorService.add(vector1, vector2);
        Vector4 expected = new Vector4(2, 2, 2);
        Assertions.assertFalse(vectorService.isUnequal(result, expected));
    }

    @Test
    public void testAddVector() {
        Vector3 vector1 = new Vector3(1, 1, 1);
        Vector4 vector2 = new Vector4(1, 1, 1);
        Vector4 result = vectorService.add(vector1, vector2);
        Vector4 expected = new Vector4(2, 2, 2);
        Assertions.assertFalse(vectorService.isUnequal(result, expected));
    }

    @Test
    public void testCrossProducts() {
        Vector4 vector1 = new Vector4(1, 1, 1);
        Vector4 vector2 = new Vector4(1, 1, 1);
        Vector4 result = vectorService.cross(vector1, vector2);
        Vector4 expected = new Vector4(0, 0, 0);
        Assertions.assertFalse(vectorService.isUnequal(result, expected));
    }

    @Test
    public void testCrossProductsWithZeros() {
        Vector4 vector1 = new Vector4(0, 0, 0);
        Vector4 vector2 = new Vector4(1, 1, 1);
        Vector4 result = vectorService.cross(vector1, vector2);
        Vector4 expected = new Vector4(0, 0, 0);
        Assertions.assertFalse(vectorService.isUnequal(result, expected));
    }

    @Test
    public void testCrossProductsWithHighValues() {
        Vector4 vector1 = new Vector4(89, 32189, 6519);
        Vector4 vector2 = new Vector4(56498, 5165, 6516);
        Vector4 result = vectorService.cross(vector1, vector2);
        Vector4 expected = new Vector4(176072889, 367730538, -1818154437);
        Assertions.assertFalse(vectorService.isUnequal(result, expected));
    }

    @Test
    public void testInvertZeros() {
        Vector4 vector1 = new Vector4(0, 0, 0);
        Vector4 result = vectorService.invert(vector1);
        Vector4 expected = new Vector4(-0, -0, -0);
        Assertions.assertFalse(vectorService.isUnequal(result, expected));
    }

    @Test
    public void testInvert() {
        Vector4 vector1 = new Vector4(7, 5, -2);
        Vector4 result = vectorService.invert(vector1);
        Vector4 expected = new Vector4(-7, -5, 2);
        Assertions.assertFalse(vectorService.isUnequal(result, expected));
    }


    @Test
    public void testSum() {
        Vector4 vector1 = new Vector4(7, 5, -2);
        Double sum = vectorService.getSum(vector1);
        Assertions.assertEquals(sum, 10);
    }

    @Test
    public void test() {
        Vector4 vector1 = new Vector4(7, 5, -2);
        Vector4 result = vectorService.scale(vector1, 10);
        Vector4 expected = new Vector4(70, 50, -20);
        Assertions.assertFalse(vectorService.isUnequal(result, expected));
    }

}
