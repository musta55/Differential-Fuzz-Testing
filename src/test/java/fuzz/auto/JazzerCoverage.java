package fuzz.auto;

import java.lang.reflect.Method;

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
 */
final class JazzerCoverage
{
  private JazzerCoverage() {}

  private static final String RECORDER = "com.code_intelligence.jazzer.instrumentor.CoverageRecorder";

  private static volatile boolean installed;

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
    Runtime.getRuntime().addShutdownHook(new Thread(() -> dump(recorder, exec, report), "jazzer-cov-dump"));
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
      invokeDump(recorder, "dumpJacocoCoverage", exec);
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
