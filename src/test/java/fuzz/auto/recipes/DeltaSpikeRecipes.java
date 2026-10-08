package fuzz.auto.recipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Proxy;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/** deltaspike classes the engine could not build. Reflection only: compiled for every project. */
final class DeltaSpikeRecipes
{
  private DeltaSpikeRecipes() {}

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
      Object bean = new BeanRecipe().build(data, beanType);
      Object context = Proxy.newProxyInstance(loader, new Class<?>[] {contextType},
          new ServletRecipes.Handler()
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
          new ServletRecipes.Handler()
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
}
