package at.ac.uibk.thesis.services.RepresentationUtils;

import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import com.aspose.threed.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.vecmath.Vector2f;
import javax.vecmath.Vector3f;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


@Component
@Scope("application")
public class UVService {

    /**
     * Adds the UV coordinates to a mesh. The UV coordinates are needed for displaying the material properly
     * @param mesh The mesh to which we want to add the UV coordinates to
     * @return The mesh with the UV coordinates
     */
    public Mesh addUVVector4sToMesh(Mesh mesh) {
        mesh = PolygonModifier.triangulate(mesh);

        List<int[]> polygons = mesh.getPolygons();
        for (int[] triangle : polygons) {
             mesh = addUVCoordinatesToMesh(Arrays.asList(
                    mesh.getControlPoints().get(triangle[0]),
                    mesh.getControlPoints().get(triangle[1]),
                    mesh.getControlPoints().get(triangle[2])
            ), mesh);
        }
        return mesh;
    }


    /**
     * Adds the UV coordinate to all the elements from the scene. This is done at the end
     * as we first need to triangulate the item (triangulating the mesh makes the opening element stuff
     * way more complicated)
     * @param items The items belonging to the scene
     */
    public void addUVCoordinatesToScene(Collection<Item> items) {
        for (Item item : items) {
            for (ItemGeometry itemGeometry : item.getItemGeometries()) {
                Mesh mesh = (Mesh) itemGeometry.getNode().getEntity();
                Mesh mesh2 = addUVVector4sToMesh(mesh);
                itemGeometry.getNode().setEntity(mesh2);
            }
        }
    }

    public static Vector2f[] calculateUVCoordinates(Vector3f pointA, Vector3f pointB, Vector3f pointC) {
        // Calculate the triangle's normal vector
        Vector3f abVector = new Vector3f(pointB.x, pointB.y, pointB.z);
        abVector.sub(pointA);
        Vector3f acVector = new Vector3f(pointC.x, pointC.y, pointC.z);
        acVector.sub(pointA);

        Vector3f normalVector = new Vector3f();
        normalVector.cross(abVector, acVector);
        normalVector.normalize();
        Vector3f uVector = new Vector3f(abVector.x, abVector.y, acVector.z);
        uVector.normalize();
        // Determine the coordinate system for the triangle
        Vector3f vVector = new Vector3f();
        vVector.cross(normalVector, uVector);
        vVector.normalize();

        // Calculate UV coordinates for each vertex
        Vector3f origin = new Vector3f(pointA);
        Vector2f uvA = calculateUV(pointA, origin, uVector, vVector);
        Vector2f uvB = calculateUV(pointB, origin, uVector, vVector);
        Vector2f uvC = calculateUV(pointC, origin, uVector, vVector);

        // Create and return the UV coordinate array
        return new Vector2f[]{uvA, uvB, uvC};
    }

    private static Vector2f calculateUV(Vector3f vertex, Vector3f origin, Vector3f uVector, Vector3f vVector) {
        Vector3f vertexVector = new Vector3f(vertex.x, vertex.y, vertex.z);
        vertexVector.sub(origin);
        float uCoordinate = vertexVector.dot(uVector);
        float vCoordinate = vertexVector.dot(vVector);
        return new Vector2f(uCoordinate, vCoordinate);
    }

    /**
     * Calculates the UV vector for a triangle by checking on which axis the triangle is
     * @param triangle The triangle of which we want to receive the UV coordinate
     * @param mesh The mesh to which the triangle belongs to
     * @return The mesh with the added UV data of the triangle
     */
    private Mesh addUVCoordinatesToMesh(List<Vector4> triangle, Mesh mesh){

        VertexElementUV elementUV = (VertexElementUV) mesh.getElement(VertexElementType.UV);
        if (elementUV == null) {
            elementUV = mesh.createElementUV(TextureMapping.DIFFUSE, MappingMode.POLYGON_VERTEX, ReferenceMode.INDEX_TO_DIRECT);
        }

        Vector2f[] uvs = calculateUVCoordinates(new Vector3f((float) triangle.get(0).x, (float) triangle.get(0).y, (float) triangle.get(0).z),
                new Vector3f((float) triangle.get(1).x, (float) triangle.get(1).y, (float) triangle.get(1).z),
                new Vector3f((float) triangle.get(2).x, (float) triangle.get(2).y, (float) triangle.get(2).z));

        elementUV.getData().add(new Vector4(uvs[0].x, uvs[0].y, 0, 0));
        elementUV.getData().add(new Vector4(uvs[1].x, uvs[1].y, 0, 0));
        elementUV.getData().add(new Vector4(uvs[2].x, uvs[2].y, 0, 0));

        elementUV.getIndices().add(elementUV.getData().size() - 3);
        elementUV.getIndices().add(elementUV.getData().size() - 2);
        elementUV.getIndices().add(elementUV.getData().size() - 1);

        return mesh;
    }
}
