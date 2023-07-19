package at.ac.uibk.thesis.services.Movement;

import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import com.aspose.threed.Vector3;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.vecmath.Matrix4d;
import javax.vecmath.Point3d;
import java.util.List;

@Component
@Scope("application")
public class GeometryService {

    /**
     * Calculates the rotation matrix by multiplying it with the parent item.
     * The rotation will also be calculated by retrieving the translation of the parent item
     * @param itemGeometry the item geometry containing the information of the rotation and translation of itself
     */
    public void setUpTranslation(ItemGeometry itemGeometry) {
        itemGeometry.getRotationMatrix().mul(itemGeometry.getItem().getRotationMatrix());
        itemGeometry.setTranslation(getTranslation(itemGeometry.getItem()));
    }

    /**
     * Calculates the absolute translation for a item by calculating the sum of the translations of all parent items
     * @param item The item of which we want to get the absolute translation
     * @return the absolute translation of the item
     */
    public Vector3 getTranslation(Item item) {
        return getTranslation(item.getTranslations());
    }

    /**
     * Calculates the absolute translation for a item by calculating the sum of the translations of all parent items
     * @param translations A list of translations
     * @return a Vector3 which is the sum of all the Vector3 items in the translation list
     */
    public Vector3 getTranslation(List<Vector3> translations) {
        Vector3 vector3 = new Vector3(0, 0, 0);
        translations.forEach(v -> vector3.set(vector3.x + v.x, vector3.y + v.y, vector3.z + v.z));
        return vector3;
    }

    /**
     * Calculates the translation by applying the rotation of the parent item as the translation depends on the
     * parents' translation
     * @param rotationMatrix The rotation matrix of the item
     * @param translations The translations of the item
     */
    public void getRotatedTranslation(Matrix4d rotationMatrix, List<Vector3> translations) {
        Matrix4d tmp = new Matrix4d();
        tmp.set(rotationMatrix);
        Matrix4d t = new Matrix4d();
        t.setIdentity();
        if (tmp.equals(t)) return;
        Point3d p = new Point3d(translations.get(0).x, translations.get(0).y, translations.get(0).z);
        tmp.transform(p);
        translations.set(0, new Vector3(p.x, p.y, p.z));
    }
}
