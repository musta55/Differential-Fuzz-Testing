package fuzz.auto.recipes;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * DefaultCallbackHandler reads the user name, password and realm from a SecurityContext that is
 * only set by setup(SecurityContext). Built by the generic tiers, setup() was never called, so the
 * Name, Password and Realm branches all threw NullPointerException on context.getValue(...) on
 * both sides, which compared as agreement without testing anything.
 *
 * <p>This calls setup() with a SecurityContext whose USER_NAME, PASSWORD and REALM are fuzzed.
 * SecurityContext is a project interface, so it is implemented with a dynamic proxy created in the
 * handler's own class loader (a class compiled here would implement the wrong loader's copy).
 * Each value may also be null, a legitimate "not configured" input. Occasionally setup() is
 * skipped altogether, which keeps the not-set-up state in the input space as well.
 */
final class DefaultCallbackHandlerRecipe implements Recipe
{
  private static final String SECURITY_CONTEXT = "com.datatorrent.common.security.SecurityContext";

  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    Object handler = type.getConstructor().newInstance();
    if (data.consumeInt(0, 7) == 0) {
      return handler; // setup() never called: context stays null
    }

    ClassLoader loader = type.getClassLoader();
    final Class<?> ctxType = Class.forName(SECURITY_CONTEXT, true, loader);
    final Object userNameKey = ctxType.getField("USER_NAME").get(null);
    final Object passwordKey = ctxType.getField("PASSWORD").get(null);
    final Object realmKey = ctxType.getField("REALM").get(null);
    final String userName = data.consumeBoolean() ? data.consumeAsciiString(32) : null;
    final char[] password = data.consumeBoolean() ? data.consumeAsciiString(32).toCharArray() : null;
    final String realm = data.consumeBoolean() ? data.consumeAsciiString(32) : null;

    Object context = Proxy.newProxyInstance(loader, new Class<?>[] {ctxType},
        new InvocationHandler()
        {
          @Override
          public Object invoke(Object proxy, Method m, Object[] args)
          {
            if (m.getName().equals("getValue") && args != null && args.length == 1) {
              Object key = args[0];
              if (key == userNameKey) {
                return userName;
              }
              if (key == passwordKey) {
                // A copy: the handler hands it to PasswordCallback, which may keep or clear it.
                return password == null ? null : password.clone();
              }
              if (key == realmKey) {
                return realm;
              }
              return null;
            }
            if (m.getName().equals("hashCode")) {
              return System.identityHashCode(proxy);
            }
            if (m.getName().equals("equals")) {
              return proxy == args[0];
            }
            if (m.getName().equals("toString")) {
              return "SecurityContext(userName=" + userName + ", realm=" + realm + ")";
            }
            return null; // getAttributes(), setCounters(), sendMetrics(): not used by the handler
          }
        });

    type.getMethod("setup", ctxType).invoke(handler, context);
    return handler;
  }
}
