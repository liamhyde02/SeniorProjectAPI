package helloworld.Parser;

import org.json.JSONArray;

import java.io.File;
import java.util.ArrayList;

public class JavaParserFunctionality implements JavaParser{
    public ArrayList<JavaEntity> parse(ArrayList<File> files) {
        ArrayList<JavaEntity> entities = new ArrayList<>();
        SourceCodeParser sourceCodeParser = new SourceCodeParser();
        try {
            entities = sourceCodeParser.parseSourceFiles(files);
        } catch (Exception e) {
            e.printStackTrace();
        }
        EntityCleaning entityCleaning = new EntityCleaning();
        entityCleaning.clean(entities);
        return entities;
    }
}
