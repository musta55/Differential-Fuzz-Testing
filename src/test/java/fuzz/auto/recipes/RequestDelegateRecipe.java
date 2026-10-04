package fuzz.auto.recipes;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * RequestFactory.RequestDelegate: the only implementation is a private inner class of
 * TupleRecorderCollection, which the engine cannot create. RequestFactory.registerDelegate only
 * stores the delegate (and prints it when replacing one), so a stand-in is enough.
 */
final class RequestDelegateRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type)
  {
    final String name = "delegate-" + data.consumeInt(0, 9);
    return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
        new InvocationHandler()
        {
          @Override
          public Object invoke(Object proxy, Method m, Object[] args)
          {
            if (m.getName().equals("toString")) {
              return name;
            }
            if (m.getName().equals("equals")) {
              return proxy == args[0];
            }
            if (m.getName().equals("hashCode")) {
              return name.hashCode();
            }
            return null; // getRequestExecutor: no request executor
          }
        });
  }
}
