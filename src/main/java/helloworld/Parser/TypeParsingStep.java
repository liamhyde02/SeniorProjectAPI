package helloworld.Parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;

import java.util.Optional;

public class TypeParsingStep extends EntityParsingStep {
    public TypeParsingStep(EntityParsingChain next) {
        super(next);
    }

    @Override
    public JavaEntity construct(EntityBuilder builder, CompilationUnit declaration) {
        Optional<ClassOrInterfaceDeclaration> classOrInterface = findFirstClassOrInterface(declaration);
        Optional<EnumDeclaration> enumDeclaration = declaration.findFirst(EnumDeclaration.class);

        if (classOrInterface.isPresent()) {
            // Determine type of Java entity
            JavaEntityType entityType = determineJavaEntityType(classOrInterface.get());
            builder.type(entityType);

            // Continue with the next step in the parsing chain
            return next != null ? next.construct(builder, declaration) : builder.build();
        } else if (enumDeclaration.isPresent()) {
            builder.type(JavaEntityType.JAVA_ENUM);
            return next != null ? next.construct(builder, declaration) : builder.build();
        }

        return null; // No relevant type declaration found
    }

    private Optional<ClassOrInterfaceDeclaration> findFirstClassOrInterface(CompilationUnit declaration) {
        return declaration.findFirst(ClassOrInterfaceDeclaration.class);
    }

    private JavaEntityType determineJavaEntityType(ClassOrInterfaceDeclaration typeDeclaration) {
        if (typeDeclaration.isInterface()) {
            return JavaEntityType.JAVA_INTERFACE;
        } else if (typeDeclaration.isAbstract()) {
            return JavaEntityType.JAVA_ABSTRACT_CLASS;
        } else {
            return JavaEntityType.JAVA_BASE_CLASS;
        }
    }
}
