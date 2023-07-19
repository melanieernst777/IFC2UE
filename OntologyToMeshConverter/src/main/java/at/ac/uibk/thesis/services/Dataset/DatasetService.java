package at.ac.uibk.thesis.services.Dataset;

import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;

@Component
@Scope("application")
public class DatasetService {

    /***
     * Create the dataset of a given ttl file
     * @param path the path to the ttl file
     * @return the created dataset
     */
    public Dataset getDataset(String path) {
        Dataset dataset;
        Model model = ModelFactory.createDefaultModel();
        try {
            model.read(new FileInputStream(path.replace("ifc", "ttl")), "http://ex.org/", "TURTLE");
            dataset = DatasetFactory.create();
            dataset.setDefaultModel(model);
            return dataset;
        } catch (Exception e) {
            System.out.println("exception occurred: " + e.getMessage());
        }
        return null;
    }
}
