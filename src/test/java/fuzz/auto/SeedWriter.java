package fuzz.auto;

import java.io.File;
import java.io.FileWriter;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Turns the constants of an EvoSuite test suite into a Jazzer seed corpus.
 *
 * <pre>
 *   java -cp target/test-classes:&lt;deps&gt; fuzz.auto.SeedWriter \
 *        &lt;project&gt; &lt;seed-values.json&gt; &lt;test-resources-root&gt;
 * </pre>
 *
 * <p>Seeds are written where Jazzer's JUnit integration looks for them — the resource directory
 * {@code <pkg>/<HarnessClass>Inputs/<testMethod>/} — so {@code mvn test} picks them up with no
 * further configuration, both as libFuzzer's starting corpus when fuzzing and as the fixed inputs
 * replayed in regression mode.
 *
 * <h2>How a seed is produced</h2>
 * Not by writing an encoder. The engine's argument layout is whatever {@link ObjectFactory} and
 * Jazzer's autofuzz happen to consume — for an instance method that includes however many bytes the
 * receiver's constructor takes — and no independent encoder could track it. Instead this runs
 * {@link ObjectFactory} itself against a {@link SeedRecorder}, a provider that emits the bytes that
 * would have produced each value it returns. The result is a byte string laid out exactly the way
 * the fuzzer will read it.
 *
 * <h2>Why every seed is verified</h2>
 * A seed that decodes to arguments other than the ones intended is worse than no seed: it silently
 * changes what the corpus means while still looking like a successful integration. So each candidate
 * is decoded back through {@link GenericDifferential#buildSide}, the real path the harness uses, and
 * kept only if the arguments come back identical. Anything the recorder cannot express exactly
 * (non-ASCII in a String parameter, a surrogate char) fails that check and is dropped.
 *
 * <h2>Alignment</h2>
 * The extractor supplies an ordered pool of constants per test case, not a resolved call. Which of
 * them are receiver arguments and which are method arguments is unknown, so several alignments are
 * tried ({@link #STRATEGIES}) and every distinct verified result is written. Redundant seeds cost
 * one file each and libFuzzer discards the ones that add no coverage; a missed alignment would cost
 * the seed entirely.
 */
public final class SeedWriter
{
  private SeedWriter() {}

  /**
   * How many leading pool values to withhold from the receiver before serving the rest.
   *
   * <p>-1 means "serve the pool to everything, receiver included". The others reserve the first
   * {@code n} constants for the receiver's constructor and start the method arguments after them,
   * which is the shape EvoSuite emits: {@code new Widget(-2816); widget.combine(-2816, 1)} yields
   * the pool {@code [-2816, -2816, 1]}, and only the offset-1 alignment recovers {@code (-2816, 1)}.
   */
  private static final int[] STRATEGIES = {-1, 0, 1, 2};

  public static void main(String[] args) throws Exception
  {
    if (args.length < 3) {
      System.err.println("usage: SeedWriter <project> <seed-values.json> <test-resources-root>");
      System.exit(2);
    }
    String project = args[0];
    Path values = Paths.get(args[1]);
    Path root = Paths.get(args[2]);
    String projkey = project.replace('-', '_');

    JsonObject byId;
    try (Reader r = Files.newBufferedReader(values)) {
      byId = JsonParser.parseReader(r).getAsJsonObject();
    }

    int methods = 0;
    int written = 0;
    int rejected = 0;
    List<String> failures = new ArrayList<>();

    for (Map.Entry<String, JsonElement> e : byId.entrySet()) {
      String id = e.getKey();
      Path dir = root.resolve(String.format("fuzz/auto/%s/Auto_%s_FuzzTestInputs/differential",
          projkey, id.replace('.', '_')));
      Set<String> seen = new LinkedHashSet<>();
      int here = 0;
      for (JsonElement ce : e.getValue().getAsJsonArray()) {
        JsonObject c = ce.getAsJsonObject();
        String test = c.get("test").getAsString();
        List<Object> pool = pool(c.getAsJsonArray("values"));
        for (int skip : STRATEGIES) {
          byte[] seed;
          try {
            seed = build(project, id, pool, skip);
          } catch (Throwable t) {
            failures.add(id + "/" + test + " skip=" + skip + ": " + t);
            rejected++;
            continue;
          }
          if (seed == null) {
            rejected++;
            continue;
          }
          String key = sha1(seed);
          if (!seen.add(key)) {
            continue; // two alignments collapsed to the same bytes
          }
          Files.createDirectories(dir);
          Files.write(dir.resolve(test + "-" + skip + "-" + key.substring(0, 8)), seed);
          here++;
          written++;
        }
      }
      if (here > 0) {
        methods++;
      }
      System.out.printf("  %-45s %2d seeds%n", id, here);
    }

    System.out.printf("%nwrote %d seeds for %d methods (%d candidates rejected by verification)%n",
        written, methods, rejected);
    if (!failures.isEmpty()) {
      System.out.println("first failures:");
      for (String f : failures.subList(0, Math.min(5, failures.size()))) {
        System.out.println("  " + f);
      }
    }
    // Machine-readable summary so run.py can report seeding without re-parsing stdout.
    try (FileWriter w = new FileWriter(new File(values.getParent().toFile(), "seed-write.json"))) {
      w.write(String.format("{\"project\":\"%s\",\"seeds\":%d,\"methods\":%d,\"rejected\":%d}%n",
          project, written, methods, rejected));
    }
  }

  // ── recording + verification ────────────────────────────────────────────────

  /**
   * Record one seed, then prove it decodes back to the same arguments.
   *
   * @param skip pool entries withheld from the receiver; -1 serves the pool to the receiver too
   * @return the verified seed, or null if it did not survive verification
   */
  private static byte[] build(String project, String id, List<Object> pool, int skip)
      throws Exception
  {
    GenericDifferential.Spec s = GenericDifferential.spec(project, id);
    Class<?> cls = Class.forName(s.original);

    if (s.isCtor) {
      Constructor<?> ctor = GenericDifferential.resolveCtor(cls, s);
      SeedRecorder rec = new SeedRecorder(pool);
      Object[] recorded = buildArgs(rec, ctor.getGenericParameterTypes());
      byte[] seed = rec.seed();
      return same(recorded, decodeCtorArgs(seed, ctor)) ? seed : null;
    }

    Method m = GenericDifferential.resolve(cls, s);
    m.setAccessible(true);

    // One recorder for both phases: the receiver's bytes must precede the arguments' in the same
    // stream, exactly as buildSide reads them. Two recorders would produce a seed whose halves
    // decode at the wrong offsets.
    SeedRecorder rec = new SeedRecorder(pool);
    if (!s.isStatic) {
      rec.limitTo(skip < 0 ? pool.size() : skip);
      ObjectFactory.build(rec, cls);
    }
    rec.limitTo(pool.size());
    Object[] recorded = buildArgs(rec, m.getGenericParameterTypes());
    byte[] seed = rec.seed();

    GenericDifferential.Side decoded = GenericDifferential.buildSide(seed, cls, m, s);
    return same(recorded, decoded.args) ? seed : null;
  }

  /** Generic types throughout, matching GenericDifferential.buildSide byte for byte. */
  private static Object[] buildArgs(SeedRecorder rec, java.lang.reflect.Type[] types)
  {
    Object[] args = new Object[types.length];
    for (int i = 0; i < types.length; i++) {
      args[i] = ObjectFactory.build(rec, types[i]);
    }
    return args;
  }

  private static Object[] decodeCtorArgs(byte[] seed, Constructor<?> ctor)
  {
    ReplayProvider p = new ReplayProvider(seed);
    java.lang.reflect.Type[] pts = ctor.getGenericParameterTypes();
    Object[] args = new Object[pts.length];
    for (int i = 0; i < pts.length; i++) {
      args[i] = ObjectFactory.build(p, pts[i]);
    }
    return args;
  }

  /** Structural equality over built arguments — Digest, because domain types inherit identity equals. */
  private static boolean same(Object[] a, Object[] b)
  {
    if (a == null || b == null || a.length != b.length) {
      return false;
    }
    for (int i = 0; i < a.length; i++) {
      if (a[i] == null || b[i] == null) {
        if (a[i] != b[i]) {
          return false;
        }
        continue;
      }
      if (a[i].getClass().isArray() && b[i].getClass().isArray()) {
        if (!Objects.deepEquals(a[i], b[i])) {
          return false;
        }
        continue;
      }
      if (isSimple(a[i]) ? !a[i].equals(b[i]) : Digest.diff(a[i], b[i]) != null) {
        return false;
      }
    }
    return true;
  }

  private static boolean isSimple(Object o)
  {
    return o instanceof Number || o instanceof CharSequence || o instanceof Boolean
        || o instanceof Character;
  }

  // ── plumbing ────────────────────────────────────────────────────────────────

  /** JSON {type,value} records to boxed Java values, in order. */
  private static List<Object> pool(JsonArray values)
  {
    List<Object> out = new ArrayList<>();
    for (JsonElement el : values) {
      JsonObject o = el.getAsJsonObject();
      String type = o.get("type").getAsString();
      JsonElement v = o.get("value");
      if (v == null || v.isJsonNull()) {
        continue; // a null literal seeds nothing
      }
      switch (type) {
        case "int": out.add(v.getAsInt()); break;
        case "long": out.add(v.getAsLong()); break;
        case "double": out.add(v.getAsDouble()); break;
        case "boolean": out.add(v.getAsBoolean()); break;
        case "String": out.add(v.getAsString()); break;
        case "char":
          String cs = v.getAsString();
          if (!cs.isEmpty()) {
            out.add(cs.charAt(0));
          }
          break;
        default: break;
      }
    }
    return out;
  }

  private static String sha1(byte[] b)
  {
    try {
      byte[] d = MessageDigest.getInstance("SHA-1").digest(b);
      StringBuilder sb = new StringBuilder();
      for (byte x : d) {
        sb.append(String.format("%02x", x));
      }
      return sb.toString();
    } catch (Exception e) {
      return Integer.toHexString(Arrays.hashCode(b));
    }
  }
}
