package helloworld.GraphBuilder;

import helloworld.Parser.JavaEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoopingFunctionality {

    public static void runChain(ArrayList<JavaEntity> javaEntities){
        for (JavaEntity javaEntity : javaEntities) {
            NodeFactory.createNode(javaEntity);
            EdgeFactory.createEdges(javaEntity);
        }
    }
}
