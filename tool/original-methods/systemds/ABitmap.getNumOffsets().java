/**
 * Get the sum of offsets contained.
 *
 * @return The sum of offsets
 */
public final long getNumOffsets() {
    long ret = 0;
    for (IntArrayList off : _offsetsLists) ret += off.size();
    return ret;
}