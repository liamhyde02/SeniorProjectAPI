package helloworld.GraphBuilder;

import helloworld.Parser.JavaEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LocCalculator {
    public static void calculateLOC(ArrayList<JavaEntity> javaEntities) {
        int totalLOC = 0;
        for (JavaEntity javaEntity : javaEntities) {
            totalLOC += javaEntity.getLinesOfCode();
        }
        double averageLOC = javaEntities.size() > 0 ? (double) totalLOC / javaEntities.size() : 0;
        DataStorage.setAverageLOC(averageLOC);
    }
}
