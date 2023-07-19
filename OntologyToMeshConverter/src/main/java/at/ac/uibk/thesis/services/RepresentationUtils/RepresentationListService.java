package at.ac.uibk.thesis.services.RepresentationUtils;

import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.services.Dataset.IFCItems.Axis2Placement3D;
import at.ac.uibk.thesis.services.Dataset.IFCItems.MappingService;
import com.aspose.threed.PhongMaterial;
import com.aspose.threed.Vector3;
import com.aspose.threed.Vector4;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope("application")
public class RepresentationListService {

    @Autowired
    MappingService ifcMapping;

    @Autowired
    Axis2Placement3D axis2Placement3D;

    @Autowired
    SurfaceRenderingService surfaceRenderingService;

    public void getItemsRepresentation(Building building, Map<String, Item> itemsWithTheirRepresentations) {
        ResultSet resultSet = getItemsRepresentationQuery(building.getOwlDataset());
        resultSet.forEachRemaining(r -> addItemsRepresentationToList(building, itemsWithTheirRepresentations, r));
    }

    /**
     * Extracts the item geometries of all the items
     * @param building The building containing the items
     * @param itemsWithTheirRepresentations A map of item names and the corresponding item
     * @param r The query solution containing the information about the item geometry
     */
    private void addItemsRepresentationToList(Building building, Map<String, Item> itemsWithTheirRepresentations, QuerySolution r) {

        itemsWithTheirRepresentations.putIfAbsent(r.get("product").toString(), new Item(building, r.get("product").toString()));

        ItemGeometry itemGeometry = extractItemInfo(r, itemsWithTheirRepresentations.get(r.get("product").toString()));
        extractSurfaceData(building, itemGeometry, r);
        if (itemGeometry.getItemRepresentation().toLowerCase().contains("mappeditem")) {
            itemsWithTheirRepresentations.get(r.get("product").toString()).getItemGeometries().addAll(ifcMapping.getDataFromMappedItem(building.getOwlDataset(), itemGeometry.getItemRepresentation(), itemGeometry, itemsWithTheirRepresentations.get(r.get("product").toString())));
        } else {
            itemsWithTheirRepresentations.get(r.get("product").toString()).getItemGeometries().add(itemGeometry);
        }
        if (r.get("next") != null) {
            getItemsNextRepresentationRecursively(building, itemsWithTheirRepresentations, r, itemsWithTheirRepresentations.get(r.get("product").toString()));
        }
    }

    /**
     * Adds the surface data, like transparency and color to the item geometry.
     * @param building The building to which we add the material
     * @param itemGeometry The item geometry of which we want to extract the surface data
     * @param querySolution The query solution containing the information about the surface
     */
    public void extractSurfaceData(Building building, ItemGeometry itemGeometry, QuerySolution querySolution) {
        if (itemGeometry.getItem().getNode().getMaterial() != null) {
            if (!itemGeometry.getItem().getNode().getMaterial().getName().contains("Color")) return;
        }
        Vector4 color = new Vector4(1, 1, 1);
        double transparency = 0;
        if (querySolution.contains("redCode")) {
            color = new Vector4(
                    querySolution.get("redCode").asLiteral().getDouble(),
                    querySolution.get("greenCode").asLiteral().getDouble(),
                    querySolution.get("blueCode").asLiteral().getDouble()
            );
        }
        if (querySolution.contains("transparencyValue")) {
            transparency = querySolution.get("transparencyValue").asLiteral().getDouble();
        }
        PhongMaterial phongMaterial;
        if (building.getMaterialsContainedInBuilding().containsKey(color.toString() + transparency)) {
            phongMaterial = building.getMaterialsContainedInBuilding().get(color.toString() + transparency);
        } else {
            phongMaterial = new PhongMaterial();
            phongMaterial.setTransparency(transparency);
            phongMaterial.setDiffuseColor(new Vector3(color.x, color.y, color.z));
            building.getMaterialsContainedInBuilding().put(color.toString() + transparency, new PhongMaterial());
        }
        itemGeometry.getNode().setMaterial(phongMaterial);
    }

    /**
     * Adds all the child items of a root item to the building
     * @param building The building to which the items belong to
     * @param itemsWithTheirRepresentations A map of item names and the corresponding item
     * @param r The query solution containing the information of the next item
     * @param item The item of which we want to retrieve the child items
     */
    private void getItemsNextRepresentationRecursively(Building building, Map<String, Item> itemsWithTheirRepresentations, QuerySolution r, Item item) {
        if (r.get("next") != null) {
            QuerySolution sol = getNextItemsRepresentation(building, r.get("next").toString(), r.get("product").toString());
            itemsWithTheirRepresentations.get(r.get("product").toString()).getItemGeometries().add(extractItemInfo(sol, item));
            getItemsNextRepresentationRecursively(building, itemsWithTheirRepresentations, sol, item);
        }
    }

    public ResultSet getItemsRepresentationQuery(IfcOWLDataset dataset) {
        return QueryExecutionFactory
                .create(dataset.getPrefixes() + """
                        select ?product ?itemsRepr ?next ?placement ?axis ?redCode ?greenCode ?blueCode ?transparencyValue where {
                            ?prodDefShape rdf:type  ifc:IfcProductDefinitionShape .
                            ?prodDefShape ifc:representations_IfcProductRepresentation ?repr .
                            ?repr list:hasContents ?ShapeRepr .
                            ?ShapeRepr ifc:items_IfcRepresentation ?itemsRepr .
                            ?itemsRepr rdf:type ?type .
                            ?product ifc:representation_IfcProduct ?prodDefShape .
                            ?product ifc:objectPlacement_IfcProduct ?placement .
                            ?placement ifc:relativePlacement_IfcLocalPlacement ?axis .
                            OPTIONAL { ?repr list:hasNext ?next }

                            OPTIONAL{
                                ?styledItem rdf:type ifc:IfcStyledItem .
                                ?styledItem ifc:styles_IfcStyledItem ?presentationStyle .
                                ?presentationStyle ifc:styles_IfcPresentationStyleAssignment ?surfaceStyle .
                                ?surfaceStyle ifc:styles_IfcSurfaceStyle ?surface .
                                ?surface rdf:type  ifc:IfcSurfaceStyleRendering .\s
                                ?styledItem ifc:item_IfcStyledItem ?itemsRepr .

                                # color\s
                                ?surface ifc:surfaceColour_IfcSurfaceStyleShading ?rgbColour .
                                ?rgbColour ifc:red_IfcColourRgb ?red .
                                ?rgbColour ifc:green_IfcColourRgb ?green .
                                ?rgbColour ifc:blue_IfcColourRgb ?blue .
                                ?red express:hasDouble ?redCode .
                                ?green express:hasDouble ?greenCode .
                                ?blue express:hasDouble ?blueCode .
                                # color end

                                # transparency
                                ?surface ifc:transparency_IfcSurfaceStyleRendering ?transparency .
                                ?transparency express:hasDouble ?transparencyValue .
                                #transparency end
                            }
                        }
                        """, dataset.getDataset())
                .execSelect();
    }

    public QuerySolution getNextItemsRepresentation(Building building, String previous, String product) {
        return QueryExecutionFactory
                .create(building.getOwlDataset().getPrefixes() +
                                "select (<" + product + "> as ?product) ?itemsRepr ?next ?placement ?axis where {\n" +
                                "    <" + previous + "> list:hasContents ?ShapeRepr .\n" +
                                "    ?ShapeRepr ifc:items_IfcRepresentation ?itemsRepr .\n" +
                                "    ?itemsRepr rdf:type ?type .\n" +
                                "    <" + product + "> ifc:representation_IfcProduct ?prodDefShape .\n" +
                                "    <" + product + "> ifc:objectPlacement_IfcProduct ?placement .\n" +
                                "    ?placement ifc:relativePlacement_IfcLocalPlacement ?axis .\n" +
                                "    OPTIONAL { <" + previous + "> list:hasNext ?next }\n" +
                                "}"
                        , building.getOwlDataset().getDataset())
                .execSelect().nextSolution();
    }

    private ItemGeometry extractItemInfo(QuerySolution querySolution, Item item) {
        Vector4 ref = new Vector4(1, 0, 0);
        Vector4 dir = new Vector4(0, 0, 1);
        ItemGeometry geometry = new ItemGeometry(querySolution.get("itemsRepr").toString(), new Vector4(0, 0, 0), dir, ref, item);
        item.getItemGeometries().add(geometry);
        return geometry;
        // return axis2Placement3D.getInformationAboutPlacement(building.getOwlDataset(), querySolution, item);
    }

}