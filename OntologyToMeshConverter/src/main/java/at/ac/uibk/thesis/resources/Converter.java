package at.ac.uibk.thesis.resources;

import at.ac.uibk.thesis.entities.FileDTO;
import at.ac.uibk.thesis.services.ConverterService;
import com.aspose.threed.FileFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/convert")
public class Converter {

    @Autowired
    ConverterService converterService;

    @PostMapping("/uploadIFC")
    public ResponseEntity<Object> uploadAndConvertIFC(@RequestBody FileDTO fileDTO) throws IOException {
        return converterService.uploadAndConvertIFC(fileDTO, FileFormat.FBX7700ASCII, "fbx", 100, false, true);
    }

    @PostMapping("/convertIFCToOBJ")
    public ResponseEntity<Object> convertIFC(@RequestParam("file") MultipartFile uploadedFile) throws IOException {
        FileDTO fileDTO = new FileDTO();
        if (uploadedFile.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload an IFC file.");
        }
        fileDTO.setFileContent(new String(uploadedFile.getBytes()));
        fileDTO.setFileName(uploadedFile.getOriginalFilename());
        return converterService.uploadAndConvertIFC(fileDTO, FileFormat.WAVEFRONTOBJ, "obj", 1, true, false);
    }
}