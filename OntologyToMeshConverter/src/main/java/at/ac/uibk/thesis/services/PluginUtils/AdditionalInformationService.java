package at.ac.uibk.thesis.services.PluginUtils;

import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.Item;
import at.ac.uibk.thesis.entities.ItemGeometry;
import at.ac.uibk.thesis.entities.LightSource;
import at.ac.uibk.thesis.services.LightService;
import com.aspose.threed.Node;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@Scope("application")
public class AdditionalInformationService {

    @Autowired
    private LightService lightService;

    /***
     * Retrieve relevant information of the building, e.g. light objects and the building name.
     * The data will be sent with the fbx file. It allows to add different functionalities in unreal engine,
     * like switching on and off light objects
     * @param building The building containing the building items
     * @return A map with the information of the building name and light objects
     */
    private Map<String, Object> getDataAsJSONObject(Building building, Map<String, String> materials) {
        List<LightSource> lightObjects = lightService.getAllLightSourcesFromScene(building.getOwlDataset(), building.getItems());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lightObjects", lightObjects);
        String fileName = Paths.get(building.getFileName()).getFileName().toString();
        jsonObject.put("buildingName", fileName.replace(".ifc", ""));
        jsonObject.put("materials", materials);
        return jsonObject.toMap();
    }

    /***
     * Creates a map with the information of light objects and their switches and the building name.
     * As we cannot read the tags of the FBX file in unreal engine (as of 2023), we need to add
     * the material name to the name of the item (this is faster than adding it to the json object)
     * @param building The building containing the building items
     * @return A map with the information of the building name and light objects
     */
    public Map<String, Object> addAdditionalInformationToName(Building building) {
        Map<String, String> materials = new HashMap<>();
        for (Item item : building.getItems()) {
            for (ItemGeometry itemGeometry : item.getItemGeometries()) {
                Node node = itemGeometry.getNode();

                if (!Objects.equals(itemGeometry.getNode().getMaterial(), null)) {
                    if (itemGeometry.getNode().getMaterial().getName() != null) {
                        String name = itemGeometry.getNode().getMaterial().getName();
                        materials.put(itemGeometry.getNode().getName(), name);
                        if(!name.contains("Color")) itemGeometry.getNode().setMaterial(null);
                        continue;
                    }
                }
                if (item.getMaterial() != null) {
                    if (item.getMaterial().getMaterialName() != null)
                        materials.put(itemGeometry.getNode().getName(), item.getMaterial().getMaterialName());
                }
            }
        }
        return getDataAsJSONObject(building, materials);
    }

}
