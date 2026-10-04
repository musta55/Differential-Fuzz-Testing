/**
 * Utility function of sparse-unsafe operations.
 *
 * @param ind row indicator vector of non zeros
 * @return offsets
 */
protected int[] computeOffsets(boolean[] ind) {
    // determine number of offsets
    int numOffsets = 0;
    for (int i = 0; i < ind.length; i++) numOffsets += ind[i] ? 1 : 0;
    // create offset lists
    int[] ret = new int[numOffsets];
    for (int i = 0, pos = 0; i < ind.length; i++) if (ind[i])
        ret[pos++] = i;
    return ret;
}