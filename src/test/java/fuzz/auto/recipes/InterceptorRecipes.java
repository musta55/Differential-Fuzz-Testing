package fuzz.auto.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/** For interceptors: the InvocationContext they receive, and deltaspike's throttling Invoker. */
final class InterceptorRecipes
{
  private InterceptorRecipes() {}

  /**
   * javax.interceptor.InvocationContext: an interface the container normally supplies. The
   * stand-in reports Object.toString() as the intercepted method, and proceed() either returns a
   * value or throws, as the intercepted call would.
   */
  static final class Context implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      final java.lang.reflect.Method method = Object.class.getMethod("toString");
      final boolean fails = data.consumeInt(0, 3) == 3; // proceed() throws now and then
      return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
          new ServletRecipes.Handler()
          {
            @Override
            Object call(String name, Object[] args) throws Exception
            {
              if (name.equals("getMethod")) {
                return method;
              }
              if (name.equals("getParameters")) {
                return new Object[0];
              }
              if (name.equals("getContextData")) {
                return new HashMap<String, Object>();
              }
              if (name.equals("proceed")) {
                if (fails) {
                  throw new IllegalStateException("intercepted call failed");
                }
                return "proceeded";
              }
              return null;
            }

            @Override
            public String toString()
            {
              return "InvocationContext(" + (fails ? "proceed throws" : "proceed returns") + ")";
            }
          });
    }
  }

  /**
   * An interceptor whose strategy is normally @Inject-ed (deltaspike's
   * NavigationParameterInterceptor and NavigationParameterListInterceptor). Created with new, the
   * field stayed null and every call threw NullPointerException. The real strategy needs a running
   * JSF application, so the field gets a stand-in strategy that just proceeds, as an interceptor
   * strategy does when it has nothing to add.
   */
  static final class WithStrategy implements Recipe
  {
    private final String field;

    WithStrategy(String field)
    {
      this.field = field;
    }

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object interceptor = type.getConstructor().newInstance();
      java.lang.reflect.Field f = type.getDeclaredField(field);
      f.setAccessible(true);
      f.set(interceptor, Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {f.getType()},
          new ServletRecipes.Handler()
          {
            @Override
            Object call(String name, Object[] args) throws Exception
            {
              if (name.equals("execute")) {
                return args[0].getClass().getMethod("proceed").invoke(args[0]); // ctx.proceed()
              }
              return null;
            }

            @Override
            public String toString()
            {
              return "Strategy(proceed)";
            }
          }));
      return interceptor;
    }
  }

  /**
   * deltaspike throttling Invoker(Semaphore, weight, timeout): with fuzzed values invoke() waited
   * up to the timeout, or forever in semaphore.acquire() when timeout <= 0 and the semaphore had
   * fewer permits than the weight, leaving a stuck thread per input. Small values, and never a
   * blocking acquire: a weight above the permits only with a short timeout (the "can't acquire"
   * path).
   */
  static final class Invoker implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      int permits = data.consumeInt(0, 3);
      int weight = data.consumeInt(0, 3);
      long timeout = data.consumeInt(0, 20); // milliseconds; 0 means acquire() without a timeout
      if (timeout == 0 && weight > permits) {
        weight = permits; // acquire() would block forever
      }
      Constructor<?> ctor = type.getDeclaredConstructor(Semaphore.class, int.class, long.class);
      ctor.setAccessible(true); // package-private
      return ctor.newInstance(new Semaphore(permits), weight, timeout);
    }
  }
}
