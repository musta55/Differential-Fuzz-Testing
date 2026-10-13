package fuzz.auto.recipes;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry of {@link Recipe}s: by fully qualified class name, and by method parameter. Add new
 * recipes here, in the section of the project they were written for; the recipes themselves live
 * in that project's file (CommonRecipes for JDK and shared-library types).
 */
public final class Recipes
{
  private Recipes() {}

  private static final Map<String, Recipe> BY_CLASS = new HashMap<>();

  static {
    // ---- common: JDK and shared-library types, used by any project ----
    BY_CLASS.put("javax.security.auth.callback.Callback", new CommonRecipes.CallbackRecipe());
    BY_CLASS.put("java.lang.Runnable", new CommonRecipes.RunnableRecipe());
    BY_CLASS.put("java.net.InetSocketAddress", new CommonRecipes.InetSocketAddressRecipe());
    BY_CLASS.put("java.io.DataInput", new CommonRecipes.DataInputRecipe());
    BY_CLASS.put("java.nio.ByteBuffer", new CommonRecipes.ByteBufferRecipe());
    BY_CLASS.put("java.awt.Component", new CommonRecipes.SwingRecipes.Component());
    BY_CLASS.put("javax.swing.JComponent", new CommonRecipes.SwingRecipes.Component());
    BY_CLASS.put("javax.swing.text.JTextComponent", new CommonRecipes.SwingRecipes.Component());
    BY_CLASS.put("java.awt.Graphics", new CommonRecipes.SwingRecipes.Graphics());
    BY_CLASS.put("java.awt.event.KeyEvent", new CommonRecipes.SwingRecipes.Key());
    BY_CLASS.put("java.awt.event.FocusEvent", new CommonRecipes.SwingRecipes.Focus());
    BY_CLASS.put("org.w3c.dom.Node", new CommonRecipes.XmlRecipe());
    BY_CLASS.put("org.w3c.dom.Document", new CommonRecipes.XmlRecipe());
    BY_CLASS.put("java.net.URL", new CommonRecipes.UrlRecipe());
    BY_CLASS.put("org.ietf.jgss.Oid", new CommonRecipes.OidRecipe());
    BY_CLASS.put("com.thoughtworks.xstream.mapper.Mapper", new CommonRecipes.XStreamMapperRecipe());
    BY_CLASS.put("com.github.benmanes.caffeine.cache.Cache", new CommonRecipes.CaffeineCacheRecipe());
    BY_CLASS.put("java.lang.reflect.Type", new CommonRecipes.TypeRecipe());
    BY_CLASS.put("java.util.Comparator", new CommonRecipes.ComparatorRecipe());
    BY_CLASS.put("java.lang.annotation.Annotation", new CommonRecipes.AnnotationRecipe());
    BY_CLASS.put("java.lang.ClassLoader", new CommonRecipes.ClassLoaderRecipe());
    BY_CLASS.put("java.lang.reflect.Method", new CommonRecipes.MemberRecipes.Method());
    BY_CLASS.put("java.lang.reflect.Constructor", new CommonRecipes.MemberRecipes.Constructor());
    BY_CLASS.put("javax.enterprise.inject.spi.Bean", new CommonRecipes.BeanRecipe());
    BY_CLASS.put("javax.enterprise.inject.spi.AnnotatedType", new CommonRecipes.CdiRecipes.AnnotatedType());
    BY_CLASS.put("javax.enterprise.inject.spi.BeforeBeanDiscovery",
        new CommonRecipes.CdiRecipes.BeforeBeanDiscovery());
    BY_CLASS.put("javax.enterprise.inject.spi.ProcessAnnotatedType",
        new CommonRecipes.CdiRecipes.ProcessAnnotatedType());
    BY_CLASS.put("org.quartz.spi.TriggerFiredBundle", new CommonRecipes.QuartzRecipes.Bundle());
    BY_CLASS.put("org.quartz.Scheduler", new CommonRecipes.QuartzRecipes.Scheduler());
    BY_CLASS.put("javax.servlet.ServletRequestEvent", new CommonRecipes.ServletRecipes.RequestEvent());
    BY_CLASS.put("javax.servlet.ServletRequest", new CommonRecipes.ServletRecipes.Request());
    BY_CLASS.put("javax.servlet.ServletResponse", new CommonRecipes.ServletRecipes.Response());
    BY_CLASS.put("javax.servlet.FilterChain", new CommonRecipes.ServletRecipes.Chain());
    BY_CLASS.put("org.apache.hadoop.fs.Path", new CommonRecipes.PathRecipe());
    BY_CLASS.put("org.apache.hadoop.fs.FileSystem", new CommonRecipes.FileSystemRecipe());

    // ---- apex-core ----
    BY_CLASS.put("org.apache.apex.engine.plugin.AbstractApexPluginDispatcher",
        new ApexRecipes.PluginDispatcherRecipe());
    BY_CLASS.put("org.apache.apex.engine.plugin.AbstractDAGExecutionPluginContext",
        new ApexRecipes.PluginContextRecipe());
    BY_CLASS.put("com.datatorrent.stram.webapp.asm.BaseSignatureVisitor",
        new ApexRecipes.SignatureVisitorRecipe());
    BY_CLASS.put("org.apache.apex.engine.YarnAppLauncherImpl$YarnAppHandleImpl",
        new ApexRecipes.YarnAppHandleRecipe());
    BY_CLASS.put("org.apache.apex.engine.YarnAppLauncherImpl", new ApexRecipes.YarnAppHandleRecipe.Launcher());
    BY_CLASS.put("com.datatorrent.stram.plan.physical.PTOperator", new ApexRecipes.GroupingRecipes.Operator());
    BY_CLASS.put("com.datatorrent.stram.util.StablePriorityQueue", new ApexRecipes.StablePriorityQueueRecipe());
    BY_CLASS.put("org.apache.apex.engine.events.grouping.GroupingManager",
        new ApexRecipes.GroupingRecipes.Manager());
    BY_CLASS.put("com.datatorrent.stram.security.StramWSFilter", new ApexRecipes.StramWSFilterRecipe());
    BY_CLASS.put("com.datatorrent.bufferserver.util.SerializedData", new ApexRecipes.SerializedDataRecipe());
    BY_CLASS.put("com.datatorrent.bufferserver.packet.SubscribeRequestTuple",
        new ApexRecipes.SubscribeRequestTupleRecipe());
    BY_CLASS.put("com.datatorrent.bufferserver.internal.PhysicalNode", new ApexRecipes.PhysicalNodeRecipe());
    BY_CLASS.put("com.datatorrent.stram.RecoverableRpcProxy", new ApexRecipes.RecoverableRpcProxyRecipe());
    BY_CLASS.put("com.datatorrent.stram.api.RequestFactory$RequestDelegate",
        new ApexRecipes.RequestDelegateRecipe());
    BY_CLASS.put("com.datatorrent.stram.plan.logical.MetricAggregatorMeta",
        new ApexRecipes.MetricAggregatorMetaRecipe());
    BY_CLASS.put("com.datatorrent.stram.client.FSAgent", new ApexRecipes.FSAgentRecipe());
    BY_CLASS.put("com.datatorrent.stram.Journal", new ApexRecipes.JournalRecipe());
    BY_CLASS.put("com.datatorrent.stram.Journal$Recoverable", new ApexRecipes.RecoverableRecipe());
    BY_CLASS.put("com.datatorrent.common.security.auth.callback.DefaultCallbackHandler",
        new ApexRecipes.DefaultCallbackHandlerRecipe());
    BY_CLASS.put("com.datatorrent.bufferserver.storage.DiskStorage", new ApexRecipes.DiskStorageRecipe());

    // ---- deltaspike ----
    BY_CLASS.put("org.apache.deltaspike.core.spi.config.view.ViewConfigNode",
        new DeltaSpikeRecipes.ViewConfigNodeRecipe());
    BY_CLASS.put("javax.interceptor.InvocationContext", new DeltaSpikeRecipes.InterceptorRecipes.Context());
    BY_CLASS.put("org.apache.deltaspike.core.impl.throttling.Invoker", new DeltaSpikeRecipes.InterceptorRecipes.Invoker());
    BY_CLASS.put("org.apache.deltaspike.core.impl.scope.conversation.InjectableGroupedConversation",
        new DeltaSpikeRecipes.InjectableRecipes.Conversation());
    BY_CLASS.put("org.apache.deltaspike.core.impl.scope.conversation.InjectableGroupedConversationManager",
        new DeltaSpikeRecipes.InjectableRecipes.ConversationManager());
    BY_CLASS.put("org.apache.deltaspike.core.impl.scope.window.InjectableWindowContext",
        new DeltaSpikeRecipes.InjectableRecipes.WindowContext());
    BY_CLASS.put("org.apache.deltaspike.core.impl.scope.viewaccess.InjectableViewAccessContextManager",
        new DeltaSpikeRecipes.InjectableRecipes.ViewAccessContextManager());
    BY_CLASS.put("org.apache.deltaspike.jsf.impl.injection.DependentBeanEntry",
        new DeltaSpikeRecipes.DependentBeanEntry());
    BY_CLASS.put("org.apache.deltaspike.data.impl.meta.RepositoryMetadata",
        new DeltaSpikeRecipes.RepositoryMetadata());
    BY_CLASS.put("org.apache.deltaspike.core.impl.jmx.BroadcasterProducer",
        new DeltaSpikeRecipes.BroadcasterProducer());
    BY_CLASS.put("javax.enterprise.inject.spi.InjectionPoint", new DeltaSpikeRecipes.InjectionPoint());
    BY_CLASS.put("org.apache.deltaspike.core.impl.scope.viewaccess.ViewAccessContextArtifactProducer",
        new DeltaSpikeRecipes.InjectedRecipes.Fields("deltaSpikeContextExtension"));
    BY_CLASS.put("org.apache.deltaspike.scheduler.impl.SchedulerProducer",
        new DeltaSpikeRecipes.InjectedRecipes.Fields("schedulerExtension"));
    BY_CLASS.put("org.apache.deltaspike.core.impl.scope.window.DefaultWindowContextQuotaHandler",
        new DeltaSpikeRecipes.InjectedRecipes.WindowQuotaHandler());
    BY_CLASS.put("org.apache.deltaspike.security.impl.extension.AuthorizationParameter",
        new DeltaSpikeRecipes.InjectedRecipes.AuthorizationParameter());
    BY_CLASS.put("org.apache.deltaspike.jsf.impl.config.view.navigation.NavigationParameterInterceptor",
        new DeltaSpikeRecipes.InterceptorRecipes.WithStrategy("navigationParameterStrategy"));
    BY_CLASS.put("org.apache.deltaspike.jsf.impl.config.view.navigation.NavigationParameterListInterceptor",
        new DeltaSpikeRecipes.InterceptorRecipes.WithStrategy("navigationParameterStrategy"));
    BY_CLASS.put("org.apache.deltaspike.core.util.interceptor.AbstractInvocationContext",
        new DeltaSpikeRecipes.InvocationContextRecipe());
    BY_CLASS.put("org.apache.deltaspike.jpa.spi.descriptor.xml.EntityDescriptor",
        new DeltaSpikeRecipes.EntityDescriptor());

    // ---- jclouds ----
    BY_CLASS.put("org.jclouds.http.HttpRequest", new JcloudsRecipes.Request());
    BY_CLASS.put("org.jclouds.http.HttpResponse", new JcloudsRecipes.Response());

    // ---- jmeter ----
    BY_CLASS.put("org.apache.jmeter.extractor.json.render.RenderAsJmesPathRenderer",
        new JmeterRecipes.JsonRenderer("a", "a.b", "a[0]", "[*].a", "length(@)", "keys(@)"));
    BY_CLASS.put("org.apache.jmeter.extractor.json.render.RenderAsJsonRenderer",
        new JmeterRecipes.JsonRenderer("$", "$.a", "$.a.b", "$..a", "$[0]", "$.*"));

    // jmeter functions: built with their parameters set, as jmeter does before execute()
    for (String f : new String[] {"CharFunction", "DateTimeConvertFunction", "DigestEncodeFunction", "IsVarDefined",
        "LogFunction2", "Property", "RandomDate", "RandomFromMultipleVars", "SetProperty", "Variable", "MachineIP",
        "MachineName", "Groovy"}) {
      BY_CLASS.put("org.apache.jmeter.functions." + f, new JmeterRecipes.Function(false));
    }
    BY_CLASS.put("org.apache.jmeter.functions.StringToFile", new JmeterRecipes.Function(true));
    BY_CLASS.put("org.apache.jmeter.functions.AbstractHostIPName",
        new JmeterRecipes.Function(false, "org.apache.jmeter.functions.MachineName"));
    BY_CLASS.put("org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient",
        new JmeterRecipes.JavaSamplerClient());

    for (String c : new String[] {"ApdexSummaryConsumer", "ErrorsSummaryConsumer", "RequestsSummaryConsumer",
        "StatisticsSummaryConsumer", "Top5ErrorsBySamplerConsumer", "SampleWriterConsumer"}) {
      BY_CLASS.put("org.apache.jmeter.report.processor." + c, new JmeterRecipes.Consumer());
    }
    BY_CLASS.put("org.apache.jmeter.assertions.JSONPathAssertion", new JmeterRecipes.JsonPathAssertion());
    BY_CLASS.put("org.apache.jmeter.gui.util.PowerTableModel", new JmeterRecipes.TableModel());
    BY_CLASS.put("org.apache.jmeter.visualizers.RenderAsRegexp", new JmeterRecipes.RegexpRenderer());
    BY_CLASS.put("org.apache.jmeter.save.converters.SampleSaveConfigurationConverter",
        new JmeterRecipes.SaveService("converter"));
    BY_CLASS.put("org.apache.jmeter.control.InterleaveControl", new JmeterRecipes.Interleave());
    BY_CLASS.put("org.apache.jmeter.report.dashboard.TemplateVisitor", new JmeterRecipes.TemplateVisitor());
    BY_CLASS.put("org.apache.jmeter.functions.FileRowColContainer", new JmeterRecipes.RowColFile());
    BY_CLASS.put("org.apache.jmeter.protocol.smtp.sampler.protocol.LocalTrustStoreSSLSocketFactory",
        new JmeterRecipes.TrustStore());

    // ---- openmeetings ----
    BY_CLASS.put("org.apache.openmeetings.cli.CleanupUnit", new OpenmeetingsRecipes.CleanupUnit());

    // ---- skywalking ----
    BY_CLASS.put("org.apache.skywalking.oap.server.configuration.etcd.EtcdConfigurationProvider",
        new SkywalkingRecipes.ConfigProviderRecipe("endpoints", "http://127.0.0.1:1"));
    BY_CLASS.put("org.apache.skywalking.oap.server.configuration.apollo.ApolloConfigurationProvider",
        new SkywalkingRecipes.ConfigProviderRecipe("apolloCluster", "default", "apolloMeta", "http://127.0.0.1:1",
            "apolloEnv", "DEV", "appId", "skywalking", "namespace", "application"));
    // never both valid: valid settings start a live ZooKeeper client, whose state differs per run
    BY_CLASS.put("org.apache.skywalking.oap.server.configuration.zookeeper.ZookeeperConfigurationProvider",
        new SkywalkingRecipes.ConfigProviderRecipe(true, "hostPort", "127.0.0.1:1", "namespace", "/default"));

    // ---- systemds ----
    BY_CLASS.put("org.apache.sysds.lops.Lop", new SystemdsRecipes.Lop());
    BY_CLASS.put("org.apache.sysds.lops.LopProperties", new SystemdsRecipes.LopProperties());
    BY_CLASS.put("org.antlr.v4.runtime.ParserRuleContext", new SystemdsRecipes.ParserRuleContext());
    BY_CLASS.put("org.apache.sysds.lops.Sql", new SystemdsRecipes.Sql());
    BY_CLASS.put("org.apache.sysds.lops.CSVReBlock", new SystemdsRecipes.CSVReBlock());
    BY_CLASS.put("org.apache.sysds.parser.IterablePredicate", new SystemdsRecipes.IterablePredicate());
    BY_CLASS.put("org.apache.sysds.parser.BinaryExpression", new SystemdsRecipes.BinaryExpression());
    BY_CLASS.put("org.apache.sysds.parser.BooleanExpression", new SystemdsRecipes.BooleanExpression());
    BY_CLASS.put("org.apache.sysds.common.Types$FunctionBlock", new SystemdsRecipes.FunctionBlock());
    BY_CLASS.put("org.apache.sysds.hops.codegen.cplan.CNode", new SystemdsRecipes.CNodeData());
    BY_CLASS.put("org.apache.sysds.hops.codegen.cplan.CNodeData", new SystemdsRecipes.CNodeData());
    BY_CLASS.put("org.apache.sysds.hops.codegen.cplan.CNodeTpl", new SystemdsRecipes.CNodeMultiAgg());
    BY_CLASS.put("org.apache.sysds.hops.codegen.cplan.CNodeMultiAgg", new SystemdsRecipes.CNodeMultiAgg());
    BY_CLASS.put("org.apache.sysds.hops.codegen.cplan.CNodeOuterProduct", new SystemdsRecipes.CNodeOuterProduct());
    BY_CLASS.put("org.apache.sysds.runtime.data.DenseBlock", new SystemdsRecipes.DenseBlock("DenseBlockFP64"));
    BY_CLASS.put("org.apache.sysds.runtime.data.DenseBlockDRB", new SystemdsRecipes.DenseBlock("DenseBlockFP64"));
    BY_CLASS.put("org.apache.sysds.runtime.data.DenseBlockLDRB", new SystemdsRecipes.DenseBlock("DenseBlockLFP64"));
    for (String c : new String[] {"FP64", "FP32", "Bool", "Int32", "Int64", "String",
        "LFP64", "LFP32", "LBool", "LInt32", "LInt64", "LString"}) {
      BY_CLASS.put("org.apache.sysds.runtime.data.DenseBlock" + c, new SystemdsRecipes.DenseBlock(null));
    }
    BY_CLASS.put("org.apache.sysds.runtime.data.SparseRow", new SystemdsRecipes.SparseRow());
    BY_CLASS.put("org.apache.sysds.runtime.functionobjects.FunctionObject", new SystemdsRecipes.FunctionObject());
    BY_CLASS.put("org.apache.sysds.runtime.functionobjects.IndexFunction", new SystemdsRecipes.FunctionObject());
    BY_CLASS.put("org.apache.sysds.runtime.functionobjects.ValueFunction", new SystemdsRecipes.FunctionObject());
    BY_CLASS.put("org.apache.sysds.runtime.matrix.operators.ScalarOperator", new SystemdsRecipes.ScalarOperator());
    BY_CLASS.put("org.apache.sysds.runtime.meta.DataCharacteristics", new SystemdsRecipes.DataCharacteristics());
    BY_CLASS.put("org.apache.sysds.api.mlcontext.Metadata", new SystemdsRecipes.Metadata());
    BY_CLASS.put("org.apache.sysds.runtime.io.hdf5.H5ByteReader", new SystemdsRecipes.H5ByteReader());
    BY_CLASS.put("org.apache.sysds.runtime.frame.data.FrameBlock", new SystemdsRecipes.FrameBlock());
    BY_CLASS.put("org.apache.sysds.runtime.instructions.spark.data.FrameReblockBuffer",
        new SystemdsRecipes.FrameReblockBuffer());
    BY_CLASS.put("org.apache.sysds.runtime.compress.CompressionSettings", new SystemdsRecipes.CompressionSettings());
    BY_CLASS.put("org.apache.sysds.runtime.compress.estim.AComEst", new SystemdsRecipes.AComEst());
    BY_CLASS.put("org.apache.sysds.runtime.compress.estim.ComEstExact", new SystemdsRecipes.ComEstExact());
    BY_CLASS.put("org.apache.sysds.runtime.controlprogram.IfProgramBlock", new SystemdsRecipes.IfProgramBlock());
    BY_CLASS.put("org.apache.sysds.runtime.util.FastStringTokenizer", new SystemdsRecipes.FastStringTokenizer());
    BY_CLASS.put("org.apache.sysds.runtime.util.DependencyTask", new SystemdsRecipes.DependencyTask());
    BY_CLASS.put("org.apache.sysds.runtime.util.DependencyWrapperTask", new SystemdsRecipes.DependencyWrapperTask());
    BY_CLASS.put("org.apache.sysds.runtime.frame.data.columns.ABooleanArray", new SystemdsRecipes.BooleanColumn());
    BY_CLASS.put("org.apache.sysds.runtime.controlprogram.parfor.opt.OptimizerHeuristic",
        new SystemdsRecipes.OptimizerHeuristic());
    BY_CLASS.put("org.apache.sysds.runtime.controlprogram.parfor.ResultMergeLocalAutomatic",
        new SystemdsRecipes.ResultMerge());
    BY_CLASS.put("org.apache.sysds.runtime.compress.cocode.MemorizerV2", new SystemdsRecipes.MemorizerV2());
    BY_CLASS.put("org.apache.sysds.runtime.compress.cost.ACostEstimate", new SystemdsRecipes.ACostEstimate());
    BY_CLASS.put("org.apache.sysds.runtime.compress.io.CompressedWriteBlock", new SystemdsRecipes.CompressedWriteBlock());
    BY_CLASS.put("org.apache.sysds.runtime.compress.colgroup.indexes.IColIndex", new SystemdsRecipes.IColIndex());
    BY_CLASS.put("org.apache.sysds.runtime.compress.colgroup.indexes.IIterate", new SystemdsRecipes.IIterate());
    BY_CLASS.put("org.apache.sysds.runtime.compress.colgroup.AColGroupOffset", new SystemdsRecipes.ColGroupOffset());
    BY_CLASS.put("org.apache.sysds.runtime.compress.colgroup.ColGroupDDC", new SystemdsRecipes.ColGroupDDC());
    BY_CLASS.put("org.apache.sysds.runtime.compress.colgroup.dictionary.ACachingMBDictionary",
        new SystemdsRecipes.Dictionary());
    BY_CLASS.put("org.apache.sysds.runtime.compress.colgroup.dictionary.AIdentityDictionary",
        new SystemdsRecipes.IdentityDictionary());
    BY_CLASS.put("org.apache.sysds.runtime.data.DenseBlockLString", new SystemdsRecipes.DenseBlockLString());
    BY_CLASS.put("org.apache.sysds.runtime.data.DenseBlockLFP64DEDUP", new SystemdsRecipes.DenseBlockLFP64DEDUP());
    BY_CLASS.put("org.apache.sysds.runtime.compress.colgroup.scheme.ConstScheme", new SystemdsRecipes.ConstScheme());
    BY_CLASS.put("org.apache.sysds.runtime.compress.colgroup.scheme.ACLAScheme", new SystemdsRecipes.Scheme());
  }

  /**
   * Recipes for one parameter of one method ("class.method#index"): methods that declare Object
   * but reject everything except one type, so building the declared type never reached the body.
   */
  private static final Map<String, Recipe> BY_PARAMETER = new HashMap<>();

  static {
    // ---- apex-core: file-system helpers work in the side's own folder ----
    BY_PARAMETER.put("com.datatorrent.stram.util.FSUtil.copy#1", new ApexRecipes.SourceStatus());
    BY_PARAMETER.put("com.datatorrent.stram.util.FSUtil.copyToLocalFileSystem#1", new ApexRecipes.DestinationFolder());
    BY_PARAMETER.put("com.datatorrent.stram.util.FSUtil.copyToLocalFileSystem#2", new ApexRecipes.DestinationName());
    BY_PARAMETER.put("com.datatorrent.stram.util.FSUtil.copyToLocalFileSystem#3", new ApexRecipes.SourceFile());

    // ---- jclouds ----
    BY_PARAMETER.put("org.jclouds.atmos.functions.AtmosObjectName.apply#0", new JcloudsRecipes.AtmosObject());
    BY_PARAMETER.put("org.jclouds.s3.binders.BindS3ObjectMetadataToRequest.bindToRequest#1",
        new JcloudsRecipes.S3Object());
    BY_PARAMETER.put("org.jclouds.glacier.binders.BindContentRangeToHeaders.bindToRequest#1",
        new JcloudsRecipes.ContentRange());
    BY_PARAMETER.put("org.jclouds.cloudwatch.binders.AlarmNamesBinder.bindToRequest#1",
        new JcloudsRecipes.Names());

    // ---- jmeter ----
    BY_PARAMETER.put("org.apache.jmeter.assertions.gui.HTMLAssertionGui.configure#0",
        new JmeterRecipes.Element("org.apache.jmeter.assertions.HTMLAssertion"));
    BY_PARAMETER.put("org.apache.jmeter.assertions.gui.XPath2AssertionGui.configure#0",
        new JmeterRecipes.Element("org.apache.jmeter.assertions.XPath2Assertion"));
    BY_PARAMETER.put("org.apache.jmeter.modifiers.gui.CounterConfigGui.configure#0",
        new JmeterRecipes.Element("org.apache.jmeter.modifiers.CounterConfig"));
    BY_PARAMETER.put("org.apache.jmeter.protocol.system.gui.SystemSamplerGui.modifyTestElement#0",
        new JmeterRecipes.Element("org.apache.jmeter.protocol.system.SystemSampler"));

    BY_PARAMETER.put("org.apache.jmeter.report.processor.AbstractSummaryConsumer.consume#0", new JmeterRecipes.Sample());
    BY_PARAMETER.put("org.apache.jmeter.assertions.JSONPathAssertion.doAssert#0", new CommonRecipes.OneOf(
        "{\"a\":1}", "{\"a\":[1,2]}", "{\"a\":null}", "{\"a\":\"x\"}", "{\"b\":{\"a\":1}}", "[]", "{}", "1"));
    BY_PARAMETER.put("org.apache.jmeter.gui.util.PowerTableModel.removeColumn#0", new CommonRecipes.OneOf(0, 1, 2));
    // marshal(Object, writer, context) only ever gets a SampleSaveConfiguration, and SaveService's own
    // writer and context (any other object made XStream reflect into JDK classes, which Java 17 forbids)
    String converter = "org.apache.jmeter.save.converters.SampleSaveConfigurationConverter";
    BY_PARAMETER.put(converter + ".marshal#0",
        new JmeterRecipes.Element("org.apache.jmeter.samplers.SampleSaveConfiguration"));
    BY_PARAMETER.put(converter + ".marshal#1", new JmeterRecipes.SaveService("writer"));
    BY_PARAMETER.put(converter + ".marshal#2", new JmeterRecipes.SaveService("context"));
    // change listeners: a recorder of the calls made to them
    BY_PARAMETER.put("org.apache.jmeter.gui.util.FileListPanel.<init>#0",
        new CommonRecipes.Recorder("javax.swing.event.ChangeListener"));
    BY_PARAMETER.put("org.apache.jmeter.gui.util.FilePanelEntry.<init>#2",
        new CommonRecipes.Recorder("javax.swing.event.ChangeListener"));
    BY_PARAMETER.put("org.apache.jorphan.gui.ui.TextAreaUIWithUndo.createUI#0", new JmeterRecipes.TextComponent());
    BY_PARAMETER.put("org.apache.jmeter.visualizers.SearchTreePanel.searchInNode#0", new JmeterRecipes.Searcher());
    // a parameters file and, for the private overload, jmeter's own callback around HttpParams
    String httpParams = "org.apache.jmeter.protocol.http.sampler.HttpClientDefaultParameters.load#";
    BY_PARAMETER.put(httpParams + "0", new JmeterRecipes.HttpParamsFile());
    BY_PARAMETER.put(httpParams + "1", new JmeterRecipes.HttpParams());
    for (String m : new String[] {"preVisitDirectory", "visitFile"}) {
      BY_PARAMETER.put("org.apache.jmeter.report.dashboard.TemplateVisitor." + m + "#0", new JmeterRecipes.TemplatePath());
      BY_PARAMETER.put("org.apache.jmeter.report.dashboard.TemplateVisitor." + m + "#1",
          new JmeterRecipes.TemplateAttributes());
    }

    // ---- openmeetings: JSON text for fromString, an existing directory for CleanupEntityUnit ----
    BY_PARAMETER.put("org.apache.openmeetings.db.dto.user.ExternalUserDTO.fromString#0",
        new OpenmeetingsRecipes.JsonText(null,
            "email", "externalId", "externalType", "firstname", "lastname", "login", "profilePictureUrl"));
    BY_PARAMETER.put("org.apache.openmeetings.db.entity.server.RemoteSessionObject.fromString#0",
        new OpenmeetingsRecipes.JsonText(null,
            "username", "firstname", "lastname", "pictureUrl", "email", "externalId", "externalType"));
    BY_PARAMETER.put("org.apache.openmeetings.webservice.util.AppointmentParamConverter.fromString#0",
        new OpenmeetingsRecipes.JsonText("appointmentDTO",
            "id", "title", "location", "start", "end", "description", "icalId", "inserted", "updated",
            "deleted", "reminderEmailSend", "languageId", "passwordProtected", "password", "connectedEvent"));
    BY_PARAMETER.put("org.apache.openmeetings.webservice.util.UserParamConverter.fromString#0",
        new OpenmeetingsRecipes.JsonText("userDTO",
            "id", "login", "password", "firstname", "lastname", "email", "languageId", "timeZoneId",
            "externalId", "externalType", "pictureUri", "type"));
    BY_PARAMETER.put("org.apache.openmeetings.cli.CleanupEntityUnit.<init>#0", new OpenmeetingsRecipes.CleanupFolder());
    // a folder with files and a recording filter; a filter and visitor for the backup file tree
    BY_PARAMETER.put("org.apache.openmeetings.cli.CleanupHelper.list#0", new OpenmeetingsRecipes.CleanupFolder());
    BY_PARAMETER.put("org.apache.openmeetings.cli.CleanupHelper.list#1", new CommonRecipes.Recorder("java.io.FilenameFilter"));
    BY_CLASS.put("org.apache.openmeetings.backup.FileTree", new OpenmeetingsRecipes.FileTree());
    BY_PARAMETER.put("org.apache.openmeetings.backup.FileTree.process#0",
        new CommonRecipes.Recorder("java.util.function.Predicate"));
    BY_PARAMETER.put("org.apache.openmeetings.backup.FileTree.process#1",
        new CommonRecipes.Recorder("java.util.function.Consumer"));
    BY_PARAMETER.put("org.apache.openmeetings.cli.OmHelpFormatter.printUsage#3", new OpenmeetingsRecipes.HelpOptions());
    BY_CLASS.put("org.apache.openmeetings.service.calendar.caldav.methods.SyncMethod", new OpenmeetingsRecipes.SyncMethod());
    BY_PARAMETER.put("org.apache.openmeetings.service.calendar.caldav.methods.SyncMethod.getResponseBodyAsMultiStatus#0",
        new OpenmeetingsRecipes.DavResponse());

    // ---- systemds ----
    // Instruction texts: "CP°opcode°operand°...", an operand being name·DATATYPE·VALUETYPE[·literal].
    // These instructions are built by parsing a text (receiver) and run in an InstructionContext.
    instruction("cp.CentralMomentCPInstruction",
        "CP°cm°A·MATRIX·FP64°2·SCALAR·INT64·true°out·SCALAR·FP64°1",
        "CP°cm°A·MATRIX·FP64°3·SCALAR·INT64·true°out·SCALAR·FP64°1",
        "CP°cm°A·MATRIX·FP64°W·MATRIX·FP64°4·SCALAR·INT64·true°out·SCALAR·FP64°1");
    instruction("cp.CovarianceCPInstruction",
        "CP°cov°A·MATRIX·FP64°B·MATRIX·FP64°out·SCALAR·FP64°1",
        "CP°cov°A·MATRIX·FP64°B·MATRIX·FP64°W·MATRIX·FP64°out·SCALAR·FP64°1");
    instruction("cp.DeCompressionCPInstruction",
        "CP°decompress°A·MATRIX·FP64°out·MATRIX·FP64");
    instruction("cp.LocalCPInstruction",
        "CP°local°A·MATRIX·FP64°out·MATRIX·FP64");
    instruction("cp.QuantilePickCPInstruction",
        "CP°qpick°A·MATRIX·FP64°q·SCALAR·FP64°out·SCALAR·FP64",
        "CP°qpick°A·MATRIX·FP64°out·SCALAR·FP64°MEDIAN°true",
        "CP°qpick°A·MATRIX·FP64°out·SCALAR·FP64°IQM°true");
    instruction("cp.UnionCPInstruction",
        "CP°union_distinct°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64°1");
    // cbind only: the refactored AppendCPInstruction parses a matrix rbind into a frame append
    instruction("cp.MatrixAppendCPInstruction",
        "CP°append°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64°true");
    instruction("cp.TernaryCPInstruction",
        "CP°+*°A·MATRIX·FP64°2·SCALAR·FP64·true°B·MATRIX·FP64°out·MATRIX·FP64",
        "CP°ifelse°A·MATRIX·FP64°B·MATRIX·FP64°W·MATRIX·FP64°out·MATRIX·FP64",
        "CP°-*°q·SCALAR·FP64°2·SCALAR·FP64·true°3·SCALAR·FP64·true°out·SCALAR·FP64",
        "CP°ifelse°q·SCALAR·FP64°a·SCALAR·STRING·true°b·SCALAR·STRING·true°out·SCALAR·STRING");
    instruction("cp.BinaryScalarScalarCPInstruction",
        "CP°+°q·SCALAR·FP64°2·SCALAR·FP64·true°out·SCALAR·FP64",
        "CP°*°3·SCALAR·INT64·true°4·SCALAR·INT64·true°out·SCALAR·INT64",
        "CP°<°q·SCALAR·FP64°2·SCALAR·INT64·true°out·SCALAR·BOOLEAN",
        "CP°>°1·SCALAR·INT64·true°2·SCALAR·INT64·true°out·SCALAR·BOOLEAN",
        "CP°==°true·SCALAR·BOOLEAN·true°false·SCALAR·BOOLEAN·true°out·SCALAR·BOOLEAN",
        "CP°==°a·SCALAR·STRING·true°b·SCALAR·STRING·true°out·SCALAR·BOOLEAN",
        "CP°+°a·SCALAR·STRING·true°b·SCALAR·STRING·true°out·SCALAR·STRING",
        "CP°-°a·SCALAR·STRING·true°b·SCALAR·STRING·true°out·SCALAR·STRING",
        "CP°&&°true·SCALAR·BOOLEAN·true°false·SCALAR·BOOLEAN·true°out·SCALAR·BOOLEAN",
        "CP°+°true·SCALAR·BOOLEAN·true°true·SCALAR·BOOLEAN·true°out·SCALAR·BOOLEAN");
    instruction("cp.BinaryFrameFrameCPInstruction",
        "CP°dropInvalidType°F·FRAME·STRING°G·FRAME·STRING°fout·FRAME·STRING",
        "CP°valueSwap°F·FRAME·STRING°G·FRAME·STRING°fout·FRAME·STRING",
        "CP°freplicate°F·FRAME·STRING°G·FRAME·STRING°fout·FRAME·STRING",
        "CP°applySchema°F·FRAME·STRING°G·FRAME·STRING°fout·FRAME·STRING",
        "CP°==°F·FRAME·STRING°G·FRAME·STRING°fout·FRAME·BOOLEAN");
    instruction("cp.BinaryFrameMatrixCPInstruction",
        "CP°dropInvalidLength°F·FRAME·STRING°A·MATRIX·FP64°fout·FRAME·STRING");
    instruction("ooc.TeeOOCInstruction",
        "OOC°tee°A·MATRIX·FP64°out·MATRIX·FP64");
    // Only the parser is tested for these: one text per branch of it, plus a wrong opcode.
    parseTexts("cp.AggregateBinaryCPInstruction",
        "CP°ba+*°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64°1",
        "CP°ba+*°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64°2°true°false",
        "CP°x°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64°1");
    parseTexts("cp.AppendCPInstruction",
        "CP°append°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64°true",
        "CP°append°A·MATRIX·FP64°B·MATRIX·FP64°0·SCALAR·INT64·true°out·MATRIX·FP64°false",
        "CP°append°F·FRAME·STRING°G·FRAME·STRING°out·FRAME·STRING°true",
        "CP°append°a·SCALAR·STRING°b·SCALAR·STRING°out·SCALAR·STRING°true",
        "CP°append°L·LIST·UNKNOWN°x·SCALAR·FP64°out·LIST·UNKNOWN°true",
        "CP°remove°L·LIST·UNKNOWN°1·SCALAR·INT64·true°out·LIST·UNKNOWN°true",
        "CP°x°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64°true");
    parseTexts("cp.BroadcastCPInstruction",
        "CP°broadcast°A·MATRIX·FP64°out·MATRIX·FP64");
    parseTexts("cp.EvictCPInstruction",
        "CP°_evict°100·SCALAR·INT64·true°out·SCALAR·INT64°1");
    parseTexts("cp.IndexingCPInstruction",
        "CP°rightIndex°A·MATRIX·FP64°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°out·MATRIX·FP64",
        "CP°rightIndex°F·FRAME·STRING°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°out·FRAME·STRING",
        "CP°rightIndex°L·LIST·UNKNOWN°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°out·LIST·UNKNOWN",
        "CP°rightIndex°a·SCALAR·FP64°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°out·MATRIX·FP64",
        "CP°rightIndex°A·MATRIX·FP64°1·SCALAR·INT64·true°out·MATRIX·FP64",
        "CP°leftIndex°A·MATRIX·FP64°B·MATRIX·FP64°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°out·MATRIX·FP64",
        "CP°leftIndex°F·FRAME·STRING°G·FRAME·STRING°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°out·FRAME·STRING",
        "CP°leftIndex°L·LIST·UNKNOWN°x·SCALAR·FP64°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°1·SCALAR·INT64·true°out·LIST·UNKNOWN",
        "CP°leftIndex°A·MATRIX·FP64°out·MATRIX·FP64",
        "CP°x°A·MATRIX·FP64°out·MATRIX·FP64");
    parseTexts("cp.MMTSJCPInstruction",
        "CP°tsmm°A·MATRIX·FP64°out·MATRIX·FP64°LEFT°1",
        "CP°tsmm°A·MATRIX·FP64°out·MATRIX·FP64°RIGHT°2",
        "CP°x°A·MATRIX·FP64°out·MATRIX·FP64°LEFT°1");
    parseTexts("cp.PMMJCPInstruction",
        "CP°pmm°A·MATRIX·FP64°B·MATRIX·FP64°5·SCALAR·INT64·true°out·MATRIX·FP64°1",
        "CP°x°A·MATRIX·FP64°B·MATRIX·FP64°5·SCALAR·INT64·true°out·MATRIX·FP64°1");
    parseTexts("spark.AppendRSPInstruction",
        "SPARK°rappend°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64°true",
        "SPARK°rappend°F·FRAME·STRING°G·FRAME·STRING°out·FRAME·STRING°false",
        "SPARK°x°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64°true");
    parseTexts("gpu.ArithmeticBinaryGPUInstruction",
        "GPU°+°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64",
        "GPU°*°A·MATRIX·FP64°2·SCALAR·FP64·true°out·MATRIX·FP64",
        "GPU°-°2·SCALAR·FP64·true°A·MATRIX·FP64°out·MATRIX·FP64",
        "GPU°+°a·SCALAR·FP64°b·SCALAR·FP64°out·SCALAR·FP64");
    parseTexts("gpu.BuiltinBinaryGPUInstruction",
        "GPU°solve°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64",
        "GPU°min°A·MATRIX·FP64°2·SCALAR·FP64·true°out·MATRIX·FP64",
        "GPU°max°2·SCALAR·FP64·true°A·MATRIX·FP64°out·MATRIX·FP64",
        "GPU°max°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64",
        "GPU°min°A·MATRIX·FP64°2·SCALAR·FP64·true°out·SCALAR·FP64",
        "GPU°min°a·SCALAR·FP64°2·SCALAR·FP64·true°out·SCALAR·FP64");
    parseTexts("gpu.MMTSJGPUInstruction",
        "GPU°tsmm°A·MATRIX·FP64°out·MATRIX·FP64°LEFT",
        "GPU°tsmm°A·MATRIX·FP64°out·MATRIX·FP64°RIGHT",
        "GPU°x°A·MATRIX·FP64°out·MATRIX·FP64°LEFT");
    parseTexts("gpu.MatrixMatrixAxpyGPUInstruction",
        "GPU°+*°A·MATRIX·FP64°2·SCALAR·FP64·true°B·MATRIX·FP64°out·MATRIX·FP64",
        "GPU°-*°A·MATRIX·FP64°2·SCALAR·FP64·true°B·MATRIX·FP64°out·MATRIX·FP64",
        "GPU°+*°A·MATRIX·FP64°2·SCALAR·FP64·true°2·SCALAR·FP64·true°out·MATRIX·FP64",
        "GPU°+*°A·MATRIX·FP64°B·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64");
    parseTexts("gpu.RelationalBinaryGPUInstruction",
        "GPU°<°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64",
        "GPU°>=°A·MATRIX·FP64°2·SCALAR·FP64·true°out·MATRIX·FP64",
        "GPU°==°2·SCALAR·FP64·true°A·MATRIX·FP64°out·MATRIX·FP64",
        "GPU°<°a·SCALAR·FP64°b·SCALAR·FP64°out·SCALAR·FP64");
    parseTexts("gpu.ReorgGPUInstruction",
        "GPU°r'°A·MATRIX·FP64°out·MATRIX·FP64",
        "GPU°x°A·MATRIX·FP64°out·MATRIX·FP64");
    BY_PARAMETER.put("org.apache.sysds.runtime.instructions.spark.UnarySPInstruction.parse#0",
        new CommonRecipes.OneOf(
            "SPARK°op°A·MATRIX·FP64°out·MATRIX·FP64",
            "SPARK°op°A·MATRIX·FP64°B·MATRIX·FP64°out·MATRIX·FP64",
            "SPARK°op°A·MATRIX·FP64°B·MATRIX·FP64°W·MATRIX·FP64°out·MATRIX·FP64",
            "SPARK°op°out·MATRIX·FP64"));
    // DenseBlock cells: a valid {row, column} and a parsable number (only the overload taking these types uses them)
    for (String b : new String[] {"FP32", "FP64", "LFP64"}) {
      BY_PARAMETER.put("org.apache.sysds.runtime.data.DenseBlock" + b + ".set#0", new SystemdsRecipes.CellIndex());
      BY_PARAMETER.put("org.apache.sysds.runtime.data.DenseBlock" + b + ".set#1", new SystemdsRecipes.NumberText());
    }
    BY_PARAMETER.put("org.apache.sysds.runtime.data.DenseBlockLString.getLong#0", new SystemdsRecipes.CellIndex());
    BY_PARAMETER.put("org.apache.sysds.runtime.data.DenseBlockLFP64DEDUP.set#0", new SystemdsRecipes.CellNumber());
    BY_PARAMETER.put("org.apache.sysds.runtime.data.DenseBlockLFP64DEDUP.set#1", new SystemdsRecipes.CellNumber());
    BY_PARAMETER.put("org.apache.sysds.runtime.compress.lib.CLALibCMOps.centralMoment#0",
        new SystemdsRecipes.CompressedColumn());
    BY_PARAMETER.put("org.apache.sysds.runtime.controlprogram.caching.PageCache.putPage#0",
        new SystemdsRecipes.PageCacheArgument("page"));
    BY_PARAMETER.put("org.apache.sysds.runtime.controlprogram.caching.PageCache.getPage#0",
        new SystemdsRecipes.PageCacheArgument("size"));
    // degree of parallelism: 0 starts no merge threads, and the merge then waits forever on both sides
    BY_PARAMETER.put("org.apache.sysds.runtime.controlprogram.parfor.ResultMergeLocalAutomatic.executeParallelMerge#0",
        new CommonRecipes.OneOf(1, 2, 3, 4));
    // the thread pool: ABooleanArray's recode map never uses it, and nothing can build one, so null
    BY_PARAMETER.put("org.apache.sysds.runtime.frame.data.columns.ABooleanArray.createRecodeMap#1",
        new CommonRecipes.OneOf((Object) null));
    BY_PARAMETER.put("org.apache.sysds.runtime.instructions.spark.functions.RecomputeNnzFunction.call#0",
        new SystemdsRecipes.MatrixBlockIterator());
    // a column slice: start and end within a 2..4 column group, and the slice's output columns
    String value = "org.apache.sysds.runtime.compress.colgroup.AColGroupValue.sliceMultiColumns#";
    BY_PARAMETER.put(value + "0", new CommonRecipes.OneOf(0, 1, 2));
    BY_PARAMETER.put(value + "1", new CommonRecipes.OneOf(1, 2, 3, 4));
    BY_PARAMETER.put(value + "2", new SystemdsRecipes.ColumnRange());
    // a row of the reader's matrix (it has at least 4)
    BY_CLASS.put("org.apache.sysds.runtime.compress.readers.ReaderColumnSelectionSparseDelta",
        new SystemdsRecipes.SparseDeltaReader());
    BY_PARAMETER.put("org.apache.sysds.runtime.compress.readers.ReaderColumnSelectionSparseDelta.processInRange#0",
        new CommonRecipes.OneOf(0, 1, 2, 3));
    // a worker id: the server has 1..3 workers, so -1 and 3 are unknown ones
    BY_CLASS.put("org.apache.sysds.runtime.controlprogram.paramserv.LocalParamServer", new SystemdsRecipes.ParamServer());
    BY_CLASS.put("org.apache.sysds.runtime.ooc.cache.packed.PackedBlock", new SystemdsRecipes.PackedBlock());
    BY_PARAMETER.put("org.apache.sysds.runtime.controlprogram.paramserv.LocalParamServer.pull#0",
        new CommonRecipes.OneOf(0, 1, 2, -1, 3));
    // callback parameters: a recorder of the calls the method makes to them
    String coll = "org.apache.sysds.runtime.ooc.cache.collections.";
    BY_PARAMETER.put(coll + "MaskedOnceArray.forEachLiveForward#0", new CommonRecipes.Recorder(coll + "IndexedObjectPredicate"));
    BY_PARAMETER.put(coll + "MaskedOnceArray.forEachLiveBackward#0", new CommonRecipes.Recorder(coll + "IndexedObjectPredicate"));
    BY_PARAMETER.put(coll + "MaskedOnceArrayList.forEachLive#0", new CommonRecipes.Recorder(coll + "IndexedObjectPredicate"));
    BY_PARAMETER.put(coll + "MaskedOnceArrayList.forEachVisible#0", new CommonRecipes.Recorder("java.util.function.Consumer"));
    BY_PARAMETER.put(coll + "SegmentedStreamTableList.forEachStreamTable#0",
        new CommonRecipes.Recorder("java.util.function.BiConsumer", "java.util.function.Consumer"));
    BY_PARAMETER.put("org.apache.sysds.runtime.ooc.stream.FilteredOOCStream.setSubscriber#0",
        new CommonRecipes.Recorder("java.util.function.Consumer"));
    BY_PARAMETER.put("org.apache.sysds.runtime.ooc.stream.SubOOCStream.setSubscriber#0",
        new CommonRecipes.Recorder("java.util.function.Consumer"));
    BY_PARAMETER.put("org.apache.sysds.runtime.ooc.stream.SplittingOOCStream.<init>#1",
        new CommonRecipes.Recorder("java.util.function.Function"));
    BY_PARAMETER.put("org.apache.sysds.runtime.io.hdf5.message.H5DataTypeMessage.<init>#2",
        new SystemdsRecipes.H5DoubleDataTypeBytes());
    BY_PARAMETER.put("org.apache.sysds.runtime.util.DependencyWrapperTask.addWrappedTaskFuture#0",
        new SystemdsRecipes.CompletedFuture());
    // combine(columns, g1, g2, max): two column infos from the receiver's own matrix
    BY_PARAMETER.put("org.apache.sysds.runtime.compress.estim.ComEstExact.combine#1", new SystemdsRecipes.ColGroupInfo());
    BY_PARAMETER.put("org.apache.sysds.runtime.compress.estim.ComEstExact.combine#2", new SystemdsRecipes.ColGroupInfo());
    BY_PARAMETER.put("org.apache.sysds.parser.IterablePredicate.checkNumericScalarOutput#0",
        new SystemdsRecipes.Expression());
    BY_PARAMETER.put("org.apache.sysds.runtime.controlprogram.federated.monitoring.Request.setContext#0",
        new SystemdsRecipes.HttpRequest());
  }

  /**
   * A systemds instruction (package and class under org.apache.sysds.runtime.instructions) built by
   * parsing one of these texts, run in an InstructionContext, with its parser tested on the texts.
   */
  private static void instruction(String cls, String... texts)
  {
    String name = "org.apache.sysds.runtime.instructions." + cls;
    BY_CLASS.put(name, new SystemdsRecipes.CPInstruction(texts));
    BY_PARAMETER.put(name + ".processInstruction#0", new SystemdsRecipes.InstructionContext());
    parseTexts(cls, texts);
  }

  /** Valid texts for parseInstruction(String) of this systemds instruction class. */
  private static void parseTexts(String cls, String... texts)
  {
    BY_PARAMETER.put("org.apache.sysds.runtime.instructions." + cls + ".parseInstruction#0",
        new CommonRecipes.OneOf(texts));
  }

  /** The recipe for this class name, or null if there is none. */
  public static Recipe forClass(String name)
  {
    return BY_CLASS.get(name);
  }

  /** The recipe for this method parameter ("class.method#index"), or null if there is none. */
  public static Recipe forParameter(String key)
  {
    return BY_PARAMETER.get(key);
  }
}
