package fuzz.auto.recipes;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * Stand-ins for the Servlet API interfaces of Filter.doFilter(request, response, chain), which the
 * engine cannot build (their real implementations live inside a web server). The response and the
 * chain record what the filter did with them, and the engine compares arguments after the call,
 * so "sent 401" and "passed the request on" become comparable. Written with reflection because
 * this folder is compiled for projects that do not have the Servlet API.
 */
final class ServletRecipes
{
  private ServletRecipes() {}

  /** An HTTP request with a fuzzed remote address, path and cookies. */
  static final class Request implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      final String remoteAddr = data.consumeBoolean() ? "127.0.0.1" : "10.0.0." + data.consumeInt(1, 9);
      final String uri = data.consumeBoolean() ? "/ws" : "/ws/v2/" + data.consumeInt(0, 9);
      final Object cookies = cookies(data, type.getClassLoader());
      return proxy(type, "javax.servlet.http.HttpServletRequest", new Handler()
      {
        @Override
        Object call(String name, Object[] args)
        {
          if (name.equals("getRemoteAddr") || name.equals("getLocalAddr")) {
            return name.equals("getRemoteAddr") ? remoteAddr : "127.0.0.1";
          }
          if (name.equals("getRequestURI")) {
            return uri;
          }
          if (name.equals("getCookies")) {
            return cookies;
          }
          return null;
        }

        @Override
        public String toString()
        {
          return "Request(" + remoteAddr + " " + uri + ")";
        }
      });
    }

    /** No cookies, or some of: the proxy's user cookie, the client token cookie, another one. */
    private static Object cookies(FuzzedDataProvider data, ClassLoader loader) throws Exception
    {
      if (data.consumeInt(0, 3) == 0) {
        return null;
      }
      Class<?> cookieType = Class.forName("javax.servlet.http.Cookie", true, loader);
      String[] names = {"proxy-user", "dt-client", "other"};
      List<Object> list = new ArrayList<>();
      for (String name : names) {
        if (data.consumeBoolean()) {
          String value = "v" + data.consumeInt(0, 9);
          list.add(cookieType.getConstructor(String.class, String.class).newInstance(name, value));
        }
      }
      Object array = Array.newInstance(cookieType, list.size());
      for (int i = 0; i < list.size(); i++) {
        Array.set(array, i, list.get(i));
      }
      return array;
    }
  }

  /** An HTTP response that records the errors sent and the names of the cookies added. */
  static final class Response implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return proxy(type, "javax.servlet.http.HttpServletResponse", new Handler()
      {
        final List<Integer> errors = new ArrayList<>();
        final List<String> cookieNames = new ArrayList<>(); // not values: tokens are random

        @Override
        Object call(String name, Object[] args) throws Exception
        {
          if (name.equals("sendError")) {
            errors.add((Integer) args[0]);
          } else if (name.equals("addCookie")) {
            cookieNames.add((String) args[0].getClass().getMethod("getName").invoke(args[0]));
          }
          return null;
        }

        @Override
        public String toString()
        {
          return "Response(errors=" + errors + ", cookies=" + cookieNames + ")";
        }
      });
    }
  }

  /** A filter chain that records whether the request was passed on, and as which user. */
  static final class Chain implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return proxy(type, null, new Handler()
      {
        boolean called;
        String user;

        @Override
        Object call(String name, Object[] args) throws Exception
        {
          if (name.equals("doFilter")) {
            called = true;
            Object principal = args[0].getClass().getMethod("getUserPrincipal").invoke(args[0]);
            user = principal == null ? null : principal.toString();
          }
          return null;
        }

        @Override
        public String toString()
        {
          return "Chain(called=" + called + ", user=" + user + ")";
        }
      });
    }
  }

  /** A ServletRequestEvent: a do-nothing ServletContext and a request from {@link Request}. */
  static final class RequestEvent implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> contextType = Class.forName("javax.servlet.ServletContext", true, loader);
      Class<?> requestType = Class.forName("javax.servlet.ServletRequest", true, loader);
      Object context = proxy(contextType, null, new Handler()
      {
        @Override
        Object call(String name, Object[] args)
        {
          return null;
        }
      });
      Object request = new Request().build(data, requestType);
      return type.getConstructor(contextType, requestType).newInstance(context, request);
    }
  }

  /** Answers equals/hashCode/toString itself, everything else through call(). */
  abstract static class Handler implements InvocationHandler
  {
    abstract Object call(String name, Object[] args) throws Exception;

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Exception
    {
      String name = m.getName();
      if (name.equals("equals")) {
        return proxy == args[0];
      }
      if (name.equals("hashCode")) {
        return System.identityHashCode(proxy);
      }
      if (name.equals("toString")) {
        return toString();
      }
      Object result = call(name, args);
      if (result == null && m.getReturnType().isPrimitive()) {
        return defaultValue(m.getReturnType());
      }
      return result;
    }
  }

  private static Object defaultValue(Class<?> t)
  {
    if (t == boolean.class) {
      return false;
    }
    if (t == void.class) {
      return null;
    }
    if (t == long.class) {
      return 0L;
    }
    return 0; // int, and the rarely used short/char/byte/float/double return no value here
  }

  /** A proxy implementing {@code type}, plus the more specific HTTP interface when given. */
  private static Object proxy(Class<?> type, String httpType, Handler handler) throws Exception
  {
    ClassLoader loader = type.getClassLoader();
    Class<?>[] interfaces = httpType == null
        ? new Class<?>[] {type}
        : new Class<?>[] {Class.forName(httpType, true, loader)};
    return Proxy.newProxyInstance(loader, interfaces, handler);
  }
}
