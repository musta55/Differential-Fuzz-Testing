public static MatrixBlock extractValues(double[] v, IColIndex colIndexes) {
    MatrixBlock rowVector = new MatrixBlock(1, colIndexes.size(), false);
    for (int i = 0; i < colIndexes.size(); i++) rowVector.set(0, i, v[colIndexes.get(i)]);
    return rowVector;
}