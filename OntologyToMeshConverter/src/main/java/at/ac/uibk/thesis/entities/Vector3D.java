package at.ac.uibk.thesis.entities;

import com.aspose.threed.Vector4;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Vector3D {

    private Vector4 position;

    private Vector4 normal;

    public Vector3D(Vector4 position) {
        this.position = position;
        this.normal = new Vector4(0, 0, 0);
    }
}
