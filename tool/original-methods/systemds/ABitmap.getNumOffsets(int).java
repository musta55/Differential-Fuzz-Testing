/**
 * Get the number of offsets for a specific unique offset.
 *
 * @param ix The offset index.
 * @return The number of offsets for this unique value.
 */
public final int getNumOffsets(int ix) {
    return _offsetsLists[ix].size();
}