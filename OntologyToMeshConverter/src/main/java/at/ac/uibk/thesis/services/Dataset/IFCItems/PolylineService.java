package at.ac.uibk.thesis.services.Dataset.IFCItems;


import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.entities.Vector3D;
import at.ac.uibk.thesis.services.Movement.RotationService;
import com.aspose.threed.Mesh;
import com.aspose.threed.Vector4;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@Scope("application")
public class PolylineService {

    @Autowired
    CartesianPointListService cartesianPointListService;

    @Autowired
    RotationService rotationService;

    /**
     * @param dataset  the dataset, generated with the ifc file
     * @param polyline the polyline of which we want to extract the data
     */
    public void extractDataFromPolyline(IfcOWLDataset dataset, ItemGeometry itemGeometry, String polyline) {
        List<Vector4> vectors = extractVectorListFromPolyline(dataset, polyline);
        Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
        if(vectors.size() < 3) return;
        for (com.aspose.threed.Vector4 vector : vectors) {
            itemGeometry.getVertices().add(new Vector3D(vector));
            mesh.getControlPoints().add(
                   // Vector4.add(vector, itemGeometry.getPosition())
                    Vector4.add(rotationService.CreateAndRotateVector4(vector, itemGeometry), itemGeometry.getPosition())

            );
        }
        List<Integer> polygonIndices = new java.util.ArrayList<>(IntStream.rangeClosed(mesh.getControlPoints().size() - vectors.size(), mesh.getControlPoints().size() - 1)
                .boxed().toList());
        mesh.createPolygon(polygonIndices.stream().mapToInt(Integer::intValue).toArray());
        Collections.reverse(polygonIndices);
        mesh.createPolygon(polygonIndices.stream().mapToInt(Integer::intValue).toArray());
    }

    public List<Vector4> extractVectorListFromPolyline(IfcOWLDataset dataset, String polyline){
        List<List<Vector4>> Vector4s = new ArrayList<>();
        ResultSet res = getCartesianPointListFromPolyline(dataset, polyline);
        res.forEachRemaining(r -> Vector4s.add(cartesianPointListService.getAllVectorOfCartesianPointList(dataset, r.get("cartesianPointList").toString())));
        return Vector4s.stream().flatMap(List::stream).toList();
    }

    /**
     * @param dataset  the dataset, generated with the ifc file
     * @param polyline the link to the polyline
     * @return the result set with the information about the cartesian point list -> Vector4s
     */
    private ResultSet getCartesianPointListFromPolyline(IfcOWLDataset dataset, String polyline) {
        return QueryExecutionFactory.create(dataset.getPrefixes() +
                        "select ?cartesianPointList where { \n" +
                        "    <" + polyline + "> ifc:points_IfcPolyline ?cartesianPointList .\n" +
                        "}", dataset.getDataset())
                .execSelect();
    }
}
