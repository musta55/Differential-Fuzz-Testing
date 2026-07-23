package fuzz.auto;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.junit.jupiter.api.Assertions;

/**
 * Reflection-based differential oracle, driven by a per-project JSON manifest.
 *
 * A thin generated harness calls {@link #run(FuzzedDataProvider, String, String)} with its
 * project and method id; this engine looks the spec up in
 * {@code /<project>/manifest.json} (on the test classpath), resolves the compiled
 * {@code <Class>Original} and {@code <Class>Refactored} snapshots, builds the SAME args from
 * the fuzzer for both, invokes each (on a 3s watchdog thread), and asserts equivalence.
 *
 * Equivalence: DIVERGENT iff the two sides differ in exception TYPE (e.g. NPE vs
 * ArrayIndexOutOfBounds) or in scalar/String return value. A one-sided TIMEOUT (watchdog)
 * is treated as inconclusive (NOT divergent) since it is timing-dependent.
 */
public final class GenericDifferential
{
  private GenericDifferential() {}

  private static final long CALL_TIMEOUT_MS = Long.getLong("fuzz.callTimeoutMs", 3000L);

  // ── entry point used by the generated harnesses ─────────────────────────────

  public static void run(FuzzedDataProvider data, String project, String id) throws Throwable
  {
    Spec s = spec(project, id);
    Class<?> oCls = Class.forName(s.original);
    Class<?> rCls = Class.forName(s.refactored);
    Method oM = method(oCls, s.method, s.paramTypes);
    Method rM = method(rCls, s.method, s.paramTypes);
    oM.setAccessible(true);
    rM.setAccessible(true);

    // Build the receiver FIRST (consumes ctor args from the fuzzer), then the method args,
    // so both sides get identical inputs throughout.
    Object oInst = null;
    Object rInst = null;
    String ctorDesc = "(static)";
    if (!s.isStatic) {
      Object[] built;
      try {
        built = construct(oCls, rCls, data);
      } catch (CannotConstruct e) {
        // No constructor can be synthesized from fuzz data (even recursively) — structural
        // skip. Print the marker and rethrow so Jazzer stops this target fast (no 1-min no-op).
        System.out.println("[SKIP] cannot construct " + s.original + " (" + e.getMessage() + ")");
        throw e;
      }
      if (built == null) {
        return; // a buildable ctor rejected THIS input — let Jazzer try another input
      }
      oInst = built[0];
      rInst = built[1];
      ctorDesc = (String) built[2];
    }
    Object[] base = buildArgs(data, s.paramTypes);

    Outcome o = invokeTimed(oM, oInst, copy(base));
    Outcome r = invokeTimed(rM, rInst, copy(base));

    if (!equivalent(o, r, oM.getReturnType())) {
      Assertions.fail(String.format(
          "[DIFFERENTIAL MISMATCH] %s.%s%s%n  ctorArgs  : %s%n  methodArgs: %s%n"
          + "  original  : %s%n  refactored: %s",
          s.original.substring(s.original.lastIndexOf('.') + 1), s.method,
          Arrays.toString(s.paramTypes), ctorDesc, render(base), o, r));
    }
  }

  // ── manifest ────────────────────────────────────────────────────────────────

  private static final Map<String, Map<String, Spec>> CACHE = new HashMap<>();

  private static final class Spec
  {
    String original;
    String refactored;
    String method;
    Class<?>[] paramTypes;
    boolean isStatic;
  }

  private static synchronized Spec spec(String project, String id) throws Exception
  {
    Map<String, Spec> byId = CACHE.get(project);
    if (byId == null) {
      byId = new HashMap<>();
      String path = "/" + project + "/manifest.json";
      try (Reader r = new InputStreamReader(
          Objects.requireNonNull(GenericDifferential.class.getResourceAsStream(path),
              "manifest not found on classpath: " + path), "UTF-8")) {
        JsonObject root = JsonParser.parseReader(r).getAsJsonObject();
        JsonArray methods = root.getAsJsonArray("methods");
        for (int i = 0; i < methods.size(); i++) {
          JsonObject m = methods.get(i).getAsJsonObject();
          Spec sp = new Spec();
          sp.original = m.get("original").getAsString();
          sp.refactored = m.get("refactored").getAsString();
          sp.method = m.get("method").getAsString();
          sp.isStatic = m.get("static").getAsBoolean();
          JsonArray ps = m.getAsJsonArray("params");
          Class<?>[] pts = new Class<?>[ps.size()];
          for (int j = 0; j < ps.size(); j++) {
            pts[j] = classFor(ps.get(j).getAsString());
          }
          sp.paramTypes = pts;
          byId.put(m.get("id").getAsString(), sp);
        }
      }
      CACHE.put(project, byId);
    }
    Spec s = byId.get(id);
    if (s == null) {
      throw new IllegalStateException("id not in manifest: " + project + "/" + id);
    }
    return s;
  }

  // ── invocation + oracle ─────────────────────────────────────────────────────

  private static final class Outcome
  {
    final Object value;
    final String exception;
    Outcome(Object v, String ex) { value = v; exception = ex; }
    boolean threw() { return exception != null; }
    @Override public String toString() { return threw() ? "throws " + exception : "returns " + render(value); }
  }

  private static Outcome invoke(Method m, Object inst, Object[] args)
  {
    try {
      return new Outcome(m.invoke(inst, args), null);
    } catch (InvocationTargetException e) {
      Throwable c = e.getCause() != null ? e.getCause() : e;
      return new Outcome(null, c.getClass().getSimpleName());
    } catch (Throwable t) {
      return new Outcome(null, t.getClass().getSimpleName());
    }
  }

  private static Outcome invokeTimed(Method m, Object inst, Object[] args)
  {
    FutureTask<Outcome> task = new FutureTask<>(() -> invoke(m, inst, args));
    Thread t = new Thread(task, "diff-invoke");
    t.setDaemon(true);
    t.start();
    try {
      return task.get(CALL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
    } catch (TimeoutException te) {
      t.interrupt();
      return new Outcome(null, "TIMEOUT");
    } catch (Throwable e) {
      Throwable c = e.getCause() != null ? e.getCause() : e;
      return new Outcome(null, c.getClass().getSimpleName());
    }
  }

  /**
   * DIVERGENT iff exception types differ OR (neither threw) scalar/JDK return values differ.
   * A one-sided TIMEOUT is inconclusive (timing-dependent) → treated as NOT divergent so the
   * watchdog can't manufacture false positives on slow/ReDoS inputs.
   */
  private static boolean equivalent(Outcome o, Outcome r, Class<?> returnType)
  {
    boolean oTo = "TIMEOUT".equals(o.exception);
    boolean rTo = "TIMEOUT".equals(r.exception);
    if (oTo || rTo) {
      return true; // inconclusive, not a reliable divergence
    }
    if (!Objects.equals(o.exception, r.exception)) {
      return false; // different exception type (e.g. NPE vs AIOOBE) → DIVERGENT
    }
    if (o.threw()) {
      return true;
    }
    if (returnType == void.class) {
      return true;
    }
    if (isComparable(returnType)) {
      return deepEquals(o.value, r.value);
    }
    return (o.value == null) == (r.value == null);
  }

  private static boolean isComparable(Class<?> t)
  {
    return t.isPrimitive() || t == String.class || Number.class.isAssignableFrom(t)
        || t == Boolean.class || t == Character.class || t.isEnum()
        || (t.isArray() && t.getComponentType().isPrimitive());
  }

  // ── reflection helpers ──────────────────────────────────────────────────────

  private static Method method(Class<?> c, String name, Class<?>[] params) throws NoSuchMethodException
  {
    try {
      return c.getDeclaredMethod(name, params);
    } catch (NoSuchMethodException e) {
      return c.getMethod(name, params);
    }
  }

  /**
   * Build matching receiver instances for original + refactored, sharing the SAME fuzzer-built
   * constructor arguments. Prefers a no-arg ctor, else the smallest ctor whose params are all
   * fuzz-buildable (scalar/String/array) and that BOTH classes declare. Returns {oInst, rInst}
   * or null if none works (or the ctor throws for this input).
   */
  /** No constructor can be synthesized (even recursively) — structural skip. */
  private static final class CannotConstruct extends RuntimeException
  {
    CannotConstruct(String m) { super(m); }
  }

  /** A required constructor-parameter type couldn't be built from fuzz data. */
  private static final class CannotBuild extends RuntimeException
  {
    CannotBuild(String m) { super(m); }
  }

  /**
   * Build matching receiver instances for original + refactored, sharing the SAME fuzzer-built
   * constructor arguments. Tries each constructor smallest-first; a parameter is a scalar
   * (built directly) or another object built recursively (depth-limited). Returns
   * {oInst, rInst, ctorArgsDesc}; returns null if a buildable ctor rejected THIS input;
   * throws CannotConstruct if no constructor can be synthesized at all.
   */
  private static Object[] construct(Class<?> oCls, Class<?> rCls, FuzzedDataProvider d)
  {
    java.lang.reflect.Constructor<?>[] ctors = oCls.getDeclaredConstructors();
    Arrays.sort(ctors, java.util.Comparator.comparingInt(java.lang.reflect.Constructor::getParameterCount));
    for (java.lang.reflect.Constructor<?> oc : ctors) {
      if (java.lang.reflect.Modifier.isPrivate(oc.getModifiers())) {
        continue;
      }
      Class<?>[] pts = oc.getParameterTypes();
      java.lang.reflect.Constructor<?> rc;
      try {
        rc = rCls.getDeclaredConstructor(pts);
      } catch (NoSuchMethodException e) {
        continue; // refactored ctor signature differs — try another
      }
      Object[] cargs;
      try {
        cargs = buildCtorArgs(d, pts, 2);
      } catch (CannotBuild e) {
        continue; // can't synthesize a parameter — try another constructor
      }
      oc.setAccessible(true);
      rc.setAccessible(true);
      try {
        Object o = oc.newInstance(copy(cargs));
        Object r = rc.newInstance(copy(cargs));
        return new Object[]{o, r, render(cargs)};
      } catch (Throwable t) {
        return null; // ctor exists + buildable but rejected this input — try another input
      }
    }
    throw new CannotConstruct("no buildable constructor");
  }

  private static Object[] buildCtorArgs(FuzzedDataProvider d, Class<?>[] pts, int depth)
  {
    Object[] a = new Object[pts.length];
    for (int i = 0; i < pts.length; i++) {
      a[i] = buildCtorArg(d, pts[i], depth);
    }
    return a;
  }

  /** Scalar param built directly; object param constructed recursively (depth-limited). */
  private static Object buildCtorArg(FuzzedDataProvider d, Class<?> t, int depth)
  {
    if (isScalar(t)) {
      return buildOne(d, t);
    }
    if (depth <= 0 || t.isInterface() || t.isArray() || t.isEnum()
        || java.lang.reflect.Modifier.isAbstract(t.getModifiers())) {
      throw new CannotBuild(t.getName());
    }
    java.lang.reflect.Constructor<?>[] cs = t.getDeclaredConstructors();
    Arrays.sort(cs, java.util.Comparator.comparingInt(java.lang.reflect.Constructor::getParameterCount));
    for (java.lang.reflect.Constructor<?> c : cs) {
      if (java.lang.reflect.Modifier.isPrivate(c.getModifiers())) {
        continue;
      }
      Object[] sub;
      try {
        sub = buildCtorArgs(d, c.getParameterTypes(), depth - 1);
      } catch (CannotBuild e) {
        continue;
      }
      c.setAccessible(true);
      try {
        return c.newInstance(sub);
      } catch (Throwable th) {
        continue; // this ctor of t threw — try another
      }
    }
    throw new CannotBuild(t.getName());
  }

  private static boolean isScalar(Class<?> t)
  {
    return t.isPrimitive() || t == String.class
        || t == Integer.class || t == Long.class || t == Short.class || t == Byte.class
        || t == Character.class || t == Boolean.class || t == Float.class || t == Double.class
        || (t.isArray() && t.getComponentType().isPrimitive());
  }

  private static Class<?> classFor(String t) throws ClassNotFoundException
  {
    switch (t) {
      case "int": return int.class;
      case "long": return long.class;
      case "short": return short.class;
      case "byte": return byte.class;
      case "char": return char.class;
      case "boolean": return boolean.class;
      case "float": return float.class;
      case "double": return double.class;
      case "int[]": return int[].class;
      case "long[]": return long[].class;
      case "boolean[]": return boolean[].class;
      case "byte[]": return byte[].class;
      case "char[]": return char[].class;
      default: return Class.forName(t);
    }
  }

  // ── argument synthesis ──────────────────────────────────────────────────────

  private static Object[] buildArgs(FuzzedDataProvider d, Class<?>[] types)
  {
    Object[] args = new Object[types.length];
    for (int i = 0; i < types.length; i++) {
      args[i] = buildOne(d, types[i]);
    }
    return args;
  }

  private static Object buildOne(FuzzedDataProvider d, Class<?> t)
  {
    if (t == int.class || t == Integer.class) { return d.consumeInt(); }
    if (t == long.class || t == Long.class) { return d.consumeLong(); }
    if (t == short.class || t == Short.class) { return d.consumeShort(); }
    if (t == byte.class || t == Byte.class) { return d.consumeByte(); }
    if (t == char.class || t == Character.class) { return d.consumeChar(); }
    if (t == boolean.class || t == Boolean.class) { return d.consumeBoolean(); }
    if (t == float.class || t == Float.class) { return d.consumeFloat(); }
    if (t == double.class || t == Double.class) { return d.consumeDouble(); }
    if (t == String.class) { return d.consumeString(64); }
    if (t == byte[].class) { return d.consumeBytes(64); }
    if (t == int[].class) { return d.consumeInts(16); }
    if (t == long[].class) { return d.consumeLongs(16); }
    if (t == boolean[].class) { return d.consumeBooleans(16); }
    if (t == char[].class) { return d.consumeString(16).toCharArray(); }
    throw new IllegalArgumentException("unsupported parameter type: " + t);
  }

  // ── value plumbing ──────────────────────────────────────────────────────────

  private static Object[] copy(Object[] args)
  {
    Object[] out = new Object[args.length];
    for (int i = 0; i < args.length; i++) {
      Object a = args[i];
      if (a instanceof byte[]) { out[i] = ((byte[]) a).clone(); }
      else if (a instanceof int[]) { out[i] = ((int[]) a).clone(); }
      else if (a instanceof long[]) { out[i] = ((long[]) a).clone(); }
      else if (a instanceof boolean[]) { out[i] = ((boolean[]) a).clone(); }
      else if (a instanceof char[]) { out[i] = ((char[]) a).clone(); }
      else { out[i] = a; }
    }
    return out;
  }

  private static boolean deepEquals(Object a, Object b)
  {
    if (a == null || b == null) { return a == b; }
    if (a.getClass().isArray()) { return Objects.deepEquals(a, b); }
    return Objects.equals(a, b);
  }

  private static String render(Object v)
  {
    if (v == null) { return "null"; }
    if (v instanceof byte[]) { return Arrays.toString((byte[]) v); }
    if (v instanceof int[]) { return Arrays.toString((int[]) v); }
    if (v instanceof long[]) { return Arrays.toString((long[]) v); }
    if (v instanceof boolean[]) { return Arrays.toString((boolean[]) v); }
    if (v instanceof char[]) { return Arrays.toString((char[]) v); }
    if (v instanceof Object[]) { return Arrays.deepToString((Object[]) v); }
    String s = String.valueOf(v);
    return s.length() > 60 ? s.substring(0, 60) + "…" : s;
  }
}
