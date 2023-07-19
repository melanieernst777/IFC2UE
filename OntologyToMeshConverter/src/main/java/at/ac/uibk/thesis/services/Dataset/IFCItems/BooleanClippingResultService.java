package at.ac.uibk.thesis.services.Dataset.IFCItems;

import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import com.aspose.threed.Mesh;
import com.aspose.threed.Vector4;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("application")
public class BooleanClippingResultService {

    List<ResultSet> resultSetList;

    @Autowired
    ExtrudedAreaSolidService extrudedAreaSolidService;

    @Autowired
    PolylineService polylineService;

    @Autowired
    FacesService facesService;

    @Autowired
    MappingService mappingService;

    @Autowired
    PolygonalBoundedHalfSpaceService polygonalBoundedHalfSpaceService;
    /**
     * Boolean clipping results contain two items, both of them need to be extracted and combined
     *
     * @param dataset        the dataset, generated with the ifc file
     * @param clippingResult the link to the clipping result
     * @param itemGeometry   the item geometry to which this clipping result belongs to
     * @param item           the item to which the item geometry belongs to
     */
    public void extractAllOperandsFromBooleanClippingResult(IfcOWLDataset dataset, String clippingResult, ItemGeometry itemGeometry, Item item) {
        resultSetList = new ArrayList<>();
        getOperandsFromBooleanClippingResult(dataset, clippingResult, itemGeometry, item);
    }

    /**
     * extract both the first and the second operand from the boolean clipping result
     *
     * @param dataset       the dataset, generated with the ifc file
     * @param querySolution the query solution which contains the information about the item representation
     * @param itemGeometry  the item geometry to which this clipping result belongs to
     * @param item          the item to which the item geometry belongs to
     */
    private void getDataForClippingResult(IfcOWLDataset dataset, QuerySolution querySolution, ItemGeometry itemGeometry, Item item) {
        mapping(dataset, querySolution.get("firstOperand").toString(), itemGeometry, item);
        mapping(dataset, querySolution.get("secondOperand").toString(), itemGeometry, item);
    }

    /**
     * @param dataset            the dataset, generated with the ifc file
     * @param itemRepresentation the link to the representation of the iem
     * @param itemGeometry       the item geometry to which the item representation belongs to
     * @param item               the item to which the item geometry belongs to
     */
    private void mapping(IfcOWLDataset dataset, String itemRepresentation, ItemGeometry itemGeometry, Item item) {
        if (itemRepresentation.contains("ExtrudedAreaSolid")) {
            extrudedAreaSolidService.getVector4sOfExtrudedAreaSolid(dataset, item, itemGeometry, itemRepresentation);
        } else if (itemRepresentation.contains("Polyline")) {
             polylineService.extractDataFromPolyline(dataset, itemGeometry, itemRepresentation);
        }  else if (itemRepresentation.contains("FacetedBrep")) {
            facesService.getDataFromFacesOfFaceBasedSurfaceModel(dataset, itemRepresentation, itemGeometry, item);
        } else if (itemRepresentation.contains("MappedItem")) {
            mappingService.getDataFromMappedItem(dataset, itemRepresentation, itemGeometry, item);
        } else if (itemRepresentation.contains("BooleanClippingResult")) {
            extractAllOperandsFromBooleanClippingResult(dataset, itemRepresentation, itemGeometry, item);
        } else if (itemRepresentation.contains("Polygonal")){
            polygonalBoundedHalfSpaceService.extractDataFromPolygonalBoundedHS(dataset, itemRepresentation, itemGeometry);
        }
        Mesh mesh = (Mesh) item.getNode().getEntity();
        if (mesh != null && itemGeometry.getVertices().size() > 0) {
            mesh.getControlPoints().clear();
            itemGeometry.getVertices().forEach(v -> mesh.getControlPoints().add(new Vector4(v.getPosition().x, v.getPosition().y, v.getPosition().z)));
        }
    }

    /**
     * @param dataset        the dataset, generated with the ifc file
     * @param clippingResult the clipping result of which we want to extract the data
     * @param itemGeometry   the item geometry to which the clipping result belongs to
     * @param item           the item to which the item geometry belongs to
     */
    public void getOperandsFromBooleanClippingResult(IfcOWLDataset dataset, String clippingResult, ItemGeometry itemGeometry, Item item) {
        ResultSet resultSet = QueryExecutionFactory.create(dataset.getPrefixes() +
                        "SELECT ?firstOperand ?secondOperand WHERE {\n" +
                        "    <" + clippingResult + "> ifc:firstOperand_IfcBooleanResult ?firstOperand .\n" +
                        "    <" + clippingResult + "> ifc:secondOperand_IfcBooleanResult ?secondOperand . \n" +
                        "} ", dataset.getDataset())
                .execSelect();
        resultSetList.add(resultSet);
        if (resultSet.hasNext()) {
            QuerySolution sol = resultSet.next();
            getDataForClippingResult(dataset, sol, itemGeometry, item);
        }
    }

}
