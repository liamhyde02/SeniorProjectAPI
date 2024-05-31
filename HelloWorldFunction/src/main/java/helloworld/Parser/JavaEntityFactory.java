package helloworld.Parser;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JavaEntityFactory implements EntityFactory{

    @Override
    public JavaEntity createEntityFromFile(File filename) throws IOException {
        /* type -> name -> association -> dependency -> LOC -> inheritance -> 
        realization -> composition -> build */
        EntityParsingChain p = new TypeParsingStep(
            new NameParsingStep(
                new EnumParsingStep(
                    new AssociationParsingStep(
                        new LOCParsingStep(
                            new InheritanceParsingStep(
                                new RealizationParsingStep(
                                    new CompositionParsingStep(
                                        new MethodParsingStep(
                                            new FieldParsingStep(
                                                    new DependencyParsingStep(
                                                            new BuildParsingFinish())))))))))));
        
        String fileContent = Files.readString(Paths.get(filename.toURI()));
        try {
            CompilationUnit compilationUnit = StaticJavaParser.parse(fileContent);
            JavaEntity entity = p.construct(new JavaEntityBuilder(), compilationUnit);
            return entity;
        }
        catch (Exception e) {
            System.out.println("Error parsing file: " + filename.getName());
//            System.out.println(e.getMessage());
//            System.out.println(e.getStackTrace());
//            System.out.println("File content:\n" + fileContent);
            return null;
        }
    }
}
