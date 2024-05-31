package helloworld.GraphBuilder;

import helloworld.FileSingleton;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DataStorage {
    private static double averageLOC = 0;
    private ArrayList<Node> nodeList = new ArrayList<>();
    private ArrayList<Edge> edgeList = new ArrayList<>();
    public static double getAverageLOC() {
        return averageLOC;
    }
    public static void setAverageLOC(double avgLOC) {
        averageLOC = avgLOC;
    }
    private static DataStorage instance = null;

    private DataStorage() {
    }

    public static DataStorage getInstance() {
        if (instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

    public void addNode(Node node) {
        this.nodeList.add(node);
    }

    public void addEdge(Edge edge) {
        edgeList.add(edge);
    }

    public ArrayList<Node> getNodes() {
        return new ArrayList<>(this.nodeList);
    }

    public ArrayList<Edge> getEdges() {
        return new ArrayList<>(this.edgeList);
    }

    public void removeDuplicateEdges() {
        List<Edge> edgeList = this.edgeList;
        Set<Edge> uniqueEdges = new HashSet<>(edgeList);
        edgeList.clear();
        edgeList.addAll(uniqueEdges);
    }
    public void removeDuplicateNodes() {
        Set<Node> uniqueNodes = new HashSet<>(nodeList);
        nodeList.clear();
        nodeList.addAll(uniqueNodes);
        ArrayList<Node> invalidNodes = new ArrayList<>();
        ArrayList<Edge> invalidEdges = new ArrayList<>();
        for (Node node : nodeList) {
            boolean isFile = false;
            for (File file : FileSingleton.getInstance().getFiles()) {
                if (file.getPath().contains(node.getName())) {
                    isFile = true;
                    break;
                }
            }
            if (!isFile) {
                invalidNodes.add(node);
            }
        }
        for (Node node : invalidNodes) {
            for (Edge edge : edgeList) {
                if (edge.getFrom().equals(node.getName()) || edge.getTo().equals(node.getName())) {
                    invalidEdges.add(edge);
                }
            }
        }
        this.nodeList.removeAll(invalidNodes);
        this.edgeList.removeAll(invalidEdges);
        System.out.println("Number of nodes: " + nodeList.size());
    }

}
