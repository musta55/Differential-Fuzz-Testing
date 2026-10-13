package fuzz.auto.recipes;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * apex-core recipes, keyed by apex-core's own classes.
 */
final class ApexRecipes
{
  private ApexRecipes() {}

  /**
   * DiskStorage (buffer-server spooling): it writes a folder per identifier under its base path, and
   * with one shared path the original's files changed what the refactored side and the self-check
   * rerun saw. Spools into the side's own empty folder ({@link CommonRecipes.SideFolder}).
   */
  static final class DiskStorageRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      java.io.File spool = new java.io.File(CommonRecipes.SideFolder.of(data, type), "spool");
      spool.mkdirs();
      return type.getConstructor(String.class).newInstance(spool.getAbsolutePath());
    }
  }

  /** A file or a folder of files in the side's own folder, as fuzzed content. */
  private static java.io.File localSource(FuzzedDataProvider data, Class<?> type) throws Exception
  {
    java.io.File src = new java.io.File(CommonRecipes.SideFolder.of(data, type), "src" + data.consumeInt(0, 3));
    if (data.consumeBoolean()) {
      src.mkdirs();
      for (int i = data.consumeInt(0, 2); i > 0; i--) {
        Files.write(new java.io.File(src, "part" + i).toPath(), data.consumeBytes(32));
      }
    } else {
      Files.write(src.toPath(), data.consumeBytes(32));
    }
    return src;
  }

  /** FileStatus, for FSUtil.copy's source: the local file system's status of a fresh file or folder. */
  static final class SourceStatus implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> path = Class.forName("org.apache.hadoop.fs.Path", true, loader);
      Object fs = new CommonRecipes.FileSystemRecipe().build(data,
          Class.forName("org.apache.hadoop.fs.FileSystem", true, loader));
      Object p = path.getConstructor(String.class).newInstance(localSource(data, type).getAbsolutePath());
      return fs.getClass().getMethod("getFileStatus", path).invoke(fs, p);
    }
  }

  /** A local destination folder for FSUtil.copyToLocalFileSystem, in the side's own folder. */
  static final class DestinationFolder implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return new java.io.File(CommonRecipes.SideFolder.of(data, type),
          data.pickValue(new String[] {"dst", "dst/sub", "dst/a/b"})).getAbsolutePath();
    }
  }

  /** A destination file name for FSUtil.copyToLocalFileSystem. */
  static final class DestinationName implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      return data.pickValue(new String[] {"out", "out.txt", "copy"});
    }
  }

  /** The file FSUtil.copyToLocalFileSystem copies: a fresh one, or now and then a missing one. */
  static final class SourceFile implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      java.io.File src = data.consumeInt(0, 7) == 0
          ? new java.io.File(CommonRecipes.SideFolder.of(data, type), "missing")
          : localSource(data, type);
      return src.getAbsolutePath();
    }
  }

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
  static final class PluginDispatcherRecipe implements Recipe
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
        plugins.add(Proxy.newProxyInstance(loader, new Class<?>[] {pluginType}, new CommonRecipes.ServletRecipes.Handler()
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
      return Proxy.newProxyInstance(loader, new Class<?>[] {t}, new CommonRecipes.ServletRecipes.Handler()
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
  static final class PluginContextRecipe implements Recipe
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
          new CommonRecipes.ServletRecipes.Handler()
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

  /**
   * BaseSignatureVisitor: visitTypeArgument(char) starts by popping the type a previous call pushed,
   * so on a fresh visitor it always threw EmptyStackException before doing anything. This makes a
   * FieldSignatureVisitor (a concrete subclass) and replays the calls a real signature parse makes
   * first: a class type, and sometimes a type argument, which leaves a parameterized type on top.
   * Now and then the visitor stays fresh, for the empty-stack case.
   */
  static final class SignatureVisitorRecipe implements Recipe
  {
    private static final String[] CLASS_TYPES = {"java/util/List", "java/util/Map", "java/lang/String"};

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> visitorType = Class.forName("com.datatorrent.stram.webapp.asm.FieldSignatureVisitor",
          true, type.getClassLoader());
      Object visitor = visitorType.getConstructor().newInstance();
      // Fresh only on the highest value: a used-up input reads as 0 and should get a primed visitor.
      int steps = data.consumeInt(0, 2);
      if (steps < 2) {
        String classType = CLASS_TYPES[data.consumeInt(0, CLASS_TYPES.length - 1)];
        visitorType.getMethod("visitClassType", String.class).invoke(visitor, classType);
      }
      if (steps == 1) {
        // '=' is SignatureVisitor.INSTANCEOF: leaves a ParameterizedTypeNode on top of the stack.
        visitorType.getMethod("visitTypeArgument", char.class).invoke(visitor, '=');
      }
      return visitor;
    }
  }

  /**
   * YarnAppLauncherImpl.YarnAppHandleImpl(ApplicationId, Configuration): an inner class holding a
   * real YARN client. The configuration points the client at a closed local port with retries off,
   * so every YARN call fails within milliseconds instead of waiting for a ResourceManager. That
   * failure is what shutdownApp(handle, KILL) handles, and the refactoring changed how.
   */
  static final class YarnAppHandleRecipe implements Recipe
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

  /**
   * For GroupingManager.moveOperatorFromUndeployListToDeployList(PTOperator): a PTOperator only
   * exists inside a physical plan, and the method only does something when grouping requests list
   * that operator.
   */
  static final class GroupingRecipes
  {
    private GroupingRecipes() {}

    /** The single operator (id 1) of the one-operator application from PluginContextRecipe. */
    static final class Operator implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        Object manager = PluginContextRecipe.manager(type.getClassLoader());
        Object plan = manager.getClass().getMethod("getPhysicalPlan").invoke(manager);
        Map<?, ?> operators = (Map<?, ?>) plan.getClass().getMethod("getAllOperators").invoke(plan);
        return operators.values().iterator().next();
      }
    }

    /**
     * A GroupingManager with 0-3 grouping requests, each listing operator 1 or another operator for
     * undeployment. A used-up input reads as 0 everywhere, which gives three requests that all list
     * operator 1: the case where moving it in every request and moving it in the first one differ.
     */
    static final class Manager implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        Object manager = type.getConstructor().newInstance();
        @SuppressWarnings("unchecked")
        Map<String, Object> requests =
            (Map<String, Object>) type.getMethod("getGroupingRequests").invoke(manager);
        Class<?> requestType = Class.forName("org.apache.apex.engine.events.grouping.GroupingRequest",
            true, type.getClassLoader());
        int n = 3 - data.consumeInt(0, 3);
        for (int i = 0; i < n; i++) {
          Object request = requestType.getConstructor().newInstance();
          int operatorId = data.consumeBoolean() ? 2 : 1;
          requestType.getMethod("addOperatorToUndeploy", int.class).invoke(request, operatorId);
          requests.put("container-" + i, request);
        }
        return manager;
      }
    }
  }

  /**
   * StablePriorityQueue: every constructor ends in new PriorityQueue(capacity, ...), which rejects a
   * capacity below 1, and a short input gives 0 (or an empty collection), so the queue was rarely
   * built (toArray: 0 of 233). This makes one with capacity 16 and adds 0-5 Integers, which sort
   * naturally. A used-up input reads as 0 everywhere: five equal elements, the case a stable queue
   * exists for.
   */
  static final class StablePriorityQueueRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object queue = type.getConstructor(int.class).newInstance(16);
      int n = 5 - data.consumeInt(0, 5);
      for (int i = 0; i < n; i++) {
        type.getMethod("add", Object.class).invoke(queue, data.consumeInt(0, 9));
      }
      return queue;
    }
  }

  /**
   * StramWSFilter is set up by init(FilterConfig), which also starts the token manager's background
   * threads: once per input, that would start thousands of threads. This sets the same fields
   * directly: the proxy host is 127.0.0.1 (an IP, so no DNS lookup) and the token manager is created
   * but not started. Without a started manager there is no signing key, so createClientToken fails
   * the same way on both sides.
   */
  static final class StramWSFilterRecipe implements Recipe
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

  /** Builds a valid SerializedData slice instead of fuzzing unrelated offset and size values. */
  static final class SerializedDataRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      byte[] array = new byte[data.consumeInt(1, 64)];
      for (int i = 0; i < array.length; i++) {
        array[i] = data.consumeByte();
      }
      return type.getConstructor(byte[].class, int.class, int.class)
          .newInstance(array, 0, array.length);
    }
  }

  /**
   * SubscribeRequestTuple(byte[] array, int offset, int length) parses the buffer in its
   * constructor, so offset and length must fit the array. Built independently from fuzz bytes they
   * almost never do, and the receiver was never built. This builds a well-formed request with the
   * class's own serializer (identical on both sides) from fuzzed field values.
   */
  static final class SubscribeRequestTupleRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      String version = data.consumeAsciiString(16);
      String id = data.consumeAsciiString(16);
      String downType = data.consumeAsciiString(16);
      String upstreamId = data.consumeAsciiString(16);
      int mask = data.consumeInt();
      List<Integer> partitions = new ArrayList<>();
      int n = data.consumeInt(0, 4);
      for (int i = 0; i < n; i++) {
        partitions.add(data.consumeInt());
      }
      long startingWindowId = data.consumeLong();
      int bufferSize = data.consumeInt();

      byte[] bytes = (byte[]) type.getMethod("getSerializedRequest", String.class, String.class,
          String.class, String.class, int.class, Collection.class, long.class, int.class)
          .invoke(null, version, id, downType, upstreamId, mask, partitions, startingWindowId,
              bufferSize);
      return type.getConstructor(byte[].class, int.class, int.class)
          .newInstance(bytes, 0, bytes.length);
    }
  }

  /**
   * PhysicalNode(WriteOnlyClient client) is a buffer-server subscriber. The generic tiers built it
   * with a null client, and PhysicalNode.hashCode() is client.hashCode(), so merely adding the node
   * to a Set threw NullPointerException inside the engine (LeastBusy/RandomOne.distribute).
   *
   * <p>This builds it around a real, unconnected WriteOnlyClient from the netlet library.
   * send() on such a client needs no socket: it queues the slice and returns true until the send
   * queue is full, then false. The queue capacity and how many slices are already queued are
   * fuzzed, so PhysicalNode.send() reaches both its "sent" path and its "blocked" path.
   */
  static final class PhysicalNodeRecipe implements Recipe
  {
    private static final String CLIENT = "com.datatorrent.netlet.WriteOnlyClient";

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      // Same loader as the node, so the client is an instance of the parameter type on this side.
      Class<?> clientType = Class.forName(CLIENT, true, type.getClassLoader());

      int capacity = data.consumeInt(1, 8);   // send-queue slots (rounded up to a power of two)
      Object client = clientType.getConstructor(int.class, int.class).newInstance(1024, capacity);

      int alreadyQueued = data.consumeInt(0, 8);
      Method send = clientType.getMethod("send", byte[].class, int.class, int.class);
      byte[] slice = {0};
      for (int i = 0; i < alreadyQueued; i++) {
        send.invoke(client, slice, 0, slice.length);
      }

      Constructor<?> ctor = type.getConstructor(clientType);
      return ctor.newInstance(client);
    }
  }

  /**
   * RecoverableRpcProxy(String appPath, Configuration conf) reads the server address from the file
   * appPath/recovery/heartbeatUri. With a random appPath that file never exists, so the proxy was
   * never built. No Hadoop cluster is needed: Hadoop's local file system reads the file, and
   * RPC.getProxy does not connect until a call is made.
   *
   * <p>The address file is written twice. The constructor reads the first address; the second one,
   * with fuzzed query parameters, makes the next connect() see a new address and parse it (the code
   * the refactoring moved into parseQueryParameters). With the same address, connect() returns
   * straight away.
   */
  static final class RecoverableRpcProxyRecipe implements Recipe
  {
    /** One folder per side, so the two sides never read each other's address file. */
    private static final Map<ClassLoader, String> APP_DIRS = new HashMap<>();

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> confType = Class.forName("org.apache.hadoop.conf.Configuration", true, loader);
      Class<?> handlerType = Class.forName("com.datatorrent.stram.FSRecoveryHandler", true, loader);

      Object conf = confType.getConstructor().newInstance();
      // Fail a refused connection at once instead of after Hadoop's 10 one-second retries.
      confType.getMethod("setInt", String.class, int.class)
          .invoke(conf, "ipc.client.connect.max.retries", 0);

      String appDir = appDir(loader);
      Object handler = handlerType.getConstructor(String.class, confType).newInstance(appDir, conf);
      handlerType.getMethod("writeConnectUri", String.class)
          .invoke(handler, "stram://localhost:1/?retryTimeoutMillis=0");

      Object proxy = type.getConstructor(String.class, confType).newInstance(appDir, conf);

      String query = param(data, "rpcTimeout") + param(data, "retryTimeoutMillis")
          + param(data, "retryDelayMillis");
      handlerType.getMethod("writeConnectUri", String.class)
          .invoke(handler, "stram://localhost:2/?" + query);
      return proxy;
    }

    /** "name=value&" with a small fuzzed value, or "" so the parameter is sometimes missing. */
    private static String param(FuzzedDataProvider data, String name)
    {
      return data.consumeBoolean() ? name + "=" + data.consumeInt(0, 100) + "&" : "";
    }

    private static synchronized String appDir(ClassLoader loader) throws Exception
    {
      String dir = APP_DIRS.get(loader);
      if (dir == null) {
        dir = Files.createTempDirectory("rpcproxy").toString();
        APP_DIRS.put(loader, dir);
      }
      return dir;
    }
  }

  /**
   * RequestFactory.RequestDelegate: the only implementation is a private inner class of
   * TupleRecorderCollection, which the engine cannot create. RequestFactory.registerDelegate only
   * stores the delegate (and prints it when replacing one), so a stand-in is enough.
   */
  static final class RequestDelegateRecipe implements Recipe
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

  /**
   * MetricAggregatorMeta(Aggregator, DimensionsScheme) takes two Apex API interfaces the engine has
   * no implementation for, so it was never built. Its getters only use the DimensionsScheme, and
   * behave differently when it is null, so the scheme is sometimes null and otherwise a stand-in
   * returning fuzzed strings. The aggregator is never used by them and stays null.
   */
  static final class MetricAggregatorMetaRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> aggregatorType = Class.forName("com.datatorrent.api.AutoMetric$Aggregator", true, loader);
      Class<?> schemeType = Class.forName("com.datatorrent.api.AutoMetric$DimensionsScheme", true, loader);

      Object scheme = null;
      if (data.consumeBoolean()) {
        final String[] values = new String[data.consumeInt(0, 3)];
        for (int i = 0; i < values.length; i++) {
          values[i] = data.consumeAsciiString(8);
        }
        scheme = Proxy.newProxyInstance(loader, new Class<?>[] {schemeType}, new InvocationHandler()
        {
          @Override
          public Object invoke(Object proxy, Method m, Object[] args)
          {
            // getTimeBuckets() and getDimensionAggregationsFor(name) both return a String[].
            return m.getReturnType() == String[].class ? values.clone() : null;
          }
        });
      }

      Constructor<?> c = type.getDeclaredConstructor(aggregatorType, schemeType);
      c.setAccessible(true); // the constructor is protected
      return c.newInstance(null, scheme);
    }
  }

  /**
   * FSAgent(FileSystem fs): left to the engine, fs was an uninitialised HarFileSystem or null, so
   * every call threw NullPointerException on its first line. This gives it Hadoop's local file
   * system, without checksums (a file written by a test would otherwise need a matching .crc file).
   */
  static final class FSAgentRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> confType = Class.forName("org.apache.hadoop.conf.Configuration", true, loader);
      Class<?> fsType = Class.forName("org.apache.hadoop.fs.FileSystem", true, loader);

      Object conf = confType.getConstructor().newInstance();
      Object localFs = fsType.getMethod("getLocal", confType).invoke(null, conf);
      Object rawFs = localFs.getClass().getMethod("getRaw").invoke(localFs);
      return type.getConstructor(fsType).newInstance(rawFs);
    }
  }

  /**
   * Journal(StreamingContainerManager scm) needs the whole application master, which the engine
   * cannot build. Journal only stores scm and write() never uses it (only replay() does), so null
   * is enough. A new Journal has no output stream and write() then skips writing, so most journals
   * get an in-memory stream; the rest keep the "output stream is null" path in the input space.
   */
  static final class JournalRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> scmType = Class.forName("com.datatorrent.stram.StreamingContainerManager", false,
          type.getClassLoader());
      Object journal = type.getConstructor(scmType).newInstance((Object) null);
      if (data.consumeInt(0, 7) != 0) {
        type.getMethod("setOutputStream", OutputStream.class)
            .invoke(journal, new ByteArrayOutputStream());
      }
      return journal;
    }
  }

  /**
   * Journal.Recoverable: Journal.write() accepts only the operation classes it has registered, and
   * all of them are private nested classes with private constructors, so the engine never found
   * one. This calls those constructors with fuzzed values. SetContainerState is left out: its only
   * useful constructor needs a whole PTContainer. Now and then it returns an unregistered stand-in,
   * which covers write()'s "Class not registered" path.
   */
  static final class RecoverableRecipe implements Recipe
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
  static final class DefaultCallbackHandlerRecipe implements Recipe
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
}
