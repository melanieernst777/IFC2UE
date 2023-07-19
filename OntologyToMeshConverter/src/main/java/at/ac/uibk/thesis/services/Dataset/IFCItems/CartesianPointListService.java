package at.ac.uibk.thesis.services.Dataset.IFCItems;


import at.ac.uibk.thesis.entities.IfcOWLDataset;
import com.aspose.threed.Vector4;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@Scope("application")
public class CartesianPointListService {

    /**
     * Returns a list of coordinates from a given cartesian point list
     * @param dataset The dataset containing the information of the ontology
     * @param cartesianPointList The key to the cartesian point list
     * @return A list of all the coordinates contained in the cartesian point list
     */
    public List<Vector4> getAllVectorOfCartesianPointList(IfcOWLDataset dataset, String cartesianPointList) {
        List<Vector4> Vector4List = new ArrayList<>();
        getAllVector4sRecursively(dataset, cartesianPointList, Vector4List);
        Collections.reverse(Vector4List);
        return Vector4List;
    }

    /**
     * Get the next vector of the cartesian point list. This needs to be done recursively as the list is
     * like a linked list
     * @param dataset The dataset containing the information of the ontology
     * @param cartesianPointList The key of the cartesian point
     * @param Vector4s A list of vectors to which we want to add the extracted cartesian poing
     */
    private void getAllVector4sRecursively(IfcOWLDataset dataset, String cartesianPointList, List<Vector4> Vector4s) {
        ResultSet result = getVector4sOfCartesianPointList(dataset, cartesianPointList);
        result.forEachRemaining(sol -> saveVector4s(dataset, sol, Vector4s));
    }

    private void saveVector4s(IfcOWLDataset dataset, QuerySolution r, List<Vector4> Vector4s) {
        for (int i = 1; i <= 3; i++) {
            double x;
            double y = -1;
            double z = -1;
            if (r.getLiteral("x_" + i) != null) {
                x = r.getLiteral("x_" + i).getDouble();
            } else break;
            if (r.getLiteral("y_" + i) != null) {
                y = r.getLiteral("y_" + i).getDouble();
            }
            if (r.getLiteral("z_" + i) != null) {
                z = r.getLiteral("z_" + i).getDouble();
            }

            Vector4 Vector4 = new Vector4(x, y, z);
            Vector4s.add(Vector4);
        }

        if (r.get("next_3") != null) {
            getAllVector4sRecursively(dataset, r.get("next_3").toString(), Vector4s);
        }
    }

    /**
     * @param cartesianPointList the list with the Vector
     * @return the result set containing the query solution
     */
    private ResultSet getVector4sOfCartesianPointList(IfcOWLDataset dataset, String cartesianPointList) {
        String query = dataset.getPrefixes() +
                "SELECT ?next_3 ?x_1 ?y_1 ?z_1 ?x_2 ?y_2 ?z_2 ?x_3 ?y_3 ?z_3 WHERE {\n" +
                "    <" + cartesianPointList + "> rdf:type  ifc:IfcCartesianPoint_List .\n" +
                "    <" + cartesianPointList + "> list:hasContents ?contents_1 .\n" +
                "    ?contents_1 ifc:coordinates_IfcCartesianPoint ?coordinates_1 .\n" +
                "    ?coordinates_1 list:hasContents ?lengthMeasure_X_1 .\n" +
                "    ?lengthMeasure_X_1 express:hasDouble ?x_1 .\n" +
                "    ?coordinates_1 list:hasNext ?lengthMeasureList_Y_1 .\n" +
                "    ?lengthMeasureList_Y_1 list:hasContents ?lengthMeasure_Y_1 .\n" +
                "    ?lengthMeasure_Y_1 express:hasDouble ?y_1 .\n" +
                "    OPTIONAL {\n" +
                "        ?lengthMeasureList_Y_1 list:hasNext ?lengthMeasureList_Z_1 .\n" +
                "        ?lengthMeasureList_Z_1 list:hasContents ?lengthMeasure_Z_1 .\n" +
                "        ?lengthMeasure_Z_1 express:hasDouble ?z_1 .\n" +
                "    }\n" +
                "    OPTIONAL {\n" +
                "        <" + cartesianPointList + "> list:hasNext ?next .\n" +
                "        ?next list:hasContents ?contents_2 .\n" +
                "        ?contents_2 ifc:coordinates_IfcCartesianPoint ?coordinates_2 .\n" +
                "        ?coordinates_2 list:hasContents ?lengthMeasure_X_2 .\n" +
                "        ?lengthMeasure_X_2 express:hasDouble ?x_2 .\n" +
                "        ?coordinates_2 list:hasNext ?lengthMeasureList_Y_2 .\n" +
                "        ?lengthMeasureList_Y_2 list:hasContents ?lengthMeasure_Y_2 .\n" +
                "        ?lengthMeasure_Y_2 express:hasDouble ?y_2 .\n" +
                "        OPTIONAL {\n" +
                "            ?lengthMeasureList_Y_2 list:hasNext ?lengthMeasureList_Z_2 .\n" +
                "            ?lengthMeasureList_Z_2 list:hasContents ?lengthMeasure_Z_2 .\n" +
                "            ?lengthMeasure_Z_2 express:hasDouble ?z_2 .\n" +
                "        }\n" +
                "        OPTIONAL {\n" +
                "            ?next list:hasNext ?next_2 .\n" +
                "            ?next_2 list:hasContents ?contents_3 .\n" +
                "            ?contents_3 ifc:coordinates_IfcCartesianPoint ?coordinates_3 .\n" +
                "            ?coordinates_3 list:hasContents ?lengthMeasure_X_3 .\n" +
                "            ?lengthMeasure_X_3 express:hasDouble ?x_3 .\n" +
                "            ?coordinates_3 list:hasNext ?lengthMeasureList_Y_3 .\n" +
                "            ?lengthMeasureList_Y_3 list:hasContents ?lengthMeasure_Y_3 .\n" +
                "            ?lengthMeasure_Y_3 express:hasDouble ?y_3 .\n" +
                "            OPTIONAL {\n" +
                "                ?lengthMeasureList_Y_3 list:hasNext ?lengthMeasureList_Z_3 .\n" +
                "                ?lengthMeasureList_Z_3 list:hasContents ?lengthMeasure_Z_3 .\n" +
                "                ?lengthMeasure_Z_3 express:hasDouble ?z_3 .\n" +
                "            }\n" +
                "            OPTIONAL {\n" +
                "                ?next_2 list:hasNext ?next_3 .\n" +
                "            }\n" +
                "        }\n" +
                "    } \n" +
                "}";
        return QueryExecutionFactory
                .create(query, dataset.getDataset())
                .execSelect();
    }

}