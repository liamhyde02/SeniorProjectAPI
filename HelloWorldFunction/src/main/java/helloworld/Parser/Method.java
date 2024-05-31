package helloworld.Parser;

public class Method {
    private String name;
    private String returnType;
    private String visibility;
    private boolean isStatic;
    private boolean isAbstract;
    private boolean isFinal;
    public Method(String name, String returnType, String visibility, boolean isStatic, boolean isAbstract, boolean isFinal) {
        this.name = name;
        this.returnType = returnType;
        this.visibility = visibility;
        this.isStatic = isStatic;
        this.isAbstract = isAbstract;
        this.isFinal = isFinal;
    }

    public String toString() {
        String staticRepr = isStatic ? "{static} " : "";
        String abstractRepr = isAbstract ? "{abstract} " : "";
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
        return visibility + " " + staticRepr + abstractRepr + finalRepr + returnType + " " + name + "()";
    }
}
