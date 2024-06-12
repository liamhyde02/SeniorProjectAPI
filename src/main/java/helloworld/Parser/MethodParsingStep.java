package helloworld.Parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;

public class MethodParsingStep extends EntityParsingStep{
    public MethodParsingStep(EntityParsingChain next) {
        super(next);
    }

    @Override
    public JavaEntity construct(EntityBuilder builder, CompilationUnit cu) {
        Optional<ClassOrInterfaceDeclaration> classOrInterface = cu.findFirst(ClassOrInterfaceDeclaration.class);
        MethodVisitor visitor = new MethodVisitor();
        if (classOrInterface.isPresent()) {
            visitor.visit(cu, builder);
        }
        if (this.next != null) {
            return this.next.construct(builder, cu);
        }
        return builder.build();
    }
    private static class MethodVisitor extends VoidVisitorAdapter<EntityBuilder> {
        @Override
        public void visit(ClassOrInterfaceDeclaration n, EntityBuilder builder) {
            super.visit(n, builder);
            n.getMethods().forEach(method -> {
                Method newMethod = new Method(method.getNameAsString(), method.getTypeAsString(), method.getAccessSpecifier().asString(), method.isStatic(), method.isAbstract(), method.isFinal());
                builder.addMethod(newMethod.toString());
            });
        }
    }
}
