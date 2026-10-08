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
    BY_CLASS.put("java.net.InetSocketAddress", new InetSocketAddressRecipe());
    BY_CLASS.put("java.io.DataInput", new DataInputRecipe());
    BY_CLASS.put("java.lang.reflect.Type", new TypeRecipe());
    BY_CLASS.put("java.util.Comparator", new ComparatorRecipe());
    BY_CLASS.put("java.lang.annotation.Annotation", new AnnotationRecipe());
    BY_CLASS.put("javax.enterprise.inject.spi.Bean", new BeanRecipe());
    BY_CLASS.put("java.lang.ClassLoader", new ClassLoaderRecipe());
    BY_CLASS.put("java.lang.reflect.Method", new MemberRecipes.Method());
    BY_CLASS.put("java.lang.reflect.Constructor", new MemberRecipes.Constructor());
    BY_CLASS.put("org.quartz.spi.TriggerFiredBundle", new QuartzRecipes.Bundle());
    BY_CLASS.put("org.quartz.Scheduler", new QuartzRecipes.Scheduler());
    BY_CLASS.put("javax.enterprise.inject.spi.AnnotatedType", new CdiRecipes.AnnotatedType());
    BY_CLASS.put("javax.enterprise.inject.spi.BeforeBeanDiscovery",
        new CdiRecipes.BeforeBeanDiscovery());
    BY_CLASS.put("javax.enterprise.inject.spi.ProcessAnnotatedType",
        new CdiRecipes.ProcessAnnotatedType());
    BY_CLASS.put("org.apache.deltaspike.core.spi.config.view.ViewConfigNode",
        new ViewConfigNodeRecipe());
    BY_CLASS.put("javax.servlet.ServletRequestEvent", new ServletRecipes.RequestEvent());
    BY_CLASS.put("javax.interceptor.InvocationContext", new InterceptorRecipes.Context());
    BY_CLASS.put("org.apache.deltaspike.core.impl.throttling.Invoker", new InterceptorRecipes.Invoker());
    BY_CLASS.put("org.apache.deltaspike.core.impl.scope.conversation.InjectableGroupedConversation",
        new InjectableRecipes.Conversation());
    BY_CLASS.put("org.apache.deltaspike.core.impl.scope.conversation.InjectableGroupedConversationManager",
        new InjectableRecipes.ConversationManager());
    BY_CLASS.put("org.apache.deltaspike.core.impl.scope.window.InjectableWindowContext",
        new InjectableRecipes.WindowContext());
    BY_CLASS.put("org.apache.deltaspike.core.impl.scope.viewaccess.InjectableViewAccessContextManager",
        new InjectableRecipes.ViewAccessContextManager());
    BY_CLASS.put("org.apache.deltaspike.jsf.impl.injection.DependentBeanEntry",
        new DeltaSpikeRecipes.DependentBeanEntry());
    BY_CLASS.put("org.apache.deltaspike.data.impl.meta.RepositoryMetadata",
        new DeltaSpikeRecipes.RepositoryMetadata());
    BY_CLASS.put("org.apache.deltaspike.core.impl.jmx.BroadcasterProducer",
        new DeltaSpikeRecipes.BroadcasterProducer());
    BY_CLASS.put("javax.enterprise.inject.spi.InjectionPoint", new DeltaSpikeRecipes.InjectionPoint());
    BY_CLASS.put("org.apache.deltaspike.core.impl.scope.viewaccess.ViewAccessContextArtifactProducer",
        new InjectedRecipes.Fields("deltaSpikeContextExtension"));
    BY_CLASS.put("org.apache.deltaspike.scheduler.impl.SchedulerProducer",
        new InjectedRecipes.Fields("schedulerExtension"));
    BY_CLASS.put("org.apache.deltaspike.core.impl.scope.window.DefaultWindowContextQuotaHandler",
        new InjectedRecipes.WindowQuotaHandler());
    BY_CLASS.put("org.apache.deltaspike.security.impl.extension.AuthorizationParameter",
        new InjectedRecipes.AuthorizationParameter());
    BY_CLASS.put("org.apache.deltaspike.jsf.impl.config.view.navigation.NavigationParameterInterceptor",
        new InterceptorRecipes.WithStrategy("navigationParameterStrategy"));
    BY_CLASS.put("org.apache.deltaspike.jsf.impl.config.view.navigation.NavigationParameterListInterceptor",
        new InterceptorRecipes.WithStrategy("navigationParameterStrategy"));
    BY_CLASS.put("org.apache.deltaspike.core.util.interceptor.AbstractInvocationContext",
        new InvocationContextRecipe());
    BY_CLASS.put("org.apache.apex.engine.plugin.AbstractApexPluginDispatcher",
        new PluginDispatcherRecipe());
    BY_CLASS.put("org.apache.apex.engine.plugin.AbstractDAGExecutionPluginContext",
        new PluginContextRecipe());
    BY_CLASS.put("com.datatorrent.stram.webapp.asm.BaseSignatureVisitor",
        new SignatureVisitorRecipe());
    BY_CLASS.put("org.apache.apex.engine.YarnAppLauncherImpl$YarnAppHandleImpl",
        new YarnAppHandleRecipe());
    BY_CLASS.put("org.apache.apex.engine.YarnAppLauncherImpl", new YarnAppHandleRecipe.Launcher());
    BY_CLASS.put("com.datatorrent.stram.plan.physical.PTOperator", new GroupingRecipes.Operator());
    BY_CLASS.put("com.datatorrent.stram.util.StablePriorityQueue", new StablePriorityQueueRecipe());
    BY_CLASS.put("org.apache.apex.engine.events.grouping.GroupingManager",
        new GroupingRecipes.Manager());
    BY_CLASS.put("javax.servlet.ServletRequest", new ServletRecipes.Request());
    BY_CLASS.put("javax.servlet.ServletResponse", new ServletRecipes.Response());
    BY_CLASS.put("javax.servlet.FilterChain", new ServletRecipes.Chain());
    BY_CLASS.put("com.datatorrent.stram.security.StramWSFilter", new StramWSFilterRecipe());
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
    BY_CLASS.put("org.apache.hadoop.fs.FileSystem", new FileSystemRecipe());
    BY_CLASS.put("com.datatorrent.stram.Journal", new JournalRecipe());
    BY_CLASS.put("com.datatorrent.stram.Journal$Recoverable", new RecoverableRecipe());
    BY_CLASS.put("com.datatorrent.common.security.auth.callback.DefaultCallbackHandler",
        new DefaultCallbackHandlerRecipe());
    BY_CLASS.put("org.apache.skywalking.oap.server.configuration.etcd.EtcdConfigurationProvider",
        new ConfigProviderRecipe("endpoints", "http://127.0.0.1:1"));  }

  /** The recipe for this class name, or null if there is none. */
  public static Recipe forClass(String name)
  {
    return BY_CLASS.get(name);
  }
}
