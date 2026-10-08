package fuzz.auto.recipes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Proxy;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * deltaspike's "Injectable..." wrappers: package-private classes around a delegate the engine
 * could not build (a GroupedConversationManager or a WindowContext, normally from the CDI
 * container). Each gets a stand-in delegate. Their lookup of a missing delegate goes through
 * BeanProvider and needs a running container, so the delegate is never null here.
 */
final class InjectableRecipes
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
          new ServletRecipes.Handler()
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
        qualifiers[i] = (Annotation) new AnnotationRecipe().build(data, Annotation.class);
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
    return Proxy.newProxyInstance(loader, new Class<?>[] {managerType}, new ServletRecipes.Handler()
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
        new ServletRecipes.Handler()
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
