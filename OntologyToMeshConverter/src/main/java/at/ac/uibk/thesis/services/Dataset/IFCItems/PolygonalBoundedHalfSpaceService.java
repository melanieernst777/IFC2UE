package at.ac.uibk.thesis.services.Dataset.IFCItems;

import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.services.Movement.RotationService;
import com.aspose.threed.Mesh;
import com.aspose.threed.PolygonModifier;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.vecmath.Matrix4d;
import java.util.List;

@Component
@Scope("application")
public class PolygonalBoundedHalfSpaceService {

    @Autowired
    PolylineService polylineService;

    @Autowired
    Axis2Placement3D axis2Placement3D;

    @Autowired
    RotationService rotationService;

    public void extractDataFromPolygonalBoundedHS(IfcOWLDataset dataset, String itemRepresentation, ItemGeometry itemGeometry) {
        ItemGeometry tmpGeometry = new ItemGeometry(itemGeometry);
        QuerySolution qs = getInformationAboutPolygonal(itemRepresentation, dataset);
        if(qs != null){
            axis2Placement3D.getInformationAboutPlacement(dataset, tmpGeometry, qs, "position");
            Matrix4d currentRotation = rotationService.setUpRotationMatrix(tmpGeometry.getDirection(), tmpGeometry.getRefDirection());
            itemGeometry.rotationMatrix.mul(currentRotation);

            axis2Placement3D.getInformationAboutPlacement(dataset, tmpGeometry, qs, "planePosition");
            currentRotation = rotationService.setUpRotationMatrix(tmpGeometry.getDirection(), tmpGeometry.getRefDirection());
            tmpGeometry.rotationMatrix.mul(currentRotation);

            if(qs.contains("polyline")){
                polylineService.extractDataFromPolyline(dataset, tmpGeometry, qs.get("polyline").toString());
                // polylineService.extractVectorListFromPolyline(dataset, qs.get("polyline").toString());
            }
            Mesh mesh = PolygonModifier.mergeMesh(List.of(itemGeometry.getNode(), tmpGeometry.getNode()));
            itemGeometry.getNode().setEntity(mesh);
        }
    }

    private QuerySolution getInformationAboutPolygonal(String polygonal, IfcOWLDataset dataset) {
        String query = dataset.getPrefixes() +
                "SELECT * WHERE { \n" +
                "\t?polygonal rdf:type  ifc:IfcPolygonalBoundedHalfSpace .\n" +
                "    <"+ polygonal + "> ifc:baseSurface_IfcHalfSpaceSolid ?plane .\n" +
                "    ?plane ifc:position_IfcElementarySurface ?planePosition . \n" +
                "    <"+ polygonal + "> ifc:position_IfcPolygonalBoundedHalfSpace ?position .\n" +
                "    <"+ polygonal +"> ifc:polygonalBoundary_IfcPolygonalBoundedHalfSpace ?polyline .\n" +
                "}\n";
        ResultSet resultSet = QueryExecutionFactory
                .create(query, dataset.getDataset())
                .execSelect();
        if (resultSet.hasNext()) {
            return resultSet.next();
        }
        return null;
    }
}
