package at.ac.uibk.thesis.services.PluginUtils;

import be.ugent.IfcSpfReader;
import org.apache.jena.graph.Graph;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.graph.GraphFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
@Scope("application")
public class IFC2OWLConverterService {

    /***
     * Converts a given IFC file to a TTL file
     * @param upload The path to the IFC file
     * @param output THe path the TTL should have
     * @return true if the creation was successful, false if an error occurred
     */
    public boolean convertIFCtoIFCOWL(String upload, String output) {
        IfcSpfReader reader = new IfcSpfReader();
        Graph expected = GraphFactory.createGraphMem();
        try {
            if (new File(output).createNewFile()) {
                RDFDataMgr.read(expected, new FileInputStream(new File(output).getAbsolutePath()), Lang.TTL);
                reader.setup(new File(upload).getAbsolutePath());
                reader.convert(upload, output, "http://linkedbuildingdata.net/ifc/resources/");

            }

        } catch (IOException | NullPointerException ignored) {
            return false;
        }
        return true;
    }
}