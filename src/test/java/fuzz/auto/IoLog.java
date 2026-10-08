package fuzz.auto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * The inputs and outputs of one method's run, as a CSV table: one row per compared input.
 *
 * <p>Active only with {@code -Dfuzz.ioFile=<path.csv>} (scripts/run_project.py sets it per method).
 * Rows are kept in memory and written once, at JVM exit. Writing every input would take time out
 * of the fixed fuzzing budget, so only the first {@code fuzz.ioRows} inputs (default 10 million) are kept,
 * plus every input where the two versions differed.
 */
final class IoLog
{
  private IoLog() {}

  private static final String FILE = System.getProperty("fuzz.ioFile");
  private static final int MAX_ROWS = Integer.getInteger("fuzz.ioRows", 10000000);

  static final boolean ON = FILE != null;

  private static final List<String> rows = new ArrayList<>();

  /** Whether the next ordinary row still fits, so the caller renders its inputs before the call. */
  static synchronized boolean underCap()
  {
    return ON && rows.size() < MAX_ROWS;
  }

  static synchronized void add(String number, String receiver, String argTypes, String args,
      String original, String refactored, String comparison)
  {
    rows.add(csv(number) + "," + csv(receiver) + "," + csv(argTypes) + "," + csv(args) + ","
        + csv(original) + "," + csv(refactored) + "," + csv(comparison));
  }

  /** What the comparison concluded, for the last column. */
  static String comparison(String why, GenericDifferential.Outcome o, GenericDifferential.Outcome r)
  {
    if ("TIMEOUT".equals(o.exception) || "TIMEOUT".equals(r.exception)) {
      return "inconclusive (timeout)";
    }
    return why == null ? "same" : "differs: " + why;
  }

  /** The declared parameter types without package names: Collection<? extends E>. */
  static String argTypes(java.lang.reflect.Type[] types)
  {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < types.length; i++) {
      if (i > 0) {
        sb.append(", ");
      }
      java.lang.reflect.Type t = types[i];
      String name = t instanceof Class ? ((Class<?>) t).getName() : t.toString();
      sb.append(name.replaceAll("\\b[a-z][\\w]*\\.", ""));
    }
    return sb.toString();
  }

  /** Called once, at JVM exit. */
  static synchronized void write()
  {
    if (!ON) {
      return;
    }
    File out = new File(FILE);
    if (out.getParentFile() != null) {
      out.getParentFile().mkdirs();
    }
    try (Writer w = new OutputStreamWriter(new FileOutputStream(out), "UTF-8")) {
      w.write("#,receiver,arg types,args,original,refactored,comparison\n");
      for (String row : rows) {
        w.write(row);
        w.write('\n');
      }
    } catch (Exception e) {
      System.out.println("[IO-LOG] could not write " + FILE + ": " + e);
    }
  }

  private static String csv(String s)
  {
    if (s == null) {
      return "";
    }
    return "\"" + s.replace("\"", "\"\"").replace("\n", "\\n").replace("\r", "\\r") + "\"";
  }
}
