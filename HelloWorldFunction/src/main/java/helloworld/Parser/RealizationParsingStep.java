package helloworld.Parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;

public class RealizationParsingStep extends EntityParsingStep {

    public RealizationParsingStep(EntityParsingChain next) {
        super(next);
    }

    @Override
    public JavaEntity construct(EntityBuilder entityBuilder, CompilationUnit compilationUnit) {
        Optional<ClassOrInterfaceDeclaration> classOrInterface = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class);
        InterfaceVisitor visitor = new InterfaceVisitor();
        if (classOrInterface.isPresent()) {
            visitor.visit(compilationUnit, entityBuilder);
        }
        if (this.next != null) {
            return this.next.construct(entityBuilder, compilationUnit);
        } else {
            return entityBuilder.build();
        }
    }

    private static class InterfaceVisitor extends VoidVisitorAdapter<EntityBuilder> {
        
        @Override
        public void visit(ClassOrInterfaceDeclaration n, EntityBuilder entityBuilder) {
            super.visit(n, entityBuilder);
            n.getImplementedTypes().forEach(implementedType -> 
                entityBuilder.addRealization(implementedType.getNameAsString()));
        }
    }
}
