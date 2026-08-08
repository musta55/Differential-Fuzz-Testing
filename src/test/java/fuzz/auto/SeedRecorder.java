package fuzz.auto;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * The inverse of {@link ReplayProvider}: a {@link FuzzedDataProvider} that <em>writes</em> the bytes
 * which would decode back into the values it hands out.
 *
 * <p>This is what lets a unit test become a fuzzer seed. The engine builds its arguments by running
 * {@link ObjectFactory#build} against a provider; run that same code against this recorder instead
 * of a reader, serving values harvested from an EvoSuite test, and the bytes it accumulates are a
 * corpus file that makes the fuzzer start from that test's inputs. Driving the real
 * {@code ObjectFactory} rather than reimplementing an encoder is the whole point — the byte layout
 * is then correct by construction for receivers, autofuzz-built objects and arrays alike, none of
 * which have a layout that could be written down independently.
 *
 * <h2>The contract</h2>
 * Every method must emit bytes {@code B} and return exactly what {@code ReplayProvider} would return
 * when reading {@code B}. Where that inverse is only conditionally exact (the clamped variants), the
 * encoding below is chosen to be exact for the ranges that occur, and {@link SeedWriter} decodes the
 * finished seed through the real path and discards it unless the arguments come back identical. So
 * an imperfect encoding costs a seed, never a wrong one.
 *
 * <h2>Values, and running out of them</h2>
 * {@code pool} is the ordered list of constants from one test case. Each request takes the first
 * still-unused entry convertible to the requested type, which skips (say) a String while an int is
 * wanted instead of jamming the alignment. Once the pool is spent, requests fall back to a neutral
 * default; the seed is still valid, just less informed.
 *
 * <p>{@link #seed()} appends trailing slack, and must: {@code ReplayProvider.lengthFor} caps a
 * requested length by the bytes actually remaining, so a length byte written here only decodes back
 * to the same length if the buffer still has room at that point.
 */
final class SeedRecorder implements FuzzedDataProvider
{
  /** Trailing slack so every {@code lengthFor} cap equals its {@code maxLength}. */
  private static final int PAD = 96;

  private final ByteArrayOutputStream out = new ByteArrayOutputStream();
  private final List<Object> pool;
  private final boolean[] used;
  private int visible;

  SeedRecorder(List<Object> pool)
  {
    this.pool = pool;
    this.used = new boolean[pool.size()];
    this.visible = pool.size();
  }

  /**
   * Restrict {@link #take} to the first {@code n} pool entries.
   *
   * <p>This is how {@link SeedWriter} splits one ordered pool between the receiver and the method
   * arguments. Without it the receiver's constructor would greedily take whichever constant happens
   * to match its parameter type first — usually the method's own argument — and the seed would
   * encode the right values in the wrong places.
   */
  void limitTo(int n)
  {
    this.visible = Math.max(0, Math.min(n, pool.size()));
  }

  /** The recorded seed, with the slack {@link ReplayProvider} needs to honour the lengths. */
  byte[] seed()
  {
    byte[] body = out.toByteArray();
    byte[] full = new byte[body.length + PAD];
    System.arraycopy(body, 0, full, 0, body.length);
    return full;
  }

  // ── byte emission ───────────────────────────────────────────────────────────

  private void put(long value, int nbytes)
  {
    for (int i = nbytes - 1; i >= 0; i--) {
      out.write((int) ((value >> (8 * i)) & 0xff));
    }
  }

  /**
   * Emit the raw value for a clamped read. {@code ReplayProvider.clamp} maps raw to
   * {@code min + floorMod(raw, span)}, so a raw of {@code target - min} is an exact inverse
   * whenever it is non-negative and fits — which holds for every range these consumers see.
   */
  private long rawFor(long target, long min, long max)
  {
    if (min > max) {
      long t = min; min = max; max = t;
    }
    if (min == max) {
      return 0;
    }
    long span = max - min + 1;
    if (span <= 0) {
      return target; // range covers the whole domain; clamp passes raw through
    }
    long r = target - min;
    return r < 0 ? 0 : r % span;
  }

  // ── value selection ─────────────────────────────────────────────────────────

  /** First unused pool entry convertible to {@code want}, or null. */
  private Object take(Class<?> want)
  {
    for (int i = 0; i < visible; i++) {
      if (used[i]) {
        continue;
      }
      Object v = convert(pool.get(i), want);
      if (v != null) {
        used[i] = true;
        return v;
      }
    }
    return null;
  }

  /**
   * Convert a pool constant to the requested slot type, or null if it does not belong there.
   *
   * <p>Conversions must be LOSSLESS. A narrowing cast looks like a match and is in fact a discard:
   * autofuzz opens every object build with an unbounded {@code consumeByte()} mode selector, and
   * with a lenient cast that byte swallowed the test's first integer — {@code (byte) -2816 == 0} —
   * so the constant was destroyed on a slot that does not care about it and every later value
   * shifted by one. Requiring the value to survive the round trip leaves it in the pool for the
   * {@code consumeInt()} that actually wants it.
   */
  private static Object convert(Object v, Class<?> want)
  {
    if (v == null) {
      return null; // a JSON null carries no value for any scalar slot
    }
    if (want == String.class) {
      return v instanceof String ? v : null;
    }
    if (want == Boolean.class) {
      return v instanceof Boolean ? v : null;
    }
    if (want == Character.class) {
      if (v instanceof Character) {
        return v;
      }
      if (v instanceof String && ((String) v).length() == 1) {
        return ((String) v).charAt(0);
      }
      if (v instanceof Number) {
        long l = ((Number) v).longValue();
        return l >= Character.MIN_VALUE && l <= Character.MAX_VALUE ? (char) l : null;
      }
      return null;
    }
    if (!(v instanceof Number)) {
      return null;
    }
    Number n = (Number) v;
    if (n instanceof Double || n instanceof Float) {
      double d = n.doubleValue();
      if (want == Double.class) return d;
      if (want == Float.class) return (double) (float) d == d ? (Float) (float) d : null;
      return null; // a fractional constant has no lossless integral slot
    }
    long l = n.longValue();
    if (want == Long.class) return l;
    if (want == Integer.class) return l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE ? (Integer) (int) l : null;
    if (want == Short.class) return l >= Short.MIN_VALUE && l <= Short.MAX_VALUE ? (Short) (short) l : null;
    if (want == Byte.class) return l >= Byte.MIN_VALUE && l <= Byte.MAX_VALUE ? (Byte) (byte) l : null;
    if (want == Double.class) return (long) (double) l == l ? (Double) (double) l : null;
    if (want == Float.class) return (long) (float) l == l ? (Float) (float) l : null;
    return null;
  }

  // ── FuzzedDataProvider ──────────────────────────────────────────────────────

  @Override
  public boolean consumeBoolean()
  {
    Object v = take(Boolean.class);
    boolean b = v != null && (Boolean) v;
    out.write(b ? 1 : 0);
    return b;
  }

  @Override
  public boolean[] consumeBooleans(int maxLength)
  {
    int n = putLength(maxLength, Boolean.class);
    boolean[] a = new boolean[n];
    for (int i = 0; i < n; i++) {
      a[i] = consumeBoolean();
    }
    return a;
  }

  @Override
  public byte consumeByte()
  {
    Object v = take(Byte.class);
    byte b = v == null ? 0 : (Byte) v;
    out.write(b & 0xff);
    return b;
  }

  @Override
  public byte consumeByte(byte min, byte max)
  {
    Object v = degenerate(min, max) ? null : take(Byte.class);
    byte target = v == null ? min : (Byte) v;
    long raw = rawFor(target, min, max);
    out.write((int) (raw & 0xff));
    // Report what ReplayProvider will actually produce, not what we asked for: consumeByte()
    // re-reads this as a SIGNED byte, so a raw above 127 comes back negative and clamps elsewhere.
    return (byte) clampLike((byte) raw, min, max);
  }

  /**
   * A range with a single admissible value decides nothing, so it must not spend a constant.
   *
   * <p>Autofuzz picks a constructor with {@code consumeInt(0, choices - 1)}; for the common
   * single-constructor class that is {@code consumeInt(0, 0)}. Letting it draw from the pool burned
   * one of the test's constants on a forced choice and shifted every later value by one slot —
   * measured on Widget(int), where it cost the seed either the right receiver or the right
   * arguments, never both.
   */
  private static boolean degenerate(long min, long max)
  {
    return min == max;
  }

  private static long clampLike(long raw, long min, long max)
  {
    if (min == max) {
      return min;
    }
    long span = max - min + 1;
    long m = raw % span;
    if (m < 0) {
      m += span;
    }
    return min + m;
  }

  @Override
  public byte[] consumeBytes(int maxLength)
  {
    int n = putLength(maxLength, Byte.class);
    byte[] a = new byte[n];
    for (int i = 0; i < n; i++) {
      a[i] = consumeByte();
    }
    return a;
  }

  @Override
  public byte[] consumeRemainingAsBytes()
  {
    return new byte[0]; // nothing remains: this provider is a sink, not a source
  }

  @Override
  public short consumeShort()
  {
    Object v = take(Short.class);
    short s = v == null ? 0 : (Short) v;
    put(s & 0xffffL, 2);
    return s;
  }

  @Override
  public short consumeShort(short min, short max)
  {
    Object v = degenerate(min, max) ? null : take(Short.class);
    short target = v == null ? min : (Short) v;
    long raw = rawFor(target, min, max);
    put(raw & 0xffffL, 2);
    return (short) clampLike((short) raw, min, max);
  }

  @Override
  public short[] consumeShorts(int maxLength)
  {
    int n = putLength(maxLength, Short.class);
    short[] a = new short[n];
    for (int i = 0; i < n; i++) {
      a[i] = consumeShort();
    }
    return a;
  }

  @Override
  public int consumeInt()
  {
    Object v = take(Integer.class);
    int x = v == null ? 0 : (Integer) v;
    put(x & 0xffffffffL, 4);
    return x;
  }

  @Override
  public int consumeInt(int min, int max)
  {
    Object v = degenerate(min, max) ? null : take(Integer.class);
    int target = v == null ? min : (Integer) v;
    long raw = rawFor(target, min, max);
    put(raw & 0xffffffffL, 4);
    return (int) clampLike((int) raw, min, max);
  }

  @Override
  public int[] consumeInts(int maxLength)
  {
    int n = putLength(maxLength, Integer.class);
    int[] a = new int[n];
    for (int i = 0; i < n; i++) {
      a[i] = consumeInt();
    }
    return a;
  }

  @Override
  public long consumeLong()
  {
    Object v = take(Long.class);
    long x = v == null ? 0 : (Long) v;
    put(x, 8);
    return x;
  }

  @Override
  public long consumeLong(long min, long max)
  {
    Object v = degenerate(min, max) ? null : take(Long.class);
    long target = v == null ? min : (Long) v;
    long raw = rawFor(target, min, max);
    put(raw, 8);
    return clampLike(raw, min, max);
  }

  @Override
  public long[] consumeLongs(int maxLength)
  {
    int n = putLength(maxLength, Long.class);
    long[] a = new long[n];
    for (int i = 0; i < n; i++) {
      a[i] = consumeLong();
    }
    return a;
  }

  @Override
  public float consumeFloat()
  {
    Object v = take(Float.class);
    float f = v == null ? 0f : (Float) v;
    put(Float.floatToRawIntBits(f) & 0xffffffffL, 4);
    return f;
  }

  @Override
  public float consumeRegularFloat()
  {
    float f = consumeFloat();
    return Float.isFinite(f) ? f : 0f;
  }

  @Override
  public float consumeRegularFloat(float min, float max)
  {
    return min + consumeProbabilityFloat() * (max - min);
  }

  @Override
  public float consumeProbabilityFloat()
  {
    int raw = consumeInt();
    return (raw & 0x7fffffff) / (float) Integer.MAX_VALUE;
  }

  @Override
  public double consumeDouble()
  {
    Object v = take(Double.class);
    double d = v == null ? 0d : (Double) v;
    put(Double.doubleToRawLongBits(d), 8);
    return d;
  }

  @Override
  public double consumeRegularDouble()
  {
    double d = consumeDouble();
    return Double.isFinite(d) ? d : 0d;
  }

  @Override
  public double consumeRegularDouble(double min, double max)
  {
    return min + consumeProbabilityDouble() * (max - min);
  }

  @Override
  public double consumeProbabilityDouble()
  {
    long raw = consumeLong();
    return (raw & Long.MAX_VALUE) / (double) Long.MAX_VALUE;
  }

  @Override
  public char consumeChar()
  {
    Object v = take(Character.class);
    char c = v == null ? 0 : (Character) v;
    put(c, 2);
    return c;
  }

  @Override
  public char consumeChar(char min, char max)
  {
    Object v = degenerate(min, max) ? null : take(Character.class);
    char target = v == null ? min : (Character) v;
    long raw = rawFor(target, min, max);
    put(raw & 0xffffL, 2);
    return (char) clampLike((char) raw, min, max);
  }

  @Override
  public char consumeCharNoSurrogates()
  {
    Object v = take(Character.class);
    char c = v == null ? 0 : (Character) v;
    if (c >= 0xd800 && c <= 0xdfff) {
      c = 0; // unrepresentable: ReplayProvider folds surrogates, so no input yields this char
    }
    put(c, 2);
    return c;
  }

  /**
   * ASCII strings are the engine's representation for every String parameter
   * ({@code Scalars.build}), so this is the encoder that matters most for seeding real inputs.
   * {@code ReplayProvider} masks each byte to 7 bits, so non-ASCII characters cannot be reproduced
   * and are dropped rather than silently mangled.
   */
  @Override
  public String consumeAsciiString(int maxLength)
  {
    Object v = take(String.class);
    String s = v == null ? "" : (String) v;
    StringBuilder kept = new StringBuilder();
    for (int i = 0; i < s.length() && kept.length() < maxLength; i++) {
      char c = s.charAt(i);
      if (c < 0x80) {
        kept.append(c);
      }
    }
    String result = kept.toString();
    writeLength(result.length(), maxLength);
    for (int i = 0; i < result.length(); i++) {
      out.write(result.charAt(i) & 0x7f);
    }
    return result;
  }

  @Override
  public String consumeRemainingAsAsciiString()
  {
    return "";
  }

  @Override
  public String consumeString(int maxLength)
  {
    Object v = take(String.class);
    String s = v == null ? "" : (String) v;
    String result = s.length() > maxLength ? s.substring(0, maxLength) : s;
    writeLength(result.length(), maxLength);
    StringBuilder actual = new StringBuilder(result.length());
    for (int i = 0; i < result.length(); i++) {
      char c = result.charAt(i);
      if (c >= 0xd800 && c <= 0xdfff) {
        c = 0;
      }
      put(c, 2);
      actual.append(c);
    }
    return actual.toString();
  }

  @Override
  public String consumeRemainingAsString()
  {
    return "";
  }

  @Override
  public int remainingBytes()
  {
    // Must look inexhaustible: ObjectFactory and autofuzz treat a low count as a starved input and
    // bail out, which would truncate the very seed being recorded.
    return Integer.MAX_VALUE;
  }

  /**
   * Emit a length byte for a variable-size read and return the length that will be decoded.
   *
   * <p>The length is how many pool constants are still available for the element type, capped by
   * {@code maxLength}. Emitting a fixed 0 instead would be self-consistent — the seed would decode
   * back to the empty array it recorded — and useless: every array parameter would be seeded empty,
   * discarding exactly the constants the test case chose for it.
   */
  private int putLength(int maxLength, Class<?> element)
  {
    int n = Math.min(maxLength, available(element));
    writeLength(n, maxLength);
    return n;
  }

  /** Unused pool entries that could fill one element of this type. */
  private int available(Class<?> want)
  {
    int n = 0;
    for (int i = 0; i < visible; i++) {
      if (!used[i] && convert(pool.get(i), want) != null) {
        n++;
      }
    }
    return n;
  }

  private void writeLength(int n, int maxLength)
  {
    if (maxLength <= 0) {
      return; // ReplayProvider returns 0 without consuming a byte
    }
    out.write(n & 0xff);
  }
}
