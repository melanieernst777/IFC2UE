package at.ac.uibk.thesis.services.Dataset;

import at.ac.uibk.thesis.TestConstants;
import org.apache.jena.query.Dataset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.nio.file.Path;

@SpringBootTest
public class DatasetServiceTest {

    @Autowired
    DatasetService datasetService;

    @Test
    public void testLoadDataset() {
        Dataset dataset = datasetService.getDataset(Path.of(TestConstants.TTLFILELOCATION).toAbsolutePath().toString());
        Assert.notNull(dataset, "The dataset shall not be null");
        Assertions.assertFalse(dataset.isEmpty());
    }

    @Test
    public void testLoadInvalidDataset() {
        Dataset dataset = datasetService.getDataset(Path.of(TestConstants.INVALID_TTL).toAbsolutePath().toString());
        Assert.isNull(dataset, "The dataset shall be null");
    }
}
