package fuzz.auto;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
 *       for {@code MuxReservoir} and {@code SweepableReservoir}), so this scans the test classpath
 *       for a concrete subtype and asks autofuzz for that instead. This is what makes an
 *       abstract-receiver method testable at all.</li>
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
  }

  /** Cache of abstract/interface type -> discovered concrete subtype (or absent if none). */
  private static final Map<Class<?>, Class<?>> SUBTYPE = new ConcurrentHashMap<>();
  private static final Class<?> NONE = Void.class; // cache sentinel: scanned, found nothing
  private static volatile List<String> classpathNames;

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

  /** Element count for a generic collection; small, because the point is entering the loop. */
  private static final int MAX_ELEMENTS = 8;

  private static Object buildCollection(FuzzedDataProvider data, Class<?> raw,
      java.lang.reflect.Type elem)
  {
    int n = data.consumeInt(0, MAX_ELEMENTS);
    @SuppressWarnings("unchecked")
    java.util.Collection<Object> c =
        (java.util.Collection<Object>) newInstanceOf(effective(raw), java.util.ArrayList.class);
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
        (java.util.Map<Object, Object>) newInstanceOf(effective(raw), java.util.LinkedHashMap.class);
    for (int i = 0; i < n; i++) {
      try {
        m.put(build(data, k), build(data, v));
      } catch (Unbuildable e) {
        break;
      }
    }
    return m;
  }

  /** Instantiate {@code raw} if it is concrete, else the given fallback implementation. */
  private static Object newInstanceOf(Class<?> raw, Class<?> fallback)
  {
    if (!raw.isInterface() && !Modifier.isAbstract(raw.getModifiers())) {
      try {
        java.lang.reflect.Constructor<?> c = raw.getDeclaredConstructor();
        c.setAccessible(true);
        return c.newInstance();
      } catch (Throwable e) {
        // no usable no-arg constructor — fall through
      }
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
    if (Scalars.isScalar(t)) {
      return Scalars.build(data, t);
    }
    if (t.isArray() && Scalars.isScalar(t.getComponentType())) {
      return Scalars.buildArray(data, t.getComponentType(), data.consumeInt(0, 32));
    }
    Class<?> target = effective(t);
    Object v = tryConsume(data, target);
    if (v != null) {
      return v;
    }
    // Autofuzz returned null: either this input starved, or it cannot construct the type at all.
    // If the type is abstract, substituting a concrete subtype is the only way forward.
    if (target.isInterface() || Modifier.isAbstract(target.getModifiers())) {
      Class<?> sub = concreteSubtypeOf(target);
      if (sub != null) {
        v = tryConsume(data, sub);
        if (v != null) {
          return v;
        }
        v = viaConstructor(data, sub, 2);
        if (v != null) {
          return v;
        }
      }
      throw new Unbuildable("no concrete subtype of " + target.getName());
    }
    // Autofuzz is not a superset of the engine's original recursive constructor synthesis: it
    // returns null for classes that synthesis handles fine (SubscribeRequestTuple among them,
    // which regressed from EQUIVALENT to NEVER-RAN when autofuzz replaced it outright). Keep both.
    v = viaConstructor(data, target, 2);
    if (v != null) {
      return v;
    }
    throw new Unbuildable("neither autofuzz nor constructor synthesis built " + target.getName());
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
    java.util.Arrays.sort(ctors,
        java.util.Comparator.comparingInt(java.lang.reflect.Constructor::getParameterCount));
    for (java.lang.reflect.Constructor<?> c : ctors) {
      if (Modifier.isPrivate(c.getModifiers())) {
        continue;
      }
      Class<?>[] pts = c.getParameterTypes();
      Object[] args = new Object[pts.length];
      boolean ok = true;
      for (int i = 0; i < pts.length && ok; i++) {
        if (Scalars.isScalar(pts[i])) {
          args[i] = Scalars.build(data, pts[i]);
        } else if (pts[i].isArray() && Scalars.isScalar(pts[i].getComponentType())) {
          args[i] = Scalars.buildArray(data, pts[i].getComponentType(), data.consumeInt(0, 32));
        } else {
          Object sub = tryConsume(data, effective(pts[i]));
          if (sub == null) {
            sub = viaConstructor(data, effective(pts[i]), depth - 1);
          }
          args[i] = sub;
          ok = sub != null;
        }
      }
      if (!ok) {
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
      return Autofuzz.consume(data, t);
    } catch (Throwable e) {
      // A raw parameterized type throws ClassCastException inside autofuzz, and a constructor it
      // picked can throw anything at all. Both mean "not this way", not "test failed".
      return null;
    }
  }

  // ── concrete-subtype discovery ──────────────────────────────────────────────

  /**
   * Find a concrete, instantiable subtype of an abstract class or interface by scanning the test
   * classpath. Result is cached per type: the scan is expensive and there are only a handful of
   * abstract receivers per project.
   *
   * <p>Candidates are ordered by simple-name length so the most specific-looking implementation
   * loses to the plainest one, which in practice picks the project's straightforward
   * implementation over test doubles and inner adapters.
   */
  static Class<?> concreteSubtypeOf(Class<?> t)
  {
    Class<?> cached = SUBTYPE.get(t);
    if (cached != null) {
      return cached == NONE ? null : cached;
    }
    Class<?> found = null;
    List<String> names = classpathClassNames();
    List<String> ordered = new ArrayList<>(names);
    Collections.sort(ordered, (a, b) -> Integer.compare(a.length(), b.length()));
    for (String name : ordered) {
      Class<?> c = loadQuietly(name);
      if (c == null || c == t || c.isInterface() || Modifier.isAbstract(c.getModifiers())
          || !t.isAssignableFrom(c) || c.isAnonymousClass() || c.isLocalClass()) {
        continue;
      }
      // A non-static inner class needs its enclosing instance; not worth the extra machinery.
      if (c.getEnclosingClass() != null && !Modifier.isStatic(c.getModifiers())) {
        continue;
      }
      if (c.getDeclaredConstructors().length == 0) {
        continue;
      }
      found = c;
      break;
    }
    SUBTYPE.put(t, found == null ? NONE : found);
    return found;
  }

  private static Class<?> loadQuietly(String name)
  {
    try {
      // initialize=false: loading a random project class must not run its static initializer,
      // which can touch config files, spawn threads, or throw.
      return Class.forName(name, false, ObjectFactory.class.getClassLoader());
    } catch (Throwable e) {
      return null;
    }
  }

  /**
   * Class names on the test classpath, restricted to the entries that can plausibly hold the
   * project's own classes: the compiled snapshots under target/test-classes and the project's own
   * jars. Scanning every dependency jar (Hadoop, Guava, Kryo ...) would cost seconds and only add
   * library types that autofuzz already handles.
   */
  private static List<String> classpathClassNames()
  {
    List<String> cached = classpathNames;
    if (cached != null) {
      return cached;
    }
    List<String> out = new ArrayList<>();
    String cp = System.getProperty("java.class.path", "");
    for (String entry : cp.split(java.io.File.pathSeparator)) {
      if (entry.isEmpty()) {
        continue;
      }
      File f = new File(entry);
      if (f.isDirectory()) {
        collectDir(f, "", out);
      } else if (entry.endsWith(".jar") && isProjectJar(entry)) {
        collectJar(f, out);
      }
    }
    classpathNames = out;
    return out;
  }

  /** Heuristic: the locally installed project artifacts, not third-party libraries. */
  private static boolean isProjectJar(String path)
  {
    String p = path.replace('\\', '/');
    return p.contains("/org/apache/apex/local/") || p.contains("/local/")
        || p.contains("-local") || p.contains("-all");
  }

  private static void collectDir(File dir, String prefix, List<String> out)
  {
    File[] kids = dir.listFiles();
    if (kids == null) {
      return;
    }
    for (File k : kids) {
      if (k.isDirectory()) {
        collectDir(k, prefix + k.getName() + ".", out);
      } else if (k.getName().endsWith(".class") && k.getName().indexOf('$') < 0) {
        out.add(prefix + k.getName().substring(0, k.getName().length() - 6));
      }
    }
  }

  private static void collectJar(File jar, List<String> out)
  {
    JarFile jf = null;
    try {
      jf = new JarFile(jar);
      java.util.Enumeration<JarEntry> en = jf.entries();
      while (en.hasMoreElements()) {
        String n = en.nextElement().getName();
        if (n.endsWith(".class") && n.indexOf('$') < 0) {
          out.add(n.substring(0, n.length() - 6).replace('/', '.'));
        }
      }
    } catch (IOException e) {
      // an unreadable jar just contributes nothing
    } finally {
      if (jf != null) {
        try {
          jf.close();
        } catch (IOException ignored) {
          // nothing useful to do
        }
      }
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
