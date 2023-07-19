package at.ac.uibk.thesis.services.Dataset.IFCItems;

import at.ac.uibk.thesis.Constants;
import com.aspose.threed.Vector3;
import com.aspose.threed.Vector4;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("application")
public class VectorService {

    public Vector4 add(Vector4 v1, Vector4 v2) {
        return new Vector4(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public Vector4 add(Vector3 v1, Vector4 v2) {
        return new Vector4(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public Vector4 invert(Vector4 v) {
        return new Vector4(v.x * -1, v.y * -1, v.z * -1);
    }

    public Double getSum(Vector4 v) {
        return v.x + v.y + v.z;
    }

    public Vector4 cross(Vector4 v1, Vector4 v2) {
        return new Vector4(
                (v1.y * v2.z) - (v1.z * v2.y),
                (v1.z * v2.x) - (v1.x * v2.z),
                (v1.x * v2.y) - (v1.y * v2.x));
    }

    public boolean isUnequal(Vector4 v1, Vector4 v2) {
        return (!(Math.abs(v1.x - v2.x) < Constants.COMPARISON_THRESHOLD)) ||
                (!(Math.abs(v1.y - v2.y) < Constants.COMPARISON_THRESHOLD)) ||
                (!(Math.abs(v1.z - v2.z) < Constants.COMPARISON_THRESHOLD));
    }

    public Vector4 scale(Vector4 d, double t) {
        return new Vector4(d.x * t, d.y * t, d.z * t);
    }

    public double distance(Vector4 v1, Vector4 v2) {
        double dx = v1.x - v2.x;
        double dy = v1.y - v2.y;
        double dz = v1.z - v2.z;

        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
