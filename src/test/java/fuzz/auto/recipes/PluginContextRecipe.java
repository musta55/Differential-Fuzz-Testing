package fuzz.auto.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * AbstractDAGExecutionPluginContext: the only subclass is the private inner class
 * AbstractApexPluginDispatcher.PluginManagerImpl, and getOperatorName/getPhysicalOperatorStats
 * read the physical plan of a real StreamingContainerManager. This builds one the way Apex's own
 * unit tests do, from a one-operator application (DefaultDelayOperator, with a file-system
 * storage agent on a temp folder), and makes the context through a dispatcher that holds it.
 *
 * <p>Building the manager takes about half a second, so one context is made per side and reused:
 * the methods under test only read from it.
 */
final class PluginContextRecipe implements Recipe
{
  private static final Map<ClassLoader, Object> CONTEXTS = new HashMap<>();

  @Override
  public synchronized Object build(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    ClassLoader loader = type.getClassLoader();
    Object context = CONTEXTS.get(loader);
    if (context == null) {
      context = create(loader);
      CONTEXTS.put(loader, context);
    }
    return context;
  }

  private static final Map<ClassLoader, Object> MANAGERS = new HashMap<>();

  /** The one-operator application's container manager for this side, built once. */
  static synchronized Object manager(ClassLoader loader) throws Exception
  {
    Object manager = MANAGERS.get(loader);
    if (manager == null) {
      manager = createManager(loader);
      MANAGERS.put(loader, manager);
    }
    return manager;
  }

  private static Object create(ClassLoader loader) throws Exception
  {
    Object manager = manager(loader);

    // A dispatcher holding the manager, and the context it creates for a plugin.
    Object appContext = stub(loader, "com.datatorrent.stram.StramAppContext");
    Object plugin = stub(loader, "org.apache.apex.engine.api.plugin.DAGExecutionPlugin");
    Class<?> dispatcherType = load(loader, "org.apache.apex.engine.plugin.DefaultApexPluginDispatcher");
    Object dispatcher = null;
    for (Constructor<?> c : dispatcherType.getConstructors()) {
      if (c.getParameterCount() == 4) {
        dispatcher = c.newInstance(null, appContext, manager, null);
      }
    }
    Class<?> contextType = load(loader,
        "org.apache.apex.engine.plugin.AbstractApexPluginDispatcher$PluginManagerImpl");
    Constructor<?> ctor = contextType.getDeclaredConstructors()[0]; // (outer dispatcher, plugin)
    ctor.setAccessible(true);
    return ctor.newInstance(dispatcher, plugin);
  }

  private static Object createManager(ClassLoader loader) throws Exception
  {
    Class<?> planType = load(loader, "com.datatorrent.stram.plan.logical.LogicalPlan");
    Object dag = planType.getConstructor().newInstance();
    Method setAttribute = null;
    for (Method m : planType.getMethods()) {
      if (m.getName().equals("setAttribute") && m.getParameterCount() == 2) {
        setAttribute = m;
      }
    }
    setAttribute.invoke(dag, load(loader, "com.datatorrent.api.Context$DAGContext")
        .getField("APPLICATION_PATH").get(null), tempDir("fuzzdag"));
    Class<?> confType = load(loader, "org.apache.hadoop.conf.Configuration");
    Object storageAgent = load(loader, "com.datatorrent.common.util.FSStorageAgent")
        .getConstructor(String.class, confType).newInstance(tempDir("fuzzckpt"), null);
    setAttribute.invoke(dag, load(loader, "com.datatorrent.api.Context$OperatorContext")
        .getField("STORAGE_AGENT").get(null), storageAgent);
    planType.getMethod("addOperator", String.class, Class.class)
        .invoke(dag, "delay", load(loader, "com.datatorrent.common.util.DefaultDelayOperator"));
    Class<?> managerType = load(loader, "com.datatorrent.stram.StreamingContainerManager");
    return managerType.getConstructor(planType).newInstance(dag);
  }

  /** An interface stand-in whose methods do nothing and return defaults. */
  private static Object stub(ClassLoader loader, String interfaceName) throws Exception
  {
    return Proxy.newProxyInstance(loader, new Class<?>[] {load(loader, interfaceName)},
        new ServletRecipes.Handler()
        {
          @Override
          Object call(String name, Object[] args)
          {
            return null;
          }
        });
  }

  private static Class<?> load(ClassLoader loader, String name) throws ClassNotFoundException
  {
    return Class.forName(name, true, loader);
  }

  private static String tempDir(String prefix) throws Exception
  {
    return Files.createTempDirectory(prefix).toString();
  }
}
