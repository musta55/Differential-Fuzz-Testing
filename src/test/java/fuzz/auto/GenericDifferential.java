package fuzz.auto;

import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
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
 * <p>A generated harness calls {@link #run(FuzzedDataProvider, String, String)} with its project
 * and method id. This engine looks the spec up in {@code /<project>/manifest.json}, resolves the
 * compiled {@code <Class>Original} and {@code <Class>Refactored} snapshots, builds the same
 * receiver and arguments for both, invokes each on a watchdog thread, and asserts equivalence.
 *
 * <h2>Identical inputs without deep-copying</h2>
 * Arguments are no longer restricted to scalars, so they cannot be cloned generically — and handing
 * both sides the same mutable instance would let the first call mutate it and make the second see a
 * different input. Instead this captures the iteration's fuzz bytes once and replays them into two
 * independent builds ({@link ReplayProvider}), giving structurally identical arguments that share
 * no references. Object construction itself is delegated to {@link ObjectFactory}, which uses
 * Jazzer's autofuzz and falls back to a concrete-subtype scan for abstract receivers.
 *
 * <h2>Equivalence</h2>
 * DIVERGENT iff the two sides differ in exception <em>type</em>, in return value, or in receiver
 * state after the call. Return values and receivers are compared by {@link Digest}, a structural
 * field walk, because domain classes inherit identity {@code equals()} and would otherwise diverge
 * on every input. A one-sided watchdog TIMEOUT is inconclusive, never divergent.
 *
 * <h2>Honesty about methods that never ran</h2>
 * A method whose receiver or arguments could not be built in any iteration would otherwise be
 * indistinguishable from one that ran clean, since neither reports a failure. The first successful
 * two-sided invocation therefore prints {@code [DIFF-RAN]}; a log without it means "never
 * exercised", not "equivalent".
 */
public final class GenericDifferential
{
  private GenericDifferential() {}

  private static final long CALL_TIMEOUT_MS = Long.getLong("fuzz.callTimeoutMs", 3000L);

  private static volatile boolean announcedRan = false;

  private static void announceRan()
  {
    if (!announcedRan) {
      announcedRan = true;
      System.out.println("[DIFF-RAN] both sides invoked at least once");
    }
  }

  // ── entry point used by the generated harnesses ─────────────────────────────

  public static void run(FuzzedDataProvider data, String project, String id) throws Throwable
  {
    Spec s = spec(project, id);
    Class<?> oCls = Class.forName(s.original);
    Class<?> rCls = Class.forName(s.refactored);

    // One capture per iteration, replayed twice below.
    //
    // Deliberately NOT gated on a minimum length. An early version required >= 24 bytes so that
    // autofuzz had something to work with, which quietly destroyed the search: libFuzzer grows
    // inputs from a few bytes, and an input rejected before execution yields no coverage feedback,
    // so it never learns to grow them. A scalar target that needed 3 bytes before then found
    // nothing at all — a known divergence (WebServicesVersionConversion.getConverter) went
    // undetected in 90s. Short inputs now run and simply degrade: ReplayProvider returns zeros on
    // exhaustion, and a starved object build raises Unbuildable, handled below.
    byte[] seed = data.consumeRemainingAsBytes();

    if (s.isCtor) {
      runCtor(seed, s, oCls, rCls);
      return;
    }

    Method oM = resolve(oCls, s);
    Method rM = resolve(rCls, s);
    oM.setAccessible(true);
    rM.setAccessible(true);

    // Permanent structural checks first, so an impossible target fails fast with a clear reason
    // instead of burning the whole budget returning from every iteration.
    if (!s.isStatic) {
      requireConstructible(oCls, s);
    }
    for (Class<?> pt : oM.getParameterTypes()) {
      if (!ObjectFactory.maybeBuildable(pt)) {
        skip(s, "parameter type has no buildable form: " + pt.getName());
      }
    }

    Side a;
    Side b;
    try {
      a = buildSide(seed, oCls, oM, s);
      b = buildSide(seed, rCls, rM, s);
    } catch (ObjectFactory.Unbuildable e) {
      // Transient: autofuzz starved or the constructor it chose rejected this input. Structural
      // impossibility was already ruled out above, so try another input.
      return;
    }

    // Snapshot receiver state BEFORE the call. Replaying identical bytes does not guarantee
    // identical receivers: the two sides are different classes, so autofuzz may pick a different
    // constructor or nested type for each. Comparing post-call state without checking that the
    // pre-call state matched reported "receiver state after call" divergences that the call had
    // nothing to do with (PhysicalNode.unblock, both sides returning true).
    boolean receiversStartedEqual = a.receiver != null && b.receiver != null
        && Digest.diff(a.receiver, b.receiver) == null;

    Outcome o = invokeTimed(oM, a.receiver, a.args);
    Outcome r = invokeTimed(rM, b.receiver, b.args);
    announceRan();

    String why = divergence(o, r, oM.getReturnType(), a.receiver, b.receiver, receiversStartedEqual);
    if (why != null) {
      Assertions.fail(String.format(
          "[DIFFERENTIAL MISMATCH] %s.%s/%d%n  reason    : %s%n  ctorArgs  : %s%n"
          + "  methodArgs: %s%n  original  : %s%n  refactored: %s",
          simple(s.original), s.method, s.arity, why, a.receiverDesc, render(a.args), o, r));
    }
  }

  // ── one side of one iteration ───────────────────────────────────────────────

  private static final class Side
  {
    Object receiver;
    Object[] args;
    String receiverDesc = "(static)";
  }

  /**
   * Build the receiver and arguments for one side from {@code seed}. Called twice with the same
   * bytes, so the two sides receive equal-but-independent values.
   */
  private static Side buildSide(byte[] seed, Class<?> cls, Method m, Spec s)
  {
    ReplayProvider p = new ReplayProvider(seed);
    Side side = new Side();
    if (!s.isStatic) {
      // Receiver first, so the byte layout matches between the two sides regardless of how many
      // bytes constructing it happens to consume.
      side.receiver = ObjectFactory.build(p, cls);
      side.receiverDesc = cls.getSimpleName() + "@built";
    }
    Class<?>[] pts = m.getParameterTypes();
    Object[] args = new Object[pts.length];
    for (int i = 0; i < pts.length; i++) {
      args[i] = ObjectFactory.build(p, pts[i]);
    }
    side.args = args;
    return side;
  }

  /** Fail fast (and loudly) when no input could ever produce a receiver for this class. */
  private static void requireConstructible(Class<?> cls, Spec s)
  {
    if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) {
      Class<?> sub = ObjectFactory.concreteSubtypeOf(cls);
      if (sub == null) {
        skip(s, "abstract/interface receiver with no concrete subtype on the classpath");
      }
      return;
    }
    if (cls.getDeclaredConstructors().length == 0) {
      skip(s, "no declared constructor");
    }
  }

  private static void skip(Spec s, String reason)
  {
    System.out.println("[SKIP] " + s.original + "." + s.method + " — " + reason);
    throw new Unsupported(reason);
  }

  /** Structural, permanent: this target cannot be tested by any input. */
  private static final class Unsupported extends RuntimeException
  {
    Unsupported(String m) { super(m); }
  }

  // ── manifest ────────────────────────────────────────────────────────────────

  private static final Map<String, Map<String, Spec>> CACHE = new HashMap<>();

  private static final class Spec
  {
    String original;
    String refactored;
    String method;
    int arity;
    /** Source-level parameter type names, used only to disambiguate same-arity overloads. */
    List<String> sourceParams = new ArrayList<>();
    boolean isStatic;
    boolean isCtor;
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
          sp.isCtor = m.has("ctor") && m.get("ctor").getAsBoolean();
          JsonArray ps = m.getAsJsonArray("params");
          for (int j = 0; j < ps.size(); j++) {
            sp.sourceParams.add(ps.get(j).getAsString());
          }
          // Parameter TYPES are not taken from the manifest. Source parsing cannot resolve a
          // simple name like `Configuration` to an FQN without replicating Java's import rules,
          // and getting it wrong is a silent NoSuchMethodException. Arity plus the compiled
          // class is enough, and reflection then gives the real types.
          sp.arity = m.has("arity") ? m.get("arity").getAsInt() : ps.size();
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

  // ── resolution by name + arity ──────────────────────────────────────────────

  /**
   * Find the method by name and arity on the compiled snapshot, disambiguating same-arity
   * overloads with the source-level parameter names from the manifest.
   */
  private static Method resolve(Class<?> c, Spec s) throws NoSuchMethodException
  {
    List<Method> exact = new ArrayList<>();
    for (Class<?> k = c; k != null; k = k.getSuperclass()) {
      for (Method m : k.getDeclaredMethods()) {
        if (m.getName().equals(s.method) && m.getParameterCount() == s.arity
            && !m.isSynthetic() && !exact.contains(m)) {
          exact.add(m);
        }
      }
    }
    if (exact.isEmpty()) {
      throw new NoSuchMethodException(c.getName() + "." + s.method + "/" + s.arity);
    }
    if (exact.size() == 1) {
      return exact.get(0);
    }
    Method best = null;
    int bestScore = -1;
    for (Method m : exact) {
      int score = 0;
      Class<?>[] pts = m.getParameterTypes();
      for (int i = 0; i < pts.length && i < s.sourceParams.size(); i++) {
        if (matches(pts[i], s.sourceParams.get(i))) {
          score++;
        }
      }
      if (score > bestScore) {
        bestScore = score;
        best = m;
      }
    }
    return best;
  }

  /** Does a reflected type plausibly correspond to this source-level type name? */
  private static boolean matches(Class<?> t, String sourceName)
  {
    String n = sourceName;
    int lt = n.indexOf('<');
    if (lt >= 0) {
      n = n.substring(0, lt); // erase generics: List<String> -> List
    }
    n = n.substring(n.lastIndexOf('.') + 1).trim();
    String actual = t.getSimpleName();
    return actual.equals(n) || actual.equalsIgnoreCase(n);
  }

  // ── invocation ──────────────────────────────────────────────────────────────

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
    return timed(() -> invoke(m, inst, args));
  }

  /** Run on a watchdog thread so a runaway input becomes a TIMEOUT rather than a stall. */
  private static Outcome timed(Callable<Outcome> body)
  {
    FutureTask<Outcome> task = new FutureTask<>(body);
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

  // ── oracle ──────────────────────────────────────────────────────────────────

  /**
   * Returns null when the two outcomes are equivalent, else a short reason naming what differed.
   *
   * <p>Order matters: exception type, then return value, then receiver state. A one-sided TIMEOUT
   * short-circuits to equivalent because it is timing-dependent and would otherwise manufacture
   * false positives on slow inputs.
   */
  private static String divergence(Outcome o, Outcome r, Class<?> returnType,
      Object oRecv, Object rRecv, boolean receiversStartedEqual)
  {
    if ("TIMEOUT".equals(o.exception) || "TIMEOUT".equals(r.exception)) {
      return null; // inconclusive
    }
    if (!Objects.equals(o.exception, r.exception)) {
      return "exception type";
    }
    if (!o.threw() && returnType != void.class) {
      if (isDirectlyComparable(returnType)) {
        if (!deepEquals(o.value, r.value)) {
          return "return value";
        }
      } else {
        String d = Digest.diff(o.value, r.value);
        if (d != null) {
          return "return value: " + d;
        }
      }
    }
    // Side effects on the receiver, but only when the two receivers started out equivalent —
    // otherwise a construction difference would be misreported as a behavioural one.
    if (receiversStartedEqual) {
      String d = Digest.diff(oRecv, rRecv);
      if (d != null) {
        return "receiver state after call: " + d;
      }
    }
    return null;
  }

  private static boolean isDirectlyComparable(Class<?> t)
  {
    return t.isPrimitive() || t == String.class || Number.class.isAssignableFrom(t)
        || t == Boolean.class || t == Character.class || t.isEnum()
        || (t.isArray() && t.getComponentType().isPrimitive());
  }

  /**
   * Value comparison that cannot throw. A domain class's own {@code equals()} is arbitrary code
   * and may throw; letting that escape would turn a testable method into a harness ERROR. On
   * failure fall back to the structural digest, which is total.
   */
  private static boolean deepEquals(Object a, Object b)
  {
    if (a == null || b == null) {
      return a == b;
    }
    try {
      if (a.getClass().isArray() && b.getClass().isArray()) {
        return Objects.deepEquals(a, b);
      }
      return Objects.equals(a, b);
    } catch (Throwable t) {
      return Digest.of(a).equals(Digest.of(b));
    }
  }

  // ── constructor differential ────────────────────────────────────────────────

  /**
   * A changed constructor is a test unit in its own right: build identical arguments for both,
   * invoke each, and compare exception type plus the structural state of the objects produced.
   */
  private static void runCtor(byte[] seed, Spec s, Class<?> oCls, Class<?> rCls) throws Throwable
  {
    Constructor<?> oc = resolveCtor(oCls, s);
    Constructor<?> rc = resolveCtor(rCls, s);
    oc.setAccessible(true);
    rc.setAccessible(true);
    for (Class<?> pt : oc.getParameterTypes()) {
      if (!ObjectFactory.maybeBuildable(pt)) {
        skip(s, "constructor parameter has no buildable form: " + pt.getName());
      }
    }
    Object[] argsA;
    Object[] argsB;
    try {
      argsA = buildCtorArgs(seed, oc);
      argsB = buildCtorArgs(seed, rc);
    } catch (ObjectFactory.Unbuildable e) {
      return; // transient
    }
    Outcome o = timed(() -> newInstanceOutcome(oc, argsA));
    Outcome r = timed(() -> newInstanceOutcome(rc, argsB));
    announceRan();

    String why = null;
    if ("TIMEOUT".equals(o.exception) || "TIMEOUT".equals(r.exception)) {
      why = null;
    } else if (!Objects.equals(o.exception, r.exception)) {
      why = "exception type";
    } else if (!o.threw()) {
      String d = Digest.diff(o.value, r.value);
      if (d != null) {
        why = "constructed state: " + d;
      }
    }
    if (why != null) {
      Assertions.fail(String.format(
          "[DIFFERENTIAL MISMATCH] %s.<init>/%d%n  reason    : %s%n  ctorArgs  : %s%n"
          + "  methodArgs: %s%n  original  : %s%n  refactored: %s",
          simple(s.original), s.arity, why, render(argsA), render(argsA), o, r));
    }
  }

  private static Object[] buildCtorArgs(byte[] seed, Constructor<?> c)
  {
    ReplayProvider p = new ReplayProvider(seed);
    Class<?>[] pts = c.getParameterTypes();
    Object[] args = new Object[pts.length];
    for (int i = 0; i < pts.length; i++) {
      args[i] = ObjectFactory.build(p, pts[i]);
    }
    return args;
  }

  private static Constructor<?> resolveCtor(Class<?> c, Spec s) throws NoSuchMethodException
  {
    List<Constructor<?>> cands = new ArrayList<>();
    for (Constructor<?> k : c.getDeclaredConstructors()) {
      if (k.getParameterCount() == s.arity && !k.isSynthetic()) {
        cands.add(k);
      }
    }
    if (cands.isEmpty()) {
      System.out.println("[SKIP] constructor not found " + c.getName() + "/" + s.arity);
      throw new NoSuchMethodException(c.getName() + ".<init>/" + s.arity);
    }
    if (cands.size() == 1) {
      return cands.get(0);
    }
    Constructor<?> best = null;
    int bestScore = -1;
    for (Constructor<?> k : cands) {
      int score = 0;
      Class<?>[] pts = k.getParameterTypes();
      for (int i = 0; i < pts.length && i < s.sourceParams.size(); i++) {
        if (matches(pts[i], s.sourceParams.get(i))) {
          score++;
        }
      }
      if (score > bestScore) {
        bestScore = score;
        best = k;
      }
    }
    return best;
  }

  private static Outcome newInstanceOutcome(Constructor<?> c, Object[] args)
  {
    try {
      return new Outcome(c.newInstance(args), null);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause() != null ? e.getCause() : e;
      return new Outcome(null, cause.getClass().getSimpleName());
    } catch (Throwable t) {
      return new Outcome(null, t.getClass().getSimpleName());
    }
  }

  // ── rendering ───────────────────────────────────────────────────────────────

  private static String simple(String fqn)
  {
    return fqn.substring(fqn.lastIndexOf('.') + 1);
  }

  private static String render(Object v)
  {
    if (v == null) {
      return "null";
    }
    if (v instanceof Object[]) {
      return renderArgs((Object[]) v);
    }
    if (v.getClass().isArray()) {
      return arrayToString(v);
    }
    try {
      String s = String.valueOf(v);
      return escape(s.length() > 120 ? s.substring(0, 120) + "..." : s);
    } catch (Throwable t) {
      return "<toString threw " + t.getClass().getSimpleName() + ">";
    }
  }

  /**
   * Escape non-printable characters in a reproducing input.
   *
   * <p>Fuzzed strings are full of control bytes and NULs. Printed raw, terminals and log viewers
   * swallow them and the recorded {@code methodArgs} reads as empty — which makes the one artifact
   * needed to reproduce a divergence by hand useless.
   */
  private static String escape(String s)
  {
    StringBuilder sb = new StringBuilder(s.length());
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c >= 0x20 && c < 0x7f) {
        sb.append(c);
      } else {
        sb.append(String.format("\\u%04x", (int) c));
      }
    }
    return sb.toString();
  }

  private static String renderArgs(Object[] args)
  {
    StringBuilder sb = new StringBuilder("[");
    for (int i = 0; i < args.length; i++) {
      if (i > 0) {
        sb.append(", ");
      }
      sb.append(render(args[i]));
    }
    return sb.append(']').toString();
  }

  private static String arrayToString(Object a)
  {
    if (a instanceof byte[]) return Arrays.toString((byte[]) a);
    if (a instanceof int[]) return Arrays.toString((int[]) a);
    if (a instanceof long[]) return Arrays.toString((long[]) a);
    if (a instanceof short[]) return Arrays.toString((short[]) a);
    if (a instanceof char[]) return Arrays.toString((char[]) a);
    if (a instanceof boolean[]) return Arrays.toString((boolean[]) a);
    if (a instanceof float[]) return Arrays.toString((float[]) a);
    if (a instanceof double[]) return Arrays.toString((double[]) a);
    return Arrays.deepToString((Object[]) a);
  }
}
