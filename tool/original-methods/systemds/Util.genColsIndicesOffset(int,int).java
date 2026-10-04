public static int[] genColsIndicesOffset(final int numCols, final int start) {
    final int[] colIndices = new int[numCols];
    for (int i = 0, j = start; i < numCols; i++, j++) colIndices[i] = j;
    return colIndices;
}