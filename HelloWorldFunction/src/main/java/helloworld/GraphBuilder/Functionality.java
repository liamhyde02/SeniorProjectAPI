package helloworld.GraphBuilder;

import helloworld.Parser.JavaEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;

public class Functionality {

    public static JSONObject toGraphData(ArrayList<JavaEntity> JavaEntities) {
        LocCalculator.calculateLOC(JavaEntities);
        LoopingFunctionality.runChain(JavaEntities);
        DataStorage.removeDuplicateEdges();
        MetricsCalculator.calculateMetrics(DataStorage.getNodes(), DataStorage.getEdges());
        DataStorage.removeDuplicateNodes();
        return DataStorage.returnData();
    }
}
