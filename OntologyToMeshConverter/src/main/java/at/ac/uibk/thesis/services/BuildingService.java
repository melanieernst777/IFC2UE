package at.ac.uibk.thesis.services;

import at.ac.uibk.thesis.entities.Building;
import at.ac.uibk.thesis.entities.FileDTO;
import at.ac.uibk.thesis.entities.IfcOWLDataset;
import at.ac.uibk.thesis.services.Dataset.DatasetService;
import at.ac.uibk.thesis.services.PluginUtils.FileService;
import at.ac.uibk.thesis.services.PluginUtils.IFC2OWLConverterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
@Scope("application")
public class BuildingService {

    @Autowired
    IFC2OWLConverterService ifc2OWLConverterService;

    @Autowired
    DatasetService datasetService;

    public Building setUpBuilding(FileDTO fileDTO) {
        long start = System.nanoTime();
        Building building = new Building(FileService.creatTmpFile(fileDTO, UUID.randomUUID().toString()));
        building.setFileName(fileDTO.getFileName());
        if (ifc2OWLConverterService.convertIFCtoIFCOWL(building.getGeneratedBuildingName(), building.getGeneratedBuildingName().replace("ifc", "ttl"))) {
            long elapsedTimeIFC2IfcOwl = System.nanoTime() - start;
            System.out.println("Execution time for converting ifc to owl: " + TimeUnit.MILLISECONDS.convert(elapsedTimeIFC2IfcOwl, TimeUnit.NANOSECONDS) + "ms");
            building.setOwlDataset(new IfcOWLDataset(datasetService.getDataset(building.getGeneratedBuildingName())));
            if(!Objects.equals(building.getOwlDataset().getPrefixes(), "")){
                return building;
            }
        }
        return null;
    }
}
