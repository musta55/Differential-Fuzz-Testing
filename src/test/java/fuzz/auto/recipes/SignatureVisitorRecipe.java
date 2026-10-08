package fuzz.auto.recipes;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * BaseSignatureVisitor: visitTypeArgument(char) starts by popping the type a previous call pushed,
 * so on a fresh visitor it always threw EmptyStackException before doing anything. This makes a
 * FieldSignatureVisitor (a concrete subclass) and replays the calls a real signature parse makes
 * first: a class type, and sometimes a type argument, which leaves a parameterized type on top.
 * Now and then the visitor stays fresh, for the empty-stack case.
 */
final class SignatureVisitorRecipe implements Recipe
{
  private static final String[] CLASS_TYPES = {"java/util/List", "java/util/Map", "java/lang/String"};

  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    Class<?> visitorType = Class.forName("com.datatorrent.stram.webapp.asm.FieldSignatureVisitor",
        true, type.getClassLoader());
    Object visitor = visitorType.getConstructor().newInstance();
    // Fresh only on the highest value: a used-up input reads as 0 and should get a primed visitor.
    int steps = data.consumeInt(0, 2);
    if (steps < 2) {
      String classType = CLASS_TYPES[data.consumeInt(0, CLASS_TYPES.length - 1)];
      visitorType.getMethod("visitClassType", String.class).invoke(visitor, classType);
    }
    if (steps == 1) {
      // '=' is SignatureVisitor.INSTANCEOF: leaves a ParameterizedTypeNode on top of the stack.
      visitorType.getMethod("visitTypeArgument", char.class).invoke(visitor, '=');
    }
    return visitor;
  }
}
