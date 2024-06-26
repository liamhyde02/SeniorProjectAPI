package helloworld.Parser;

import java.util.ArrayList;
import java.util.Optional;

public abstract class JavaClass implements JavaEntity {
    String fullyQualifiedName, name;
    ArrayList<String> dependencies, realizations, compositions, associations, methods, fields;
    Optional<String> parent;
    int linesOfCode;
    public JavaClass(String name, String fullyQualifiedName, int linesOfCode,
                         ArrayList<String> dependencies, ArrayList<String> realizations,
                         ArrayList<String> compositions, ArrayList<String> associations, 
                         ArrayList<String> methods, ArrayList<String> fields,
                        Optional<String> parent) {
        this.name = name;
        this.fullyQualifiedName = fullyQualifiedName;
        this.linesOfCode = linesOfCode;
        this.dependencies = dependencies;
        this.realizations = realizations;
        this.compositions = compositions;
        this.associations = associations;
        this.parent = parent;
        this.methods = methods;
        this.fields = fields;
    }
    public ArrayList<String> getAssociations() {
        return associations;
    }
    public ArrayList<String> getDependencies() {
        return dependencies;
    }
    public ArrayList<String> getCompositions() {
        return compositions;
    }
    public ArrayList<String> getRealizations() {
        return realizations;
    }
    public ArrayList<String> getFields() {return fields;}
    public ArrayList<String> getMethods() {
        return methods;
    }
    public String getParent() {
        return parent.orElse(null);
    }
    public String getName() {
        return name;
    }
    public String getFullyQualifiedName() {
        return fullyQualifiedName;
    }
    public int getLinesOfCode() {
        return linesOfCode;
    }
    public abstract String getStrType();
    public abstract JavaEntityType getType();
    public void setParent(String parent) {
        this.parent = Optional.ofNullable(parent);
    }
}
