package at.ac.uibk.thesis.services.Dataset.IFCItems;

import at.ac.uibk.thesis.Constants;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.services.Movement.RotationService;
import com.aspose.threed.Mesh;
import com.aspose.threed.Vector4;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import java.util.Arrays;
import java.util.List;

@Component
@Scope("application")
public class ExtrudedAreaSolidService {

    @Autowired
    ItemService itemService;

    @Autowired
    PolylineService polylineService;

    @Autowired
    RotationService rotationService;

    @Autowired
    VectorService vectorService;

    /**
     * @param dataset        the dataset, generated with the ifc file
     * @param item           the item to which the item geometry belongs to
     * @param itemGeometry   the item geometry of which we want to extract the data
     * @param representation the link of the extruded area
     */
    public void getVector4sOfExtrudedAreaSolid(IfcOWLDataset dataset, Item item, ItemGeometry itemGeometry, String representation) {
        QuerySolution qs = getInformationAboutExtrudedArea(representation, dataset);
        if (qs == null) return;
        Vector4 position = new Vector4(qs.get("xPos").asLiteral().getDouble(), qs.get("yPos").asLiteral().getDouble(), qs.get("zPos").asLiteral().getDouble());

        if (qs.contains("xDir")) {

            itemGeometry.setDirection(new Vector4(
                            qs.get("xDir").asLiteral().getDouble(),
                            qs.get("yDir").asLiteral().getDouble(),
                            qs.get("zDir").asLiteral().getDouble()
                    )
            );


            itemGeometry.setRefDirection(new Vector4(
                            qs.get("xRefDir").asLiteral().getDouble(),
                            qs.get("yRefDir").asLiteral().getDouble(),
                            qs.get("zRefDir").asLiteral().getDouble()
                    )
            );

        }

        if (qs.contains("polyline") && qs.get("yCyDim") == null) {
            //  itemGeometry.rotationMatrix.setIdentity(); ?
            // check if the polyline has an additional transformation
            Matrix4d currentRotation = rotationService.setUpRotationMatrix(itemGeometry.getDirection(), itemGeometry.getRefDirection());
            itemGeometry.rotationMatrix.mul(currentRotation);
            // itemGeometry.rotationMatrix  = itemService.setUpRotationMatrix(itemGeometry.getDirection(), itemGeometry.getRefDirection());
            //polylineService.extractDataFromPolyline(dataset, itemGeometry, qs.get("polyline").toString());
        }

        else {
            Matrix4d currentRotation = rotationService.setUpRotationMatrix(itemGeometry.getDirection(), itemGeometry.getRefDirection());
            itemGeometry.rotationMatrix.mul(currentRotation);
            Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
            List<Vector4> toInvert = addVerticesOfBox(itemGeometry, qs);
            Vector4 pos;
            if (item.getTrimmedProductName().contains("Opening")) {
                int i = 0;
                if (itemGeometry.getDirection().y == 1) {
                    i = 3;
                }
                if (itemGeometry.getDirection().y == -1) {
                    i = 1;
                }
                pos = vectorService.invert(toInvert.get(i));

            } else {
                pos = getTranslationOfExtrudedElement(currentRotation, position, qs.get("yCyDim").asLiteral().getDouble(), toInvert.get(0));
            }
            pos = vectorService.add(itemGeometry.getTranslation(), pos);
            itemGeometry.setPosition(vectorService.add(pos, itemGeometry.getPosition()));
            for (Vector4 v : toInvert) {
                mesh.getControlPoints().add(
                        Vector4.add(v, itemGeometry.getPosition())
                );
            }
        }
    }

    /**
     * Calculates the translation that needs to be applied to the extruded element
     * @param rotation The rotation which needs to be applied to the position
     * @param position The position of the extruded area solid
     * @param thickness The thickness of the extruded area solid
     * @param toInvert The vector we need to invert
     * @return The rotated and inverted translation
     */
    private Vector4 getTranslationOfExtrudedElement(Matrix4d rotation, Vector4 position, double thickness, Vector4 toInvert) {
        Vector4 referencePoint = new Vector4(position.x, position.y, position.z);
        Vector4 c = vectorService.invert(toInvert);
        Point3d p = new Point3d(c.x, c.y, c.z);

        rotation.transform(p);
        c = new Vector4(p.x, p.y, p.z);
        if (vectorService.getSum(referencePoint) == 0) {
            if (Math.abs(c.x - thickness / 2) < Constants.COMPARISON_THRESHOLD || Math.abs(c.x + thickness / 2) < Constants.COMPARISON_THRESHOLD) {
                referencePoint.y = (referencePoint.y + c.y);
            } else {
                referencePoint.x = (referencePoint.x + c.x);
            }
        }
        Point3d point = new Point3d(referencePoint.x, referencePoint.y, referencePoint.z);
        return new Vector4(point.x, point.y, point.z);
    }


    /**
     * @param itemGeometry the item geometry to which the extracted data belongs to
     * @param r            the query solution containing the information about the extruded area
     */
    private List<Vector4> addVerticesOfBox(ItemGeometry itemGeometry, QuerySolution r) {

        double x = r.get("xCxDim").asLiteral().getDouble();
        double y = r.get("yCyDim").asLiteral().getDouble();
        double z = r.get("depthValue").asLiteral().getDouble();

        double xOffset = (x / 2) * (-1);
        double yOffset = (y / 2) * (-1);
        double zOffset = 0;

        List<Vector4> Vector4s = Arrays.asList(
                rotationService.CreateAndRotateVector4(xOffset, yOffset, z + zOffset, itemGeometry),
                rotationService.CreateAndRotateVector4(x + xOffset, yOffset, z + zOffset, itemGeometry),
                rotationService.CreateAndRotateVector4(x + xOffset, y + yOffset, z + zOffset, itemGeometry),
                rotationService.CreateAndRotateVector4(xOffset, y + yOffset, z + zOffset, itemGeometry),
                rotationService.CreateAndRotateVector4(xOffset, yOffset, zOffset, itemGeometry),
                rotationService.CreateAndRotateVector4(x + xOffset, yOffset, zOffset, itemGeometry),
                rotationService.CreateAndRotateVector4(x + xOffset, y + yOffset, zOffset, itemGeometry),
                rotationService.CreateAndRotateVector4(xOffset, y + yOffset, zOffset, itemGeometry)
        );

        Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
        mesh.setName(itemGeometry.getItemRepresentation());

        if(itemGeometry.item.getProductName().contains("Opening")){
            mesh.createPolygon(new int[]{3,2,1,0});
            mesh.createPolygon(new int[]{2,6,5,1});
            mesh.createPolygon(new int[]{6,7,4,5});
            mesh.createPolygon(new int[]{7,3,0,4});
            mesh.createPolygon(new int[]{1,5,4,0});
            mesh.createPolygon(new int[]{7,6,2,3});
        } else {
            mesh.createPolygon(new int[]{0, 1, 2, 3});
            mesh.createPolygon(new int[]{1, 5, 6, 2});
            mesh.createPolygon(new int[]{5, 4, 7, 6});
            mesh.createPolygon(new int[]{4, 0, 3, 7});
            mesh.createPolygon(new int[]{0, 4, 5, 1});
            mesh.createPolygon(new int[]{3, 2, 6, 7});
        }

        return Vector4s;
    }
    /**
     * an extruded area is like a cube, with height, depth and dimension
     *
     * @param extrudedArea the link to the extruded area
     * @param dataset      the dataset, generated with the ifc file
     * @return the query solution with the information of the extruded area
     */
    private QuerySolution getInformationAboutExtrudedArea(String extrudedArea, IfcOWLDataset dataset) {
        String query = dataset.getPrefixes() +
                "SELECT ?xPos ?yPos ?zPos ?axis ?xCxDim ?yCyDim ?depthValue ?xDirection ?yDirection ?zDirection  ?xDir ?yDir ?zDir ?xRefDir ?yRefDir ?zRefDir ?polyline\n" +
                "#SELECT ?xCxDim ?xDirection ?yDirection ?zDirection ?yCyDim ?depth ?pl ?plD ?plR ?polyline\n" +
                "#SELECT ?xCxDim ?yCyDim ?depth ?pl ?plD ?plR \n" +
                "WHERE {\n" +
                "    \n" +
                "    <" + extrudedArea + "> rdf:type ifc:IfcExtrudedAreaSolid .\n" +
                "    OPTIONAL {\n" +
                "    # direction of the extruded area\n" +
                "    <" + extrudedArea + "> ifc:extrudedDirection_IfcExtrudedAreaSolid ?direction .\n" +
                "    ?direction ifc:directionRatios_IfcDirection ?directionList .\n" +
                "    ?directionList list:hasContents ?xMeasureDirection .\n" +
                "    ?xMeasureDirection express:hasDouble ?xDirection .\n" +
                "    ?directionList list:hasNext ?yDirectionList .\n" +
                "    ?yDirectionList list:hasContents ?yDirectionMeasure .\n" +
                "    ?yDirectionMeasure express:hasDouble ?yDirection .\n" +
                "    ?yDirectionList list:hasNext ?zDirectionList .\n" +
                "    ?zDirectionList list:hasContents ?zDirectionMeasure .\n" +
                "    ?zDirectionMeasure express:hasDouble ?zDirection .\n" +
                "    # direction of the extruded area end\n" +
                "    } \n" +
                "    <" + extrudedArea + "> ifc:sweptArea_IfcSweptAreaSolid ?sweptArea .\n" +
                "    OPTIONAL {\n" +
                "    # dimensions of the area\n" +
                "    ?sweptArea ifc:xDim_IfcRectangleProfileDef ?xDim .\n" +
                "    ?sweptArea ifc:yDim_IfcRectangleProfileDef ?yDim .\n" +
                "    ?xDim express:hasDouble ?xCxDim .\n" +
                "    ?yDim express:hasDouble ?yCyDim .\n" +
                "    } \n" +
                "    OPTIONAL {\n" +
                "       ?sweptArea ifc:outerCurve_IfcArbitraryClosedProfileDef ?polyline .\n" +
                "    }\n" +
                "    OPTIONAL {\n" +
                "    <" + extrudedArea + "> ifc:depth_IfcExtrudedAreaSolid ?depth .\n" +
                "    ?depth express:hasDouble ?depthValue .\n" +
                "    # dimension end\n" +
                "    } \n" +
                "    OPTIONAL {\n" +
                "    # position of the axis\n" +
                "    <" + extrudedArea + "> ifc:position_IfcSweptAreaSolid ?axis .\n" +
                "    ?axis ifc:location_IfcPlacement ?cartesian .\n" +
                "    ?cartesian ifc:coordinates_IfcCartesianPoint ?xList .\n" +
                "    ?xList list:hasNext ?yList .\n" +
                "    ?yList list:hasNext ?zList .\n" +
                "    ?xList list:hasContents ?xContent .\n" +
                "    ?xContent express:hasDouble ?xPos .\n" +
                "    ?yList list:hasContents ?yContent .\n" +
                "    ?yContent express:hasDouble ?yPos .\n" +
                "    ?zList list:hasContents ?zContent .\n" +
                "    ?zContent express:hasDouble ?zPos .\n" +
                "    # position end\n" +
                "    }\n" +
                "    # optional direction of axis\n" +
                "    OPTIONAL {\n" +
                "        ?axis ifc:axis_IfcAxis2Placement3D ?dir .\n" +
                "        ?dir ifc:directionRatios_IfcDirection ?dirList .\n" +
                "        ?dirList list:hasContents ?xMeasure .\n" +
                "        ?xMeasure express:hasDouble ?xDir .\n" +
                "        ?dirList list:hasNext ?yDirList .\n" +
                "        ?yDirList list:hasContents ?yDirMeasure .\n" +
                "        ?yDirMeasure express:hasDouble ?yDir .\n" +
                "        ?yDirList list:hasNext ?zDirList .\n" +
                "        ?zDirList list:hasContents ?zDirMeasure .\n" +
                "        ?zDirMeasure express:hasDouble ?zDir .\n" +
                "\n" +
                "        ?axis ifc:refDirection_IfcAxis2Placement3D ?refDir .\n" +
                "        ?refDir ifc:directionRatios_IfcDirection ?refDirList .\n" +
                "        ?refDirList list:hasContents ?xMeasureRef .\n" +
                "        ?xMeasureRef express:hasDouble ?xRefDir .\n" +
                "        ?refDirList list:hasNext ?yRefDirList .\n" +
                "        ?yRefDirList list:hasContents ?yRefDirMeasure .\n" +
                "        ?yRefDirMeasure express:hasDouble ?yRefDir .\n" +
                "        ?yRefDirList list:hasNext ?zRefDirList .\n" +
                "        ?zRefDirList list:hasContents ?zRefDirMeasure .\n" +
                "        ?zRefDirMeasure express:hasDouble ?zRefDir .\n" +
                "    }\n" +
                "    # optional direction of axis end\n" +
                "    BIND(concat(str(?xPos), \"_\", str(?yPos), \"_\", str(?zPos) ) as ?pl) \n" +
                "    BIND(concat(str(?xDir), \" \", str(?yDir), \" \", str(?zDir) ) as ?plD) \n" +
                "    BIND(concat(str(?xRefDir), \" \", str(?yRefDir), \" \", str(?zRefDir) ) as ?plR) \n" +
                "    \n" +
                "} \n";
        ResultSet resultSet = QueryExecutionFactory
                .create(query, dataset.getDataset())
                .execSelect();
        if (resultSet.hasNext()) {
            return resultSet.next();
        }
        return null;
    }
}