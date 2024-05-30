package helloworld.Parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;


public class FieldParsingStep extends EntityParsingStep{
    public FieldParsingStep(EntityParsingChain next) {
        super(next);
    }

    @Override
    public JavaEntity construct(EntityBuilder builder, CompilationUnit cu) {
        FieldVisitor visitor = new FieldVisitor();
        visitor.visit(cu, builder);
        if (this.next != null) {
            return this.next.construct(builder, cu);
        }
        return builder.build();
    }
    private static class FieldVisitor extends VoidVisitorAdapter<EntityBuilder> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, EntityBuilder builder) {
            super.visit(n, builder);
            n.getFields().forEach(field -> {
                Field newField = new Field(field.getVariables().get(0).getNameAsString(), field.getElementType().asString(), field.getAccessSpecifier().asString(), field.isStatic(), field.isFinal());
                builder.addField(newField.toString());
            });
        }
    }
}
