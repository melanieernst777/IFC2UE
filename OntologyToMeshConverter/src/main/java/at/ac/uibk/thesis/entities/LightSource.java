package at.ac.uibk.thesis.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LightSource {

    private String lightObject;

    private String lightSwitch;

    private double voltage;
}
