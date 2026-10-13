package fuzz.auto.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.Semaphore;

/** deltaspike classes the engine could not build. Reflection only: compiled for every project. */
final class DeltaSpikeRecipes
{
  private DeltaSpikeRecipes() {}

  /**
   * EntityDescriptor (JPA orm.xml entity): autofuzz left the entity and id classes empty, so
   * toString failed on getEntityClass().getName() almost every time. Real classes, a fuzzed name,
   * id fields and table name, and sometimes a parent descriptor.
   */
  static final class EntityDescriptor implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object parent = data.consumeBoolean() ? null : descriptor(data, type, null);
      return descriptor(data, type, parent);
    }

    private static Object descriptor(FuzzedDataProvider data, Class<?> type, Object parent) throws Exception
    {
      Class<?> entity = data.pickValue(new Class<?>[] {String.class, Integer.class, java.util.Date.class});
      Class<?> id = data.pickValue(new Class<?>[] {Long.class, Integer.class, String.class});
      String[] ids = new String[data.consumeInt(0, 2)];
      for (int i = 0; i < ids.length; i++) {
        ids[i] = "id" + i;
      }
      return type.getConstructor(String[].class, String.class, String.class, Class.class, Class.class,
          Class.forName("org.apache.deltaspike.jpa.spi.descriptor.xml.AbstractEntityDescriptor", true,
              type.getClassLoader()), String.class)
          .newInstance(ids, data.consumeBoolean() ? "version" : null, data.consumeString(8), entity, id,
              parent, data.consumeBoolean() ? null : data.consumeString(8));
    }
  }

  /**
   * jsf DependentBeanEntry(instance, Bean, CreationalContext): package-private, and its Bean and
   * CreationalContext come from the CDI container. A fuzzed instance, the Bean stand-in, and a
   * CreationalContext whose methods do nothing.
   */
  static final class DependentBeanEntry implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> beanType = Class.forName("javax.enterprise.inject.spi.Bean", true, loader);
      Class<?> contextType = Class.forName("javax.enterprise.context.spi.CreationalContext", true, loader);
      Object bean = new CommonRecipes.BeanRecipe().build(data, beanType);
      Object context = Proxy.newProxyInstance(loader, new Class<?>[] {contextType},
          new CommonRecipes.ServletRecipes.Handler()
          {
            @Override
            Object call(String name, Object[] args)
            {
              return null; // push(), release()
            }
          });
      Constructor<?> ctor = type.getDeclaredConstructors()[0]; // (instance, bean, creationalContext)
      ctor.setAccessible(true);
      return ctor.newInstance(data.consumeAsciiString(16), bean, context);
    }
  }

  /**
   * data RepositoryMetadata: the engine could not build its EntityMetadata. An entity class with
   * real properties, so validate(name, ...) can find one (every class has "class", from getClass).
   */
  static final class RepositoryMetadata implements Recipe
  {
    private static final Class<?>[] ENTITIES = {java.util.AbstractMap.SimpleEntry.class, Object.class};

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> entityType = Class.forName("org.apache.deltaspike.data.impl.meta.EntityMetadata", true,
          type.getClassLoader());
      Object entity = entityType.getConstructor(Class.class)
          .newInstance(ENTITIES[data.consumeInt(0, ENTITIES.length - 1)]);
      return type.getConstructor(Class.class, entityType).newInstance(Object.class, entity);
    }
  }

  /**
   * core BroadcasterProducer: its MBeanExtension is @Inject-ed, so created with new it stayed null
   * and every call threw NullPointerException. A fresh MBeanExtension has no broadcaster
   * registered, which is the "invalid injection" path the producer reports.
   */
  static final class BroadcasterProducer implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Object producer = type.getConstructor().newInstance();
      Field f = type.getDeclaredField("extension");
      f.setAccessible(true);
      f.set(producer, f.getType().getConstructor().newInstance());
      return producer;
    }
  }

  /** CDI InjectionPoint: a stand-in whose member is Object.toString(). */
  static final class InjectionPoint implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      final java.lang.reflect.Member member = Object.class.getMethod("toString");
      return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
          new CommonRecipes.ServletRecipes.Handler()
          {
            @Override
            Object call(String name, Object[] args)
            {
              return name.equals("getMember") ? member : null;
            }

            @Override
            public String toString()
            {
              return "InjectionPoint(Object.toString)";
            }
          });
    }
  }

  /**
   * deltaspike ViewConfigNode: an interface the engine found no implementation of
   * (FolderConfigNode(ViewConfigNode, Class)). A FolderConfigNode at the root, with no parent and no
   * metadata, is a real node of its own.
   */
  static final class ViewConfigNodeRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> folderType = Class.forName("org.apache.deltaspike.jsf.impl.config.view.FolderConfigNode",
          true, type.getClassLoader());
      Set<Annotation> metaData = new HashSet<Annotation>();
      return folderType.getConstructor(Class.class, type, Set.class).newInstance(Object.class, null, metaData);
    }
  }

  /** For interceptors: the InvocationContext they receive, and deltaspike's throttling Invoker. */
  static final class InterceptorRecipes
  {
    private InterceptorRecipes() {}

    /**
     * javax.interceptor.InvocationContext: an interface the container normally supplies. The
     * stand-in reports Object.toString() as the intercepted method, and proceed() either returns a
     * value or throws, as the intercepted call would.
     */
    static final class Context implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        final java.lang.reflect.Method method = Object.class.getMethod("toString");
        final boolean fails = data.consumeInt(0, 3) == 3; // proceed() throws now and then
        return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
            new CommonRecipes.ServletRecipes.Handler()
            {
              @Override
              Object call(String name, Object[] args) throws Exception
              {
                if (name.equals("getMethod")) {
                  return method;
                }
                if (name.equals("getParameters")) {
                  return new Object[0];
                }
                if (name.equals("getContextData")) {
                  return new HashMap<String, Object>();
                }
                if (name.equals("proceed")) {
                  if (fails) {
                    throw new IllegalStateException("intercepted call failed");
                  }
                  return "proceeded";
                }
                return null;
              }

              @Override
              public String toString()
              {
                return "InvocationContext(" + (fails ? "proceed throws" : "proceed returns") + ")";
              }
            });
      }
    }

    /**
     * An interceptor whose strategy is normally @Inject-ed (deltaspike's
     * NavigationParameterInterceptor and NavigationParameterListInterceptor). Created with new, the
     * field stayed null and every call threw NullPointerException. The real strategy needs a running
     * JSF application, so the field gets a stand-in strategy that just proceeds, as an interceptor
     * strategy does when it has nothing to add.
     */
    static final class WithStrategy implements Recipe
    {
      private final String field;

      WithStrategy(String field)
      {
        this.field = field;
      }

      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        Object interceptor = type.getConstructor().newInstance();
        java.lang.reflect.Field f = type.getDeclaredField(field);
        f.setAccessible(true);
        f.set(interceptor, Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {f.getType()},
            new CommonRecipes.ServletRecipes.Handler()
            {
              @Override
              Object call(String name, Object[] args) throws Exception
              {
                if (name.equals("execute")) {
                  return args[0].getClass().getMethod("proceed").invoke(args[0]); // ctx.proceed()
                }
                return null;
              }

              @Override
              public String toString()
              {
                return "Strategy(proceed)";
              }
            }));
        return interceptor;
      }
    }

    /**
     * deltaspike throttling Invoker(Semaphore, weight, timeout): with fuzzed values invoke() waited
     * up to the timeout, or forever in semaphore.acquire() when timeout <= 0 and the semaphore had
     * fewer permits than the weight, leaving a stuck thread per input. Small values, and never a
     * blocking acquire: a weight above the permits only with a short timeout (the "can't acquire"
     * path).
     */
    static final class Invoker implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        int permits = data.consumeInt(0, 3);
        int weight = data.consumeInt(0, 3);
        long timeout = data.consumeInt(0, 20); // milliseconds; 0 means acquire() without a timeout
        if (timeout == 0 && weight > permits) {
          weight = permits; // acquire() would block forever
        }
        Constructor<?> ctor = type.getDeclaredConstructor(Semaphore.class, int.class, long.class);
        ctor.setAccessible(true); // package-private
        return ctor.newInstance(new Semaphore(permits), weight, timeout);
      }
    }
  }

  /**
   * deltaspike's "Injectable..." wrappers: package-private classes around a delegate the engine
   * could not build (a GroupedConversationManager or a WindowContext, normally from the CDI
   * container). Each gets a stand-in delegate. Their lookup of a missing delegate goes through
   * BeanProvider and needs a running container, so the delegate is never null here.
   */
  static final class InjectableRecipes
  {
    private InjectableRecipes() {}

    private static final String CONVERSATION = "org.apache.deltaspike.core.impl.scope.conversation.";

    /** InjectableGroupedConversationManager(GroupedConversationManager). */
    static final class ConversationManager implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        return create(type, managerStub(type.getClassLoader()));
      }
    }

    /** InjectableWindowContext(WindowContext). */
    static final class WindowContext implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        Class<?> windowType = Class.forName("org.apache.deltaspike.core.spi.scope.window.WindowContext",
            true, type.getClassLoader());
        return create(type, stub(windowType, "WindowContext"));
      }
    }

    /** InjectableViewAccessContextManager(ViewAccessContextManager): a delegate that records close(). */
    static final class ViewAccessContextManager implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        Class<?> managerType = Class.forName(
            "org.apache.deltaspike.core.spi.scope.viewaccess.ViewAccessContextManager", true,
            type.getClassLoader());
        Object delegate = Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {managerType},
            new CommonRecipes.ServletRecipes.Handler()
            {
              boolean closed;

              @Override
              Object call(String name, Object[] args)
              {
                if (name.equals("close")) {
                  closed = true;
                }
                return null;
              }

              @Override
              public String toString()
              {
                return "ViewAccessContextManager(closed=" + closed + ")";
              }
            });
        return create(type, delegate);
      }
    }

    /**
     * InjectableGroupedConversation(ConversationKey, GroupedConversationManager): a key for the
     * Object group with 0-2 qualifiers, and a manager that records what close() asks it to close.
     */
    static final class Conversation implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        ClassLoader loader = type.getClassLoader();
        Annotation[] qualifiers = new Annotation[data.consumeInt(0, 2)];
        for (int i = 0; i < qualifiers.length; i++) {
          qualifiers[i] = (Annotation) new CommonRecipes.AnnotationRecipe().build(data, Annotation.class);
        }
        Class<?> keyType = Class.forName(CONVERSATION + "ConversationKey", true, loader);
        Object key = keyType.getConstructor(Class.class, Annotation[].class)
            .newInstance(Object.class, qualifiers);
        return create(type, key, managerStub(loader));
      }
    }

    /** A GroupedConversationManager that records the last conversation it was asked to close. */
    private static Object managerStub(ClassLoader loader) throws Exception
    {
      Class<?> managerType = Class.forName(
          "org.apache.deltaspike.core.spi.scope.conversation.GroupedConversationManager", true, loader);
      return Proxy.newProxyInstance(loader, new Class<?>[] {managerType}, new CommonRecipes.ServletRecipes.Handler()
      {
        String closed;

        @Override
        Object call(String name, Object[] args)
        {
          if (name.equals("closeConversation")) {
            int count = args[1] == null ? -1 : Array.getLength(args[1]);
            closed = args[0] + " with " + count + " qualifiers";
          }
          return null;
        }

        @Override
        public String toString()
        {
          return "GroupedConversationManager(closed=" + closed + ")";
        }
      });
    }

    /** A stand-in for an interface whose methods do nothing. */
    private static Object stub(Class<?> type, final String name)
    {
      return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
          new CommonRecipes.ServletRecipes.Handler()
          {
            @Override
            Object call(String method, Object[] args)
            {
              return null;
            }

            @Override
            public String toString()
            {
              return name;
            }
          });
    }

    /** Calls the (package-private) constructor of {@code type} that takes these arguments. */
    private static Object create(Class<?> type, Object... args) throws Exception
    {
      for (Constructor<?> c : type.getDeclaredConstructors()) {
        if (c.getParameterCount() == args.length) {
          c.setAccessible(true);
          return c.newInstance(args);
        }
      }
      throw new NoSuchMethodException(type.getName());
    }
  }

  /**
   * deltaspike classes whose collaborators are normally @Inject-ed by the CDI container. Created
   * with new, those fields stayed null and every call threw NullPointerException on its first line
   * ("body never executed"). Reflection only: this folder is compiled for every project.
   */
  static final class InjectedRecipes
  {
    private InjectedRecipes() {}

    /**
     * Fills the named @Inject fields with a new instance of their declared type, which here are
     * plain classes with a no-argument constructor (deltaspike's CDI extensions, for instance).
     */
    static final class Fields implements Recipe
    {
      private final String[] fields;

      Fields(String... fields)
      {
        this.fields = fields;
      }

      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        Constructor<?> ctor = type.getDeclaredConstructor();
        ctor.setAccessible(true);
        Object target = ctor.newInstance();
        for (String name : fields) {
          Field f = type.getDeclaredField(name);
          f.setAccessible(true);
          Constructor<?> c = f.getType().getDeclaredConstructor();
          c.setAccessible(true);
          f.set(target, c.newInstance());
        }
        return target;
      }
    }

    /**
     * DefaultWindowContextQuotaHandler: its cache is @Inject-ed and its limit set by @PostConstruct
     * from configuration. A real cache, a small fuzzed limit and 0-3 known window ids already on the
     * stack, so the "move to front" and "drop the oldest" paths are reachable in one call.
     */
    static final class WindowQuotaHandler implements Recipe
    {
      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        Object handler = new Fields("quotaHandlerCache").build(data, type);
        set(handler, type, "maxWindowContextCount", data.consumeInt(0, 3));
        Stack<String> stack = new Stack<String>();
        int known = data.consumeInt(0, 3);
        for (int i = 0; i < known; i++) {
          stack.push("w" + i);
        }
        set(handler, type, "windowIdStack", stack);
        return handler;
      }
    }

    /**
     * security AuthorizationParameter(Type, Set<Annotation>): the engine used the empty constructor,
     * which leaves the type null, so matches() threw on its first line. A real type and 0-2
     * annotations as bindings, so two parameters can match or differ.
     */
    static final class AuthorizationParameter implements Recipe
    {
      private static final Type[] TYPES = {String.class, Integer.class};

      @Override
      public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
      {
        Set<Annotation> bindings = new HashSet<Annotation>();
        int n = data.consumeInt(0, 2);
        for (int i = 0; i < n; i++) {
          bindings.add((Annotation) new CommonRecipes.AnnotationRecipe().build(data, Annotation.class));
        }
        Constructor<?> ctor = type.getDeclaredConstructor(Type.class, Set.class);
        ctor.setAccessible(true);
        return ctor.newInstance(TYPES[data.consumeInt(0, TYPES.length - 1)], bindings);
      }
    }

    private static void set(Object target, Class<?> type, String name, Object value) throws Exception
    {
      Field f = type.getDeclaredField(name);
      f.setAccessible(true);
      f.set(target, value);
    }
  }

  /**
   * AbstractInvocationContext: its concrete subclass DeltaSpikeProxyInvocationContext takes a proxy
   * handler, a BeanManager, interceptors, a target, a method, parameters and a timer, which the
   * engine could not build. The constructor only stores them, and getContextData() uses none of
   * them, so all of them are null.
   */
  static final class InvocationContextRecipe implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> contextType = Class.forName(
          "org.apache.deltaspike.proxy.spi.invocation.DeltaSpikeProxyInvocationContext", true,
          type.getClassLoader());
      Constructor<?> ctor = contextType.getConstructors()[0];
      return ctor.newInstance(new Object[ctor.getParameterCount()]);
    }
  }
}
