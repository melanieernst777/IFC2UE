package at.ac.uibk.thesis.services.Dataset.IFCItems;

import at.ac.uibk.thesis.entities.IfcOWLDataset;
import com.aspose.threed.Vector4;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class DirectionService {

    /**
     * Get the Vector of a given direction
     *
     * @param dataset   the dataset, generated with the ifc file
     * @param directionLiteral the link to the direction of which we want to extract the data
     * @return the query solution containing the x, y and z values of the direction
     */
    public Vector4 getDirectionData(IfcOWLDataset dataset, String directionLiteral) {
        QuerySolution direction = getDirectionDataFromOntology(dataset, directionLiteral);
        return new Vector4(direction.get("x").asLiteral().getDouble(), direction.get("y").asLiteral().getDouble(), direction.get("z").asLiteral().getDouble());
    }

    /**
     * Get the Vector of a given direction
     *
     * @param dataset   the dataset, generated with the ifc file
     * @param direction the link to the direction of which we want to extract the data
     * @return the query solution containing the x, y and z values of the direction
     */
    public QuerySolution getDirectionDataFromOntology(IfcOWLDataset dataset, String direction) {
        return QueryExecutionFactory.create(dataset.getPrefixes() +
                        "SELECT ?x ?y ?z WHERE {\n" +
                        "    <" + direction + "> ifc:directionRatios_IfcDirection ?xList .\n" +
                        "    ?xList list:hasContents ?xContent .\n" +
                        "    ?xContent express:hasDouble ?x .\n" +
                        "    ?xList list:hasNext ?yList .\n" +
                        "    ?yList list:hasContents ?yContent .\n" +
                        "    ?yContent express:hasDouble ?y .\n" +
                        "    ?yList list:hasNext ?zList .\n" +
                        "    ?zList list:hasContents ?zContent .\n" +
                        "    ?zContent express:hasDouble ?z .\n" +
                        "}", dataset.getDataset())
                .execSelect().next();
    }
}
