package at.ac.uibk.thesis.services.Dataset.IFCItems;

import at.ac.uibk.thesis.entities.IfcOWLDataset;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class PolygonalBoundService {

    /**
     * @param dataset   the dataset, generated with the ifc file
     * @param halfSpace the link to the half space
     */
    public void getPolygonalHalfBoundData(IfcOWLDataset dataset, String halfSpace) {
        ResultSet res = queryPolygonalBound(dataset, halfSpace);
        res.forEachRemaining(this::extractMoreData);
    }

    private void extractMoreData(QuerySolution q) {

    }

    /**
     * @param dataset   the dataset, generated with the ifc file
     * @param halfSpace the link to the half space
     * @return the result set with the information about the position, plane etc. of the half space
     */
    private ResultSet queryPolygonalBound(IfcOWLDataset dataset, String halfSpace) {
        return QueryExecutionFactory.create(
                dataset.getPrefixes() +
                                "SELECT ?planePos ?agreementFlagValue ?position ?boundary WHERE {\n" +
                                "    <" + halfSpace + "> ifc:baseSurface_IfcHalfSpaceSolid  ?plane .\n" +
                                "    ?plane ifc:position_IfcElementarySurface ?planePos .\n" +
                                "    <" + halfSpace + "> ifc:agreementFlag_IfcHalfSpaceSolid ?agreementFlag .\n" +
                                "    ?agreementFlag express:hasBoolean ?agreementFlagValue .\n" +
                                "    <" + halfSpace + "> ifc:position_IfcPolygonalBoundedHalfSpace ?position .\n" +
                                "    <" + halfSpace + "> ifc:polygonalBoundary_IfcPolygonalBoundedHalfSpace ?boundary .\n" +
                                "}"
                        , dataset.getDataset())
                .execSelect();
    }


}
