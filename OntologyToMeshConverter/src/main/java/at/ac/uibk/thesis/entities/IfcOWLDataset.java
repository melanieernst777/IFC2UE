package at.ac.uibk.thesis.entities;

import lombok.Getter;
import org.apache.jena.query.Dataset;

import java.util.Map;

@Getter
public class IfcOWLDataset {

    private final Dataset dataset;

    private String prefixes;

    public IfcOWLDataset(Dataset dataset){
        this.dataset = dataset;
        this.prefixes = getPrefixes();
    }

    public String getPrefixes() {
        if(prefixes == null){
            StringBuilder prefixList = new StringBuilder();
            for (Map.Entry<String, String> entry : dataset.getPrefixMapping().getNsPrefixMap().entrySet()) {
                prefixList.append("PREFIX ").append(entry.getKey()).append(": <").append(entry.getValue()).append("> \n");
            }
            prefixes = prefixList.toString();
        }
        return prefixes;
    }
}
