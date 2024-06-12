package helloworld.GraphBuilder;

import helloworld.Parser.JavaEntity;
import org.json.JSONArray;
import org.json.JSONObject;


public class EdgeFactory {

    public static void createEdges(JavaEntity javaEntity){
        String name = javaEntity.getName();
        JSONObject jsonObject = javaEntity.toJSON();
        if (jsonObject.has("associations")) {
            createOuts(name, "association", jsonObject.getJSONArray("associations"));
        }
        if (jsonObject.has("realizations")) {
            createOuts(name, "realization", jsonObject.getJSONArray("realizations"));
        }
        if (jsonObject.has("compositions")) {
            createIns(name, "composition", jsonObject.getJSONArray("compositions"));
        }
        if (jsonObject.has("dependencies")) {
            createOuts(name, "dependencies", jsonObject.getJSONArray("dependencies"));
        }
    }

    public static void createOuts(String name, String type, JSONArray outs){
        for (int i = 0; i < outs.length(); i++) {
            Edge newEdge = new Edge(name, outs.getString(i), type);
            DataStorage.getInstance().addEdge(newEdge);
        }
    }
    public static void createIns(String name, String type, JSONArray ins){
        for (int i = 0; i < ins.length(); i++) {
            Edge newEdge = new Edge(ins.getString(i), name, type);
            DataStorage.getInstance().addEdge(newEdge);
        }
    }
}
