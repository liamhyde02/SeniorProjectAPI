package helloworld.Parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;

public class CompositionParsingStep extends EntityParsingStep {

    public CompositionParsingStep(EntityParsingChain next) {
        super(next);
    }

    public JavaEntity construct(EntityBuilder builder, CompilationUnit declaration) {
        // Parse the CompilationUnit to find composition relationships
        Optional<ClassOrInterfaceDeclaration> classOrInterface = declaration.findFirst(ClassOrInterfaceDeclaration.class);
        CompositionVisitor visitor = new CompositionVisitor();
        if (classOrInterface.isPresent()) {
            visitor.visit(declaration, builder);
        }
        // Proceed with the next step in the chain, if any
        if (next != null) {
            return next.construct(builder, declaration);
        } else {
            return builder.build();
        }
    }

    private static class CompositionVisitor extends VoidVisitorAdapter<EntityBuilder> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, EntityBuilder builder) {
            super.visit(n, builder);
            n.getFields().forEach(field -> analyzeField(field, builder));
        }

        private void analyzeField(FieldDeclaration field, EntityBuilder builder) {
            field.getVariables().forEach(variable -> {
                if (variable.getType().isClassOrInterfaceType()) {
                    builder.addComposition(variable.getType().asString());
                }
            });
        }
    }
}
