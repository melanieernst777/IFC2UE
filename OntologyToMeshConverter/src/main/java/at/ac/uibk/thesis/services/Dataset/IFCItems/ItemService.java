package at.ac.uibk.thesis.services.Dataset.IFCItems;

import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.services.Movement.RotationService;
import com.aspose.threed.Mesh;
import com.aspose.threed.Vector4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RestController
@Scope("application")
public class ItemService {

    @Autowired
    RotationService rotationService;

    @Autowired
    VectorService vectorService;

    /**
     * @param geometry the item geometry of which we want to add the Vectors to
     * @param vectors the Vectors we want to add to the item geometry
     */
    public void computeGeometricVertices(ItemGeometry geometry, List<Vector4> vectors) {
        Mesh mesh = (Mesh) geometry.getNode().getEntity();
        if(addControlPoints(geometry, mesh, vectors)){
            addPolygons(mesh, vectors);
        }
    }

    private void addPolygons(Mesh mesh, List<Vector4> vectors) {
        int start = mesh.getControlPoints().size() - vectors.size();
        List<Integer> polygonIndices = new java.util.ArrayList<>(IntStream.rangeClosed(start, start + vectors.size() - 1)
                .boxed().toList());
        // need to reverse it first
        Collections.reverse(polygonIndices);
        mesh.createPolygon(polygonIndices.stream().mapToInt(Integer::intValue).toArray());
    }

    private void addPolygons(Mesh mesh, List<Vector4> outerBound, List<Vector4> innerBound) {
        int start = mesh.getControlPoints().size() - (outerBound.size() + innerBound.size());
        List<Integer> innerPolygonIndices = new java.util.ArrayList<>(IntStream.rangeClosed(start, start + innerBound.size() - 1)
                .boxed().toList());
        innerPolygonIndices.add(start);
        start += innerBound.size();
        List<Integer> polygonIndices = addOuterBound(outerBound, innerBound, start, innerPolygonIndices);
        // need to reverse it first
        Collections.reverse(polygonIndices);
        mesh.createPolygon(polygonIndices.stream().mapToInt(Integer::intValue).toArray());
    }

    private List<Integer> addOuterBound(List<Vector4> outerBound, List<Vector4> innerBound, int start, List<Integer> innerPolygonIndices) {
        int startInOuter = getIndexOfSmallestDiff(innerBound.get(0), outerBound);
        List<Integer> outerPolygonIndices = getPolygonForOuterBound(startInOuter, outerBound, start);
        return Stream.concat(innerPolygonIndices.stream(),outerPolygonIndices.stream()).collect(Collectors.toList());
    }

    private List<Integer> getPolygonForOuterBound(int startInOuter, List<Vector4> outerBound, int start) {
        List<Integer> outerPolygonIndices = new ArrayList<>();

        for (int i = startInOuter; i < outerBound.size(); i++) {
            outerPolygonIndices.add(start + i);
        }
        for (int i = 0; i < startInOuter; i++) {
            outerPolygonIndices.add(start + i);
        }
        outerPolygonIndices.add(start + startInOuter);
        return outerPolygonIndices;
    }

    private int getIndexOfSmallestDiff(Vector4 vector4, List<Vector4> outerBound) {
        int smallest = -1;
        double minDistance = Double.MAX_VALUE;
        for (int i = 0; i < outerBound.size(); i++) {
            double dist = vectorService.distance(vector4, outerBound.get(i));
            if(dist < minDistance){
                minDistance = dist;
                smallest = i;
            }
        }
        return smallest;
    }

    private boolean addControlPoints(ItemGeometry geometry, Mesh mesh, List<Vector4> vectors) {
        if(vectors.size() < 3) return false;
        // add control points
        vectors.forEach(v -> mesh.getControlPoints().add(rotationService.rotateAndMove(geometry, v)));
        mesh.setName(geometry.getItemRepresentation());
        return true;
    }

    private boolean addControlPoints(ItemGeometry geometry, Mesh mesh, List<Vector4> vectors, List<Vector4> outerVectors) {
        if(vectors.size() < 3 || outerVectors.size() < 3) return false;
        // add control points
        vectors.forEach(v -> mesh.getControlPoints().add(rotationService.rotateAndMove(geometry, v)));
        outerVectors.forEach(v -> mesh.getControlPoints().add(rotationService.rotateAndMove(geometry, v)));
        mesh.setName(geometry.getItemRepresentation());
        return true;
    }

    public void computeGeometricVertices(ItemGeometry geometry, List<Vector4> vectors, List<Vector4> vectorsOfBound) {
        Mesh mesh = (Mesh) geometry.getNode().getEntity();
        if(addControlPoints(geometry, mesh, vectors, vectorsOfBound)){
            addPolygons(mesh, vectors, vectorsOfBound);
        }
    }
}