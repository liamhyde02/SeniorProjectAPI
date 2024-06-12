package helloworld.Parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.EnumDeclaration;

import java.util.Optional;

public class BuildParsingFinish extends EntityParsingFinish{
    @Override
    public JavaEntity construct(EntityBuilder builder, CompilationUnit unit) {
        Optional<EnumDeclaration> enumDeclaration = unit.findFirst(EnumDeclaration.class);
        if (enumDeclaration.isPresent()) {
        }
        return builder.build();
    }
}
