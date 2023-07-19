package at.ac.uibk.thesis.entities;

import at.ac.uibk.thesis.enums.ObjectType;
import at.ac.uibk.thesis.enums.SurfaceMaterial;
import com.aspose.threed.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.vecmath.Matrix4d;
import java.util.*;

@Getter
@Setter
public class Item {

    private final ObjectType objectType;
    public Matrix4d rotationMatrix;
    private String productName;
    private LinkedHashSet<ItemGeometry> itemGeometries;
    private Map<String, LinkedHashSet<ItemGeometry>> materialsAndTheirItemGeometries;
    private Vector4 placement;
    private Vector4 direction;
    private Vector4 refDirection;
    @JsonIgnore
    private Item parentItem;
    @JsonIgnore
    private Set<Item> childItems;
    private Node node;
    private SurfaceMaterial material;
    private List<Vector3> translations;
    private Building building;

    public Item(Building building, String productName) {
        this.productName = productName;
        itemGeometries = new LinkedHashSet<>();
        materialsAndTheirItemGeometries = new HashMap<>();
        this.direction = new Vector4(0, 0, 1);
        this.refDirection = new Vector4(1, 0, 0);
        this.childItems = new HashSet<>();
        this.objectType = ObjectType.getObjectType(productName);
        this.building = building;
        this.rotationMatrix = new Matrix4d();
        this.rotationMatrix.setIdentity();
        this.translations = new ArrayList<>();
    }

    public String getTrimmedProductName() {
        return productName.substring(productName.lastIndexOf("/") + 1).trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return productName.equals(item.productName);
    }

    public void setParentItem(Item parentItem) {
        this.parentItem = parentItem;
        this.getNode().setParentNode(parentItem.node);
    }

    public Node getNode() {
        if (node == null) {
            this.node = new Node();
            this.node.setName(this.getTrimmedProductName());
            this.node.setAssetInfo(new AssetInfo());
            this.node.getAssetInfo().setUpVector(Axis.Y_AXIS);
        }
        return node;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName);
    }

    public void addChildItem(Item item) {
        this.childItems.add(item);
        this.getNode().getChildNodes().add(item.getNode());
    }
}
