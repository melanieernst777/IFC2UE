package at.ac.uibk.thesis.services.RepresentationUtils;

import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.services.Dataset.IFCItems.*;
import at.ac.uibk.thesis.services.Movement.GeometryService;
import com.aspose.threed.Mesh;
import com.aspose.threed.VertexElementType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class ItemsRepresentation {

    @Autowired
    ExtrudedAreaSolidService extrudedAreaSolidService;

    @Autowired
    PolylineService polylineService;

    @Autowired
    FacesService facesService;

    @Autowired
    BooleanClippingResultService booleanClippingResultService;

    @Autowired
    UVService uvService;

    @Autowired
    OpeningService openingService;

    @Autowired
    GeometryService geometryService;

    @Autowired
    PolygonalBoundService polygonalBoundService;

    @Autowired
    MappingService mappingService;

    /***
     * Retrieves the geometries of the item. Ignores spaces as they should not be displayed
     * (big boxes in the room, we don't want stuff like that)
     * @param item The item containing different item representations
     */
    public void findRepresentationAndExecuteQuery(Item item) {

        Mesh itemMesh = (Mesh) item.getNode().getEntity();
        if (itemMesh != null) itemMesh.createElement(VertexElementType.MATERIAL);
        for (ItemGeometry geometry : item.getItemGeometries()) {
            if (item.getProductName().contains("Space")) {
                if (item.getNode() != null) item.getNode().getEntities().clear();
                item.getItemGeometries().clear();
                return;
            }

            boolean success = addRepresentationToItemGeometry(item, geometry);
            if (success) {
                item.getNode().getChildNodes().add(geometry.getNode());
            }
        }
    }

    /**
     * Checks what type the item geometry is and adds the control points and polygons
     * @param item The item to which the geometry belongs to
     * @param geometry The item geometry containing to which we want to add the control points and vertices
     * @return true, if the geometry has been extracted
     */
    private boolean addRepresentationToItemGeometry(Item item, ItemGeometry geometry) {

        geometryService.setUpTranslation(geometry);
        if(item.getTrimmedProductName().contains("Roof")){
            int i = 0;
        }
        Mesh mesh = (Mesh) geometry.getNode().getEntity();
        String representation = geometry.getItemRepresentation();
        IfcOWLDataset dataset = item.getBuilding().getOwlDataset();
        if (representation.contains("ExtrudedAreaSolid")) {
            extrudedAreaSolidService.getVector4sOfExtrudedAreaSolid(dataset, item, geometry, representation);
        } else if (representation.contains("Polyline")) {
            polylineService.extractDataFromPolyline(dataset, geometry, representation);
        } else if (representation.contains("MappedItem")) {
            mappingService.getDataFromMappedItem(dataset, representation, geometry, item);
        } else if (representation.contains("PolygonalBoundedHalf")) {
             polygonalBoundService.getPolygonalHalfBoundData(dataset, representation);
        } else if (representation.contains("FacetedBrep")) {
            facesService.getDataFromFacetedBoundaryRepresentation(dataset, representation, geometry, item);
        } else if (representation.contains("BooleanClippingResult") || representation.contains("BooleanResult")) {
            booleanClippingResultService.extractAllOperandsFromBooleanClippingResult(dataset, representation, geometry, item);
        } else if (representation.contains("FaceBasedSurfaceModel")) {
            facesService.getDataFromFacesOfFaceBasedSurfaceModel(dataset, representation, geometry, item);
        }
        if (mesh.getControlPoints().size() == 0) {
            item.getChildItems().remove(geometry);
            item.getNode().getChildNodes().remove(geometry.getNode());
            return false;
        }
        return true;
    }

}
