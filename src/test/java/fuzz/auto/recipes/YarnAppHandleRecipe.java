package fuzz.auto.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * YarnAppLauncherImpl.YarnAppHandleImpl(ApplicationId, Configuration): an inner class holding a
 * real YARN client. The configuration points the client at a closed local port with retries off,
 * so every YARN call fails within milliseconds instead of waiting for a ResourceManager. That
 * failure is what shutdownApp(handle, KILL) handles, and the refactoring changed how.
 */
final class YarnAppHandleRecipe implements Recipe
{
  /**
   * YarnAppLauncherImpl itself: plain new YarnAppLauncherImpl(). Left to autofuzz, building it
   * used up the short early inputs, so shutdownApp's ShutdownMode always read as 0
   * (AWAIT_TERMINATION) and the KILL branch, the one the refactoring changed, never ran.
   */
  static final class Launcher implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return type.getConstructor().newInstance();
    }
  }

  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    ClassLoader loader = type.getClassLoader();
    Class<?> confType = Class.forName("org.apache.hadoop.conf.Configuration", true, loader);
    Object conf = confType.getConstructor().newInstance();
    Method set = confType.getMethod("set", String.class, String.class);
    set.invoke(conf, "yarn.resourcemanager.address", "127.0.0.1:1");
    set.invoke(conf, "yarn.resourcemanager.connect.max-wait.ms", "0");
    set.invoke(conf, "yarn.resourcemanager.connect.retry-interval.ms", "1");
    set.invoke(conf, "ipc.client.connect.max.retries", "0");

    Class<?> appIdType = Class.forName("org.apache.hadoop.yarn.api.records.ApplicationId", true, loader);
    // A fixed id: fuzzing it used up the short early inputs, so the ShutdownMode argument built
    // after this always read as 0 (AWAIT_TERMINATION) and the KILL branch never ran.
    Object appId = appIdType.getMethod("newInstance", long.class, int.class).invoke(null, 1L, 1);

    Class<?> launcherType = Class.forName("org.apache.apex.engine.YarnAppLauncherImpl", true, loader);
    Object launcher = launcherType.getConstructor().newInstance();
    Constructor<?> ctor = type.getDeclaredConstructors()[0]; // (outer launcher, appId, conf)
    ctor.setAccessible(true);
    return ctor.newInstance(launcher, appId, conf);
  }
}
