package at.ac.uibk.thesis.services.Dataset.IFCItems;


import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import com.aspose.threed.Vector4;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("application")
public class Axis2Placement3D {

    @Autowired
    DirectionService directionService;

    /**
     * Each item has a geometry, this is
     *
     * @param dataset the dataset, generated with the ifc file
     * @param q       query solution which contains information about the geometry from an items representation
     * @return a new item geometry with the location and the direction of the item
     */
    public ItemGeometry getInformationAboutPlacement(IfcOWLDataset dataset, QuerySolution q, Item item, String axisIdentifier) {

        Vector4 ref = new Vector4(1, 0, 0);
        Vector4 dir = new Vector4(0, 0, 1);
        QuerySolution res = queryAxisData(dataset, q.get(axisIdentifier).toString());
        if (res.contains("refDirection")) {
            ref = directionService.getDirectionData(dataset, res.get("refDirection").toString());
        }
        if (res.contains("direction")) {
            dir = directionService.getDirectionData(dataset, res.get("direction").toString());
         }
        return new ItemGeometry(q.get("itemsRepr").toString(), new Vector4(0, 0, 0), dir, ref, item);
    }

    public ItemGeometry getInformationAboutPlacement(IfcOWLDataset dataset, ItemGeometry itemGeometry, QuerySolution q, String axisIdentifier) {

        QuerySolution res = queryAxisData(dataset, q.get(axisIdentifier).toString());
        if (res.contains("refDirection")) {
            Vector4 ref = directionService.getDirectionData(dataset, res.get("refDirection").toString());
            itemGeometry.setRefDirection(ref);
        }
        if (res.contains("direction")) {
            Vector4 dir = directionService.getDirectionData(dataset, res.get("direction").toString());
            itemGeometry.setDirection(dir);
        }
        return itemGeometry;
    }

    /**
     * With this method we extract the position of an element. some elements have a direction and a ref direction, this
     * will also be extracted
     *
     * @param dataset the dataset, generated with the ifc file
     * @param axis    the axis of the element
     * @return the result aka query solution
     */
    private QuerySolution queryAxisData(IfcOWLDataset dataset, String axis) {
        return QueryExecutionFactory.create(dataset.getPrefixes() +
                                "SELECT ?position ?direction ?refDirection WHERE {\n" +
                                "    <" + axis + "> ifc:location_IfcPlacement ?position .\n" +
                                "OPTIONAL {" +
                                "    <" + axis + "> ifc:axis_IfcAxis2Placement3D ?direction .\n" +
                                "    <" + axis + "> ifc:refDirection_IfcAxis2Placement3D ?refDirection .\n" +
                                "}" +
                                "}"
                        , dataset.getDataset())
                .execSelect().next();
    }

}
