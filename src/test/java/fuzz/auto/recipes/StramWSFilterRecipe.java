package fuzz.auto.recipes;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicInteger;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * StramWSFilter is set up by init(FilterConfig), which also starts the token manager's background
 * threads: once per input, that would start thousands of threads. This sets the same fields
 * directly: the proxy host is 127.0.0.1 (an IP, so no DNS lookup) and the token manager is created
 * but not started. Without a started manager there is no signing key, so createClientToken fails
 * the same way on both sides.
 */
final class StramWSFilterRecipe implements Recipe
{
  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    Class<?> managerType = Class.forName("com.datatorrent.stram.security.StramDelegationTokenManager",
        true, type.getClassLoader());
    long hour = 60 * 60 * 1000L;
    Object manager = managerType.getConstructor(long.class, long.class, long.class, long.class)
        .newInstance(24 * hour, hour, hour, hour);

    Object filter = type.getConstructor().newInstance();
    set(filter, "proxyHosts", new String[] {"127.0.0.1"});
    set(filter, "tokenManager", manager);
    set(filter, "sequenceNumber", new AtomicInteger(0));
    set(filter, "loginUser", "fuzz");
    return filter;
  }

  private static void set(Object target, String field, Object value) throws Exception
  {
    Field f = target.getClass().getDeclaredField(field);
    f.setAccessible(true);
    f.set(target, value);
  }
}
