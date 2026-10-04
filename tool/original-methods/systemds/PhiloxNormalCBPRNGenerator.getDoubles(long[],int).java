/**
 * Generate a sequence of random doubles using the Philox4x64 counter-based PRNG.
 *
 * @param ctr  The start counter to use for the PRNG
 * @param size The number of doubles to generate
 * @return An array of random doubles distributed normally with mean 0 and variance 1
 */
public double[] getDoubles(long[] ctr, int size) {
    // Ensure the key is correct size
    if (this.seed.length != 2) {
        throw new IllegalArgumentException("Key must be 128 bits");
    }
    // Ensure the counter is correct size
    if (ctr.length != 4) {
        throw new IllegalArgumentException("Counter must be 256 bits");
    }
    double[] uniform = uniformGen.getDoubles(ctr, size + size % 2);
    double[] normal = new double[size];
    for (int i = 0; i < size; i += 2) {
        double v1 = Math.sqrt(-2 * Math.log(uniform[i]));
        double v2 = 2 * Math.PI * uniform[i + 1];
        normal[i] = v1 * Math.cos(v2);
        if (i + 1 < size) {
            normal[i + 1] = v1 * Math.sin(v2);
        }
    }
    return normal;
}