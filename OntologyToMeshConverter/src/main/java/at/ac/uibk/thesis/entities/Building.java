package at.ac.uibk.thesis.entities;

import com.aspose.threed.PhongMaterial;
import com.aspose.threed.Scene;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class Building {

    private IfcOWLDataset owlDataset;
    private String fileName;
    private String generatedBuildingName;
    private List<Item> items;
    private List<LightSource> lightSources;
    private List<Item> movableObjects;
    private Scene scene;

    private Map<String, PhongMaterial> materialsContainedInBuilding;

    public Building(String name) {
        this.generatedBuildingName = name;
        this.materialsContainedInBuilding = new HashMap<>();
        this.scene = new Scene();
        this.items = new ArrayList<>();
    }

}
