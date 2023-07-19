package at.ac.uibk.thesis.services;

import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.BuildingDTO;
import at.ac.uibk.thesis.entities.FileDTO;
import at.ac.uibk.thesis.services.PluginUtils.FileService;
import com.aspose.threed.FileFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
@Scope("application")
public class ConverterService {

    @Autowired
    ProductService productService;

    @Autowired
    BuildingService buildingService;

    /**
     * Converts the uploaded IFC to a specified format
     * @param fileDTO The content of the IFC file
     * @param format The format to which the IFC file should be converted to, e.g FBX, SLT, OBJ
     * @param unitScaleFactor The scale factor (in unreal engine one unit is one cm)
     * @param saveAsFile Save as file if true
     * @return The generated file or an error message
     * @throws IOException Thrown if the saving of a file fails
     */
    public ResponseEntity<Object> uploadAndConvertIFC(FileDTO fileDTO, FileFormat format, String suffix, int unitScaleFactor, boolean saveAsFile, boolean triangualte) throws IOException {
        Building building = buildingService.setUpBuilding(fileDTO);
        if (building == null) {
            return new ResponseEntity<>("Die IFC Datei konnte nicht konvertiert werden!", HttpStatus.BAD_REQUEST);
        }
        long start = System.nanoTime();

        Map<String, Object> lights = productService.convertBuildingToFBX(building, format, unitScaleFactor, triangualte);
        if (lights != null) {
            long elapsedTime = System.nanoTime() - start;
            System.out.println("Execution time for converting owl to fbx: " + TimeUnit.MILLISECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS) + "ms");
            if (saveAsFile) {
                building.getScene().save(fileDTO.getFileName() + "." + suffix, format);
                return new ResponseEntity<>("Die Datei wurde gespeichert", HttpStatus.OK);
            } else {
                return new ResponseEntity<>(
                        new BuildingDTO(
                                FileService.getFileContentAsString(building.getGeneratedBuildingName() + "." + suffix),
                                lights)
                        , HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Es ist ein Fehler aufgetreten", HttpStatus.BAD_REQUEST);
    }
}