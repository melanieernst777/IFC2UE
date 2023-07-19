package at.ac.uibk.thesis.services.RepresentationUtils;

import at.ac.uibk.thesis.Constants;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.services.Dataset.IFCItems.VectorService;
import com.aspose.threed.Mesh;
import com.aspose.threed.Node;
import com.aspose.threed.PolygonModifier;
import com.aspose.threed.Vector4;
import org.apache.tomcat.util.bcel.Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The OpeningService creates a hole in a wall.
 * For now, it only supports creating hole for walls that are orthogonal to an axis.
 * Aspose3D might support the creation of holes in a better and more efficient way
 */
@Component
@Scope("application")
public class OpeningService {

    @Autowired
    VectorService vectorService;

    public void cutHoles(Collection<Item> values) {
        for (Item item : values) {
            List<Item> openingElementsInItem = getAllOpeningElementsInItem(item);
            if(openingElementsInItem.isEmpty()) continue;
            Mesh mergedMesh = mergeMeshes(openingElementsInItem);
            Mesh extrudedArea = getMesh(item);
            addOpeningElementToExtrudedArea(mergedMesh, extrudedArea, openingElementsInItem);
        }
    }

    private Mesh getMesh(Item item) {
        for (ItemGeometry itemGeometry : item.getItemGeometries()) {
            Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
            if(mesh != null && mesh.getPolygons().size() > 0) return mesh;
        }
        return null;
    }

    private Mesh mergeMeshes(List<Item> openingElementsInItem) {
        List<Node> nodes = new ArrayList<>();
        for (Item item : openingElementsInItem) {
            for (ItemGeometry itemGeometry : item.getItemGeometries()) {
                nodes.add(itemGeometry.getNode());
            }
        }
        return PolygonModifier.mergeMesh(nodes);
    }

    private List<Item> getAllOpeningElementsInItem(Item item) {
        List<Item> items = new ArrayList<>();
        for (Item childItem : item.getChildItems()) {
            if(childItem.getProductName().contains("OpeningElement")){
                items.add(childItem);
            }
        }
        return items;
    }

    /***
     * Get all indexes of the polygon of the given mesh that are on the given axis
     * @param axis The axis of which we want to retrieve the points of the given mesh
     * @param points The points from the wall on the given axis. This helps us find out
     *               what points of the opening mesh are on the same plane
     * @param openingMesh The mesh of the opening mesh
     * @return a list containing the polygons of the opening mesh, which are on a given plane
     */
    private static List<Integer> getIndexesOfPolygons(Axis axis, List<Vector4> points, Mesh openingMesh) {
        if (axis == Axis.X) return getPolygonFromMeshOnAxis(points.get(0).x, Axis.X, openingMesh);
        if (axis == Axis.Y) return getPolygonFromMeshOnAxis(points.get(0).y, Axis.Y, openingMesh);
        return getPolygonFromMeshOnAxis(points.get(0).z, Axis.Z, openingMesh);
    }

    /**
     * Get all the polygons from a mesh on a given axis.
     * By checking if the value of the given axis is the same of the polygon points on the same axis,
     * we retrieve all the polygons of the opening mesh which are on the same plane
     * @param value A value from the wall on the plane
     * @param axis The axis of the current wall
     * @param mesh The mesh of the opening element
     * @return a list containing the polygons of the opening mesh, which are on a given plane
     */
    private static List<Integer> getPolygonFromMeshOnAxis(double value, Axis axis, Mesh mesh) {
        List<Integer> polygonsOnSameAxis = new ArrayList<>();
        for (int j = 0; j < mesh.getPolygons().size(); j++) {
            boolean onSameAxis = true;
            for (int i : mesh.getPolygons().get(j)) {
                if (axis == Axis.X) {
                    if (Math.abs(mesh.getControlPoints().get(i).x - value) >= Constants.COMPARISON_THRESHOLD) {
                        onSameAxis = false;
                    }
                } else if (axis == Axis.Y) {
                    if (Math.abs(mesh.getControlPoints().get(i).y - value) >= Constants.COMPARISON_THRESHOLD) {
                        onSameAxis = false;
                    }
                } else if (axis == Axis.Z) {
                    if (Math.abs(mesh.getControlPoints().get(i).z - value) >= Constants.COMPARISON_THRESHOLD) {
                        onSameAxis = false;
                    }
                }
            }
            if (onSameAxis) polygonsOnSameAxis.add(j);
        }
        return polygonsOnSameAxis;
    }

    /***
     * Cuts the given mesh of the opening element out of the parent element
     */
    public void addOpeningElementToExtrudedArea(Mesh mergedMesh, Mesh extrudedMesh, List<Item> openingItems) {

        Axis axis = null;

        for (Item openingItem : openingItems) {
            Mesh openingMesh = getMesh(openingItem);
            // ignore if meshes are empty
            if (extrudedMesh == null || openingMesh == null) return;

            if (extrudedMesh.getControlPoints().isEmpty() || openingMesh.getControlPoints().isEmpty())
                return;
            List<int[]> tempPolygons = new ArrayList<>();
            for (int i = 0; i < extrudedMesh.getPolygons().size(); i++) {
                // get all control points belonging to this polygon -> each polygon is one side
                List<Vector4> controlPoints = new ArrayList<>();
                for (int j : extrudedMesh.getPolygons().get(i)) {
                    controlPoints.add(extrudedMesh.getControlPoints().get(j));
                }
                axis = getAxis(controlPoints);
                int[] newCoordinates = retrieveTheOpeningCoordinatesOnTheSameAxis(mergedMesh.getPolygons(), mergedMesh.getControlPoints() ,extrudedMesh.getPolygons().get(i), controlPoints, extrudedMesh.getControlPoints(), openingMesh, axis);
                tempPolygons.add(newCoordinates);
            }
            if (axis != null) removePolygonsOnSameAxis(openingMesh, axis);
            extrudedMesh.getPolygons().clear();
            extrudedMesh.getPolygons().addAll(tempPolygons);
        }
    }

    /**
     * Retrieves the polygons of the opening mesh on the same plane as the wall polygon and updated the wall polygon
     * by adding the polygon of the opening element
     * @param wallPolygon The polygon of the wall
     * @param polygonPoints The points of the opening element
     * @param wallVectors All the vectors of the wall
     * @param openingMesh The mesh of the opening element
     * @param axis The axis on which the given wall polygon is
     * @return the updated wall polygon
     */
    private int[] retrieveTheOpeningCoordinatesOnTheSameAxis(List<int[]> allWallPolygons, List<Vector4> allWallVectors, int[] wallPolygon, List<Vector4> polygonPoints, List<Vector4> wallVectors, Mesh openingMesh, Axis axis) {
        List<Integer> openingPointsOnAxis = getIndexesOfPolygons(axis, polygonPoints, openingMesh);
        if (openingPointsOnAxis.size() == 0) return wallPolygon;

        return updateWallPolygon(allWallPolygons, allWallVectors, wallPolygon, wallVectors, openingPointsOnAxis, openingMesh, axis);
    }

    /**
     * Updates the wall polygon by adding the polygons of the opening element on the same plane
     * @param wallPolygons The polygons of the wall
     * @param wallVectors The vectors of the wall
     * @param openingPolygonsOnAxis The polygons of the opening element which are on the same plane as the wall polygons
     * @param openingMesh The mesh of the opening element
     * @param axis The axis on which the wall polygon is
     * @return The updated wall polygon
     */
    private int[] updateWallPolygon(List<int[]> allOpeningElementPolygons, List<Vector4> allOpeningElementVectors, int[] wallPolygons, List<Vector4> wallVectors, List<Integer> openingPolygonsOnAxis, Mesh openingMesh, Axis axis) {
        List<Integer> newPolygon = new ArrayList<>();
        for (int integer = 0; integer < openingPolygonsOnAxis.size(); integer = integer + 2) {
            int[] openingPointsOnAxis = openingMesh.getPolygons().get(openingPolygonsOnAxis.get(integer));
            int startInOpening = -1;
            int startInWall = -1;
            // get all the polygons of the allWallVectors (should be all) of the given axis
            // for each polygon on the opening, check if there is a point from the wall that can be connected


            for (int i = 0; i < wallPolygons.length; i++) {
                startInWall = i;
              //   startInOpening = findEntryPoint(allWallPolygons, allWallVectors, openingMesh, openingPointsOnAxis, axis);
                startInOpening = lineIntersectsOpeningElement(wallVectors.get(wallPolygons[i]), openingMesh, openingPointsOnAxis, axis);
                if (startInOpening != -1) {
                    if(lineIsIntersectingOtherOpening(allOpeningElementPolygons, allOpeningElementVectors, wallVectors.get(wallPolygons[i]), openingMesh.getControlPoints().get(openingPointsOnAxis[startInOpening]), axis)) {
                        startInWall = -1;
                    } else {
                        break;
                    }
                }
            }
            if (startInWall != -1 && startInOpening != -1) {
                for (int index = 0; index < wallPolygons.length; index++) {

                    newPolygon.add(wallPolygons[index]);
                    if (index == startInWall) {
                        // add the opening stuff
                        Collection<Integer> toAdd = renewOpeningPolygon(openingPointsOnAxis, startInOpening, wallVectors.size(), wallVectors, openingMesh);
                        newPolygon.addAll(toAdd);
                        // add the control points
                        newPolygon.add(wallPolygons[index]);
                    }
                }
            }
            if (newPolygon.size() == 0) return wallPolygons;
        }
        for (int i = 0; i < openingPolygonsOnAxis.size(); i++) {
            openingMesh.getPolygons().remove(openingPolygonsOnAxis.get(i) - i);
        }
        return newPolygon.stream().mapToInt(i -> i).toArray();
    }

    private boolean lineIsIntersectingOtherOpening(List<int[]> allOpeningElementPolygons, List<Vector4> allOpeningElementVectors,
                                                   Vector4 a, Vector4 b, Axis axis) {

        for (int[] allOpeningElementPolygon : allOpeningElementPolygons) {
            for(int i = 0; i < allOpeningElementPolygon.length; i++){
                if(i == allOpeningElementPolygon.length-1){
                    if(lineIntersect(a, b, allOpeningElementVectors.get(i), allOpeningElementVectors.get(0), axis)){
                        if(pointsAreEqual(a, allOpeningElementVectors.get(i)) || pointsAreEqual(a, allOpeningElementVectors.get(0)) ||
                                pointsAreEqual(b, allOpeningElementVectors.get(i)) || pointsAreEqual(b, allOpeningElementVectors.get(0))){
                            continue;
                        }
                        return true;
                    }
                }
                else {
                    if(lineIntersect(a, b, allOpeningElementVectors.get(i), allOpeningElementVectors.get(i+1), axis)) {
                        if(pointsAreEqual(a, allOpeningElementVectors.get(i)) || pointsAreEqual(a, allOpeningElementVectors.get(i+1)) ||
                                pointsAreEqual(b, allOpeningElementVectors.get(i)) || pointsAreEqual(b, allOpeningElementVectors.get(i+1))){
                            continue;
                        }
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean pointsAreEqual(Vector4 a, Vector4 vector4) {
        return Math.abs(a.x - vector4.x) < Constants.COMPARISON_THRESHOLD &&
                Math.abs(a.y - vector4.y) < Constants.COMPARISON_THRESHOLD &&
                Math.abs(a.z - vector4.z) < Constants.COMPARISON_THRESHOLD;
    }


    /**
     * Checks if a given line intersects with an opening element
     * @param p1 A point in the wall polygon
     * @param openingMesh The mesh of the opening element
     * @param openingPointsOnAxis The points of the opening which are on the same axis as the wall polygon to
     *                            which p1 belongs to
     * @param axis The axis on which the wall polygon is to which p1 belongs to
     * @return The index of the point which does not intersect with the opening element, -1 if there exists no point
     * without intersection
     */
    private int lineIntersectsOpeningElement(Vector4 p1, Mesh openingMesh, int[] openingPointsOnAxis, Axis axis) {
        int inter = -1;
        for (int i = 0; i < openingPointsOnAxis.length; i++) {
            int intersections = 0;
            for (int j = 0; j < openingPointsOnAxis.length; j++) {
                inter = i;
                int pointB = (j == openingPointsOnAxis.length - 1) ? 0 : j + 1;
                Vector4 p2 = openingMesh.getControlPoints().get(openingPointsOnAxis[i]);
                Vector4 p3 = openingMesh.getControlPoints().get(openingPointsOnAxis[j]);
                Vector4 p4 = openingMesh.getControlPoints().get(openingPointsOnAxis[pointB]);
                if (lineIntersect(p1, p2, p3, p4, axis)) {
                    intersections++;
                }
            }
            if (intersections < 3) {
                return inter;
            }
        }
        return -1;
    }

    /**
     * Removes all polygons which are on the same axis. This is necessary as the opening of a window for example
     * has many layers. If we would not remove them, we would still see a sort of wall
     * @param openingMesh The mesh of the opening element of which we want to remove the polygons
     * @param axis The axis of which we want to remove the polygons
     */
    private void removePolygonsOnSameAxis(Mesh openingMesh, Axis axis) {
        List<int[]> polygons = openingMesh.getPolygons();
        for (int i = 0; i < polygons.size(); i++) {
            boolean toRemove = true;
            for (int j = 0; j < polygons.get(i).length - 1; j++) {
                if (axis == Axis.X) {
                    if (Math.abs(openingMesh.getControlPoints().get(polygons.get(i)[j]).x - openingMesh.getControlPoints().get(polygons.get(i)[j + 1]).x) >= Constants.COMPARISON_THRESHOLD) {
                        toRemove = false;
                        break;
                    }
                }
                if (axis == Axis.Y) {
                    if (Math.abs(openingMesh.getControlPoints().get(polygons.get(i)[j]).y - openingMesh.getControlPoints().get(polygons.get(i)[j + 1]).y) >= Constants.COMPARISON_THRESHOLD) {
                        toRemove = false;
                        break;
                    }
                }
                if (axis == Axis.Z) {
                    if (Math.abs(openingMesh.getControlPoints().get(polygons.get(i)[j]).z - openingMesh.getControlPoints().get(polygons.get(i)[j + 1]).z) >= Constants.COMPARISON_THRESHOLD) {
                        toRemove = false;
                        break;
                    }
                }
            }
            if (toRemove) {
                openingMesh.getPolygons().remove(polygons.get(i));
                i = i - 1;
            }

        }
    }

    /**
     * Updates the opening polygon by adding the opening mesh polygon to the wall polygon
     * @param openingPointsOnAxis the vectors of the opening mesh which are on the same axis
     * @param startInOpening  The vector of the opening element which does not intersect with any other line when
     *                        connecting it to the wall polygon via the entryInWall
     * @param entryInWall The index of the point which connects the wall to the opening element
     * @param wallVectors The vectors of the wall
     * @param openingMesh The mesh of the opening element
     * @return The new polygon of the opening mesh which will be added to the wall polygon
     */
    private Collection<Integer> renewOpeningPolygon(int[] openingPointsOnAxis, int startInOpening, int entryInWall, List<Vector4> wallVectors, Mesh openingMesh) {
        Collection<Integer> openingPolygon = new ArrayList<>();
        int len = openingPointsOnAxis.length;
        for (int i = startInOpening; i < len; i++) {
            wallVectors.add(openingMesh.getControlPoints().get(openingPointsOnAxis[i]));
            openingPolygon.add(entryInWall + i);
        }
        for (int i = 0; i < startInOpening; i++) {
            wallVectors.add(openingMesh.getControlPoints().get(openingPointsOnAxis[i]));
            openingPolygon.add(entryInWall + i);
        }
        openingPolygon.add(startInOpening + entryInWall);
        return openingPolygon;
    }

    /**
     * Checks if two lines, given by two points each, are intersecting
     * @param p1 The start point of the first line
     * @param p2 The end point of the first line
     * @param t1 The start point fo the second line
     * @param t2 The end point of the second line
     * @param axis The axis on which the lines are
     * @return True, if lines are intersecting
     */
    private boolean lineIntersect(Vector4 p1, Vector4 p2, Vector4 t1, Vector4 t2, Axis axis) {
        Line2D line1, line2;
        if (axis == Axis.X) {
            if(Math.abs(p1.x - t1.x) > Constants.COMPARISON_THRESHOLD) return false;
            line1 = new Line2D.Double(p1.y, p1.z, p2.y, p2.z);
            line2 = new Line2D.Double(t1.y, t1.z, t2.y, t2.z);
        } else if (axis == Axis.Y) {
            if(Math.abs(p1.y - t1.y) > Constants.COMPARISON_THRESHOLD) return false;
            line1 = new Line2D.Double(p1.x, p1.z, p2.x, p2.z);
            line2 = new Line2D.Double(t1.x, t1.z, t2.x, t2.z);
        } else {
            if(Math.abs(p1.z - t1.z) > Constants.COMPARISON_THRESHOLD) return false;
            line1 = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
            line2 = new Line2D.Double(t1.x, t1.y, t2.x, t2.y);
        }
        return line2.intersectsLine(line1);
    }

    enum Axis {
        X, Y, Z
    }

    /**
     * Check on what axis the points are
     * @param points A list of points in a 3D space
     * @return The axis on which the points are
     */
    private static Axis getAxis(List<Vector4> points) {
        boolean sameX = true, sameY = true;

        // check if all points have the same x, y, or z coordinate
        for (Vector4 point : points) {
            if (Math.abs(point.x - points.get(0).x) >= Constants.COMPARISON_THRESHOLD) {
                sameX = false;
            }
            if (Math.abs(point.y - points.get(0).y) >= Constants.COMPARISON_THRESHOLD) {
                sameY = false;
            }
        }
        if (sameX) return Axis.X;
        if (sameY) return Axis.Y;
        return Axis.Z;
    }
}
