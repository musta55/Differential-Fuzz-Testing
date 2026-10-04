public static int[] genColsIndices(final int cl, final int cu) {
    final int[] colIndices = new int[cu - cl];
    for (int i = 0, j = cl; j < cu; i++, j++) colIndices[i] = j;
    return colIndices;
}