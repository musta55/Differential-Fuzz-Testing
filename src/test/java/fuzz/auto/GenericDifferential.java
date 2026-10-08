package fuzz.auto;

import java.io.InputStreamReader;
import java.io.PrintStream;
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
 * and method id. This engine looks the spec up in {@code /<project>/manifest.json}, loads the class
 * twice under its real name, once from each side's {@link SideLoader}, builds the same receiver and
 * arguments for both, invokes each on a watchdog thread, and asserts equivalence.
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
 * DIVERGENT iff the two sides differ in the exception they throw (class <em>and</em> cause chain),
 * in return value, or in receiver state after the call. Return values and receivers are compared by {@link Digest}, a structural
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

  /**
   * How much testing the fixed time budget actually bought.
   *
   * <p>Neither number is available from outside this class. Surefire reports "Tests run: 8", which
   * counts JUnit invocations — the seed corpus replayed once each plus a single call for the whole
   * fuzzing session — not fuzz iterations, and libFuzzer's own {@code #N} progress lines never reach
   * the log under the JUnit integration. So "30 seconds" was previously a duration with no idea how
   * many inputs fitted inside it.
   *
   * <p>The two counts differ, and the gap is the interesting part: {@code inputs} is every input the
   * fuzzer produced, {@code comparisons} is only those that got as far as invoking BOTH versions.
   * A method whose receiver is expensive to build can try a million inputs and compare almost none,
   * and that is precisely when an EQUIVALENT verdict should not be believed.
   */
  private static final java.util.concurrent.atomic.AtomicLong inputCount =
      new java.util.concurrent.atomic.AtomicLong();
  private static final java.util.concurrent.atomic.AtomicLong comparisonCount =
      new java.util.concurrent.atomic.AtomicLong();
  private static volatile boolean statsHookInstalled = false;

  private static synchronized void installStatsHook()
  {
    if (statsHookInstalled) {
      return;
    }
    statsHookInstalled = true;
    Runtime.getRuntime().addShutdownHook(new Thread(
        new Runnable() {
          @Override
          public void run() {
            if (nullMismatch != null) {
              System.out.println(nullMismatch);
            }
            IoLog.write();
            System.out.println("[DIFF-STATS] inputs=" + inputCount.get()
                + " comparisons=" + comparisonCount.get());
          }
        },
        "diff-stats"));
  }

  /** Sides already reported as unbuildable, so a 30-second run prints one line, not a million. */
  private static final java.util.Set<String> unbuildableSeen =
      java.util.Collections.synchronizedSet(new java.util.HashSet<String>());

  /**
   * Record why one side could not be built.
   *
   * <p>Without this a method that never ran is reported as "arguments were never built" and the
   * reader has to guess between two very different causes: a parameter type the engine cannot
   * synthesise, or a refactored constructor that now throws — the latter being a real finding about
   * the refactoring, hiding inside a non-result.
   */
  private static void noteUnbuildable(String side, Throwable e)
  {
    String msg = side + ": " + e.getMessage();
    if (unbuildableSeen.add(msg)) {
      System.out.println("[UNBUILT] " + msg);
    }
  }

  // ── entry point used by the generated harnesses ─────────────────────────────

  public static void run(FuzzedDataProvider data, String project, String id) throws Throwable
  {
    JazzerCoverage.installIfRequested();
    installStatsHook();
    inputCount.incrementAndGet();
    try {
      runOnce(data, project, id);
    } catch (AssertionError | Unsupported e) {
      throw e; // a DIFFERENTIAL MISMATCH or a [SKIP]: the engine's own, intended outcomes
    } catch (Throwable t) {
      // The methods under test run inside invoke()/newInstanceOutcome(), which turn whatever they
      // throw into an Outcome. Anything that reaches here therefore escaped the engine itself
      // (building arguments, digesting results, reading the manifest), not the refactoring.
      throw engineError(id, t);
    } finally {
      JazzerCoverage.collectAfterInput();
    }
  }

  /** Engine errors already reported, so a repeated failure prints one line. */
  private static final java.util.Set<String> engineErrorsSeen =
      java.util.Collections.synchronizedSet(new java.util.HashSet<String>());

  /**
   * Name an error that escaped the engine: what was thrown, where, and which engine step it came
   * through. Jazzer reports only the harness line and surefire trims the rest, so without this a
   * NullPointerException in PhysicalNode.hashCode() while ObjectFactory filled a Set read as a bare
   * "NullPointerException at Auto_LeastBusy_distribute_FuzzTest.java:14".
   */
  private static EngineError engineError(String id, Throwable t)
  {
    StackTraceElement[] st = t.getStackTrace();
    String origin = st.length > 0 ? frame(st[0]) : "unknown location";
    String step = "unknown engine step";
    for (StackTraceElement f : st) {
      String c = f.getClassName();
      if (c.startsWith("fuzz.auto.") && !c.substring(c.lastIndexOf('.') + 1).startsWith("Auto_")) {
        step = frame(f);
        break;
      }
    }
    String msg = "[ENGINE-ERROR] " + id + ": " + t + " at " + origin + " (engine step: " + step
        + ") — a fault in the fuzzing engine, not in the code under test";
    if (engineErrorsSeen.add(msg)) {
      System.out.println(msg);
    }
    return new EngineError(msg, t);
  }

  private static String frame(StackTraceElement f)
  {
    String c = f.getClassName();
    return c.substring(c.lastIndexOf('.') + 1) + "." + f.getMethodName() + ":" + f.getLineNumber();
  }

  /** Wraps an error that escaped the engine, with {@link #engineError}'s message. */
  private static final class EngineError extends RuntimeException
  {
    EngineError(String m, Throwable cause) { super(m, cause); }
  }

  // ── the null check ──────────────────────────────────────────────────────────

  /**
   * The fuzzer never builds null, yet null is a legal value for every object parameter, and a
   * refactoring can change what it does (StablePriorityQueue.addAll: NullPointerException vs false).
   * So once per method, both sides are also called with null in each object argument, one at a
   * time. A difference found that way is not raised: it would stop the fuzzing, and a difference on
   * normal inputs matters more. It is printed at exit as [NULL-MISMATCH], and the report shows it
   * as DIVERGENT ("null input") only when the fuzzing found no other difference.
   */
  private static volatile boolean nullChecked = false;
  private static volatile String nullMismatch = null;

  private static void nullCheckMethod(byte[] seed, Spec s, Class<?> oCls, Class<?> rCls,
      Method oM, Method rM)
  {
    Class<?>[] pts = oM.getParameterTypes();
    for (int i = 0; i < pts.length && nullMismatch == null; i++) {
      if (pts[i].isPrimitive()) {
        continue;
      }
      Side a;
      Side b;
      try {
        a = buildSide(seed, oCls, oM, s); // fresh sides: the normal call must not see this one
        b = buildSide(seed, rCls, rM, s);
      } catch (ObjectFactory.Unbuildable e) {
        return;
      }
      a.args[i] = null;
      b.args[i] = null;
      boolean receiversStartedEqual = a.receiver != null && b.receiver != null
          && Digest.diff(a.receiver, b.receiver) == null;
      String receiverText = s.isStatic ? "(static)" : show(a.receiver);
      String argsText = show(a.args);
      Outcome o = invokeTimed(oM, a.receiver, a.args, oCls);
      Outcome r = invokeTimed(rM, b.receiver, b.args, rCls);
      String why = divergence(o, r, oM.getReturnType(), a.receiver, b.receiver,
          receiversStartedEqual);
      if (IoLog.ON) {
        IoLog.add("null check", receiverText, IoLog.argTypes(oM.getGenericParameterTypes()),
            argsText, outcomeText(o), outcomeText(r), IoLog.comparison(why, o, r));
      }
      if (why != null) {
        nullMismatch = nullReport(simple(s.original) + "." + s.method + "/" + s.arity, i, pts[i],
            why, a.args, o, r);
      }
    }
  }

  private static void nullCheckCtor(byte[] seed, Spec s, final Constructor<?> oc,
      final Constructor<?> rc, Class<?> oCls, Class<?> rCls)
  {
    Class<?>[] pts = oc.getParameterTypes();
    for (int i = 0; i < pts.length && nullMismatch == null; i++) {
      if (pts[i].isPrimitive()) {
        continue;
      }
      final Object[] argsA;
      final Object[] argsB;
      try {
        argsA = buildCtorArgs(seed, oc);
        argsB = buildCtorArgs(seed, rc);
      } catch (ObjectFactory.Unbuildable e) {
        return;
      }
      argsA[i] = null;
      argsB[i] = null;
      Outcome o = timed(new Callable<Outcome>() {
        @Override
        public Outcome call() throws Exception {
          return newInstanceOutcome(oc, argsA);
        }
      }, oCls);
      Outcome r = timed(new Callable<Outcome>() {
        @Override
        public Outcome call() throws Exception {
          return newInstanceOutcome(rc, argsB);
        }
      }, rCls);
      String why = ctorDivergence(o, r);
      if (IoLog.ON) {
        IoLog.add("null check", "(constructor)", IoLog.argTypes(oc.getGenericParameterTypes()),
            show(argsA), outcomeText(o), outcomeText(r), IoLog.comparison(why, o, r));
      }
      if (why != null) {
        nullMismatch = nullReport(simple(s.original) + ".<init>/" + s.arity, i, pts[i], why,
            argsA, o, r);
      }
    }
  }

  private static String nullReport(String target, int i, Class<?> type, String why,
      Object[] args, Outcome o, Outcome r)
  {
    return String.format("[NULL-MISMATCH] %s%n  null arg  : #%d (%s)%n  reason    : %s%n"
        + "  methodArgs: %s%n  original  : %s%n  refactored: %s",
        target, i, type.getSimpleName(), why, render(args), o, r);
  }

  private static void runOnce(FuzzedDataProvider data, String project, String id) throws Throwable
  {
    Spec s = spec(project, id);
    Class<?> oCls = SideLoader.of(project, SideLoader.ORIGINAL).load(s.original);
    Class<?> rCls = SideLoader.of(project, SideLoader.REFACTORED).load(s.refactored);

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
    } catch (ObjectFactory.Unbuildable e) {
      // Transient: autofuzz starved or the constructor it chose rejected this input. Structural
      // impossibility was already ruled out above, so try another input.
      noteUnbuildable("original", e);
      if (IoLog.underCap()) {
        IoLog.add(String.valueOf(inputCount.get()), "(not built)",
            IoLog.argTypes(oM.getGenericParameterTypes()), inputBytes(seed), buildFailure(e),
            "(not run)", "not compared: original failed to build");
      }
      return;
    }
    try {
      b = buildSide(seed, rCls, rM, s);
    } catch (ObjectFactory.Unbuildable e) {
      // Split from the original's build on purpose. When a method is never exercised, the only
      // thing worth knowing is WHICH version refused to be built — a refactored constructor that
      // now throws looks identical, in a merged handler, to an argument type nothing can make.
      noteUnbuildable("refactored", e);
      if (IoLog.underCap()) {
        IoLog.add(String.valueOf(inputCount.get()), s.isStatic ? "(static)" : show(a.receiver),
            IoLog.argTypes(oM.getGenericParameterTypes()), show(a.args), "(built, not run)",
            buildFailure(e), "not compared: refactored failed to build");
      }
      return;
    }

    if (!nullChecked) {
      nullChecked = true;
      nullCheckMethod(seed, s, oCls, rCls, oM, rM);
    }

    // Snapshot receiver state BEFORE the call. Replaying identical bytes does not guarantee
    // identical receivers: the two sides are different classes, so autofuzz may pick a different
    // constructor or nested type for each. Comparing post-call state without checking that the
    // pre-call state matched reported "receiver state after call" divergences that the call had
    // nothing to do with (PhysicalNode.unblock, both sides returning true).
    boolean receiversStartedEqual = a.receiver != null && b.receiver != null
        && Digest.diff(a.receiver, b.receiver) == null;
    // The same guard for the arguments, so a method's writes into them can be compared too.
    boolean[] argsStartedEqual = new boolean[a.args.length];
    for (int i = 0; i < a.args.length; i++) {
      argsStartedEqual[i] = Digest.diff(a.args[i], b.args[i]) == null;
    }

    // The input as it was before the call (the call may change it), for the input/output table.
    String receiverText = null;
    String argsText = null;
    if (IoLog.underCap()) {
      receiverText = s.isStatic ? "(static)" : show(a.receiver);
      argsText = show(a.args);
    }

    Outcome o = invokeTimed(oM, a.receiver, a.args, oCls);
    Outcome r = invokeTimed(rM, b.receiver, b.args, rCls);
    announceRan();
    comparisonCount.incrementAndGet();

    String why = divergence(o, r, oM.getReturnType(), a.receiver, b.receiver, receiversStartedEqual);
    if (why == null) {
      why = argumentDivergence(o, r, a.args, b.args, argsStartedEqual);
    }
    if (receiverText != null || (IoLog.ON && why != null)) {
      if (receiverText == null) { // past the row cap, but a difference is always kept
        receiverText = (s.isStatic ? "(static)" : show(a.receiver)) + " (after call)";
        argsText = show(a.args) + " (after call)";
      }
      IoLog.add(String.valueOf(inputCount.get()), receiverText, IoLog.argTypes(oM.getGenericParameterTypes()),
          argsText, outcomeText(o), outcomeText(r), IoLog.comparison(why, o, r));
    }
    if (why != null) {
      Assertions.fail(String.format(
          "[DIFFERENTIAL MISMATCH] %s.%s/%d%n  reason    : %s%n  ctorArgs  : %s%n"
          + "  methodArgs: %s%n  original  : %s%n  refactored: %s",
          simple(s.original), s.method, s.arity, why, a.receiverDesc, render(a.args), o, r));
    }
  }

  // ── one side of one iteration ───────────────────────────────────────────────

  static final class Side
  {
    Object receiver;
    Object[] args;
    String receiverDesc = "(static)";
  }

  /**
   * Build the receiver and arguments for one side from {@code seed}. Called twice with the same
   * bytes, so the two sides receive equal-but-independent values.
   */
  static Side buildSide(byte[] seed, Class<?> cls, Method m, Spec s)
  {
    ClassLoader previous = SideLoader.useContextOf(cls);
    try {
      return buildSideIn(seed, cls, m, s);
    } finally {
      Thread.currentThread().setContextClassLoader(previous);
    }
  }

  private static Side buildSideIn(byte[] seed, Class<?> cls, Method m, Spec s)
  {
    ReplayProvider p = new ReplayProvider(seed);
    Side side = new Side();
    if (!s.isStatic) {
      // Receiver first, so the byte layout matches between the two sides regardless of how many
      // bytes constructing it happens to consume.
      side.receiver = ObjectFactory.build(p, cls);
      side.receiverDesc = cls.getSimpleName() + "@built";
    }
    // Generic types, not raw ones: a List<Integer> parameter must reach ObjectFactory with its
    // element type intact, or it is built empty and the method under test never enters its loop.
    java.lang.reflect.Type[] pts = m.getGenericParameterTypes();
    Object[] args = new Object[pts.length];
    for (int i = 0; i < pts.length; i++) {
      args[i] = ObjectFactory.build(p, pts[i]);
      requireFits(args[i], m.getParameterTypes()[i]);
    }
    side.args = args;
    return side;
  }

  /** Fail fast (and loudly) when no input could ever produce a receiver for this class. */
  private static void requireConstructible(Class<?> cls, Spec s)
  {
    if (cls.isInterface() || Modifier.isAbstract(cls.getModifiers())) {
      // The same check as for arguments: a recipe, or else a concrete subtype on the classpath.
      if (!ObjectFactory.maybeBuildable(cls)) {
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

  /**
   * Package-private, not private, so {@link SeedWriter} can drive the identical spec lookup,
   * overload resolution and argument construction when it encodes a seed. Duplicating any of that
   * would let the encoder and the fuzzer drift, and a seed that decodes to different arguments than
   * the engine builds is worse than no seed at all.
   */
  static final class Spec
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

  private static final Map<String, JsonObject> MANIFESTS = new HashMap<>();

  /** The parsed {@code /<project>/manifest.json}, read from the test classpath once. */
  static synchronized JsonObject manifest(String project) throws Exception
  {
    JsonObject root = MANIFESTS.get(project);
    if (root == null) {
      String path = "/" + project + "/manifest.json";
      try (Reader r = new InputStreamReader(
          Objects.requireNonNull(GenericDifferential.class.getResourceAsStream(path),
              "manifest not found on classpath: " + path), "UTF-8")) {
        root = JsonParser.parseReader(r).getAsJsonObject();
      }
      MANIFESTS.put(project, root);
    }
    return root;
  }

  static synchronized Spec spec(String project, String id) throws Exception
  {
    Map<String, Spec> byId = CACHE.get(project);
    if (byId == null) {
      byId = new HashMap<>();
      JsonArray methods = manifest(project).getAsJsonArray("methods");
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
  static Method resolve(Class<?> c, Spec s) throws NoSuchMethodException
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

  static final class Outcome
  {
    final Object value;
    final String exception;
    Outcome(Object v, String ex) { value = v; exception = ex; }
    boolean threw() { return exception != null; }
    @Override public String toString() { return threw() ? "throws " + exception : "returns " + render(value); }
  }

  /**
   * Name a thrown outcome by its class AND the chain of causes underneath it.
   *
   * <p>The simple name alone is not the observable behaviour of a throw. {@code
   * throw new Error(new NullPointerException())} and {@code throw new Error(new
   * UnsupportedOperationException())} are different things to every caller that inspects
   * {@code getCause()}, yet both reduce to "Error" and were reported EQUIVALENT (github issue #1).
   * Wrapping an exception in a runtime type is also a common refactoring, so the wrapped type is
   * exactly where a behaviour change hides.
   *
   * <p>Class names only, never messages: fuzzed messages routinely embed the input, a path or a
   * hash, so comparing them would report almost every method as divergent. Identity-tracked and
   * depth-capped because a cause chain may be self-referential or arbitrarily long.
   */
  private static String describeThrown(Throwable t)
  {
    StringBuilder sb = new StringBuilder();
    java.util.Set<Throwable> seen = java.util.Collections.newSetFromMap(
        new java.util.IdentityHashMap<Throwable, Boolean>());
    Throwable cur = t;
    for (int depth = 0; cur != null && depth < 5 && seen.add(cur); depth++) {
      if (depth > 0) {
        sb.append("<-");
      }
      sb.append(cur.getClass().getSimpleName());
      cur = cur.getCause();
    }
    return sb.toString();
  }

  private static Outcome invoke(Method m, Object inst, Object[] args)
  {
    PrintStream out = System.out;
    PrintStream err = System.err;
    try {
      return new Outcome(m.invoke(inst, args), null);
    } catch (InvocationTargetException e) {
      Throwable c = e.getCause() != null ? e.getCause() : e;
      return new Outcome(null, describeThrown(c));
    } catch (Throwable t) {
      return new Outcome(null, describeThrown(t));
    } finally {
      restoreStreams(out, err);
    }
  }

  /**
   * Put back System.out/err if the code under test replaced them. StreamingAppMaster.main wraps
   * them in a logging proxy on every call: the engine's own [DIFF-STATS] and mismatch lines then
   * vanished into log4j, and thousands of nested proxies overflowed the stack at exit.
   */
  private static void restoreStreams(PrintStream out, PrintStream err)
  {
    System.setOut(out);
    System.setErr(err);
  }

  private static Outcome invokeTimed(final Method m, final Object inst, final Object[] args,
      Class<?> side)
  {
    // return timed(() -> invoke(m, inst, args));
    return timed(new Callable<Outcome>() {
    @Override
      public Outcome call() throws Exception {
          return invoke(m, inst, args);
      }
    }, side);
  }

  /**
   * Run on a watchdog thread so a runaway input becomes a TIMEOUT rather than a stall. The thread's
   * context class loader is the side's own, as it would be inside that version of the project.
   */
  private static Outcome timed(Callable<Outcome> body, Class<?> side)
  {
    FutureTask<Outcome> task = new FutureTask<>(body);
    Thread t = new Thread(task, "diff-invoke");
    t.setContextClassLoader(side.getClassLoader());
    t.setDaemon(true);
    t.start();
    try {
      return task.get(CALL_TIMEOUT_MS, TimeUnit.MILLISECONDS);
    } catch (TimeoutException te) {
      t.interrupt();
      return new Outcome(null, "TIMEOUT");
    } catch (Throwable e) {
      Throwable c = e.getCause() != null ? e.getCause() : e;
      return new Outcome(null, describeThrown(c));
    }
  }

  // ── oracle ──────────────────────────────────────────────────────────────────

  /**
   * Side effects on the arguments. A method that returns void and writes its result into an
   * argument (DefaultCallbackHandler.processCallback calls namecb.setName(...)) was invisible to
   * the oracle, which looked only at the exception, the return value and the receiver: a wrong
   * value written into the argument still scored EQUIVALENT. Only arguments that started out
   * equivalent are compared, for the same reason as the receiver.
   */
  private static String argumentDivergence(Outcome o, Outcome r, Object[] oArgs, Object[] rArgs,
      boolean[] startedEqual)
  {
    if ("TIMEOUT".equals(o.exception) || "TIMEOUT".equals(r.exception)) {
      return null; // a timed-out call may still be writing into its arguments
    }
    for (int i = 0; i < oArgs.length; i++) {
      if (!startedEqual[i] || oArgs[i] == null || rArgs[i] == null) {
        continue;
      }
      String d = Digest.diff(oArgs[i], rArgs[i]);
      if (d != null) {
        return "argument " + i + " after call: " + d;
      }
    }
    return null;
  }

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
      if (a instanceof Enum && b instanceof Enum) {
        // The two sides' enum classes come from different loaders, so equals() is always false.
        return ((Enum<?>) a).name().equals(((Enum<?>) b).name());
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
    Constructor<?> ocResolved = resolveCtor(oCls, s);
    Constructor<?> rcResolved = resolveCtor(rCls, s);
    // An abstract class's constructor only ever runs as a subclass's super(...) call. newInstance
    // on it throws InstantiationException on BOTH sides before any constructor code executes, and
    // the comparison below scores two equal exceptions as agreement: every input, a false EQUIVALENT.
    // So run it the way Java does: through a concrete subclass's constructor with the same
    // parameters, the same subclass on both sides — the same substitution ObjectFactory makes for
    // an abstract argument type.
    if (Modifier.isAbstract(oCls.getModifiers()) || Modifier.isAbstract(rCls.getModifiers())) {
      Constructor<?>[] via = standInCtors(oCls, rCls, ocResolved.getParameterTypes());
      if (via == null) {
        skip(s, "abstract class with no concrete subclass constructor taking the same parameters");
      }
      ocResolved = via[0];
      rcResolved = via[1];
      announceVia(s, ocResolved);
    }
    final Constructor<?> oc = ocResolved;
    final Constructor<?> rc = rcResolved;
    oc.setAccessible(true);
    rc.setAccessible(true);
    for (Class<?> pt : oc.getParameterTypes()) {
      if (!ObjectFactory.maybeBuildable(pt)) {
        skip(s, "constructor parameter has no buildable form: " + pt.getName());
      }
    }
    final Object[] argsA;
    final Object[] argsB;
    try {
      argsA = buildCtorArgs(seed, oc);
      argsB = buildCtorArgs(seed, rc);
    } catch (ObjectFactory.Unbuildable e) {
      noteUnbuildable("constructor arguments", e);
      if (IoLog.underCap()) {
        IoLog.add(String.valueOf(inputCount.get()), "(constructor)",
            IoLog.argTypes(oc.getGenericParameterTypes()), inputBytes(seed), buildFailure(e),
            buildFailure(e), "not compared: arguments failed to build");
      }
      return; // transient
    }
    if (!nullChecked) {
      nullChecked = true;
      nullCheckCtor(seed, s, oc, rc, oCls, rCls);
    }
    // As for methods: a constructor can write into its arguments (fill a passed-in collection,
    // register itself with a passed-in object), so compare those that started out equivalent.
    boolean[] argsStartedEqual = new boolean[argsA.length];
    for (int i = 0; i < argsA.length; i++) {
      argsStartedEqual[i] = Digest.diff(argsA[i], argsB[i]) == null;
    }
    String argsText = IoLog.underCap() ? show(argsA) : null;
    // Outcome o = timed(() -> newInstanceOutcome(oc, argsA));
    Outcome o = timed(new Callable<Outcome>() {
    @Override
      public Outcome call() throws Exception {
          return newInstanceOutcome(oc, argsA);
      }
    }, oCls);
    // Outcome r = timed(() -> newInstanceOutcome(rc, argsB));
    Outcome r = timed(new Callable<Outcome>() {
    @Override
      public Outcome call() throws Exception {
          return newInstanceOutcome(rc, argsB);
      }
    }, rCls);
    announceRan();
    comparisonCount.incrementAndGet();

    String why = ctorDivergence(o, r);
    if (why == null) {
      why = argumentDivergence(o, r, argsA, argsB, argsStartedEqual);
    }
    if (argsText != null || (IoLog.ON && why != null)) {
      if (argsText == null) { // past the row cap, but a difference is always kept
        argsText = show(argsA) + " (after call)";
      }
      IoLog.add(String.valueOf(inputCount.get()), "(constructor)", IoLog.argTypes(oc.getGenericParameterTypes()),
          argsText, outcomeText(o), outcomeText(r), IoLog.comparison(why, o, r));
    }
    if (why != null) {
      Assertions.fail(String.format(
          "[DIFFERENTIAL MISMATCH] %s.<init>/%d%n  reason    : %s%n  ctorArgs  : %s%n"
          + "  methodArgs: %s%n  original  : %s%n  refactored: %s",
          simple(s.original), s.arity, why, render(argsA), render(argsA), o, r));
    }
  }

  /** How two constructor calls differ, or null: thrown exception type, then the built object. */
  private static String ctorDivergence(Outcome o, Outcome r)
  {
    if ("TIMEOUT".equals(o.exception) || "TIMEOUT".equals(r.exception)) {
      return null;
    }
    if (!Objects.equals(o.exception, r.exception)) {
      return "exception type";
    }
    if (!o.threw()) {
      String d = Digest.diff(o.value, r.value);
      if (d != null) {
        return "constructed state: " + d;
      }
    }
    return null;
  }

  /**
   * A concrete subclass constructor on each side that reaches the abstract constructor under test:
   * same subclass name on both sides, same parameter types (by name, since project types come from
   * different loaders) as the abstract one. Candidates come ranked from
   * {@link ObjectFactory#concreteSubtypesOf}, and a direct subclass is preferred, since its
   * constructor is the one most likely to be a plain super(...) pass-through. Returns
   * {original, refactored}, or null when no subclass fits.
   */
  private static Constructor<?>[] standInCtors(final Class<?> oCls, Class<?> rCls, Class<?>[] pts)
  {
    List<Class<?>> subs = new ArrayList<>(ObjectFactory.concreteSubtypesOf(oCls));
    // Stable sort: rank order is kept within each group.
    java.util.Collections.sort(subs, new java.util.Comparator<Class<?>>() {
      @Override
      public int compare(Class<?> a, Class<?> b) {
        return Boolean.compare(a.getSuperclass() != oCls, b.getSuperclass() != oCls);
      }
    });
    for (Class<?> oSub : subs) {
      Class<?> rSub;
      try {
        rSub = Class.forName(oSub.getName(), false, rCls.getClassLoader());
      } catch (Throwable e) {
        continue; // this subclass does not exist on the refactored side
      }
      if (!rCls.isAssignableFrom(rSub) || Modifier.isAbstract(rSub.getModifiers())) {
        continue;
      }
      Constructor<?> o = ctorTaking(oSub, pts);
      Constructor<?> r = ctorTaking(rSub, pts);
      if (o != null && r != null) {
        return new Constructor<?>[] {o, r};
      }
    }
    return null;
  }

  /** The non-private constructor of {@code c} whose parameter type names match {@code pts}. */
  private static Constructor<?> ctorTaking(Class<?> c, Class<?>[] pts)
  {
    for (Constructor<?> k : c.getDeclaredConstructors()) {
      if (k.isSynthetic() || Modifier.isPrivate(k.getModifiers())
          || k.getParameterCount() != pts.length) {
        continue;
      }
      Class<?>[] kts = k.getParameterTypes();
      boolean same = true;
      for (int i = 0; i < pts.length && same; i++) {
        same = kts[i].getName().equals(pts[i].getName());
      }
      if (same) {
        return k;
      }
    }
    return null;
  }

  private static volatile boolean announcedVia = false;

  /**
   * Say once which subclass stood in, so a verdict on an abstract constructor is read as "tested
   * through this subclass" — whose own constructor and overrides (parse(), say) ran as well.
   */
  private static void announceVia(Spec s, Constructor<?> c)
  {
    if (!announcedVia) {
      announcedVia = true;
      System.out.println("[CTOR-VIA] " + simple(s.original) + ".<init>/" + s.arity
          + " tested through " + c.getDeclaringClass().getName());
    }
  }

  private static Object[] buildCtorArgs(byte[] seed, Constructor<?> c)
  {
    ClassLoader previous = SideLoader.useContextOf(c.getDeclaringClass());
    try {
      ReplayProvider p = new ReplayProvider(seed);
      java.lang.reflect.Type[] pts = c.getGenericParameterTypes();
      Object[] args = new Object[pts.length];
      for (int i = 0; i < pts.length; i++) {
        args[i] = ObjectFactory.build(p, pts[i]);
        requireFits(args[i], c.getParameterTypes()[i]);
      }
      return args;
    } finally {
      Thread.currentThread().setContextClassLoader(previous);
    }
  }

  /**
   * An argument of the wrong type makes reflection throw IllegalArgumentException ("argument type
   * mismatch") on both sides before the body runs, and two equal exceptions score as agreement —
   * a false EQUIVALENT. Reject it here so it is reported as unbuilt instead.
   */
  private static void requireFits(Object arg, Class<?> param)
  {
    if (arg != null && !param.isPrimitive() && !param.isInstance(arg)) {
      throw new ObjectFactory.Unbuildable("built " + arg.getClass().getName()
          + " for parameter " + param.getName());
    }
  }

  static Constructor<?> resolveCtor(Class<?> c, Spec s) throws NoSuchMethodException
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
    PrintStream out = System.out;
    PrintStream err = System.err;
    try {
      return new Outcome(c.newInstance(args), null);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause() != null ? e.getCause() : e;
      return new Outcome(null, describeThrown(cause));
    } catch (Throwable t) {
      return new Outcome(null, describeThrown(t));
    } finally {
      restoreStreams(out, err);
    }
  }

  // ── rendering ───────────────────────────────────────────────────────────────

  private static String simple(String fqn)
  {
    return fqn.substring(fqn.lastIndexOf('.') + 1);
  }

  /**
   * A value for the input/output table: its own toString when its class has one, otherwise its
   * fields (Digest), since "StablePriorityQueue@38be305c" says nothing about what was passed in.
   */
  private static String show(Object v)
  {
    if (v == null) {
      return "null";
    }
    if (v instanceof Object[]) {
      Object[] a = (Object[]) v;
      StringBuilder sb = new StringBuilder("[");
      for (int i = 0; i < a.length; i++) {
        sb.append(i > 0 ? ", " : "").append(show(a[i]));
      }
      return sb.append("]").toString();
    }
    try {
      if (v.getClass().isArray()
          || v.getClass().getMethod("toString").getDeclaringClass() != Object.class) {
        return render(v);
      }
      String d = Digest.of(v);
      return escape(d.length() > 200 ? d.substring(0, 200) + "..." : d);
    } catch (Throwable t) {
      return render(v);
    }
  }

  /**
   * The raw fuzzer input, for a row whose receiver or arguments could not be built from it: no
   * object exists then, so the bytes they were being decoded from are the actual input.
   */
  private static String inputBytes(byte[] seed)
  {
    StringBuilder sb = new StringBuilder("input bytes (").append(seed.length).append("):");
    for (int i = 0; i < seed.length && i < 64; i++) {
      sb.append(String.format(" %02x", seed[i] & 0xff));
    }
    return sb.append(seed.length > 64 ? " ..." : "").toString();
  }

  /** The table text for a side whose receiver or arguments could not be built. */
  private static String buildFailure(Throwable e)
  {
    String m = String.valueOf(e.getMessage());
    return "failed to build: " + escape(m.length() > 200 ? m.substring(0, 200) + "..." : m);
  }

  private static String outcomeText(Outcome o)
  {
    return o.threw() ? "throws " + o.exception : "returns " + show(o.value);
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
