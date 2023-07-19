package at.ac.uibk.thesis.services.Movement;


import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.services.Dataset.IFCItems.VectorService;
import com.aspose.threed.Vector4;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

@RestController
@Scope("application")
public class RotationService {

    @Autowired
    private VectorService vectorService;

    /**
     * Calculates the angle between two given vectors and returns the radian value
     * @param v the first vector
     * @param u the second vector
     * @return the angle between the two given vectors
     */
    public Double getAngleBetweenTwoVectors(Vector4 v, Vector4 u) {
        boolean invert = v.x < 0 | v.y < 0 | v.z < 0;
        double top = v.x * u.x + v.y * u.y + v.z * u.z;
        double bottom = Math.sqrt(v.x * v.x + v.y * v.y + v.z * v.z) * Math.sqrt(u.x * u.x + u.y * u.y + u.z * u.z);
        double cos = top / bottom;

        double theta = Math.acos(cos);
        if (invert) {
            theta = Math.PI * 2 - theta;
        }
        return theta;
    }

    /**
     * Adds the rotation to an already existing rotation matrix
     * @param matrix The rotation matrix to which the new rotations should be added
     * @param dir The rotation around the X axis
     * @param refDir The rotation around the Z axis
     */
    public void addRotation(Matrix4d matrix, Vector4 dir, Vector4 refDir) {
        if (vectorService.isUnequal(new Vector4(0, 0, 1), dir)) {
            matrix.rotX(getAngleBetweenTwoVectors(dir, new Vector4(0, 0, 1)));
        }
        if (vectorService.isUnequal(new Vector4(1, 0, 0), refDir)) {
            Matrix4d secondRotation = new Matrix4d();
            secondRotation.setIdentity();
            secondRotation.rotZ(getAngleBetweenTwoVectors(refDir, new Vector4(1, 0, 0)));
            matrix.mul(secondRotation);

        }
    }

    public Vector4 CreateAndRotateVector4(double x, double y, double z, ItemGeometry itemGeometry) {
        Vector3d v = new Vector3d(x, y, z);
        itemGeometry.rotationMatrix.transform(v);
        return new Vector4(v.x, v.y, v.z);
    }

    public Vector4 CreateAndRotateVector4(Vector4 vector, ItemGeometry itemGeometry) {
        return CreateAndRotateVector4(vector.x, vector.y, vector.z, itemGeometry);
    }

    /**
     * Transforms a given coordinate according to the transformation stored in the item geometry
     * @param itemGeometry The item geometry containing the rotation matrix
     * @param coordinate The coordinate we want to transform
     * @return The transformed coordinate
     */
    public Vector4 rotateAndMove(ItemGeometry itemGeometry, Vector4 coordinate) {
        Point3d point = new Point3d(coordinate.x, coordinate.y, coordinate.z);
        itemGeometry.getRotationMatrix().transform(point);
        coordinate = new Vector4(point.x, point.y, point.z);
        coordinate = vectorService.add(itemGeometry.getTranslation(), coordinate);
        return coordinate;
    }

    /**
     * Adds the rotation to an already existing rotation matrix
     * @param direction The rotation in regard to the X axis
     * @param refDirection The rotation in regard the Z axis
     * @return The matrix which contains the rotation around the X and Z axis
     */
    public Matrix4d setUpRotationMatrix(Vector4 direction, Vector4 refDirection) {
        Matrix4d translationMatrix = new Matrix4d();
        translationMatrix.setIdentity();
        addRotation(translationMatrix, direction, refDirection);
        return translationMatrix;
    }
}