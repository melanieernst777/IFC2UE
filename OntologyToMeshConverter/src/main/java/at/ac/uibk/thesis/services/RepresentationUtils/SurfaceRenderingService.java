package at.ac.uibk.thesis.services.RepresentationUtils;

import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.enums.SurfaceMaterial;
import com.aspose.threed.Node;
import com.aspose.threed.PhongMaterial;
import com.aspose.threed.Vector3;
import com.aspose.threed.Vector4;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class SurfaceRenderingService {

    public ResultSet getSurfaceRendering(Building building, ItemGeometry itemGeometry) {
        return QueryExecutionFactory
                .create(building.getOwlDataset().getPrefixes() +
                        "SELECT ?side ?specularValue ?reflectanceMethod ?redCode ?greenCode ?blueCode ?transparencyValue WHERE {\n" +
                        "    ?style rdf:type ifc:IfcStyledItem .\n" +
                        "    ?style ifc:item_IfcStyledItem <" + itemGeometry.getItemRepresentation() + "> .\n" +
                        "    ?style ifc:styles_IfcStyledItem ?repr .\n" +
                        "    ?repr ifc:styles_IfcPresentationStyleAssignment ?surfaceStyle .\n" +
                        "    ?surfaceStyle ifc:styles_IfcSurfaceStyle ?rendering .\n" +
                        "    ?surfaceStyle ifc:side_IfcSurfaceStyle ?side .\n" +
                        "    # can be more than just specular: https://standards.buildingsmart.org/IFC/RELEASE/IFC2x3/TC1/HTML/ifcpresentationappearanceresource/lexical/ifcsurfacestylerendering.html\n" +
                        "    OPTIONAL {\n" +
                        "        ?rendering ifc:specularHighlight_IfcSurfaceStyleRendering ?specular .\n" +
                        "        ?specular express:hasDouble ?specularValue .\n" +
                        "        ?rendering ifc:reflectanceMethod_IfcSurfaceStyleRendering ?reflectanceMethod .\n" +
                        "        ?rendering ifc:specularColour_IfcSurfaceStyleRendering ?specularColor .\n" +
                        "    }\n" +
                        "    # colors\n" +
                        "    ?rendering ifc:surfaceColour_IfcSurfaceStyleShading ?rgbColour .\n" +
                        "    ?rgbColour ifc:red_IfcColourRgb ?red .\n" +
                        "    ?rgbColour ifc:green_IfcColourRgb ?green .\n" +
                        "    ?rgbColour ifc:blue_IfcColourRgb ?blue .\n" +
                        "    ?red express:hasDouble ?redCode .\n" +
                        "    ?green express:hasDouble ?greenCode .\n" +
                        "    ?blue express:hasDouble ?blueCode .\n" +
                        "    # color end\n" +
                        "    # transparency\n" +
                        "    ?rendering ifc:transparency_IfcSurfaceStyleRendering ?transparency .\n" +
                        "    ?transparency express:hasDouble ?transparencyValue .\n" +
                        "    #transparency end\n" +
                        "}", building.getOwlDataset().getDataset())
                .execSelect();
    }


    public Node addMaterialToNode(Building building, Node node, SurfaceMaterial surfaceMaterial) {
        if (surfaceMaterial == null) return node;
        if (surfaceMaterial != SurfaceMaterial.UNKNOWN) {
            PhongMaterial itemMaterial = new PhongMaterial();
            itemMaterial.setName(surfaceMaterial.getMaterialName());
            building.getMaterialsContainedInBuilding().putIfAbsent(surfaceMaterial.getMaterialName(), itemMaterial);
            node.setMaterial(building.getMaterialsContainedInBuilding().get(surfaceMaterial.getMaterialName()));
        }
        return node;
    }

    public void addMaterialToItem(Item item, IfcOWLDataset dataset) {
        ResultSet resultSet = getMaterialNameOfItem(item, dataset);
        if (resultSet.hasNext()) {
            String materialName = resultSet.next().get("name").toString().toLowerCase();
            item.setMaterial(getMaterialName(materialName));
        }
        retrieveMaterialForItemGeometries(item);

        if (item.getMaterial() != null) addMaterialToNode(item.getBuilding(), item.getNode(), item.getMaterial());
    }

    private ResultSet getMaterialNameOfItem(Item item, IfcOWLDataset dataset) {
        return QueryExecutionFactory.create(dataset.getPrefixes() +
                "SELECT * WHERE {\n" +
                "    { \n" +
                "        SELECT * WHERE {\n" +
                "            ?typeDef ifc:relatedObjects_IfcRelDefines <" + item.getProductName() + "> .\n" +
                "            ?typeDef ifc:relatingType_IfcRelDefinesByType ?type .\n" +
                "            ?association ifc:relatedObjects_IfcRelAssociates ?type .\n" +
                "            ?association ifc:relatingMaterial_IfcRelAssociatesMaterial ?material_set .\n" +
                "            ?material_set ifc:materialLayers_IfcMaterialLayerSet ?material_list .\n" +
                "            ?material_list list:hasContents ?material_layer .\n" +
                "            ?material_layer ifc:material_IfcMaterialLayer ?material . \n" +
                "            ?material ifc:name_IfcMaterial ?label .\n" +
                "            ?label express:hasString ?name .\n" +
                "        }  \n" +
                "    } UNION \n" +
                "    {\n" +
                "        SELECT * WHERE {\n" +
                "            ?association ifc:relatedObjects_IfcRelAssociates <" + item.getProductName() + "> .\n" +
                "            ?association ifc:relatingMaterial_IfcRelAssociatesMaterial ?material_set .\n" +
                "            ?material_set ifc:materialLayers_IfcMaterialLayerSet ?material_list .\n" +
                "            ?material_list list:hasContents ?material_layer .\n" +
                "            ?material_layer ifc:material_IfcMaterialLayer ?material . \n" +
                "            ?material ifc:name_IfcMaterial ?label .\n" +
                "            ?label express:hasString ?name .\n" +
                "        }\n" +
                "    } \n" +
                "}\n" +
                "\n", dataset.getDataset()).execSelect();
    }

    /**
     * Extracts the material of the item geometry and adds it to the building
     * (otherwise it would export the material of each item geometry as an extra asset)
     * @param building The building containing a list of materials
     * @param itemGeometry The item geometry for which we want to extract the material
     */
    public void extractSurfaceData(Building building, ItemGeometry itemGeometry) {
        ResultSet resultSet = this.getSurfaceRendering(building, itemGeometry);
        PhongMaterial material = new PhongMaterial();
        if (resultSet.hasNext()) {
            QuerySolution querySolution = resultSet.next();
            if (querySolution.contains("redCode")) {
                itemGeometry.setColor(new Vector4(
                        querySolution.get("redCode").asLiteral().getDouble(),
                        querySolution.get("greenCode").asLiteral().getDouble(),
                        querySolution.get("blueCode").asLiteral().getDouble()
                ));
                material.setSpecularColor(new Vector3(itemGeometry.getColor()));
                material.setDiffuseColor(new Vector3(itemGeometry.getColor()));
            }
            if (querySolution.contains("transparencyValue")) {
                if (querySolution.get("transparencyValue").asLiteral().getDouble() > 0.8) {
                    itemGeometry.setNode(addMaterialToNode(building, itemGeometry.getNode(), SurfaceMaterial.GLASS));
                    return;
                }
                itemGeometry.setTransparency(querySolution.get("transparencyValue").asLiteral().getDouble());
            }
            if (querySolution.contains("specularValue")) {
                itemGeometry.setSpecularValue(querySolution.get("specularValue").asLiteral().getDouble());
            }
        }
        if (!building.getMaterialsContainedInBuilding().containsKey(itemGeometry.getColorOfItemGeometry())) {

            material.setName("Color_" + itemGeometry.getColorOfItemGeometry());
            building.getMaterialsContainedInBuilding().putIfAbsent(itemGeometry.getColorOfItemGeometry(), material);
        }
        itemGeometry.getNode().setMaterial(building.getMaterialsContainedInBuilding().get(itemGeometry.getColorOfItemGeometry()));
    }

    /**
     * Extracts the material of all the geometries of the item if the item does not have a material,
     * otherwise it will add the material of the item to the item geometries
     * @param item The item to which we want to aad the material
     */
    public void retrieveMaterialForItemGeometries(Item item) {
        if (item.getMaterial() == null) {
            for (ItemGeometry itemGeometry : item.getItemGeometries()) {
                extractSurfaceData(item.getBuilding(), itemGeometry);
            }
        } else {
            for (ItemGeometry itemGeometry : item.getItemGeometries()) {
                if (itemGeometry.getNode().getMaterial() == null) {
                    itemGeometry.getNode().setMaterial(new PhongMaterial());
                }
                itemGeometry.getNode().getMaterial().setName(item.getMaterial().getMaterialName());
            }
        }
    }

    /**
     * Maps the material name stored in the IFC file to a enum which in turn contains the name
     * for the material in Unreal Engine
     * @param materialName The name of the material of an item stored in IFC
     * @return The material of the surface
     */
    public SurfaceMaterial getMaterialName(String materialName) {
        materialName = materialName.toLowerCase();
        if (materialName.contains("gras") || materialName.contains("erde")) {
            return SurfaceMaterial.GRASS;
        } else if (materialName.contains("plastik") || materialName.contains("plastic")) {
            return SurfaceMaterial.PLASTIC;
        } else if (materialName.contains("bitumen")) {
            return SurfaceMaterial.BITUMEN;
        } else if (materialName.contains("varnish")) {
            return SurfaceMaterial.VARNISH;
        } else if (materialName.contains("oak")) {
            return SurfaceMaterial.BROWN_WOOD;
        } else if (materialName.contains("metal") || materialName.contains("alumin") || materialName.contains("steel")) {
            return SurfaceMaterial.METAL;
        } else if (materialName.contains("holz") || materialName.contains("wood") || materialName.contains("laminat") || materialName.contains("birch") || materialName.contains("pine") || materialName.contains("roble denver")) {
            if (materialName.contains("dunkel") || materialName.contains("dark") || materialName.contains("black")) {
                return SurfaceMaterial.DARK_WOOD;
            } else if (materialName.contains("brow")) {
                return SurfaceMaterial.BROWN_WOOD;
            }
            return SurfaceMaterial.WOOD;
        } else if (materialName.contains("glas") || materialName.contains("glass")) {
            return SurfaceMaterial.GLASS;
        } else if (materialName.contains("ziegel") || materialName.contains("mauer") || materialName.contains("wall") || materialName.contains("brick")) {
            return SurfaceMaterial.BRICK;
        } else if (materialName.contains("beton") || materialName.contains("concrete") || materialName.contains("d\\x2\\00e4\\x0\\mmung - hart") || materialName.contains("d\\x\\e4mmung - hart")) {
            return SurfaceMaterial.CONCRETE;
        }
        return null;
    }
}