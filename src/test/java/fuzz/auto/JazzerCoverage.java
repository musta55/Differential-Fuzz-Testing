package fuzz.auto;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Dumps Jazzer's own coverage at JVM exit, in JaCoCo {@code .exec} format.
 *
 * <p>Jazzer already instruments every class it loads in order to drive its coverage-guided search,
 * so what it has accumulated by the end of a run is exactly "what the fuzzer reached" — a more
 * honest number than a second, independent JaCoCo agent, which also counts classes touched during
 * JUnit setup and manifest parsing. Jazzer exposes this as {@code --coverage_dump}, but that option
 * is only acted on by {@code FuzzTargetRunner.shutdown()}, which the JUnit integration never calls:
 * under {@code mvn test} the flag is silently inert (verified on 0.22.0 — passing
 * {@code -Djazzer.coverage_dump} produced no file). Registering the same two calls as a shutdown
 * hook produces the file the flag promises.
 *
 * <p>Everything here is reflective on purpose. {@code com.code_intelligence.jazzer.instrumentor} is
 * Jazzer's internal API, not its supported one, so a version bump may rename or relocate it. A
 * missing class must degrade to "no coverage file", never to a failed fuzz run.
 *
 * <p>Activated by {@code -Dfuzz.jazzerCoverage=<path.exec>}; {@code -Dfuzz.jazzerCoverageReport=
 * <path.txt>} additionally writes Jazzer's human-readable per-class summary.
 *
 * <h2>One file per side</h2>
 * Both sides load a class under the same name, and Jazzer keeps its per-class coverage record in a
 * map keyed by that name, so the side loaded second overwrites the first. {@link SideLoader} calls
 * {@link #remember} right after defining each class, while the record is still that side's, and at
 * exit each side's records are put back in turn and dumped to {@code <path>-original.exec} and
 * {@code <path>-refactored.exec}.
 */
final class JazzerCoverage
{
  private JazzerCoverage() {}

  private static final String RECORDER = "com.code_intelligence.jazzer.instrumentor.CoverageRecorder";

  private static volatile boolean installed;

  /** Jazzer's "add the current counters to the covered set", or null when coverage is off. */
  private static Method flush;

  /** Jazzer's own name -> coverage-record map, or null when coverage was not requested. */
  private static Map<Object, Object> jazzerRecords;

  /** The records each side's classes had when they were loaded: side -> (name -> record). */
  private static final Map<String, Map<Object, Object>> SIDE_RECORDS = new HashMap<>();

  /** Idempotent: the harness calls this on every iteration, but only the first one does work. */
  static synchronized void installIfRequested()
  {
    if (installed) {
      return;
    }
    installed = true;
    final String exec = System.getProperty("fuzz.jazzerCoverage");
    final String report = System.getProperty("fuzz.jazzerCoverageReport");
    if (exec == null && report == null) {
      return;
    }
    final Class<?> recorder;
    try {
      recorder = Class.forName(RECORDER);
    } catch (Throwable t) {
      System.out.println("[JAZZER-COV] unavailable: " + RECORDER + " not on the classpath");
      return;
    }
    jazzerRecords = recordsOf(recorder);
    try {
      flush = recorder.getMethod("updateCoveredIdsWithCoverageMap");
    } catch (Throwable t) {
      System.out.println("[JAZZER-COV] cannot collect coverage per input: " + t);
    }
    // Runtime.getRuntime().addShutdownHook(new Thread(() -> dump(recorder, exec, report), "jazzer-cov-dump"));
    Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
    @Override
      public void run() {
          dump(recorder, exec, report);
      }
    }, "jazzer-cov-dump"));
  }

  /** Jazzer's private {@code instrumentedClassInfo} map, keyed by internal class name. */
  @SuppressWarnings("unchecked")
  private static Map<Object, Object> recordsOf(Class<?> recorder)
  {
    try {
      Field f = recorder.getDeclaredField("instrumentedClassInfo");
      f.setAccessible(true);
      return (Map<Object, Object>) f.get(null);
    } catch (Throwable t) {
      System.out.println("[JAZZER-COV] per-side coverage unavailable: " + t);
      return null;
    }
  }

  /**
   * Add the coverage of the input that just ran to the covered set. Called after every input.
   *
   * <p>libFuzzer clears the coverage counters before each input, so the counters only ever hold
   * the current input's hits. Without this, the file written at exit held what ran before fuzzing
   * started plus the very last input, not the union over the run.
   */
  static void collectAfterInput()
  {
    if (flush == null) {
      return;
    }
    try {
      flush.invoke(null);
    } catch (Throwable t) {
      flush = null; // report nothing rather than fail every input
      System.out.println("[JAZZER-COV] per-input collection stopped: " + t);
    }
  }

  /** Keep a copy of the coverage record of a class one side has just loaded. */
  static synchronized void remember(String side, Class<?> c)
  {
    if (jazzerRecords == null) {
      return;
    }
    String name = c.getName().replace('.', '/');
    Object record = jazzerRecords.get(name);
    if (record != null) {
      Map<Object, Object> records = SIDE_RECORDS.get(side);
      if (records == null) {
        records = new HashMap<>();
        SIDE_RECORDS.put(side, records);
      }
      records.put(name, record);
    }
  }

  /** {@code target/jazzer-cov.exec} -> {@code target/jazzer-cov-original.exec}. */
  static String sidePath(String exec, String side)
  {
    int dot = exec.lastIndexOf('.');
    return dot < 0 ? exec + "-" + side : exec.substring(0, dot) + "-" + side + exec.substring(dot);
  }

  /**
   * Flush the live coverage map into the recorder's covered-id set, then write the files.
   *
   * <p>The flush is not optional: Jazzer keeps the current run's hits in a native counter map and
   * only folds them into {@code additionalCoverage} when asked, so dumping without it yields a file
   * describing an empty run.
   */
  private static void dump(Class<?> recorder, String exec, String report)
  {
    try {
      Method update = recorder.getMethod("updateCoveredIdsWithCoverageMap");
      update.invoke(null);
    } catch (Throwable t) {
      System.out.println("[JAZZER-COV] could not flush coverage map: " + t);
      return;
    }
    if (exec != null) {
      synchronized (JazzerCoverage.class) {
        if (SIDE_RECORDS.isEmpty()) {
          invokeDump(recorder, "dumpJacocoCoverage", exec);
        }
        for (Map.Entry<String, Map<Object, Object>> side : SIDE_RECORDS.entrySet()) {
          jazzerRecords.putAll(side.getValue());
          invokeDump(recorder, "dumpJacocoCoverage", sidePath(exec, side.getKey()));
        }
      }
    }
    if (report != null) {
      invokeDump(recorder, "dumpCoverageReport", report);
    }
  }

  private static void invokeDump(Class<?> recorder, String name, String path)
  {
    try {
      Method m = recorder.getMethod(name, String.class);
      m.invoke(null, path);
      System.out.println("[JAZZER-COV] " + name + " -> " + path);
    } catch (Throwable t) {
      System.out.println("[JAZZER-COV] " + name + " failed: " + t);
    }
  }
}
