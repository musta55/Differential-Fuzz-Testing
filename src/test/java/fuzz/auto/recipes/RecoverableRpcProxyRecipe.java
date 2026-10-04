package fuzz.auto.recipes;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

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
final class RecoverableRpcProxyRecipe implements Recipe
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
