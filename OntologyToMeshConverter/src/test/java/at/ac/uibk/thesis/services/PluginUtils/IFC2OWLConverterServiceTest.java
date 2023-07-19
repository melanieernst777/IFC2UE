package at.ac.uibk.thesis.services.PluginUtils;

import at.ac.uibk.thesis.TestConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IFC2OWLConverterServiceTest {

    @Autowired
    IFC2OWLConverterService ifc2OWLConverterService;

    @Test
    public void testIFCOwlGeneration(){
        String fileName = TestConstants.IFCFILELOCATION;
        boolean success = ifc2OWLConverterService.convertIFCtoIFCOWL(fileName, fileName.replace("ifc", "ttl"));
        Assertions.assertTrue(success);
    }

    @Test
    public void testIFCOwlGenerationWithEmptyPath(){
        String fileName = "";
        boolean success = ifc2OWLConverterService.convertIFCtoIFCOWL(fileName, fileName.replace("ifc", "ttl"));
        Assertions.assertFalse(success);
    }

    @Test
    public void testIFCOwlGenerationWithInvalidPath(){
        boolean success = ifc2OWLConverterService.convertIFCtoIFCOWL(null, null);
        Assertions.assertFalse(success);
    }

}
