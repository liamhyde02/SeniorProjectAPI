package helloworld.GraphBuilder;

import helloworld.Parser.JavaEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;

public class Functionality {

    public static void toGraphData(ArrayList<JavaEntity> JavaEntities) {
        LoopingFunctionality.runChain(JavaEntities);
        DataStorage.getInstance().removeDuplicateNodes();
        DataStorage.getInstance().removeDuplicateEdges();
    }
}
