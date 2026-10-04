public static int[] genColsIndicesOffset(final int numCols, final int start) {
    int[] colIndices = new int[numCols];
    for (int i = 0, j = start; i < numCols; i++, j++) {
        colIndices[i] = j;
    }
    return colIndices;
}
// ---- helper method(s) introduced by the refactoring ----
private static int mergeSortedArrays(int[] lhs, int[] rhs, int[] joined) {
    int lp = 0, rp = 0, i = 0;
    while (lp < lhs.length && rp < rhs.length) {
        joined[i++] = lhs[lp] < rhs[rp] ? lhs[lp++] : rhs[rp++];
    }
    return i;
}

private static void appendRemainingElements(int[] array, int[] joined, int startIndex) {
    for (int i = startIndex, p = 0; p < array.length; i++, p++) {
        joined[i] = array[p];
    }
}

