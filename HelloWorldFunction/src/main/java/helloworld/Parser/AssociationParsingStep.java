package helloworld.Parser;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.Optional;

public class AssociationParsingStep extends EntityParsingStep {

  public AssociationParsingStep(EntityParsingChain next) {
    super(next);
  }

  @Override
  public JavaEntity construct(EntityBuilder builder, CompilationUnit declaration) {
    Optional<ClassOrInterfaceDeclaration> classOrInterface = declaration.findFirst(ClassOrInterfaceDeclaration.class);
    AssociationVisitor visitor = new AssociationVisitor();
    if (classOrInterface.isPresent()) {
      visitor.visit(declaration, builder);
    }
    if (this.next != null) {
      return this.next.construct(builder, declaration);
    } else {
      return builder.build();
    }
  }

  private static class AssociationVisitor extends VoidVisitorAdapter<EntityBuilder> {
    @Override
    public void visit(MethodDeclaration n, EntityBuilder builder) {
      super.visit(n, builder);
      n.findAll(MethodCallExpr.class).forEach(m -> {
        if (m.getScope().isPresent()) {
          builder.addAssociation(m.getScope().get().toString());
        }
      });
    }
  }
}
