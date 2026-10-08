package fuzz.auto.recipes;

import java.util.ArrayList;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * java.lang.reflect.Method and Constructor: Autofuzz built them through their internal
 * constructors with fuzzed values, including the internal slot that points at the method inside
 * the JVM. Such a forged object crashed the whole JVM (SIGSEGV in Executable.getParameters0) when
 * ParameterUtil.getName asked it for its parameters. These are real members of harmless JDK
 * classes, the same objects on both sides; calling any of them cannot exit or harm the JVM.
 */
final class MemberRecipes
{
  private MemberRecipes() {}

  private static final Class<?>[] OWNERS = {Object.class, String.class, ArrayList.class};

  /** A real public method of Object, String or ArrayList. */
  static final class Method implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      java.lang.reflect.Method[] methods = owner(data).getMethods();
      return methods[data.consumeInt(0, methods.length - 1)];
    }
  }

  /** A real public constructor of Object, String or ArrayList. */
  static final class Constructor implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      java.lang.reflect.Constructor<?>[] constructors = owner(data).getConstructors();
      return constructors[data.consumeInt(0, constructors.length - 1)];
    }
  }

  private static Class<?> owner(FuzzedDataProvider data)
  {
    return OWNERS[data.consumeInt(0, OWNERS.length - 1)];
  }
}
