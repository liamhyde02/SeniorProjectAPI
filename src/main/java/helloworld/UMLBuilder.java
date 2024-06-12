package helloworld;

import helloworld.GraphBuilder.DataStorage;
import helloworld.GraphBuilder.Edge;
import helloworld.GraphBuilder.Node;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.xml.crypto.Data;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class UMLBuilder {
    private HashSet<String> edgeSet;
    private S3Handler s3Handler;


    public UMLBuilder(S3Handler s3Handler) {

        this.edgeSet = new HashSet<>();
        this.s3Handler = s3Handler;
    }

    public String buildUMLDiagram() {
        List<Node> nodes = DataStorage.getInstance().getNodes();
        System.out.println("Nodes: " + nodes.size());
        List<Edge> edges = DataStorage.getInstance().getEdges();
        System.out.println("Edges: " + edges.size());
        StringBuilder uml = new StringBuilder("@startuml\n");

        for (Node node : nodes) {
            String nodeType = node.getType();
            String nodeName = node.getName();
            switch (nodeType) {
                case "iface":
                    uml.append("interface ");
                    break;
                case "class":
                    uml.append("class ");
                    break;
                case "aclass":
                    uml.append("abstract class ");
                    break;
                case "enum":
                    uml.append("enum ");
                    break;
            }
            uml.append(nodeName);
            switch (node.getPattern()) {
                case "Singleton":
                    uml.append("<< (S,#FF7700) >>");
                    break;
            }
            uml.append(" {\n");
            ArrayList<String> fields = node.getFields();
            for (String field : fields) {
                uml.append("{field}").append(" ").append(field).append("\n");
            }
            ArrayList<String> methods = node.getMethods();
            for (String method : methods) {
                uml.append("{method}").append(" ").append(method).append("\n");
            }
            ArrayList<String> types = node.getTypes();
            for (String type : types) {
                uml.append(type).append("\n");
            }
            uml.append("}\n");
        }

        for (Edge edge : edges) {
            String from = edge.getFrom();
            String to = edge.getTo();
            String type = edge.getType();

            if (!edgeSet.contains(from + to)) {
                switch (type) {
                    case "realization":
                        uml.append(from).append(" ..|> ");
                        break;
                    case "composition":
                        uml.append(from).append(" --* ");
                        break;
                    case "dependencies":
                        uml.append(from).append(" ..> ");
                        break;
                    case "association":
                        uml.append(from).append(" -- ");
                        break;
                    case "parent":
                        uml.append(from).append(" --|> ");
                        break;
                }
                uml.append(to).append("\n");
                edgeSet.add(from + to);
            }
        }

        uml.append("@enduml");
        return generateImage(uml.toString());
    }

    private String generateImage(String uml) {
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            SourceStringReader reader = new SourceStringReader(uml);
            String desc = reader.generateImage(os, new FileFormatOption(FileFormat.SVG));
            System.out.println("Description: " + desc);
            byte[] imageBytes = os.toByteArray();
            System.out.println("Image bytes: " + imageBytes.length);

            // Upload to S3
            InputStream is = new ByteArrayInputStream(imageBytes);
            String s3Link = s3Handler.uploadImageToS3(is, "uml_diagram.svg", imageBytes.length);
            return s3Link;
        } catch (Exception e) {
            System.err.println("Failed to generate or upload UML diagram.");
            e.printStackTrace();
            return null;
        }
    }
}
