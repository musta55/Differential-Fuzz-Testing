package fuzz.auto.recipes;

import java.util.HashMap;
import java.util.Map;

/** Registry of {@link Recipe}s by fully qualified class name. Add new recipes here. */
public final class Recipes
{
  private Recipes() {}

  private static final Map<String, Recipe> BY_CLASS = new HashMap<>();

  static {
    BY_CLASS.put("javax.security.auth.callback.Callback", new CallbackRecipe());
    BY_CLASS.put("java.lang.Runnable", new RunnableRecipe());
    BY_CLASS.put("com.datatorrent.bufferserver.util.SerializedData",
      new SerializedDataRecipe());
    BY_CLASS.put("com.datatorrent.bufferserver.packet.SubscribeRequestTuple",
        new SubscribeRequestTupleRecipe());
    BY_CLASS.put("com.datatorrent.bufferserver.internal.PhysicalNode", new PhysicalNodeRecipe());
    BY_CLASS.put("com.datatorrent.stram.RecoverableRpcProxy", new RecoverableRpcProxyRecipe());
    BY_CLASS.put("com.datatorrent.stram.api.RequestFactory$RequestDelegate",
        new RequestDelegateRecipe());
    BY_CLASS.put("com.datatorrent.stram.plan.logical.MetricAggregatorMeta",
        new MetricAggregatorMetaRecipe());
    BY_CLASS.put("com.datatorrent.stram.client.FSAgent", new FSAgentRecipe());
    BY_CLASS.put("org.apache.hadoop.fs.Path", new PathRecipe());
    BY_CLASS.put("com.datatorrent.stram.Journal", new JournalRecipe());
    BY_CLASS.put("com.datatorrent.stram.Journal$Recoverable", new RecoverableRecipe());
    BY_CLASS.put("com.datatorrent.common.security.auth.callback.DefaultCallbackHandler",
        new DefaultCallbackHandlerRecipe());
  }

  /** The recipe for this class name, or null if there is none. */
  public static Recipe forClass(String name)
  {
    return BY_CLASS.get(name);
  }
}
