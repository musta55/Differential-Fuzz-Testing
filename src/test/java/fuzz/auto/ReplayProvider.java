package fuzz.auto;

import java.nio.charset.StandardCharsets;

import com.code_intelligence.jazzer.api.FuzzedDataProvider;

/**
 * A deterministic {@link FuzzedDataProvider} over a fixed {@code byte[]}.
 *
 * <p>This exists to solve an aliasing problem that only appears once the engine builds real
 * objects instead of scalars. The oracle must invoke both the original and the refactored method
 * on the <em>same</em> input; with scalars that was just {@code copy(args)}, but an arbitrary
 * object graph cannot be deep-copied generically. If both sides were handed the same mutable
 * instance, the first call could mutate it and the second would then observe a different input,
 * reporting a divergence that the refactoring did not cause.
 *
 * <p>The fix is to build the arguments twice rather than copy them once: capture the fuzzer's
 * bytes for this iteration, then replay those identical bytes into two independent builds. Both
 * sides get structurally identical arguments that share no references.
 *
 * <p>Determinism is the only contract that matters here — the decoding does not need to match
 * Jazzer's own provider, because the bytes still come from Jazzer and coverage feedback still
 * drives the search. On exhaustion the provider returns zeros and empty values (Jazzer does the
 * same) so a short input degrades instead of throwing.
 *
 * <p>NOT thread-safe, and not meant to be: one instance backs one side of one iteration.
 */
final class ReplayProvider implements FuzzedDataProvider
{
  private final byte[] buf;
  private int pos;

  ReplayProvider(byte[] bytes)
  {
    this.buf = bytes == null ? new byte[0] : bytes;
  }

  /** Bytes left. Callers use this to detect a starved input rather than trusting a null. */
  @Override
  public int remainingBytes()
  {
    return buf.length - pos;
  }

  private int next()
  {
    return pos < buf.length ? (buf[pos++] & 0xff) : 0;
  }

  private long nextLong(int nbytes)
  {
    long v = 0;
    for (int i = 0; i < nbytes; i++) {
      v = (v << 8) | next();
    }
    return v;
  }

  /** Map a raw value into [min,max] without bias assumptions; min>max is treated as [max,min]. */
  private long clamp(long raw, long min, long max)
  {
    if (min > max) {
      long t = min; min = max; max = t;
    }
    if (min == max) {
      return min;
    }
    long span = max - min + 1;
    if (span <= 0) { // range spans the whole long domain and overflowed
      return raw;
    }
    long m = raw % span;
    if (m < 0) {
      m += span;
    }
    return min + m;
  }

  @Override public boolean consumeBoolean() { return (next() & 1) != 0; }

  @Override
  public boolean[] consumeBooleans(int maxLength)
  {
    int n = lengthFor(maxLength);
    boolean[] a = new boolean[n];
    for (int i = 0; i < n; i++) {
      a[i] = consumeBoolean();
    }
    return a;
  }

  @Override public byte consumeByte() { return (byte) next(); }

  @Override
  public byte consumeByte(byte min, byte max) { return (byte) clamp(consumeByte(), min, max); }

  @Override
  public byte[] consumeBytes(int maxLength)
  {
    int n = lengthFor(maxLength);
    byte[] a = new byte[n];
    for (int i = 0; i < n; i++) {
      a[i] = (byte) next();
    }
    return a;
  }

  @Override
  public byte[] consumeRemainingAsBytes()
  {
    byte[] a = new byte[remainingBytes()];
    System.arraycopy(buf, pos, a, 0, a.length);
    pos = buf.length;
    return a;
  }

  @Override public short consumeShort() { return (short) nextLong(2); }

  @Override
  public short consumeShort(short min, short max) { return (short) clamp(consumeShort(), min, max); }

  @Override
  public short[] consumeShorts(int maxLength)
  {
    int n = lengthFor(maxLength);
    short[] a = new short[n];
    for (int i = 0; i < n; i++) {
      a[i] = consumeShort();
    }
    return a;
  }

  @Override public int consumeInt() { return (int) nextLong(4); }

  @Override public int consumeInt(int min, int max) { return (int) clamp(consumeInt(), min, max); }

  @Override
  public int[] consumeInts(int maxLength)
  {
    int n = lengthFor(maxLength);
    int[] a = new int[n];
    for (int i = 0; i < n; i++) {
      a[i] = consumeInt();
    }
    return a;
  }

  @Override public long consumeLong() { return nextLong(8); }

  @Override public long consumeLong(long min, long max) { return clamp(consumeLong(), min, max); }

  @Override
  public long[] consumeLongs(int maxLength)
  {
    int n = lengthFor(maxLength);
    long[] a = new long[n];
    for (int i = 0; i < n; i++) {
      a[i] = consumeLong();
    }
    return a;
  }

  @Override public float consumeFloat() { return Float.intBitsToFloat(consumeInt()); }

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
    return (consumeInt() & 0x7fffffff) / (float) Integer.MAX_VALUE;
  }

  @Override public double consumeDouble() { return Double.longBitsToDouble(consumeLong()); }

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
    return (consumeLong() & Long.MAX_VALUE) / (double) Long.MAX_VALUE;
  }

  @Override public char consumeChar() { return (char) nextLong(2); }

  @Override public char consumeChar(char min, char max) { return (char) clamp(consumeChar(), min, max); }

  @Override
  public char consumeCharNoSurrogates()
  {
    char c = consumeChar();
    return (c >= 0xd800 && c <= 0xdfff) ? (char) (c - 0xd800) : c;
  }

  @Override
  public String consumeString(int maxLength)
  {
    int n = lengthFor(maxLength);
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      sb.append(consumeCharNoSurrogates());
    }
    return sb.toString();
  }

  @Override
  public String consumeRemainingAsString()
  {
    return new String(consumeRemainingAsBytes(), StandardCharsets.UTF_8);
  }

  @Override
  public String consumeAsciiString(int maxLength)
  {
    int n = lengthFor(maxLength);
    StringBuilder sb = new StringBuilder(n);
    for (int i = 0; i < n; i++) {
      sb.append((char) (next() & 0x7f));
    }
    return sb.toString();
  }

  @Override
  public String consumeRemainingAsAsciiString()
  {
    byte[] b = consumeRemainingAsBytes();
    StringBuilder sb = new StringBuilder(b.length);
    for (byte x : b) {
      sb.append((char) (x & 0x7f));
    }
    return sb.toString();
  }

  /**
   * Length for a variable-size consume: one byte picks a size in [0,maxLength], additionally
   * capped by what is actually left so a 3-byte input cannot ask for a 100-element array.
   */
  private int lengthFor(int maxLength)
  {
    if (maxLength <= 0 || remainingBytes() == 0) {
      return 0;
    }
    int cap = Math.min(maxLength, Math.max(1, remainingBytes()));
    return (int) clamp(next(), 0, cap);
  }
}
