package at.ac.uibk.thesis.services;

import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.LightSource;
import com.aspose.threed.Node;
import com.aspose.threed.PolygonModifier;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@Scope("application")
public class LightService {

    public List<LightSource> getAllLightSourcesFromScene(IfcOWLDataset dataset, List<Item> items) {
        ResultSet resultSet = getSwitches(dataset);
        List<LightSource> lightSources = new ArrayList<>();
        resultSet.forEachRemaining(qs -> lightSources.addAll(extractLightInformation(dataset, qs, items)));
        return lightSources;
    }

    private List<LightSource> extractLightInformation(IfcOWLDataset dataset, QuerySolution qs, List<Item> items) {
        // get the id of the switch
        String switchId = qs.get("switchId").toString();
        ResultSet resultSet = getLightSourcesBelongingToSwitchId(dataset, switchId);
        List<LightSource> lightSources = new ArrayList<>();
        resultSet.forEachRemaining(res -> lightSources.add(addNewLightSource(res, items, qs.get("buildingElementProxy"))));
        return lightSources;
    }

    /**
     * Create a new light source which contains the light object and its switch. Also extracts the voltage of
     * the light element
     * @param qs The query solution containing the information of the light object, switch and voltage
     * @param items The items of the building
     * @param buildingElementProxy The RDF node of the building element proxy
     * @return A new light source
     */
    private LightSource addNewLightSource(QuerySolution qs, List<Item> items, RDFNode buildingElementProxy) {
        Optional<Item> optionalSwitch = items.stream().filter(i -> i.getProductName().equals(buildingElementProxy.toString())).findFirst();
        Optional<Item> optionalLight = items.stream().filter(i -> i.getProductName().equals(qs.get("flowTerminal").toString())).findFirst();
        if (optionalSwitch.isPresent() && optionalLight.isPresent()) {
            Node light = GetLightObjectWithValidMesh(optionalLight.get().getNode());
            Node lightSwitch = GetLightObjectWithValidMesh(optionalSwitch.get().getNode());
            if (light != null && lightSwitch != null) {
                light.setName(light.getName() + "LightObject");
                lightSwitch.setName(lightSwitch.getName() + "LightSwitch");
                double voltage = 250.0;
                if (qs.contains("voltage")) {
                    voltage = qs.get("voltage").asLiteral().getDouble();
                }
                return new LightSource(light.getName(), lightSwitch.getName(), voltage);
            }
        }
        return null;
    }

    /**
     * Merges the meshes of the light item.
     * This is done because only one part of the light-able item actually holds light, e.g the bulb.
     * In Unreal Engine the other meshes would have a shadow, therefore we need to merge the meshes so that
     * we do not have any shadows.
     * @param node The node of the light object
     * @return The new where all child objects are merged
     */
    private Node GetLightObjectWithValidMesh(Node node) {
        if (node.getEntity() != null) return node;
        node.setEntity(PolygonModifier.mergeMesh(node.getChildNodes()));
        node.getChildNodes().clear();
        /*
        for(Node n : node.getChildNodes()){
            if(n.getEntity() != null) return n;
        }
        */
        return node;
    }


    private ResultSet getLightSourcesBelongingToSwitchId(IfcOWLDataset dataset, String switchID) {
        String query = dataset.getPrefixes() +
                "SELECT DISTINCT ?flowTerminal ?switchID ?voltage WHERE {\n" +
                "#SELECT * WHERE {\n" +
                "    ?flowTerminal rdf:type ifc:IfcFlowTerminal .\n" +
                "    ?propsVoltage rdf:type ifc:IfcRelDefinesByProperties .\n" +
                "    ?propsVoltage ifc:relatedObjects_IfcRelDefines ?flowTerminal .\n" +
                "    ?propsSwitch rdf:type ifc:IfcRelDefinesByProperties .\n" +
                "    ?propsSwitch ifc:relatedObjects_IfcRelDefines ?flowTerminal .\n" +
                "    ?flowTerminal ifc:name_IfcRoot ?proxy_label .\n" +
                "    ?proxy_label express:hasString ?proxy_name .\n" +
                "    \n" +
                "    #Switch ID\n" +
                "    ?propsSwitch ifc:relatingPropertyDefinition_IfcRelDefinesByProperties ?propertiesSwitch .\n" +
                "    ?propertiesSwitch ifc:hasProperties_IfcPropertySet ?propertySetSwitch .\n" +
                "    ?propertiesSwitch ifc:name_IfcRoot ?label_rootSwitch .\n" +
                "    \n" +
                "    ?label_rootSwitch express:hasString ?label_root_nameSwitch .\n" +
                "    ?propertySetSwitch ifc:nominalValue_IfcPropertySingleValue ?single_valueSwitch .\n" +
                "    ?propertySetSwitch ifc:name_IfcProperty ?identifierSwitch .\n" +
                "    ?identifierSwitch express:hasString ?id_labelSwitch .\n" +
                "    OPTIONAL{?single_valueSwitch express:hasString ?switchID .}\n" +
                "    FILTER (?id_labelSwitch = \"Schalter-ID\" && ?switchID = \"" + switchID + "\")" +
                "    \n" +
                "    OPTIONAL {\n" +
                "        # Voltage\n" +
                "        ?propsVoltage ifc:relatingPropertyDefinition_IfcRelDefinesByProperties ?propertiesVoltage .\n" +
                "        ?propertiesVoltage ifc:hasProperties_IfcPropertySet ?propertySetVoltage .\n" +
                "        ?propertiesVoltage ifc:name_IfcRoot ?label_rootVoltage .\n" +
                "        ?label_rootVoltage express:hasString ?label_root_nameVoltage .\n" +
                "        ?propertySetVoltage ifc:nominalValue_IfcPropertySingleValue ?single_valueVoltage .\n" +
                "        ?propertySetVoltage ifc:name_IfcProperty ?identifierVoltage .\n" +
                "        ?identifierVoltage express:hasString ?id_labelVoltage .\n" +
                "        #OPTIONAL{?single_value express:hasString ?switch_id .}\n" +
                "        OPTIONAL{?single_valueVoltage express:hasDouble ?voltage .}\n" +
                "        FILTER (?id_labelVoltage = \"Spannung\")\n" +
                "    }\n" +
                "}";
        return QueryExecutionFactory.create(query, dataset.getDataset()).execSelect();
    }

    private ResultSet getSwitches(IfcOWLDataset dataset) {
        String query = dataset.getPrefixes() + """
                 SELECT DISTINCT ?buildingElementProxy ?id_label ?switchId WHERE {
                     ?buildingElementProxy rdf:type ifc:IfcBuildingElementProxy .
                     ?props rdf:type ifc:IfcRelDefinesByProperties .
                     ?props ifc:relatedObjects_IfcRelDefines ?buildingElementProxy .
                     ?buildingElementProxy ifc:name_IfcRoot ?proxy_label .
                     ?proxy_label express:hasString ?proxy_name .
                     ?props ifc:relatingPropertyDefinition_IfcRelDefinesByProperties ?properties .
                     ?properties ifc:hasProperties_IfcPropertySet ?property_set .
                     ?properties ifc:name_IfcRoot ?label_root .
                     ?label_root express:hasString ?label_root_name .
                     ?property_set ifc:nominalValue_IfcPropertySingleValue ?single_value .
                     ?property_set ifc:name_IfcProperty ?identifier .
                     ?identifier express:hasString ?id_label .
                     ?single_value express:hasString ?switchId .
                     FILTER (?id_label = "Schalter-ID") .
                 }""";
        return QueryExecutionFactory.create(query, dataset.getDataset()).execSelect();
    }
}
