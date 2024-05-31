package helloworld.Parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;

public class EnumParsingStep extends EntityParsingStep{
    public EnumParsingStep(EntityParsingChain next) {
        super(next);
    }

    @Override
    public JavaEntity construct(EntityBuilder entityBuilder, CompilationUnit compilationUnit) {
        Optional<EnumDeclaration> enumDeclaration = compilationUnit.findFirst(EnumDeclaration.class);
        if (enumDeclaration.isPresent()) {
            EnumVisitor enumVisitor = new EnumVisitor();
            enumVisitor.visit(enumDeclaration.get(), entityBuilder);
            this.next = new BuildParsingFinish();
            this.next.construct(entityBuilder, compilationUnit);
        }
        return next.construct(entityBuilder, compilationUnit);
    }

    private static class EnumVisitor extends VoidVisitorAdapter<EntityBuilder> {
        @Override
        public void visit(EnumDeclaration n, EntityBuilder entityBuilder) {
            super.visit(n, entityBuilder);
            try {
                n.getEntries().stream()
                        .map(e -> e.getNameAsString())
                        .forEach(name -> entityBuilder.addType(name));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
