package fuzz.auto;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.code_intelligence.jazzer.api.Autofuzz;
import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * Builds a value of an arbitrary type from fuzz bytes, so a changed method taking domain objects
 * can be differentially tested instead of dropped.
 *
 * <p>Three tiers, in order:
 * <ol>
 *   <li><b>Scalars</b> built directly. Cheap, and it keeps the byte layout of the previously
 *       scalar-only path recognisable.</li>
 *   <li><b>{@link Autofuzz#consume}</b> for everything else. Measured behaviour on Jazzer 0.22.0:
 *       it handles concrete classes and <em>JDK</em> abstract types (asked for
 *       {@code java.io.InputStream} it returns a {@code ByteArrayInputStream}), and it constructs
 *       classes whose constructors themselves need interfaces — which is what recovered
 *       {@code StreamGobbler(InputStream)} and {@code Slider(Unifier,int,int)} from SKIP.</li>
 *   <li><b>Concrete-subtype substitution</b> when tier 2 cannot help. Autofuzz finds no
 *       implementors of a <em>project</em> interface or abstract class (verified: it returns null
 *       for {@code MuxReservoir} and {@code SweepableReservoir}), so this searches the type's own
 *       side ({@link SideLoader}: the changed classes plus the whole fat jar) for a concrete
 *       subtype and asks autofuzz for that instead. This is what makes an abstract-receiver method
 *       testable at all.</li>
 * </ol>
 *
 * <p>Raw generic types must not be handed to autofuzz directly: asking it for {@code java.util.Map}
 * throws {@code ClassCastException (Class cannot be cast to ParameterizedType)}. Known collection
 * interfaces are therefore mapped to a concrete implementation up front.
 */
final class ObjectFactory
{
  private ObjectFactory() {}

  /** A type autofuzz refuses raw; substitute a concrete implementation before asking. */
  private static final Map<Class<?>, Class<?>> CONCRETE = new HashMap<>();

  static {
    CONCRETE.put(java.util.Collection.class, ArrayList.class);
    CONCRETE.put(java.util.List.class, ArrayList.class);
    CONCRETE.put(java.util.Set.class, java.util.LinkedHashSet.class);
    CONCRETE.put(java.util.SortedSet.class, java.util.TreeSet.class);
    CONCRETE.put(java.util.Map.class, java.util.LinkedHashMap.class);
    CONCRETE.put(java.util.SortedMap.class, java.util.TreeMap.class);
    CONCRETE.put(java.lang.Iterable.class, ArrayList.class);
    CONCRETE.put(java.lang.CharSequence.class, String.class);
    CONCRETE.put(java.lang.Number.class, Integer.class);
    CONCRETE.put(java.lang.Object.class, String.class);
    CONCRETE.put(java.io.InputStream.class, java.io.ByteArrayInputStream.class);
    CONCRETE.put(java.io.OutputStream.class, java.io.ByteArrayOutputStream.class);
    CONCRETE.put(java.io.Reader.class, java.io.StringReader.class);
    CONCRETE.put(java.io.Writer.class, java.io.StringWriter.class);
    CONCRETE.put(java.io.DataInput.class, java.io.DataInputStream.class);
    CONCRETE.put(java.io.DataOutput.class, java.io.DataOutputStream.class);
    CONCRETE.put(java.util.Comparator.class, java.text.Collator.class);
  }

  /** Cache of abstract/interface type -> its ranked concrete subtypes (empty if none). */
  private static final Map<Class<?>, List<Class<?>>> SUBTYPES = new ConcurrentHashMap<>();

  /**
   * How many concrete subtypes to try per abstract type. Each failed attempt still consumes fuzz
   * bytes, and finding more means loading more classes, so this stays small.
   */
  private static final int MAX_SUBTYPES = Integer.getInteger("fuzz.maxSubtypes", 4);

  /** Thrown when no tier can produce a value of the requested type. */
  static final class Unbuildable extends RuntimeException
  {
    Unbuildable(String m) { super(m); }
  }

  /**
   * Build one value of a possibly-generic parameter type.
   *
   * <p>This overload exists because erasure loses exactly the information that makes a collection
   * parameter testable. Handed the raw {@code List}, autofuzz has no element type to work with and
   * returns an <em>empty</em> ArrayList, every time — so a method that loops over its argument never
   * entered the loop, and the demo's {@code Grader.total(List&lt;Integer&gt;)} sat at 1/4 branches
   * while still reporting EQUIVALENT. The declared type {@code List<Integer>} is available from
   * {@code Method.getGenericParameterTypes()}, so callers pass that and collections get populated
   * with real elements.
   *
   * <p>Callers must be consistent about which overload they use: {@link SeedRecorder} and
   * {@link ReplayProvider} have to consume the same number of bytes in the same order, so a seed
   * recorded through the generic path and replayed through the raw one would decode to different
   * arguments. Every call site therefore passes generic types.
   */
  static Object build(FuzzedDataProvider data, java.lang.reflect.Type t)
  {
    if (t instanceof java.lang.reflect.ParameterizedType) {
      java.lang.reflect.ParameterizedType pt = (java.lang.reflect.ParameterizedType) t;
      if (!(pt.getRawType() instanceof Class)) {
        return build(data, Object.class);
      }
      Class<?> raw = (Class<?>) pt.getRawType();
      java.lang.reflect.Type[] targs = pt.getActualTypeArguments();
      if (java.util.Map.class.isAssignableFrom(raw) && targs.length == 2) {
        return buildMap(data, raw, targs[0], targs[1]);
      }
      if (java.util.Collection.class.isAssignableFrom(raw) && targs.length == 1) {
        return buildCollection(data, raw, targs[0]);
      }
      return build(data, raw);
    }
    if (t instanceof java.lang.reflect.GenericArrayType) {
      return build(data, Object[].class);
    }
    if (t instanceof Class) {
      return build(data, (Class<?>) t);
    }
    // A type variable (T) or wildcard has no concrete form here; String is the same stand-in
    // CONCRETE already uses for a bare Object.
    return build(data, String.class);
  }

  private static Object buildCallbackArray(FuzzedDataProvider data, Class<?> componentType)
  {
    javax.security.auth.callback.Callback[] callbacks =
        new javax.security.auth.callback.Callback[data.consumeInt(1, 4)];
    for (int i = 0; i < callbacks.length; i++) {
      callbacks[i] = (javax.security.auth.callback.Callback) build(data, componentType);
    }
    return callbacks;
  }

  /** Element count for a generic collection; small, because the point is entering the loop. */
  private static final int MAX_ELEMENTS = 8;

  private static Object buildCollection(FuzzedDataProvider data, Class<?> raw,
      java.lang.reflect.Type elem)
  {
    int n = data.consumeInt(0, MAX_ELEMENTS);
    @SuppressWarnings("unchecked")
    java.util.Collection<Object> c =
        (java.util.Collection<Object>) newInstanceOf(data, effective(raw), java.util.ArrayList.class);
    for (int i = 0; i < n; i++) {
      try {
        c.add(build(data, elem));
      } catch (Unbuildable e) {
        break; // a partially filled collection is still better than an empty one
      }
    }
    return c;
  }

  private static Object buildMap(FuzzedDataProvider data, Class<?> raw,
      java.lang.reflect.Type k, java.lang.reflect.Type v)
  {
    int n = data.consumeInt(0, MAX_ELEMENTS);
    @SuppressWarnings("unchecked")
    java.util.Map<Object, Object> m =
        (java.util.Map<Object, Object>) newInstanceOf(data, effective(raw), java.util.LinkedHashMap.class);
    for (int i = 0; i < n; i++) {
      try {
        m.put(build(data, k), build(data, v));
      } catch (Unbuildable e) {
        break;
      }
    }
    return m;
  }

  /**
   * Instantiate {@code raw} if it is concrete, else the given fallback implementation.
   *
   * <p>The fallback is only ever used when it is assignable to {@code raw}. A concrete collection
   * class without a no-arg constructor (StablePriorityQueue) used to get an ArrayList here, which
   * reflection then rejected with "argument type mismatch" on both sides before the method body
   * ran — scored as agreement, a false EQUIVALENT.
   */
  private static Object newInstanceOf(FuzzedDataProvider data, Class<?> raw, Class<?> fallback)
  {
    if (!raw.isInterface() && !Modifier.isAbstract(raw.getModifiers())) {
      try {
        java.lang.reflect.Constructor<?> c = raw.getDeclaredConstructor();
        c.setAccessible(true);
        return c.newInstance();
      } catch (Throwable e) {
        // No usable no-arg constructor: try the class's other constructors
        // (StablePriorityQueue(int initialCapacity), for instance).
        Object v = viaConstructor(data, raw, 2);
        if (v != null) {
          return v;
        }
      }
    }
    if (!raw.isAssignableFrom(fallback)) {
      throw new Unbuildable("no instance of " + raw.getName() + " (fallback "
          + fallback.getName() + " does not fit)");
    }
    try {
      return fallback.getDeclaredConstructor().newInstance();
    } catch (Throwable e) {
      throw new Unbuildable("could not instantiate " + fallback.getName());
    }
  }

  /**
   * Build one value of {@code t}. Never returns a value for a type it could not honour: an
   * unbuildable type raises {@link Unbuildable} so the caller can report SKIP rather than
   * silently pass null and mistake a NullPointerException on both sides for equivalence.
   */
  static Object build(FuzzedDataProvider data, Class<?> t)
  {
    Object v = buildOrNull(data, t, CTOR_DEPTH);
    if (v != null) {
      return v;
    }
    Class<?> target = effective(t);
    if (isAbstractType(target)) {
      throw new Unbuildable("no concrete subtype of " + target.getName());
    }
    throw new Unbuildable("neither autofuzz nor constructor synthesis built " + target.getName());
  }

  /**
   * How many constructor/factory levels synthesis may nest. 2 was too shallow for a receiver whose
   * constructor needs a project interface: WindowIdActivatedReservoir(String, SweepableReservoir,
   * long) needs ForwardingReservoir(AbstractReservoir), which needs AbstractReservoir.newReservoir
   * — three levels.
   */
  private static final int CTOR_DEPTH = Integer.getInteger("fuzz.ctorDepth", 3);

  /**
   * Every tier, applied at any nesting level: the receiver, a method argument, and a constructor
   * parameter of either all go through here. Constructor parameters used to get only autofuzz and
   * plain constructor synthesis, so a constructor taking a project interface (which autofuzz cannot
   * implement and synthesis cannot instantiate) made the whole receiver unbuildable.
   *
   * <p>Returns null when nothing worked; {@link #build} turns that into {@link Unbuildable}.
   */
  private static Object buildOrNull(FuzzedDataProvider data, Class<?> t, int depth)
  {
    if (t.isArray()
        && t.getComponentType() == javax.security.auth.callback.Callback.class) {
      return buildCallbackArray(data, t.getComponentType());
    }
    if (Scalars.isScalar(t)) {
      return Scalars.build(data, t);
    }
    if (t.isArray() && Scalars.isScalar(t.getComponentType())) {
      return Scalars.buildArray(data, t.getComponentType(), data.consumeInt(0, 32));
    }
    Class<?> target = effective(t);
    Object v = viaRecipe(data, target);
    if (v != null) {
      return v;
    }
    v = tryConsume(data, target);
    if (v != null || depth <= 0) {
      return v;
    }
    // Autofuzz returned null: either this input starved, or it cannot construct the type at all.
    // If the type is abstract, substituting a concrete subtype is the only way forward.
    if (isAbstractType(target)) {
      // Try each candidate in rank order: the first-ranked one may be unbuildable (a private
      // constructor, a parameter nothing can supply) while the next one is fine.
      for (Class<?> sub : concreteSubtypesOf(target)) {
        v = tryConsume(data, sub);
        if (v != null) {
          return v;
        }
        v = viaConstructor(data, sub, depth);
        if (v != null) {
          return v;
        }
      }
      // The subtypes found may only be reachable through the type's own factory: AbstractReservoir's
      // implementations are private nested classes with private constructors, and the one public
      // way in is the static AbstractReservoir.newReservoir(String, int).
      return viaFactory(data, target, depth);
    }
    // Autofuzz is not a superset of the engine's original recursive constructor synthesis: it
    // returns null for classes that synthesis handles fine (SubscribeRequestTuple among them,
    // which regressed from EQUIVALENT to NEVER-RAN when autofuzz replaced it outright). Keep both.
    v = viaConstructor(data, target, depth);
    return v != null ? v : viaFactory(data, target, depth);
  }

  /** A hand-written {@link fuzz.auto.recipes.Recipe} for this class, if one is registered. */
  private static Object viaRecipe(FuzzedDataProvider data, Class<?> t)
  {
    fuzz.auto.recipes.Recipe r = fuzz.auto.recipes.Recipes.forClass(t.getName());
    if (r == null) {
      return null;
    }
    try {
      Object v = r.build(data, t);
      return t.isInstance(v) ? v : null;
    } catch (Throwable e) {
      return null; // fall back to the generic tiers
    }
  }

  private static boolean isAbstractType(Class<?> t)
  {
    return t.isInterface() || Modifier.isAbstract(t.getModifiers());
  }

  /**
   * Build {@code t} through a non-private static method declared on {@code t} that returns a
   * {@code t}, fewest parameters first. Methods are sorted by parameter count then signature so
   * both sides try them in the same order and consume the same bytes.
   */
  private static Object viaFactory(FuzzedDataProvider data, Class<?> t, int depth)
  {
    List<java.lang.reflect.Method> factories = new ArrayList<>();
    for (java.lang.reflect.Method m : t.getDeclaredMethods()) {
      int mod = m.getModifiers();
      if (Modifier.isStatic(mod) && !Modifier.isPrivate(mod) && !m.isSynthetic()
          && t.isAssignableFrom(m.getReturnType())) {
        factories.add(m);
      }
    }
    Collections.sort(factories, new Comparator<java.lang.reflect.Method>() {
      @Override
      public int compare(java.lang.reflect.Method a, java.lang.reflect.Method b) {
        if (a.getParameterCount() != b.getParameterCount()) {
          return Integer.compare(a.getParameterCount(), b.getParameterCount());
        }
        return a.toGenericString().compareTo(b.toGenericString());
      }
    });
    for (java.lang.reflect.Method m : factories) {
      Object[] args = buildArgs(data, m.getParameterTypes(), depth);
      if (args == null) {
        continue;
      }
      try {
        m.setAccessible(true);
        Object v = m.invoke(null, args);
        if (t.isInstance(v)) {
          return v;
        }
      } catch (Throwable e) {
        // this factory rejected the input or is unreachable — try the next
      }
    }
    return null;
  }

  /** Arguments for a constructor or factory, each one level deeper; null if any cannot be built. */
  private static Object[] buildArgs(FuzzedDataProvider data, Class<?>[] pts, int depth)
  {
    Object[] args = new Object[pts.length];
    for (int i = 0; i < pts.length; i++) {
      args[i] = buildOrNull(data, pts[i], depth - 1);
      if (args[i] == null) {
        return null;
      }
    }
    return args;
  }

  /**
   * The engine's original approach: pick the smallest non-private constructor whose parameters can
   * themselves be built, and invoke it. Depth-limited so a recursive type cannot loop.
   *
   * <p>Returns null rather than throwing — every failure here simply means "try something else".
   */
  private static Object viaConstructor(FuzzedDataProvider data, Class<?> t, int depth)
  {
    if (depth <= 0 || t.isInterface() || t.isArray() || t.isEnum()
        || Modifier.isAbstract(t.getModifiers())) {
      return null;
    }
    java.lang.reflect.Constructor<?>[] ctors = t.getDeclaredConstructors();
    // java.util.Arrays.sort(ctors,
    //     java.util.Comparator.comparingInt(java.lang.reflect.Constructor::getParameterCount));
    java.util.Arrays.sort(ctors, new java.util.Comparator<java.lang.reflect.Constructor<?>>() {
      @Override
        public int compare(java.lang.reflect.Constructor<?> a,
                          java.lang.reflect.Constructor<?> b) {
            return Integer.compare(a.getParameterCount(), b.getParameterCount());
        }
    });
    for (java.lang.reflect.Constructor<?> c : ctors) {
      if (Modifier.isPrivate(c.getModifiers())) {
        continue;
      }
      Object[] args = buildArgs(data, c.getParameterTypes(), depth);
      if (args == null) {
        continue;
      }
      try {
        c.setAccessible(true); // may throw InaccessibleObjectException on a JPMS-closed package
        return c.newInstance(args);
      } catch (Throwable e) {
        continue; // this constructor rejected the input or is unreachable — try the next
      }
    }
    return null;
  }

  /** True when {@link #build} has any chance for this type — used for a cheap pre-flight. */
  static boolean maybeBuildable(Class<?> t)
  {
    if (Scalars.isScalar(t) || t.isArray()) {
      return true;
    }
    Class<?> target = effective(t);
    if (fuzz.auto.recipes.Recipes.forClass(target.getName()) != null) {
      return true;                        // a hand-written recipe can build it
    }
    if (target.isInterface() || Modifier.isAbstract(target.getModifiers())) {
      return concreteSubtypeOf(target) != null;
    }
    return true;
  }

  private static Class<?> effective(Class<?> t)
  {
    Class<?> c = CONCRETE.get(t);
    return c != null ? c : t;
  }

  private static Object tryConsume(FuzzedDataProvider data, Class<?> t)
  {
    try {
      Object v = Autofuzz.consume(data, t);
      // Autofuzz finds implementations of an abstract type with its own class-path scan, so for a
      // project type it can return an object from the application class loader: same class name,
      // but not an instance of this side's type. Invoking the method on it would throw
      // IllegalArgumentException on both sides and pass for agreement, so treat it as "not built".
      return t.isInstance(v) ? v : null;
    } catch (Throwable e) {
      // A raw parameterized type throws ClassCastException inside autofuzz, and a constructor it
      // picked can throw anything at all. Both mean "not this way", not "test failed".
      return null;
    }
  }

  // ── concrete-subtype discovery ──────────────────────────────────────────────

  /** The first-ranked concrete subtype of {@code t}, or null; see {@link #concreteSubtypesOf}. */
  static Class<?> concreteSubtypeOf(Class<?> t)
  {
    List<Class<?>> subs = concreteSubtypesOf(t);
    return subs.isEmpty() ? null : subs.get(0);
  }

  /**
   * Find up to {@link #MAX_SUBTYPES} concrete, instantiable subtypes of an abstract class or
   * interface, best first. Result is cached per type: the search is expensive and there are only a
   * handful of abstract types per project.
   *
   * <p>Only a project type has project subtypes worth finding, and only its own side can supply
   * them: a subtype loaded anywhere else would not be assignable to it. So the candidates are the
   * classes that type's {@link SideLoader} can load, limited to the type's own root package (the
   * first two segments, e.g. {@code com.datatorrent}) so that thousands of library classes in the
   * fat jar are not loaded one by one. The order is deterministic and identical on both sides:
   * same package first, then the shortest (plainest) name, which in practice picks the project's
   * straightforward implementation over test doubles and inner adapters.
   */
  static List<Class<?>> concreteSubtypesOf(Class<?> t)
  {
    List<Class<?>> cached = SUBTYPES.get(t);
    if (cached != null) {
      return cached;
    }
    List<Class<?>> found = new ArrayList<>();
    if (t.getClassLoader() instanceof SideLoader) {
      SideLoader side = (SideLoader) t.getClassLoader();
      for (String name : candidates(t, side.classNames())) {
        Class<?> c = loadQuietly(name, side);
        if (c != null && isInstantiableSubtype(c, t)) {
          found.add(c);
          if (found.size() >= MAX_SUBTYPES) {
            break;
          }
        }
      }
    }
    found = Collections.unmodifiableList(found);
    SUBTYPES.put(t, found);
    return found;
  }

  /** Names in t's root package, same package first, then shortest, then alphabetical. */
  private static List<String> candidates(Class<?> t, List<String> names)
  {
    final String pkg = packageOf(t.getName());
    String[] parts = pkg.split("\\.");
    String root = parts.length >= 2 ? parts[0] + "." + parts[1] + "." : pkg + ".";
    List<String> out = new ArrayList<>();
    for (String name : names) {
      if (name.startsWith(root) && !name.equals(t.getName())) {
        out.add(name);
      }
    }
    Collections.sort(out, new Comparator<String>() {
      @Override
      public int compare(String a, String b) {
        boolean aHere = packageOf(a).equals(pkg);
        boolean bHere = packageOf(b).equals(pkg);
        if (aHere != bHere) {
          return aHere ? -1 : 1;
        }
        if (a.length() != b.length()) {
          return Integer.compare(a.length(), b.length());
        }
        return a.compareTo(b);
      }
    });
    return out;
  }

  private static String packageOf(String className)
  {
    int dot = className.lastIndexOf('.');
    return dot < 0 ? "" : className.substring(0, dot);
  }

  private static boolean isInstantiableSubtype(Class<?> c, Class<?> t)
  {
    if (c.isInterface() || Modifier.isAbstract(c.getModifiers()) || !t.isAssignableFrom(c)
        || c.isAnonymousClass() || c.isLocalClass()) {
      return false;
    }
    // A non-static inner class needs its enclosing instance; not worth the extra machinery.
    if (c.getEnclosingClass() != null && !Modifier.isStatic(c.getModifiers())) {
      return false;
    }
    return c.getDeclaredConstructors().length > 0;
  }

  private static Class<?> loadQuietly(String name, ClassLoader loader)
  {
    try {
      // initialize=false: loading a random project class must not run its static initializer,
      // which can touch config files, spawn threads, or throw.
      return Class.forName(name, false, loader);
    } catch (Throwable e) {
      return null;
    }
  }

  /** Scalar construction, split out so both this class and the ctor path share one definition. */
  static final class Scalars
  {
    private Scalars() {}

    static boolean isScalar(Class<?> t)
    {
      return t.isPrimitive() || t == String.class || t == Integer.class || t == Long.class
          || t == Short.class || t == Byte.class || t == Character.class || t == Boolean.class
          || t == Float.class || t == Double.class;
    }

    static Object build(FuzzedDataProvider d, Class<?> t)
    {
      if (t == int.class || t == Integer.class) return d.consumeInt();
      if (t == long.class || t == Long.class) return d.consumeLong();
      if (t == short.class || t == Short.class) return d.consumeShort();
      if (t == byte.class || t == Byte.class) return d.consumeByte();
      if (t == char.class || t == Character.class) return d.consumeChar();
      if (t == boolean.class || t == Boolean.class) return d.consumeBoolean();
      if (t == float.class || t == Float.class) return d.consumeFloat();
      if (t == double.class || t == Double.class) return d.consumeDouble();
      if (t == String.class) return d.consumeAsciiString(64);
      throw new Unbuildable("not a scalar: " + t.getName());
    }

    static Object buildArray(FuzzedDataProvider d, Class<?> comp, int n)
    {
      if (comp == byte.class) return d.consumeBytes(n);
      if (comp == int.class) return d.consumeInts(n);
      if (comp == long.class) return d.consumeLongs(n);
      if (comp == short.class) return d.consumeShorts(n);
      if (comp == boolean.class) return d.consumeBooleans(n);
      Object arr = java.lang.reflect.Array.newInstance(comp, n);
      for (int i = 0; i < n; i++) {
        java.lang.reflect.Array.set(arr, i, build(d, comp));
      }
      return arr;
    }
  }

  /** Reported in the SKIP line so a skip says which type defeated construction. */
  static String describeSubtype(Class<?> t)
  {
    Class<?> c = concreteSubtypeOf(t);
    return c == null ? "(none)" : c.getName();
  }
}
