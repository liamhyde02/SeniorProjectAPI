package helloworld.Parser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;

public class DependencyParsingStep extends EntityParsingStep {

    public DependencyParsingStep(EntityParsingChain next) {
        super(next);
    }

    @Override
    public JavaEntity construct(EntityBuilder entityBuilder, CompilationUnit compilationUnit) {
        Optional<ClassOrInterfaceDeclaration> classOrInterface = compilationUnit.findFirst(ClassOrInterfaceDeclaration.class);
        DependencyVisitor visitor = new DependencyVisitor();
        if (classOrInterface.isPresent()) {
            visitor.visit(compilationUnit, entityBuilder);
        }
        if (this.next != null) {
            return this.next.construct(entityBuilder, compilationUnit);
        } else {
            return entityBuilder.build();
        }
    }

    private static class DependencyVisitor extends VoidVisitorAdapter<EntityBuilder> {
        
        @Override
        public void visit(MethodDeclaration n, EntityBuilder entityBuilder) {
            super.visit(n, entityBuilder);
            n.getParameters().forEach(p -> {
                if (p.getType().isClassOrInterfaceType()) {
                    entityBuilder.addDependency(p.getType().
                    asClassOrInterfaceType().getNameAsString());
                }
            });
        }
    }
}
