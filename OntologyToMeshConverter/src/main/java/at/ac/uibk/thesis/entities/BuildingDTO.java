package at.ac.uibk.thesis.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class BuildingDTO {

    private String fileContent;
    private Map<String, Object> information;

    public BuildingDTO(String fileContent, Map<String, Object> information) {
        this.fileContent = fileContent;
        this.information = information;
    }

}
