@Override
protected void tsmm(double[] result, int numColumns, int nRows) {
    final int[] counts = getCounts();
    tsmm(result, numColumns, counts, _dict, _colIndexes);
}