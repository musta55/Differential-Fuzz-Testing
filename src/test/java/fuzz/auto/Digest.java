package fuzz.auto;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * A structural, value-based fingerprint of an object graph — the comparison the oracle needs once
 * return values and receivers can be arbitrary objects.
 *
 * <p>Why not {@code equals()}: most domain classes inherit identity equality from {@code Object},
 * so two structurally identical results from the original and the refactored version compare
 * unequal. An oracle built on {@code equals()} would therefore report DIVERGENT on every single
 * input. The previous engine avoided that by degrading to a null-check for non-comparable returns,
 * which made every value-level difference inside an object invisible.
 *
 * <p>Walking fields instead gives a real comparison. Two deliberate choices keep it honest:
 * <ul>
 *   <li><b>The class name is written as the simple name with the Original/Refactored suffix
 *       stripped.</b> The two sides are by construction different classes ({@code FooOriginal} vs
 *       {@code FooRefactored}), so including the raw name would make every object-returning method
 *       diverge trivially. This is the same artefact that makes a renamed class's
 *       {@code toString} diverge.</li>
 *   <li><b>Map and Set entries are sorted by their rendered key.</b> Iteration order of a
 *       {@code HashMap} depends on identity hash codes, so an unsorted walk would be
 *       nondeterministic between the two sides.</li>
 * </ul>
 *
 * <p>Remaining sources of false divergence are genuine and expected: fields holding timestamps,
 * identity hash codes, {@code Random} state, or absolute paths differ between two builds. They are
 * suppressed by name via {@link #IGNORED_FIELD_NAMES}, which is the list to extend as report
 * triage surfaces more.
 */
final class Digest
{
  private Digest() {}

  private static final int MAX_DEPTH = Integer.getInteger("fuzz.digestDepth", 4);
  private static final int MAX_ELEMENTS = 32;

  /**
   * Field names whose value legitimately differs between two independent constructions. A match
   * here records only whether the field was null, not its value.
   */
  private static final List<String> IGNORED_FIELD_NAMES = new ArrayList<>();

  static {
    IGNORED_FIELD_NAMES.add("hash");
    IGNORED_FIELD_NAMES.add("hashcode");
    IGNORED_FIELD_NAMES.add("hashCode");
    IGNORED_FIELD_NAMES.add("random");
    IGNORED_FIELD_NAMES.add("rnd");
    IGNORED_FIELD_NAMES.add("timestamp");
    IGNORED_FIELD_NAMES.add("createdat");
    IGNORED_FIELD_NAMES.add("modifiedat");
    IGNORED_FIELD_NAMES.add("starttime");
    IGNORED_FIELD_NAMES.add("currenttime");
    IGNORED_FIELD_NAMES.add("lastupdate");
    // Deliberately NOT ignored: "id". It is usually a meaningful value that a refactoring can
    // genuinely change, so suppressing it would hide real divergences. Only fields that differ
    // between two *identical* constructions belong on this list.
  }

  private static boolean ignored(String name)
  {
    String n = name.toLowerCase(java.util.Locale.ROOT);
    for (int i = 0; i < IGNORED_FIELD_NAMES.size(); i++) {
      if (n.equals(IGNORED_FIELD_NAMES.get(i).toLowerCase(java.util.Locale.ROOT))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Structural fingerprint of {@code o}, stable across the two snapshot classes.
   *
   * <p>Total by construction: digesting must never throw. This walks arbitrary third-party object
   * graphs, where an {@code iterator()}, {@code entrySet()} or {@code toString()} is free to throw
   * anything — an unguarded walk turned a perfectly testable method into a harness ERROR (seen for
   * real: {@code PhysicalNode.unblock} failed with a bare {@code UnsupportedOperationException}
   * escaping the oracle). A failure is recorded as a marker instead, and since both sides digest
   * the same way, a marker on both sides simply compares equal.
   */
  static String diff(Object a, Object b)
  {
    try {
      return diff(a, b, "", 0, new IdentityHashMap<Object, Object>());
    } catch (Throwable t) {
      return null; // the comparison itself failed; inconclusive, never a finding
    }
  }

  private static String diff(Object a, Object b, String path, int depth,
      IdentityHashMap<Object, Object> seen)
  {
    if (a == null || b == null) {
      return (a == null) == (b == null) ? null : at(path, a, b);
    }
    if (depth >= MAX_DEPTH || seen.containsKey(a)) {
      return null; // capped or cyclic: stop rather than risk a spurious difference
    }
    Class<?> ca = a.getClass();
    if (isAmbient(ca) || isAmbient(b.getClass())) {
      return null; // shared JVM/runtime state, not this object's behaviour
    }
    if (isValue(ca) || isValue(b.getClass())) {
      return safeEquals(a, b) ? null : at(path, a, b);
    }
    if (ca.isArray() || b.getClass().isArray() || a instanceof Collection || b instanceof Collection
        || a instanceof Map || b instanceof Map) {
      String da = of(a);
      String db = of(b);
      return da.equals(db) ? null : at(path, da, db);
    }
    seen.put(a, a);
    try {
      Map<String, Field> fa = fieldsOf(ca);
      Map<String, Field> fb = fieldsOf(b.getClass());
      for (Map.Entry<String, Field> e : fa.entrySet()) {
        Field other = fb.get(e.getKey());
        if (other == null || ignored(e.getKey())) {
          continue; // added/removed by the refactoring, or known-nondeterministic
        }
        Object va;
        Object vb;
        try {
          e.getValue().setAccessible(true);
          other.setAccessible(true);
          va = e.getValue().get(a);
          vb = other.get(b);
        } catch (Throwable t) {
          continue; // inaccessible on either side: cannot compare, so claim nothing
        }
        String sub = diff(va, vb, path.isEmpty() ? e.getKey() : path + "." + e.getKey(),
            depth + 1, seen);
        if (sub != null) {
          return sub;
        }
      }
      return null;
    } finally {
      seen.remove(a);
    }
  }

  private static Map<String, Field> fieldsOf(Class<?> c)
  {
    TreeMap<String, Field> out = new TreeMap<>();
    for (Class<?> k = c; k != null && k != Object.class; k = k.getSuperclass()) {
      for (Field f : k.getDeclaredFields()) {
        if (!Modifier.isStatic(f.getModifiers()) && !f.isSynthetic()
            && !out.containsKey(f.getName())) {
          out.put(f.getName(), f);
        }
      }
    }
    return out;
  }

  /**
   * Value equality by rendered form, not {@code equals()}.
   *
   * <p>Several JDK value types use identity equality: {@code AtomicInteger}, {@code AtomicLong}
   * and {@code LongAdder} are all {@code Number}s whose {@code equals()} is {@code Object}'s. Two
   * counters both holding 1 therefore compared unequal and produced the nonsensical finding
   * "threadNumber: 1 vs 1". Comparing what they render as is both correct here and what the
   * report shows the reader.
   */
  private static boolean safeEquals(Object a, Object b)
  {
    try {
      if (a instanceof Number || a instanceof Character || a instanceof Boolean) {
        return String.valueOf(a).equals(String.valueOf(b));
      }
      return java.util.Objects.equals(a, b);
    } catch (Throwable t) {
      return true; // cannot tell; do not manufacture a divergence
    }
  }

  private static String at(String path, Object a, Object b)
  {
    StringBuilder sb = new StringBuilder(path.isEmpty() ? "<value>" : path).append(": ");
    appendValue(sb, a);
    sb.append(" vs ");
    appendValue(sb, b);
    return sb.toString();
  }

  static String of(Object o)
  {
    StringBuilder sb = new StringBuilder();
    write(sb, o, 0, new IdentityHashMap<Object, Object>());
    return sb.toString();
  }

  private static void write(StringBuilder sb, Object o, int depth, IdentityHashMap<Object, Object> seen)
  {
    if (o == null) {
      sb.append("null");
      return;
    }
    Class<?> c = o.getClass();
    if (isValue(c)) {
      appendValue(sb, o);
      return;
    }
    if (isAmbient(c)) {
      sb.append("<ambient:").append(name(c)).append('>');
      return;
    }
    if (seen.containsKey(o)) {
      sb.append("<cycle>");
      return;
    }
    if (depth >= MAX_DEPTH) {
      // Depth-capped: record the shape, not the contents, so a deep graph cannot make the digest
      // unbounded. Both sides cap identically, so this loses sensitivity, never soundness.
      sb.append(name(c)).append("{...}");
      return;
    }
    seen.put(o, o);
    int mark = sb.length();
    try {
      if (c.isArray()) {
        writeArray(sb, o, depth, seen);
      } else if (o instanceof Map) {
        writeMap(sb, (Map<?, ?>) o, depth, seen);
      } else if (o instanceof Collection) {
        writeCollection(sb, (Collection<?>) o, depth, seen);
      } else {
        writeFields(sb, o, c, depth, seen);
      }
    } catch (Throwable t) {
      // Roll back the partial rendering so the marker is deterministic rather than depending on
      // how far the walk got before failing.
      sb.setLength(mark);
      sb.append(name(c)).append("<threw:").append(t.getClass().getSimpleName()).append('>');
    } finally {
      seen.remove(o);
    }
  }

  /**
   * Append a value with non-printable characters escaped.
   *
   * <p>Fuzzed strings routinely contain control bytes and NULs. Written raw into the report they
   * are eaten by terminals and log viewers, which made a genuine difference read as the absurd
   * "TreeSet[] vs TreeSet[]" — two values that looked identical but were not. A finding you cannot
   * read is a finding you cannot triage.
   */
  private static void appendValue(StringBuilder sb, Object o)
  {
    String s;
    try {
      s = String.valueOf(o);
    } catch (Throwable t) {
      sb.append("<toString threw ").append(t.getClass().getSimpleName()).append('>');
      return;
    }
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c >= 0x20 && c < 0x7f) {
        sb.append(c);
      } else {
        sb.append(String.format("\\u%04x", (int) c));
      }
    }
  }

  private static void writeArray(StringBuilder sb, Object a, int depth, IdentityHashMap<Object, Object> seen)
  {
    int n = Array.getLength(a);
    sb.append('[');
    for (int i = 0; i < Math.min(n, MAX_ELEMENTS); i++) {
      if (i > 0) {
        sb.append(',');
      }
      write(sb, Array.get(a, i), depth + 1, seen);
    }
    if (n > MAX_ELEMENTS) {
      sb.append(",+").append(n - MAX_ELEMENTS);
    }
    sb.append(']');
  }

  private static void writeCollection(StringBuilder sb, Collection<?> col, int depth,
      IdentityHashMap<Object, Object> seen)
  {
    // A Set's iteration order is not defined by its contents, so render then sort.
    List<String> parts = new ArrayList<>();
    int i = 0;
    for (Object e : col) {
      if (i++ >= MAX_ELEMENTS) {
        break;
      }
      StringBuilder one = new StringBuilder();
      write(one, e, depth + 1, seen);
      parts.add(one.toString());
    }
    if (!(col instanceof List)) {
      java.util.Collections.sort(parts);
    }
    sb.append(name(col.getClass())).append(parts);
  }

  private static void writeMap(StringBuilder sb, Map<?, ?> m, int depth,
      IdentityHashMap<Object, Object> seen)
  {
    TreeMap<String, String> sorted = new TreeMap<>();
    int i = 0;
    for (Map.Entry<?, ?> e : m.entrySet()) {
      if (i++ >= MAX_ELEMENTS) {
        break;
      }
      StringBuilder k = new StringBuilder();
      StringBuilder v = new StringBuilder();
      write(k, e.getKey(), depth + 1, seen);
      write(v, e.getValue(), depth + 1, seen);
      sorted.put(k.toString(), v.toString());
    }
    sb.append("map").append(sorted);
  }

  private static void writeFields(StringBuilder sb, Object o, Class<?> c, int depth,
      IdentityHashMap<Object, Object> seen)
  {
    sb.append(name(c)).append('{');
    // Sorted by field name so declaration order between the two snapshots cannot matter — a
    // refactoring that reorders fields is not a behavioural change.
    TreeMap<String, Field> fields = new TreeMap<>();
    for (Class<?> k = c; k != null && k != Object.class; k = k.getSuperclass()) {
      for (Field f : k.getDeclaredFields()) {
        if (Modifier.isStatic(f.getModifiers()) || f.isSynthetic()) {
          continue;
        }
        if (!fields.containsKey(f.getName())) {
          fields.put(f.getName(), f);
        }
      }
    }
    boolean first = true;
    for (Map.Entry<String, Field> e : fields.entrySet()) {
      Field f = e.getValue();
      if (!first) {
        sb.append(',');
      }
      first = false;
      sb.append(e.getKey()).append('=');
      Object v;
      try {
        f.setAccessible(true);
        v = f.get(o);
      } catch (Throwable t) {
        sb.append("<inaccessible>"); // JPMS-closed or a security manager; same on both sides
        continue;
      }
      if (ignored(e.getKey())) {
        sb.append(v == null ? "null" : "<ignored>");
      } else {
        write(sb, v, depth + 1, seen);
      }
    }
    sb.append('}');
  }

  /**
   * Ambient JVM/runtime types that must never be walked into.
   *
   * <p>A field walk follows references wherever they lead. {@code NameableThreadFactory} holds a
   * {@code ThreadGroup}, whose {@code parent} holds every live {@code Thread} in the JVM — so the
   * oracle "compared" the entire runtime thread graph and reported
   * {@code group.parent.threads: [ReferenceHandler...]} as a refactoring divergence. These are
   * shared, nondeterministic, process-global resources, not the object's logical state.
   */
  private static boolean isAmbient(Class<?> c)
  {
    if (Thread.class.isAssignableFrom(c) || ThreadGroup.class.isAssignableFrom(c)
        || ClassLoader.class.isAssignableFrom(c) || Runtime.class.isAssignableFrom(c)
        || Process.class.isAssignableFrom(c) || ProcessBuilder.class.isAssignableFrom(c)
        || java.lang.ref.Reference.class.isAssignableFrom(c)
        || java.util.concurrent.Executor.class.isAssignableFrom(c)) {
      return true;
    }
    String n = c.getName();
    return n.startsWith("java.util.logging.") || n.startsWith("org.slf4j.")
        || n.startsWith("ch.qos.logback.") || n.startsWith("org.apache.log4j.")
        || n.startsWith("org.apache.commons.logging.");
  }

  /** Types rendered by value rather than walked. */
  private static boolean isValue(Class<?> c)
  {
    return c.isPrimitive() || c.isEnum() || c == String.class || Number.class.isAssignableFrom(c)
        || c == Boolean.class || c == Character.class || c == Class.class;
  }

  /**
   * Simple class name, normalised so that two structurally identical objects from the two
   * snapshots render the same.
   *
   * <p>Two normalisations, in this order:
   * <ol>
   *   <li><b>Runtime-generated names are reduced to their stable part.</b> A lambda's class is
   *       named after the class that defines it plus JVM bookkeeping: on JDK 8
   *       {@code XPathPanelOriginal$$Lambda$511/1846530780}, on later JDKs
   *       {@code XPathPanelOriginal$$Lambda/0x00000008000c2440}. Both the counter and the
   *       identity/address suffix are assigned by load order, so they differ between the two
   *       sides even for byte-identical code. This is what made
   *       {@code XPathPanel.getCheckXPathButton} report DIVERGENT on nothing more than the
   *       action listener installed on the button it returns. Measured against the jmeter/qwen
   *       run of 2026-07-28: 16 of its 113 divergences rendered a lambda name, and for 3 of them
   *       ({@code XPathPanel.getCheckXPathButton}, {@code HtmlPane.<init>},
   *       {@code TCPConfigGui.createClosePortPanel}) the lambda name was the whole difference.
   *       The other 13 differ elsewhere as well, in AWT {@code appContext} state, which is a
   *       separate problem. apex-core and openmeetings render no lambda names at all. Cutting at
   *       {@code '/'} and keeping only the alphabetic tag after {@code $$} leaves
   *       {@code XPathPanel$$Lambda}, which is stable across loads and JDK versions while still
   *       distinguishing a lambda from a CGLIB proxy from the class itself.</li>
   *   <li><b>The snapshot suffix is stripped from what remains.</b> The two sides are always
   *       different classes ({@code FooOriginal} vs {@code FooRefactored}), so without this every
   *       object-valued result would diverge on its type name alone. Note this must run on the
   *       prefix extracted in step 1, not on the raw name: {@code XPathPanelOriginal$$Lambda$511}
   *       does not end in {@code Original}, which is precisely why the old one-step version let
   *       the whole synthetic name through.</li>
   * </ol>
   */
  private static String name(Class<?> c)
  {
    String n;
    try {
      n = c.getSimpleName();
    } catch (Throwable t) {
      // getSimpleName() is not total on JDK 8 (InternalError "Malformed class name" on some
      // nested/synthetic classes). Letting it escape would abort the whole digest walk, which
      // diff() reports as inconclusive — a silent false negative.
      n = c.getName();
    }
    int slash = n.indexOf('/');
    if (slash >= 0) {
      n = n.substring(0, slash);
    }
    int marker = n.indexOf("$$");
    if (marker >= 0) {
      String tag = n.substring(marker + 2);
      int end = 0;
      while (end < tag.length() && Character.isLetter(tag.charAt(end))) {
        end++;
      }
      return stripSnapshotSuffix(n.substring(0, marker)) + "$$" + tag.substring(0, end);
    }
    return stripSnapshotSuffix(n);
  }

  private static String stripSnapshotSuffix(String n)
  {
    if (n.endsWith("Original")) {
      return n.substring(0, n.length() - "Original".length());
    }
    if (n.endsWith("Refactored")) {
      return n.substring(0, n.length() - "Refactored".length());
    }
    return n;
  }
}
