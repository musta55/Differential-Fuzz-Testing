package fuzz.auto.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * Journal.Recoverable: Journal.write() accepts only the operation classes it has registered, and
 * all of them are private nested classes with private constructors, so the engine never found
 * one. This calls those constructors with fuzzed values. SetContainerState is left out: its only
 * useful constructor needs a whole PTContainer. Now and then it returns an unregistered stand-in,
 * which covers write()'s "Class not registered" path.
 */
final class RecoverableRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    ClassLoader loader = type.getClassLoader();
    switch (data.consumeInt(0, 3)) {
      case 0: {
        Class<?> stateType = load("com.datatorrent.stram.plan.physical.PTOperator$State", loader);
        Object[] states = stateType.getEnumConstants();
        return create("com.datatorrent.stram.plan.physical.PTOperator$SetOperatorState", loader,
            new Class<?>[] {int.class, stateType},
            data.consumeInt(), states[data.consumeInt(0, states.length - 1)]);
      }
      case 1:
        return create("com.datatorrent.stram.StreamingContainerManager$SetOperatorProperty", loader,
            new Class<?>[] {String.class, String.class, String.class},
            text(data), text(data), text(data));
      case 2:
        return create("com.datatorrent.stram.StreamingContainerManager$SetPhysicalOperatorProperty",
            loader, new Class<?>[] {int.class, String.class, String.class},
            data.consumeInt(), text(data), text(data));
      default:
        return Proxy.newProxyInstance(loader, new Class<?>[] {type}, new InvocationHandler()
        {
          @Override
          public Object invoke(Object proxy, Method m, Object[] args)
          {
            return null; // read()/write() do nothing; Journal rejects it before calling them
          }
        });
    }
  }

  /** A fuzzed string, or null (the default constructors use null values too). */
  private static String text(FuzzedDataProvider data)
  {
    return data.consumeBoolean() ? data.consumeAsciiString(16) : null;
  }

  private static Class<?> load(String name, ClassLoader loader) throws ClassNotFoundException
  {
    return Class.forName(name, true, loader);
  }

  private static Object create(String className, ClassLoader loader, Class<?>[] params,
      Object... args) throws Exception
  {
    Constructor<?> c = load(className, loader).getDeclaredConstructor(params);
    c.setAccessible(true); // the constructors are private
    return c.newInstance(args);
  }
}
