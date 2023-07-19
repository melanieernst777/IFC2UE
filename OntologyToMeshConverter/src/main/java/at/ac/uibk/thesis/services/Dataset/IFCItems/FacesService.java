package at.ac.uibk.thesis.services.Dataset.IFCItems;


import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.enums.SurfaceMaterial;
import at.ac.uibk.thesis.services.RepresentationUtils.SurfaceRenderingService;
import com.aspose.threed.*;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("application")
public class FacesService {

    @Autowired
    CartesianPointListService cartesianPointListService;

    @Autowired
    ItemService itemService;

    @Autowired
    SurfaceRenderingService surfaceRenderingService;

    /**
     * @param dataset        the dataset, generated with the ifc file
     * @param representation the link to the representation of the item geometry
     * @param itemGeometry   the item geometry of which we want to get the data
     * @param item           the item to which the item belongs to
     */
    public void getDataFromFacetedBoundaryRepresentation(IfcOWLDataset dataset, String representation, ItemGeometry itemGeometry, Item item) {
        ResultSet result = getFacesOfFacetedBrep(dataset, representation);
        getDataFromItem(result, itemGeometry, item);
    }

    public void getDataFromFacesOfFaceBasedSurfaceModel(IfcOWLDataset dataset, String representation, ItemGeometry itemGeometry, Item item) {
        ResultSet result = getFacesOfFaceBasedSurfaceModel(dataset, representation);
        getDataFromItem(result, itemGeometry, item);
    }

    /**
     * @param result   the result set which contains the information about the geometry
     * @param geometry the item geometry of which we extract the data
     * @param item     the item to which the item belongs to
     */
    private void getDataFromItem(ResultSet result, ItemGeometry geometry, Item item) {
        result.forEachRemaining(r -> addToMesh(geometry, item.getBuilding(), r));
    }

    private void addToMesh(ItemGeometry itemGeometry, Building building, QuerySolution r) {
        List<Vector4> vectorsOfFaces = getVector4sOfFaces(building.getOwlDataset(), r.get("cartesianPointList").toString());
        if(r.contains("?cartesianPointListBound")){
            List<Vector4> vectorsOfBound = getVector4sOfFaces(building.getOwlDataset(), r.get("?cartesianPointListBound").toString());
            itemService.computeGeometricVertices(itemGeometry, vectorsOfFaces, vectorsOfBound);
        }
        else {
            itemService.computeGeometricVertices(itemGeometry, vectorsOfFaces);
        }
        addColorToMesh(building, itemGeometry, r);
    }

    /**
     * Adds the color to the mesh and the material to the list of materials which is stored in the building
     * @param building The building containing a list of materials
     * @param itemGeometry The item geometry to which we want to add the material
     * @param querySolution The query solution containing the information of the material
     */
    private void addColorToMesh(Building building, ItemGeometry itemGeometry, QuerySolution querySolution) {
        if (itemGeometry.getNode().getMaterial() != null)
            if (!itemGeometry.getNode().getMaterial().getName().contains("Color")) return;
        Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
        mesh.createElement(VertexElementType.MATERIAL);
        PhongMaterial material = new PhongMaterial();
        if (querySolution.contains("styleLabel")) {
            SurfaceMaterial surfaceMaterial = surfaceRenderingService.getMaterialName(querySolution.get("styleLabel").toString());
            if (surfaceMaterial != null) {
                itemGeometry.setNode(surfaceRenderingService.addMaterialToNode(building, itemGeometry.getNode(), surfaceMaterial));
            }
        }
        if (!querySolution.contains("styleLabel") && querySolution.contains("red")) {
            itemGeometry.setColor(new Vector4(
                    querySolution.get("red").asLiteral().getDouble(),
                    querySolution.get("green").asLiteral().getDouble(),
                    querySolution.get("blue").asLiteral().getDouble()
            ));
            material.setSpecularColor(new Vector3(itemGeometry.getColor()));
            material.setDiffuseColor(new Vector3(itemGeometry.getColor()));
            if (!building.getMaterialsContainedInBuilding().containsKey(itemGeometry.getColorOfItemGeometry())) {
                material.setName("Color_" + itemGeometry.getColorOfItemGeometry());
                building.getMaterialsContainedInBuilding().putIfAbsent(itemGeometry.getColorOfItemGeometry(), material);
            }
            itemGeometry.getNode().setMaterial(building.getMaterialsContainedInBuilding().get(itemGeometry.getColorOfItemGeometry()));
        }
    }


    /**
     * @param dataset            the dataset, generated with the ifc file
     * @param cartesianPointList the link to the cartesian point of which we want to extract the Vector4s
     * @return a list of Vectors from the point list
     */
    private List<Vector4> getVector4sOfFaces(IfcOWLDataset dataset, String cartesianPointList) {
        return cartesianPointListService.getAllVectorOfCartesianPointList(dataset, cartesianPointList);
    }

    /**
     * @param dataset     the dataset, generated with the ifc file
     * @param facetedBrep the link to the faceted brep
     * @return the result set containing information about the faceted brep
     */
    private ResultSet getFacesOfFacetedBrep(IfcOWLDataset dataset, String facetedBrep) {
        String query = dataset.getPrefixes() +
                "SELECT ?face ?polyloop ?cartesianPointList ?closedShell ?styleLabel ?red ?green ?blue ?bound ?polyloopBound ?cartesianPointListBound WHERE {\n" +
                "    <" + facetedBrep + "> ifc:outer_IfcManifoldSolidBrep ?closedShell .\n" +
                "    ?closedShell ifc:cfsFaces_IfcConnectedFaceSet ?face .\n" +
                "    ?face ifc:bounds_IfcFace ?outterBound .\n" +
                "    ?outterBound ifc:bound_IfcFaceBound ?polyloop .\n" +
                "    ?outterBound rdf:type  ?orientation .\n" +
                "    ?outterBound rdf:type ifc:IfcFaceOuterBound .\n" +
                "    ?polyloop ifc:polygon_IfcPolyLoop ?cartesianPointList .\n" +
                "\n" +
                "    # material color of the faceted brep\n" +
                "    OPTIONAL {\n" +
                "        ?styledItem ifc:item_IfcStyledItem <" + facetedBrep + "> .\n" +
                "        ?styledItem ifc:styles_IfcStyledItem ?presentationLayerAssignment .\n" +
                "        ?presentationLayerAssignment ifc:styles_IfcPresentationStyleAssignment ?surfaceStyle .\n" +
                "        ?surfaceStyle ifc:styles_IfcSurfaceStyle ?surfaceStyleRendering .\n" +
                "        ?surfaceStyle ifc:name_IfcPresentationStyle ?style .\n" +
                "        ?style express:hasString ?styleLabel .\n" +
                "\n" +
                "        OPTIONAL {\n" +
                "            ?surfaceStyleRendering ifc:surfaceColour_IfcSurfaceStyleShading ?color .\n" +
                "            ?color ifc:red_IfcColourRgb ?redColor .\n" +
                "            ?redColor express:hasDouble ?red .    \n" +
                "            ?color ifc:blue_IfcColourRgb ?blueColor .\n" +
                "            ?blueColor express:hasDouble ?blue .  \n" +
                "            ?color ifc:green_IfcColourRgb ?greenColor .\n" +
                "            ?greenColor express:hasDouble ?green .  \n" +
                "        }\n" +
                "    }\n" +
                "    OPTIONAL {\n" +
                "      ?face ifc:bounds_IfcFace ?bound .\n" +
                "      ?bound ifc:bound_IfcFaceBound ?polyloopBound .\n" +
                "      ?bound rdf:type ifc:IfcFaceBound .\n" +
                "       ?polyloopBound ifc:polygon_IfcPolyLoop ?cartesianPointListBound .\n" +
                "    }\n" +
                "} \n" +
                "\n" +
                "ORDER BY ASC(?face)\n";
        return QueryExecutionFactory
                .create(query,
                        dataset.getDataset())
                .execSelect();
    }

    /**
     * @param dataset               the dataset, generated with the ifc file
     * @param faceBasedSurfaceModel the link to the faceBasedSurfaceModel
     * @return the result set containing information about the faceBasedSurfaceModel
     */
    private ResultSet getFacesOfFaceBasedSurfaceModel(IfcOWLDataset dataset, String faceBasedSurfaceModel) {
        String query = dataset.getPrefixes() +
                "SELECT ?face ?polyloop ?cartesianPointList ?connectedFaceSet WHERE {\n" +
                "    <" + faceBasedSurfaceModel + "> ifc:fbsmFaces_IfcFaceBasedSurfaceModel ?connectedFaceSet .\n" +
                "    ?connectedFaceSet ifc:cfsFaces_IfcConnectedFaceSet ?face .\n" +
                "    ?face ifc:bounds_IfcFace ?outterBound .\n" +
                "    ?outterBound ifc:bound_IfcFaceBound ?polyloop .\n" +
                "    ?polyloop ifc:polygon_IfcPolyLoop ?cartesianPointList .\n" +
                "}\n" +
                "ORDER BY ASC(?face)\n";
        return QueryExecutionFactory
                .create(query, dataset.getDataset())
                .execSelect();
    }
}
