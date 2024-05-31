package helloworld.Parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;


public class NameParsingStep extends EntityParsingStep {
    public NameParsingStep(EntityParsingChain next) {
        super(next);
    }

    @Override
    public JavaEntity construct(EntityBuilder builder, CompilationUnit cu) {
        Optional<ClassOrInterfaceDeclaration> classOrInterface = cu.findFirst(ClassOrInterfaceDeclaration.class);
        Optional<EnumDeclaration> enumDeclaration = cu.findFirst(EnumDeclaration.class);
        String packageName = cu.getPackageDeclaration().isPresent()
                ? cu.getPackageDeclaration().get().getNameAsString() : "";
        ClassCreatorCollector visitor = new ClassCreatorCollector(packageName);
        if (classOrInterface.isPresent()) {
            visitor.visit(cu, builder);
        } else enumDeclaration.ifPresent(declaration -> visitor.visitEnum(declaration, builder));
        if (this.next != null) {
            return this.next.construct(builder, cu);
        }
        return builder.build();
    }

    private static class ClassCreatorCollector extends VoidVisitorAdapter<EntityBuilder>
    {
        private final String packageName;

        public ClassCreatorCollector(String packageName) {
            super();
            this.packageName = packageName;
        }

        @Override
        public void visit(ClassOrInterfaceDeclaration n, EntityBuilder builder)
        {
            super.visit(n, builder);
            if (!n.isInnerClass()) {
                String name = n.getNameAsString();
                builder.name(name);
                if (this.packageName.equals("")) {
                    builder.fullyQualifiedName(name);
                }
                else {
                    builder.fullyQualifiedName(this.packageName + "." + name);
                }
            }
        }
        public void visitEnum(EnumDeclaration e, EntityBuilder builder)
        {
            String name = e.getNameAsString();
            builder.name(name);
            if (this.packageName.equals("")) {
                builder.fullyQualifiedName(name);
            }
            else {
                builder.fullyQualifiedName(this.packageName + "." + name);
            }
        }
    }
}