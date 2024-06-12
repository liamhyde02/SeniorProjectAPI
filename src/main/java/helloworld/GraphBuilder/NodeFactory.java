package helloworld.GraphBuilder;

import java.util.ArrayList;

import helloworld.Parser.JavaClass;
import helloworld.Parser.JavaEntity;
import helloworld.Parser.JavaEntityType;
import org.json.JSONArray;
import org.json.JSONObject;

public class NodeFactory {

    public static void createNode(JavaEntity javaEntity){
        try {
            String name = javaEntity.getName();
            String type = javaEntity.getStrType();
            double size = getSize(javaEntity.getLinesOfCode());
            ArrayList<String> methods = javaEntity.getMethods();
            ArrayList<String> fields = new ArrayList<>();
            ArrayList<String> types = new ArrayList<>();
            if (javaEntity.getType().equals(JavaEntityType.JAVA_BASE_CLASS) || javaEntity.getType().equals(JavaEntityType.JAVA_ABSTRACT_CLASS)) {
                JavaClass javaClass = (JavaClass) javaEntity;
                fields = javaClass.getFields();
            } else if (javaEntity.getType().equals(JavaEntityType.JAVA_ENUM)) {
                types = javaEntity.getMethods();
            }
            Node newNode = new Node(name, type, size, methods, fields, types);
            DataStorage.getInstance().addNode(newNode);
        } catch (Exception e) {
            return;
        }

    }

    private static double getSize(int loc){
        double size = (double) loc / DataStorage.getAverageLOC();
        size = Math.round(size * 1000) / 1000.0;
        return size;
    }
}
