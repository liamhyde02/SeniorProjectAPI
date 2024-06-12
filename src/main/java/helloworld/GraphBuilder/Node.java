package helloworld.GraphBuilder;

import helloworld.FileSingleton;
import org.json.JSONObject;

import java.util.Objects;
import java.util.ArrayList;
import java.util.UUID;

public class Node {
    private final String name;
    private final String type;
    private final double size;
    private final ArrayList<String> methods, fields, types;
    private double instability;
    private double abstractness;
    private String pattern;
    private UUID id;

    public Node(String name, String type, double size, ArrayList<String> methods, ArrayList<String> fields, ArrayList<String> types) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.methods = methods;
        this.fields = fields;
        this.types = types;
        this.pattern = "";
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
    public double getSize() {
        return size;
    }

    public ArrayList<String> getMethods() {
        return methods;
    }

    public ArrayList<String> getFields() {
        return fields;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public UUID getId() {
        return id;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
    public void setInstability(double instability) {
        this.instability = instability;
    }

    public void setAbstractness(double abstractness) {
        this.abstractness = abstractness;
    }

    public double getInstability() {
        return instability;
    }

    public double getAbstractness() {
        return abstractness;
    }

    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", this.name);
        jsonObject.put("type", this.type);
        jsonObject.put("size", this.size);
        jsonObject.put("instability", this.instability);
        jsonObject.put("abstractness", this.abstractness);
        return jsonObject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return name.equals(node.name) && type.equals(node.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

}