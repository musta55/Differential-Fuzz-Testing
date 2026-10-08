package fuzz.auto.recipes;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * deltaspike classes whose collaborators are normally @Inject-ed by the CDI container. Created
 * with new, those fields stayed null and every call threw NullPointerException on its first line
 * ("body never executed"). Reflection only: this folder is compiled for every project.
 */
final class InjectedRecipes
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
        bindings.add((Annotation) new AnnotationRecipe().build(data, Annotation.class));
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
