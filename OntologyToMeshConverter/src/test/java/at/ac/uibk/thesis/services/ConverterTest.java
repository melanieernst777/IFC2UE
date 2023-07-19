package at.ac.uibk.thesis.services;


import at.ac.uibk.thesis.TestConstants;
import at.ac.uibk.thesis.entities.FileDTO;
import com.aspose.threed.FileFormat;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@SpringBootTest
public class ConverterTest {

    @Autowired
    ConverterService converterService;

    @Test
    void testConvertIFCtoFBX() throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(Files.readString(Path.of(TestConstants.IFCFILELOCATION)));
        fileDTO.setFileName("SensorBIM");
        ResponseEntity<Object> result = converterService.uploadAndConvertIFC(fileDTO, FileFormat.FBX7700ASCII, "fbx", 1, true, false);
        Assertions.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).toString(), "Die Datei wurde gespeichert");
    }

    @Test
    void testConvertIFCtoFBXWindow() throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(Files.readString(Path.of(TestConstants.IFCFILELOCATION_WINDOWS)));
        fileDTO.setFileName("Windows");
        ResponseEntity<Object> result = converterService.uploadAndConvertIFC(fileDTO, FileFormat.FBX7700ASCII, "fbx", 1, true, false);
        Assertions.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).toString(), "Die Datei wurde gespeichert");
    }

    @Test
    void testConvertIFCtoOBJ() throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(Files.readString(Path.of(TestConstants.IFCFILELOCATION_WINDOWS)));
        fileDTO.setFileName("Windows");
        ResponseEntity<Object> result = converterService.uploadAndConvertIFC(fileDTO, FileFormat.WAVEFRONTOBJ, "obj", 1, true, false);
        Assertions.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).toString(), "Die Datei wurde gespeichert");
    }

    @Test
    void testConvertIFCtoSTL() throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(Files.readString(Path.of(TestConstants.IFCFILELOCATION_WINDOWS)));
        fileDTO.setFileName("Windows");
        ResponseEntity<Object> result = converterService.uploadAndConvertIFC(fileDTO, FileFormat.STLASCII, "stl", 1, true, false);
        Assertions.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).toString(), "Die Datei wurde gespeichert");
    }

    @Test
    void testConvertIFCtoFBXAscii() throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(Files.readString(Path.of(TestConstants.IFCFILELOCATION_WINDOWS)));
        fileDTO.setFileName("Windows");
        ResponseEntity<Object> result = converterService.uploadAndConvertIFC(fileDTO, FileFormat.FBX7400ASCII, "fbx", 1, true, false);
        Assertions.assertEquals(result.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).toString(), "Die Datei wurde gespeichert");
    }


    @Test
    void testConvertInvalidTTLtoFBX() throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(Files.readString(Path.of(TestConstants.INVALID_IFC)));
        fileDTO.setFileName("Invalid IFC");
        ResponseEntity<Object> result = converterService.uploadAndConvertIFC(fileDTO, FileFormat.FBX7700ASCII, "fbx", 1, true, false);
        Assertions.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).toString(), "Die IFC Datei konnte nicht konvertiert werden!");
    }

    @Test
    void testConvertInvalidIFCSensorBIMtoFBX() throws IOException {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileContent(Files.readString(Path.of(TestConstants.INVALID_SENSORBIM_IFC)));
        fileDTO.setFileName("Invalid SensorBIM IFC");
        ResponseEntity<Object> result = converterService.uploadAndConvertIFC(fileDTO, FileFormat.FBX7700ASCII, "fbx", 1, true, false);
        Assertions.assertEquals(result.getStatusCode(), HttpStatus.BAD_REQUEST);
        Assertions.assertEquals(Objects.requireNonNull(result.getBody()).toString(), "Die IFC Datei konnte nicht konvertiert werden!");
    }


}
