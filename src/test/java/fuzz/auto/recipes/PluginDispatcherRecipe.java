package fuzz.auto.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * AbstractApexPluginDispatcher: its only concrete subclass, DefaultApexPluginDispatcher, takes a
 * plugin locator, an app context, the container manager and app stats, none of which the engine
 * can build. The constructor only stores them, and serviceInit/serviceStop/dispatch use just the
 * locator and the context's application path, so the manager and stats stay null.
 *
 * <p>The dispatcher comes in one of three states: fresh; with 0-2 do-nothing plugins already
 * registered; or initialised (init(conf), which registers the plugins and creates the executor),
 * so serviceStop has something to stop.
 */
final class PluginDispatcherRecipe implements Recipe
{
  private static String appDir;

  @Override
  public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    ClassLoader loader = type.getClassLoader();
    Class<?> pluginType = Class.forName("org.apache.apex.engine.api.plugin.DAGExecutionPlugin", true, loader);

    final List<Object> plugins = new ArrayList<>();
    int n = data.consumeInt(0, 2);
    for (int i = 0; i < n; i++) {
      final String name = "plugin-" + i;
      plugins.add(Proxy.newProxyInstance(loader, new Class<?>[] {pluginType}, new ServletRecipes.Handler()
      {
        @Override
        Object call(String method, Object[] args)
        {
          return null; // setup(), teardown(): nothing to do
        }

        @Override
        public String toString()
        {
          return name;
        }
      }));
    }
    Object locator = stub(loader, "org.apache.apex.engine.api.plugin.PluginLocator", "discoverPlugins",
        new LinkedHashSet<>(plugins)); // discoverPlugins returns a Set
    Object context = stub(loader, "com.datatorrent.stram.StramAppContext", "getApplicationPath", appDir());

    Class<?> dispatcherType = Class.forName("org.apache.apex.engine.plugin.DefaultApexPluginDispatcher", true, loader);
    Constructor<?> ctor = null;
    for (Constructor<?> c : dispatcherType.getConstructors()) {
      if (c.getParameterCount() == 4) {
        ctor = c;
      }
    }
    Object dispatcher = ctor.newInstance(locator, context, null, null);

    switch (data.consumeInt(0, 2)) {
      case 1:
        // Plugins registered, but no executor: dispatch() then starts no thread.
        pluginsOf(type, dispatcher).addAll(plugins);
        break;
      case 2:
        Class<?> confType = Class.forName("org.apache.hadoop.conf.Configuration", true, loader);
        dispatcherType.getMethod("init", confType).invoke(dispatcher, confType.getConstructor().newInstance());
        letExecutorThreadsEnd(dispatcherType, dispatcher);
        break;
      default:
        break; // fresh
    }
    return dispatcher;
  }

  /** An interface stand-in that returns {@code value} from {@code method} and defaults otherwise. */
  private static Object stub(ClassLoader loader, String interfaceName, final String method, final Object value)
      throws Exception
  {
    Class<?> t = Class.forName(interfaceName, true, loader);
    return Proxy.newProxyInstance(loader, new Class<?>[] {t}, new ServletRecipes.Handler()
    {
      @Override
      Object call(String name, Object[] args)
      {
        return name.equals(method) ? value : null;
      }
    });
  }

  @SuppressWarnings("unchecked")
  private static Collection<Object> pluginsOf(Class<?> abstractType, Object dispatcher) throws Exception
  {
    Field f = abstractType.getDeclaredField("plugins");
    f.setAccessible(true);
    return (Collection<Object>) f.get(dispatcher);
  }

  /**
   * init() creates a one-thread executor whose thread would otherwise stay alive forever once
   * dispatch() hands it an event: one idle thread per input. Let it end when idle.
   */
  private static void letExecutorThreadsEnd(Class<?> dispatcherType, Object dispatcher) throws Exception
  {
    Field f = dispatcherType.getDeclaredField("executorService");
    f.setAccessible(true);
    Object executor = f.get(dispatcher);
    if (executor instanceof ThreadPoolExecutor) {
      ((ThreadPoolExecutor) executor).setKeepAliveTime(1, TimeUnit.MILLISECONDS);
      ((ThreadPoolExecutor) executor).allowCoreThreadTimeOut(true);
    }
  }

  private static synchronized String appDir() throws Exception
  {
    if (appDir == null) {
      appDir = Files.createTempDirectory("fuzzapp").toString();
    }
    return appDir;
  }
}
