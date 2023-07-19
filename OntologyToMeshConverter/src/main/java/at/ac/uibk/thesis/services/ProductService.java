package at.ac.uibk.thesis.services;

import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.services.Dataset.IFCItems.ItemService;
import at.ac.uibk.thesis.services.Movement.GeometryService;
import at.ac.uibk.thesis.services.Movement.RotationService;
import at.ac.uibk.thesis.services.PluginUtils.AdditionalInformationService;
import at.ac.uibk.thesis.services.RepresentationUtils.*;
import com.aspose.threed.*;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

@RestController
@Scope("application")
public class ProductService {

    @Autowired
    RepresentationListService representationListService;

    @Autowired
    ItemsRepresentation itemsRepresentation;

    @Autowired
    SurfaceRenderingService surfaceRenderingService;

    @Autowired
    LightService lightService;

    @Autowired
    ItemService itemService;

    @Autowired
    AdditionalInformationService additionalInformationService;

    @Autowired
    GeometryService geometryService;

    @Autowired
    OpeningService openingService;

    @Autowired
    UVService uvService;

    @Autowired
    RotationService rotationService;

    public Map<String, Object> convertBuildingToFBX(Building building, FileFormat format, int unitScaleFactor, boolean triangulate) {

        Map<String, Object> lights = null;
        Map<String, Item> items = getItemRepresentationRecursively(building);
        Scene scene = building.getScene();
        scene.getAssetInfo().setUpVector(Axis.X_AXIS);
        scene.getAssetInfo().setUnitScaleFactor(unitScaleFactor);
        Optional<Item> optionalItem = items.values().stream().filter(i -> i.getParentItem() == null).findFirst();
        Item root;
        if (optionalItem.isPresent()) {
            root = optionalItem.get();
            scene.getRootNode().getChildNodes().add(root.getNode());
            representationListService.getItemsRepresentation(building, items);
            System.out.println("Added surface material to items");

            items.values().parallelStream().forEach(i -> surfaceRenderingService.addMaterialToItem(i, building.getOwlDataset()));
            addTranslationToChildren(root);
            System.out.println("Added translation to items");

            getItemRepresentationParallel(items.values());
            System.out.println("Added representation to items");

            openingService.cutHoles(items.values());
            System.out.println("Cut holes out of items");

            if(triangulate){
                uvService.addUVCoordinatesToScene(items.values());
                System.out.println("Added UV coordinates to items");
            }



            building.setItems(items.values().stream().toList());
            lights = additionalInformationService.addAdditionalInformationToName(building);
        }
        try {
            scene.save(building.getGeneratedBuildingName() + ".obj", format);
            return lights;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void getItemRepresentationParallel(Collection<Item> values) {
        ForkJoinPool myPool = new ForkJoinPool(16);
        try {
            myPool.submit(() -> values.parallelStream().forEach(i -> itemsRepresentation.findRepresentationAndExecuteQuery(i))).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Item> getItemRepresentationRecursively(Building building) {
        Map<String, Item> items = new HashMap<>();
        ResultSet resultSet = queryProductsAndTheirPlacements(building.getOwlDataset());
        if (resultSet.hasNext()) {
            resultSet.forEachRemaining(s -> extractInformationFromQuery(building, items, s));
        }
        return items;
    }

    private void addTranslationToChildren(Item root) {

        for (Item item : root.getChildItems()) {
            geometryService.getRotatedTranslation(root.rotationMatrix, item.getTranslations());

            item.getTranslations().addAll(root.getTranslations());
            item.getRotationMatrix().mul(root.getRotationMatrix());

            if (!item.getChildItems().isEmpty()) {
                addTranslationToChildren(item);
            }
        }
    }

    private void extractInformationFromQuery(Building building, Map<String, Item> items, QuerySolution s) {
        Item item;
        if (items.containsKey(s.get("prod").toString())) {
            item = items.get(s.get("prod").toString());
        } else {
            item = new Item(building, s.get("prod").toString());
            items.put(s.get("prod").toString(), item);
        }
        Vector3 placement = new Vector3(s.get("x").asLiteral().getDouble(), s.get("y").asLiteral().getDouble(), s.get("z").asLiteral().getDouble());
        item.getTranslations().add(placement);
        item.rotationMatrix.setIdentity();
        if (s.contains("xD")) {
            Vector4 refDir = new Vector4(s.get("xR").asLiteral().getDouble(), s.get("yR").asLiteral().getDouble(), s.get("zR").asLiteral().getDouble());
            Vector4 dir = new Vector4(s.get("xD").asLiteral().getDouble(), s.get("yD").asLiteral().getDouble(), s.get("zD").asLiteral().getDouble());

            item.rotationMatrix = rotationService.setUpRotationMatrix(dir, refDir);
        }
        if (s.contains("relatesTo")) {
            items.putIfAbsent(s.get("relatesTo").toString(), new Item(building, s.get("relatesTo").toString()));
            item.setParentItem(items.get(s.get("relatesTo").toString()));
            items.get(s.get("relatesTo").toString()).addChildItem(item);
        }
    }

    private ResultSet queryProductsAndTheirPlacements(IfcOWLDataset dataset) {
        return QueryExecutionFactory.create(dataset.getPrefixes() + """
                select DISTINCT ?prod  ?x ?y ?z ?xD ?yD ?zD ?xR ?yR ?zR ?relatesTo where {\s
                #SELECT DISTINCT ?prod ?localPlacement ?x ?y ?z ?relatesTo ?localPlacement WHERE {
                \t?localPlacement rdf:type  ifc:IfcLocalPlacement .
                    OPTIONAL {\s
                        ?localPlacement ifc:placementRelTo_IfcLocalPlacement ?relatesToPlacement .\s
                    \t?relatesTo ifc:objectPlacement_IfcProduct ?relatesToPlacement .\s
                    }
                    ?localPlacement ifc:relativePlacement_IfcLocalPlacement ?axis .
                    ?axis ifc:location_IfcPlacement ?cartesian .
                    ?cartesian ifc:coordinates_IfcCartesianPoint ?xList .
                    ?prod ifc:objectPlacement_IfcProduct ?localPlacement .
                \t?xList list:hasNext ?yList .
                    ?yList list:hasNext ?zList .
                    ?xList list:hasContents ?xContent .
                    ?xContent express:hasDouble ?x .
                    ?yList list:hasContents ?yContent .
                    ?yContent express:hasDouble ?y .
                    ?zList list:hasContents ?zContent .
                    ?zContent express:hasDouble ?z .
                    OPTIONAL {
                        ?axis ifc:axis_IfcAxis2Placement3D ?direction .
                        ?direction ifc:directionRatios_IfcDirection ?xListD .
                        ?xListD list:hasContents ?xContentD .
                        ?xContentD express:hasDouble ?xD .
                        ?xListD list:hasNext ?yListD .
                        ?yListD list:hasContents ?yContentD .
                        ?yContentD express:hasDouble ?yD .
                        ?yListD list:hasNext ?zListD .
                        ?zListD list:hasContents ?zContentD .
                        ?zContentD express:hasDouble ?zD .
                        ?axis ifc:refDirection_IfcAxis2Placement3D ?refDirection .
                        ?refDirection ifc:directionRatios_IfcDirection ?xListR .
                        ?xListR list:hasContents ?xContentR .
                        ?xContentR express:hasDouble ?xR .
                        ?xListR list:hasNext ?yListR .
                        ?yListR list:hasContents ?yContentR .
                        ?yContentR express:hasDouble ?yR .
                        ?yListR list:hasNext ?zListR .
                        ?zListR list:hasContents ?zContentR .
                        ?zContentR express:hasDouble ?zR .
                    }
                }
                """, dataset.getDataset()).execSelect();
    }
}
