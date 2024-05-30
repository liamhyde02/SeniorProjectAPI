package helloworld.Parser;

public class Field {
    private String name;
    private String type;
    private String visibility;
    private boolean isStatic;
    private boolean isFinal;
    public Field(String name, String type, String visibility, boolean isStatic, boolean isFinal) {
        this.name = name;
        this.type = type;
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.isFinal = isFinal;
    }

    public String toString() {
        String staticRepr = isStatic ? "static " : "";
        String finalRepr = isFinal ? "final " : "";
        switch (visibility) {
            case "public":
                visibility = "+";
                break;
            case "private":
                visibility = "-";
                break;
            case "protected":
                visibility = "#";
                break;
            default:
                visibility = "";
        }
        return visibility + " " + staticRepr + finalRepr + type + " " + name;
    }
}
