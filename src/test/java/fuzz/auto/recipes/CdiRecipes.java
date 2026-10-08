package fuzz.auto.recipes;

import java.lang.reflect.Proxy;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * CDI types the engine could not build, which normally come from a CDI container. Written with
 * reflection because this folder is also compiled for projects without CDI.
 */
final class CdiRecipes
{
  private CdiRecipes() {}

  /**
   * AnnotatedType: a real one, made by deltaspike's own AnnotatedTypeBuilder from a plain class or
   * from DefaultEchoService, whose @Named("DefaultEchoService") is what
   * NamingConventionAwareMetadataFilter rewrites. Only classes with few methods: an AnnotatedType
   * keeps its methods in a HashSet, and the comparison looks at the first 32 elements of a set, so
   * for String (100+ methods) each side showed a different 32 and reported a false divergence.
   */
  static final class AnnotatedType implements Recipe
  {
    private static final String[] CLASSES = {
        "org.apache.deltaspike.example.echo.DefaultEchoService", "java.lang.Object", "java.lang.Number"};

    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      ClassLoader loader = type.getClassLoader();
      Class<?> source;
      try {
        source = Class.forName(CLASSES[data.consumeInt(0, CLASSES.length - 1)], false, loader);
      } catch (ClassNotFoundException e) {
        source = Object.class; // the example module is not in this jar
      }
      Class<?> builderType = Class.forName(
          "org.apache.deltaspike.core.util.metadata.builder.AnnotatedTypeBuilder", true, loader);
      Object builder = builderType.getConstructor().newInstance();
      builderType.getMethod("readFromType", Class.class).invoke(builder, source);
      return builderType.getMethod("create").invoke(builder);
    }
  }

  /** BeforeBeanDiscovery: a container event; a stand-in whose methods do nothing. */
  static final class BeforeBeanDiscovery implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type)
    {
      return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
          new ServletRecipes.Handler()
          {
            @Override
            Object call(String name, Object[] args)
            {
              return null;
            }
          });
    }
  }

  /**
   * ProcessAnnotatedType: a container event carrying an AnnotatedType. The stand-in hands out one
   * from the recipe above and records whether the observer replaced it or vetoed it.
   */
  static final class ProcessAnnotatedType implements Recipe
  {
    @Override
    public Object build(FuzzedDataProvider data, Class<?> type) throws Exception
    {
      Class<?> annotatedTypeType =
          Class.forName("javax.enterprise.inject.spi.AnnotatedType", true, type.getClassLoader());
      final Object annotatedType = new AnnotatedType().build(data, annotatedTypeType);
      return Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] {type},
          new ServletRecipes.Handler()
          {
            boolean replaced;
            boolean vetoed;

            @Override
            Object call(String name, Object[] args)
            {
              if (name.equals("getAnnotatedType")) {
                return annotatedType;
              }
              if (name.equals("setAnnotatedType")) {
                replaced = true;
              } else if (name.equals("veto")) {
                vetoed = true;
              }
              return null;
            }

            @Override
            public String toString()
            {
              return "ProcessAnnotatedType(replaced=" + replaced + ", vetoed=" + vetoed + ")";
            }
          });
    }
  }
}
