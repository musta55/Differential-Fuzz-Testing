public static int[] genColsIndices(final int numCols) {
    final int[] colIndices = new int[numCols];
    for (int i = 0; i < numCols; i++) colIndices[i] = i;
    return colIndices;
}