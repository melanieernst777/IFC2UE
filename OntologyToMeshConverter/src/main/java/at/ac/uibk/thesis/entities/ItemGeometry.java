package at.ac.uibk.thesis.entities;

import com.aspose.threed.Mesh;
import com.aspose.threed.Node;
import com.aspose.threed.Vector3;
import com.aspose.threed.Vector4;
import lombok.Getter;
import lombok.Setter;

import javax.vecmath.Matrix4d;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ItemGeometry {

    public Matrix4d rotationMatrix;
    public Item item;
    private String itemRepresentation;
    private Vector4 direction;
    private Vector4 position;
    private Vector4 refDirection;
    private Vector4 color;
    private List<Vector3D> vertices;
    private List<List<Integer>> polygon;
    private double transparency = 0.0;
    private double reflectanceFactor;
    private double specularValue;
    private Node node;
    private Vector3 translation;

    public ItemGeometry(String itemRepresentation, Vector4 position, Vector4 dir, Vector4 ref, Item item) {
        this.position = position;
        this.itemRepresentation = itemRepresentation;
        this.direction = dir;
        this.refDirection = ref;
        setColor(new Vector4(0.84, 0.65, 0.39));
        setUp(item);
    }

    // deep copy of item geometry
    public ItemGeometry(ItemGeometry itemGeometry, String itemRepresentation, Item item) {
        this.position = new Vector4(itemGeometry.getPosition().x, itemGeometry.getPosition().y, itemGeometry.getPosition().z);
        this.direction = new Vector4(itemGeometry.getDirection().x, itemGeometry.getDirection().y, itemGeometry.getDirection().z);
        this.refDirection = new Vector4(itemGeometry.getRefDirection().x, itemGeometry.getRefDirection().y, itemGeometry.getRefDirection().z);
        setColor(new Vector4(itemGeometry.getColor().x, itemGeometry.getColor().y, itemGeometry.getColor().z));
        this.itemRepresentation = itemRepresentation;
        setUp(item);
    }

    // deep copy of item geometry
    public ItemGeometry(ItemGeometry itemGeometry) {
        this.position = new Vector4(itemGeometry.getPosition().x, itemGeometry.getPosition().y, itemGeometry.getPosition().z);
        this.direction = new Vector4(itemGeometry.getDirection().x, itemGeometry.getDirection().y, itemGeometry.getDirection().z);
        this.refDirection = new Vector4(itemGeometry.getRefDirection().x, itemGeometry.getRefDirection().y, itemGeometry.getRefDirection().z);
        setColor(new Vector4(itemGeometry.getColor().x, itemGeometry.getColor().y, itemGeometry.getColor().z));
        this.itemRepresentation = itemGeometry.getItemRepresentation();
        setUp(itemGeometry.item);
    }

    private void setUp(Item item) {
        this.item = item;
        this.vertices = new ArrayList<>();
        this.polygon = new ArrayList<>();
        this.node = new Node();
        this.node.setName(item.getTrimmedProductName() + this.getTrimmedItemRepresentationName());
        this.node.setEntity(new Mesh());
        this.rotationMatrix = new Matrix4d();
        this.rotationMatrix.setIdentity();
    }

    public void setColor(Vector4 color) {
        // round values, because many are almost the same
        this.color = new Vector4(
                Math.round(color.x * 100.0) / 100.0,
                Math.round(color.y * 100.0) / 100.0,
                Math.round(color.z * 100.0) / 100.0
        );
    }

    public String getColorOfItemGeometry() {
        String s = color.x + "_" + color.y + "_" + color.z;
        return s.replace(".", "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemGeometry that = (ItemGeometry) o;
        return itemRepresentation.equals(that.itemRepresentation);
    }


    public void setSpecularValue(double specularValue) {
        this.specularValue = specularValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemRepresentation);
    }

    public void setTransparency(double transparency) {
        this.transparency = transparency;
    }

    public String getTrimmedItemRepresentationName() {
        return itemRepresentation.substring(itemRepresentation.lastIndexOf("/") + 1).trim();
    }
}
