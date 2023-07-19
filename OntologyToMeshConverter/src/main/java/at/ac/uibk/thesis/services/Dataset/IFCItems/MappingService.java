package at.ac.uibk.thesis.services.Dataset.IFCItems;


import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import com.aspose.threed.Vector4;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@Scope("application")
public class MappingService {

    /**
     * @param dataset        the dataset, generated with the ifc file
     * @param representation the link to the representation
     * @param itemGeometry   the item geometry to which the representation belongs to
     * @return the list of item geometries belonging to the mapped item
     */
    public List<ItemGeometry> getDataFromMappedItem(IfcOWLDataset dataset, String representation, ItemGeometry itemGeometry, Item item) {
        List<ItemGeometry> geometries = new ArrayList<>();
        ResultSet resultSet = getInfoOfMappedItem(dataset, representation);
        if (resultSet.hasNext()) {
            resultSet.forEachRemaining(qs -> addNewItemToList(geometries, itemGeometry, qs.get("item").toString(), qs, item));
        }
        return geometries;
    }

    private void addNewItemToList(List<ItemGeometry> geometries, ItemGeometry itemGeometry, String representation, QuerySolution qs, Item item) {
        ItemGeometry geometry = new ItemGeometry(itemGeometry, representation, item);
        // extract color and transparency
        if (qs.contains("redCode")) {
            geometry.setColor(new Vector4(qs.get("redCode").asLiteral().getDouble(), qs.get("blueCode").asLiteral().getDouble(), qs.get("blueCode").asLiteral().getDouble()));
            geometry.setTransparency(qs.get("transparencyValue").asLiteral().getDouble());
        }
        geometries.add(geometry);
    }

    /**
     * @param dataset    the dataset, generated with the ifc file
     * @param mappedItem the link to the mapped item
     * @return the result set containing basic information like color
     */
    private ResultSet getInfoOfMappedItem(IfcOWLDataset dataset, String mappedItem) {
        String query =
                dataset.getPrefixes() +
                "SELECT ?item ?side ?specularValue ?reflectanceMethod ?redCode ?greenCode ?blueCode ?transparencyValue WHERE {\n" +
                "    <" + mappedItem + "> ifc:mappingSource_IfcMappedItem ?reprMap .\n" +
                "    ?reprMap ifc:mappedRepresentation_IfcRepresentationMap ?shapeRepr .\n" +
                "    ?shapeRepr ifc:items_IfcRepresentation ?item .\n" +
                "    OPTIONAL {\n" +
                "        ?style ifc:item_IfcStyledItem ?item .\n" +
                "        ?style ifc:styles_IfcStyledItem ?repr .\n" +
                "        ?repr ifc:styles_IfcPresentationStyleAssignment ?surfaceStyle .\n" +
                "        ?surfaceStyle ifc:styles_IfcSurfaceStyle ?rendering .\n" +
                "        ?surfaceStyle ifc:side_IfcSurfaceStyle ?side .\n" +
                "        # can be more than just specular: https://standards.buildingsmart.org/IFC/RELEASE/IFC2x3/TC1/HTML/ifcpresentationappearanceresource/lexical/ifcsurfacestylerendering.htm\n" +
                "\n" +
                "        ?rendering ifc:specularHighlight_IfcSurfaceStyleRendering ?specular .\n" +
                "        ?specular express:hasDouble ?specularValue .\n" +
                "        ?rendering ifc:reflectanceMethod_IfcSurfaceStyleRendering ?reflectanceMethod .\n" +
                "        ?rendering ifc:specularColour_IfcSurfaceStyleRendering ?specularColor .\n" +
                "\n" +
                "        # color \n" +
                "        ?rendering ifc:surfaceColour_IfcSurfaceStyleShading ?rgbColour .\n" +
                "        ?rgbColour ifc:red_IfcColourRgb ?red .\n" +
                "        ?rgbColour ifc:green_IfcColourRgb ?green .\n" +
                "        ?rgbColour ifc:blue_IfcColourRgb ?blue .\n" +
                "        ?red express:hasDouble ?redCode .\n" +
                "        ?green express:hasDouble ?greenCode .\n" +
                "        ?blue express:hasDouble ?blueCode .\n" +
                "        # color end\n" +
                "\n" +
                "        # transparency\n" +
                "        ?rendering ifc:transparency_IfcSurfaceStyleRendering ?transparency .\n" +
                "        ?transparency express:hasDouble ?transparencyValue .\n" +
                "        #transparency end\n" +
                "    }\n" +
                "\n" +
                "}";
        return QueryExecutionFactory
                .create(query, dataset.getDataset())
                .execSelect();
    }

}
